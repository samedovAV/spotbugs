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
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.ch.Subtypes2;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class UnsafeDeserialization extends OpcodeStackDetector {

    private final BugReporter bugReporter;

    private boolean isSerializable;

    private final Set<XField> interestingFields = new HashSet<>();

    public UnsafeDeserialization(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        if (Subtypes2.instanceOf(classContext.getJavaClass(), "java.io.Serializable")) {
            isSerializable = true;
            super.visitClassContext(classContext);
        }
    }

    @Override
    public void visit(Field obj) {
        if (isSerializable) {
            XField xField = getXField();
            if (!xField.isStatic() && (xField.isPrivate() || xField.isFinal()) && xField.isReferenceType()) {
                // transient field check
                // collections
                interestingFields.add(xField);
            }
        }
    }

    @Override
    public void visitAfter(JavaClass obj) {
        Optional<Method> readObjMethod = Arrays.stream(obj.getMethods())
                .filter(i -> i.getName().equals("readObject")
                        && "(Ljava/io/ObjectInputStream;)V".equals(i.getSignature())
                        && i.isPrivate())
                .findFirst();

        if (!interestingFields.isEmpty() && readObjMethod.isPresent()) {
            String allFields = interestingFields.stream()
                    .map(XField::getName)
                    .sorted()
                    .collect(Collectors.joining(","));
            bugReporter.reportBug(new BugInstance("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", NORMAL_PRIORITY)
                    .addClass(getClassContext().getJavaClass())
                    .addString(allFields)
                    .addMethod(obj, readObjMethod.get()));
        }

    }

    @Override
    public void sawOpcode(int seen) {
        if ("readObject".equals(getMethodName()) && "(Ljava/io/ObjectInputStream;)V".equals(getMethodSig()) && isSerializable && getMethod()
                .isPrivate()) {
            if (seen == Const.PUTFIELD || seen == Const.PUTSTATIC) {
                XField field = getXFieldOperand();
                if (interestingFields.contains(field)) {
                    interestingFields.remove(field);
                }
            } else if (seen == Const.INVOKESTATIC || seen == Const.INVOKEVIRTUAL
                    || seen == Const.INVOKESPECIAL || seen == Const.INVOKEINTERFACE) {
                String methodName = getNameConstantOperand();
                // Collections.copy()
                int stackDepth = stack.getStackDepth();
                if ("copy".equals(methodName) && "java/util/Collections".equals(getClassConstantOperand())
                        && stackDepth >= 2) { // check method before the stack
                    XField field = stack.getStackItem(1).getXField();
                    if (interestingFields.contains(stack.getStackItem(1).getXField())) {
                        interestingFields.remove(field);
                    }
                }
                // System.arraycopy()
                if ("arraycopy".equals(methodName) && "java/lang/System".equals(getClassConstantOperand())
                        && stackDepth >= 3) {
                    XField field = stack.getStackItem(2).getXField();
                    interestingFields.remove(field);
                }
            }
        }
    }

    @Override
    public void afterOpcode(int seen) {
        super.afterOpcode(seen);

        if (seen == Const.INVOKESTATIC) {
            // todo
        }
    }
}
