/*
 * FindBugs - Find bugs in Java programs
 * Copyright (C) 2004,2005 Dave Brosius <dbrosius@users.sourceforge.net>
 * Copyright (C) 2005 William Pugh
 * Copyright (C) 2004,2005 University of Maryland
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


import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;

public class InvalidJUnitTest extends BytecodeScanningDetector {

	private static final int SEEN_NOTHING = 0;

	private static final int SEEN_ALOAD_0 = 1;

	private BugReporter bugReporter;
	private boolean checkedTestCaseClass;
	private boolean haveTestCaseClass;

	private int state;

	public InvalidJUnitTest(BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}



	boolean directChildOfTestCase;

	@Override
         public void visitClassContext(ClassContext classContext) {
		if (!enabled())
			return;
		
		JavaClass jClass = classContext.getJavaClass();

		try {
			if (!Repository.instanceOf(jClass, "junit.framework.TestCase"))
				return;
			
			if ((jClass.getAccessFlags() & ACC_ABSTRACT) == 0) {
				Method[] methods = jClass.getMethods();
				boolean foundTest = false;
				for (Method m : methods) {
					if (m.getName().startsWith("test")) {
						foundTest = true;
						break;
					}
				}
				if (!foundTest) {
					bugReporter.reportBug( new BugInstance( this, "IJU_NO_TESTS", LOW_PRIORITY)
							.addClass(jClass));
				}
			}

			JavaClass superClass = jClass.getSuperClass();
			directChildOfTestCase = superClass.getClassName().equals(
					"junit.framework.TestCase");
			jClass.accept(this);
		} catch (ClassNotFoundException cnfe) {
			bugReporter.reportMissingClass(cnfe);
		}

	}

	/**
	 * Check whether or not this detector should be enabled.
	 * The detector is disabled if the TestCase class cannot be found
	 * (meaning we don't have junit.jar on the aux classpath).
	 * 
	 * @return true if it should be enabled, false if not
	 */
	private boolean enabled() {
		if (!checkedTestCaseClass) {
			checkedTestCaseClass = true;
			try {
				Repository.lookupClass("junit.framework.TestCase");
				haveTestCaseClass = true;
			} catch (ClassNotFoundException e) {
				// No TestCase class, so don't bother running
				// the detector.
			}
		}
		
		return haveTestCaseClass;
	}

	@Override
         public void visit(Method obj) {
		if (getMethodName().equals("suite") && !obj.isStatic())
			bugReporter.reportBug(new BugInstance(this, "IJU_SUITE_NOT_STATIC",
					NORMAL_PRIORITY).addClassAndMethod(this));

	}

	private boolean sawSuperCall;

	@Override
         public void visit(Code obj) {
		if (!directChildOfTestCase
				&& (getMethodName().equals("setUp") || getMethodName().equals(
						"tearDown"))
				&& !getMethod().isPrivate()) {
			sawSuperCall = false;
			super.visit(obj);
			if (sawSuperCall)
				return;
			JavaClass we = Lookup.findSuperImplementor(getThisClass(),
					getMethodName(), "()V", bugReporter);
			if (we != null && !we.getClassName().equals("junit.framework.TestCase")) {
				// OK, got a bug
				bugReporter.reportBug(new BugInstance(this, getMethodName()
						.equals("setUp") ? "IJU_SETUP_NO_SUPER"
						: "IJU_TEARDOWN_NO_SUPER", NORMAL_PRIORITY)
						.addClassAndMethod(this));
			}
		}
	}

	@Override
         public void sawOpcode(int seen) {
		switch (state) {
		case SEEN_NOTHING:
			if (seen == ALOAD_0)
				state = SEEN_ALOAD_0;
			break;

		case SEEN_ALOAD_0:
			if ((seen == INVOKESPECIAL)
					&& (getNameConstantOperand().equals(getMethodName()))
					&& (getMethodSig().equals("()V")))
				sawSuperCall = true;
			state = SEEN_NOTHING;
			break;
		default:
			state = SEEN_NOTHING;
		}
	}
}

// vim:ts=4
