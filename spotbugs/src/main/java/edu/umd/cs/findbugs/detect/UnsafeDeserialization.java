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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UnsafeDeserialization extends OpcodeStackDetector {

    // Collections.copy(dest, src) has 2 arguments, we need src
    public static final int STACK_ITEM_FOR_COPY = 1;

    private final BugReporter bugReporter;

    private final Set<XField> mutableFields = new HashSet<>();

    public UnsafeDeserialization(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private Method isReadObjectFoundAndGet(JavaClass obj) {
        Optional<Method> readObjMethod = Arrays.stream(obj.getMethods())
                .filter(i -> i.getName().equals("readObject")
                        && "(Ljava/io/ObjectInputStream;)V".equals(i.getSignature())
                        && i.isPrivate())
                .findFirst();

        return readObjMethod.orElse(null);
    }

    private boolean isReadObjectFound() {
        return "readObject".equals(getMethodName())
                && "(Ljava/io/ObjectInputStream;)V".equals(getMethodSig())
                && getMethod().isPrivate();
    }

    private boolean isSerializable() {
        return Subtypes2.instanceOf(getClassContext().getJavaClass(), "java.io.Serializable");
    }

    @Override
    public void visit(Field obj) {
        if (isSerializable()) {
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
        if (!mutableFields.isEmpty() && isReadObjectFoundAndGet(obj) != null) {
            String allFields = mutableFields.stream()
                    .map(XField::getName)
                    .sorted()
                    .collect(Collectors.joining(", "));
            bugReporter.reportBug(new BugInstance("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", NORMAL_PRIORITY)
                    .addClass(obj)
                    .addString(allFields)
                    .addMethod(obj, isReadObjectFoundAndGet(obj)));
        }
    }

    @Override
    public void sawOpcode(int seen) {
        if (isSerializable() && isReadObjectFound()) {
            // There are two ways to make a defensive copy:
            // 1. Assignment: PUTFILED, PUTSTATIC
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
                    if (mutableFields.contains(stack.getStackItem(STACK_ITEM_FOR_COPY).getXField())) {
                        mutableFields.remove(field);
                    }
                }
                // System.arraycopy()
                if ("arraycopy".equals(methodName) && "java/lang/System".equals(getClassConstantOperand())
                        && stackDepth >= 3) {
                    XField field = stack.getStackItem(2).getXField();
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
