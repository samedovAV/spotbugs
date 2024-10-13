/*
 * SpotBugs - Find bugs in Java programs
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edu.umd.cs.findbugs.detect;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.XFactory;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.ch.Subtypes2;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.util.MutableClasses;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UnsafeDeserialization extends OpcodeStackDetector {

    private final BugReporter bugReporter;

    private final Set<XField> mutableFields = new HashSet<>();

    public UnsafeDeserialization(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private Method getReadObjectMethodIfFound(JavaClass obj) {
        return Arrays.stream(obj.getMethods())
                .filter(this::isReadObject)
                .findFirst()
                .orElse(null);
    }

    private boolean isReadObject(Method method) {
        return "readObject".equals(method.getName())
                && "(Ljava/io/ObjectInputStream;)V".equals(method.getSignature())
                && method.isPrivate();
    }

    private boolean isSerializable(JavaClass javaClass) {
        return Subtypes2.instanceOf(javaClass, "java.io.Serializable");
    }

    @Override
    public void visit(JavaClass obj) {
        if (isSerializable(obj)) {
            for (Field fld : obj.getFields()) {
                if (!fld.isTransient()) {
                    XField xField = XFactory.createXField(obj, fld);
                    if (MutableClasses.mutableSignature(xField.getSignature())
                            && !xField.isStatic()
                            && (xField.isPrivate() || xField.isFinal())
                            && xField.isReferenceType()
                            && !xField.isVolatile()
                            && !xField.isSynthetic()
                            && !xField.isEnum()) {
                        mutableFields.add(xField);
                    }
                }
            }
        }
    }

    @Override
    public void visitAfter(JavaClass obj) {
        Method readObjectFoundAndGet = getReadObjectMethodIfFound(obj);
        if (!mutableFields.isEmpty() && readObjectFoundAndGet != null) {
            String allFields = mutableFields.stream()
                    .map(field -> field.getName() + " (" + field.getClassName() + ")")
                    .collect(Collectors.joining(", "));
            bugReporter.reportBug(new BugInstance("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", NORMAL_PRIORITY)
                    .addClass(obj)
                    .addMethod(obj, readObjectFoundAndGet)
                    .addString(allFields));
        }
        mutableFields.clear();
    }

    @Override
    public void sawOpcode(int seen) {
        if (isSerializable(getClassContext().getJavaClass()) && isReadObject(getMethod())) {
            // There are two ways to make a defensive copy:
            // 1. Assignment: PUTFIELD, PUTSTATIC
            // (If the assignment is performed we suppose that the defensive copy is done)
            // 2. Method call: INVOKESTATIC, INVOKEVIRTUAL, INVOKESPECIAL, INVOKEINTERFACE
            // (A method call can be used to create a defensive copy if the method is known to create a new object that is a copy of the original)
            if (seen == Const.PUTFIELD || seen == Const.PUTSTATIC) {
                XField field = getXFieldOperand();
                mutableFields.remove(field);
            } else if (seen == Const.INVOKESTATIC || seen == Const.INVOKEVIRTUAL
                    || seen == Const.INVOKESPECIAL || seen == Const.INVOKEINTERFACE) {
                String methodName = getNameConstantOperand();
                int stackDepth = stack.getStackDepth();
                int stackItemForCopy = 1; // Collections.copy(dest, src) has 2 arguments, we need src
                int stackItemForSystemArraycopy = 2; // System.arraycopy() has 3 arguments, we need the second one
                // Collections.copy()
                if ("copy".equals(methodName)
                        && "java/util/Collections".equals(getClassConstantOperand())
                        && stackDepth > stackItemForCopy) { // check method before the stack
                    mutableFields.remove(stack.getStackItem(stackItemForCopy).getXField());
                }
                // System.arraycopy()
                if ("arraycopy".equals(methodName)
                        && "java/lang/System".equals(getClassConstantOperand())
                        && stackDepth > stackItemForSystemArraycopy) {
                    mutableFields.remove(stack.getStackItem(stackItemForSystemArraycopy).getXField());
                }
            }
        }
    }

}
