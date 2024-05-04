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
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.ch.Subtypes2;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UnsafeDeserialization extends OpcodeStackDetector {

    public static final int STACK_ITEM_FOR_COPY = 1; // Collections.copy(dest, src) has 2 arguments, we need src
    public static final int STACK_DEPTH_FOR_SYSTEM_ARRAYCOPY = 3; // Minimum stack depth for System.arraycopy()
    public static final int STACK_ITEM_FOR_SYSTEM_ARRAYCOPY = 2; // System.arraycopy() has 3 arguments, we need the second one

    private final BugReporter bugReporter;

    private final Set<XField> mutableFields = new HashSet<>();

    public UnsafeDeserialization(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private Method isReadObjectFoundAndGet(JavaClass obj) {
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
    public void visit(Field obj) {
        if (isSerializable(getClassContext().getJavaClass())) {
            XField xField = getXField();
            if (!xField.isStatic()
                    && (xField.isPrivate() || xField.isFinal())
                    && xField.isReferenceType()
                    && !obj.isTransient()
                    && !xField.isVolatile()
                    && !xField.isSynthetic()
                    && !xField.isEnum()) {
                mutableFields.add(xField);
            }
        }
    }

    @Override
    public void visitAfter(JavaClass obj) {
        Method readObjectFoundAndGet = isReadObjectFoundAndGet(obj);
        if (!mutableFields.isEmpty() && readObjectFoundAndGet != null) {
            String allFields = mutableFields.stream()
                    .map(XField::getName)
                    .sorted()
                    .collect(Collectors.joining(", "));
            bugReporter.reportBug(new BugInstance("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", NORMAL_PRIORITY)
                    .addClass(obj)
                    .addString(allFields)
                    .addMethod(obj, readObjectFoundAndGet));
        }
    }

    @Override
    public void sawOpcode(int seen) {
        if (isSerializable(getClassContext().getJavaClass()) && isReadObject(getMethod())) {
            // There are two ways to make a defensive copy:
            // 1. Assignment: PUTFILED, PUTSTATIC (If the assignment is performed we suppose that the copy is done)
            // 2. Method call: INVOKESTATIC, INVOKEVIRTUAL, INVOKESPECIAL, INVOKEINTERFACE
            if (seen == Const.PUTFIELD || seen == Const.PUTSTATIC) {
                XField field = getXFieldOperand();
                mutableFields.remove(field);
            } else if (seen == Const.INVOKESTATIC || seen == Const.INVOKEVIRTUAL
                    || seen == Const.INVOKESPECIAL || seen == Const.INVOKEINTERFACE) {
                String methodName = getNameConstantOperand();
                // Collections.copy()
                int stackDepth = stack.getStackDepth();
                if ("copy".equals(methodName) && "java/util/Collections".equals(getClassConstantOperand())
                        && stackDepth > STACK_ITEM_FOR_COPY) { // check method before the stack
                    XField field = stack.getStackItem(STACK_ITEM_FOR_COPY).getXField();
                    mutableFields.remove(field);
                }
                // System.arraycopy()
                if ("arraycopy".equals(methodName) && "java/lang/System".equals(getClassConstantOperand())
                        && stackDepth >= STACK_DEPTH_FOR_SYSTEM_ARRAYCOPY) {
                    XField field = stack.getStackItem(STACK_ITEM_FOR_SYSTEM_ARRAYCOPY).getXField();
                    mutableFields.remove(field);
                }
            }
        }
    }

    @Override
    public void report() {
        mutableFields.clear();
    }
}
