package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;
import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;

import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.ITASTTestUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;

@UserBased(ITASTTestUser.class)
public class ASTTest {

	@UserTestResults
	private static Events tests;

	public static void setTests(Events tests) {
		ASTTest.tests = tests;
	}

	// <editor-fold desc="For-Loop">
	@TestTest
	void test_testHasBelowNoForLoop_Success() {
		String testHasBelowNoForLoop_Success = "testHasBelowNoForLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoForLoop_Fail() {
		String testHasBelowNoForLoop_Fail = "testHasBelowNoForLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoForLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Statement was found:\n" + "   - Between line 6 (column 3) and line 8 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoForLoop_Success() {
		String testHasAtNoForLoop_Success = "testHasAtNoForLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoForLoop_Success));
	}

	@TestTest
	void test_testHasAtNoForLoop_Fail() {
		String testHasAtNoForLoop_Fail = "testHasAtNoForLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoForLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Statement was found:\n" + "   - Between line 6 (column 3) and line 8 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="For-Each-Loop">
	@TestTest
	void test_testHasBelowNoForEachLoop_Success() {
		String testHasBelowNoForEachLoop_Success = "testHasBelowNoForEachLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForEachLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoForEachLoop_Fail() {
		String testHasBelowNoForEachLoop_Fail = "testHasBelowNoForEachLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoForEachLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Each-Statement was found:\n"
						+ "   - Between line 12 (column 3) and line 14 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoForEachLoop_Success() {
		String testHasAtNoForEachLoop_Success = "testHasAtNoForEachLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoForEachLoop_Success));
	}

	@TestTest
	void test_testHasAtNoForEachLoop_Fail() {
		String testHasAtNoForEachLoop_Fail = "testHasAtNoForEachLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoForEachLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Each-Statement was found:\n"
						+ "   - Between line 12 (column 3) and line 14 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="While-Loop">

	@TestTest
	void test_testHasBelowNoWhileLoop_Success() {
		String testHasBelowNoWhileLoop_Success = "testHasBelowNoWhileLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoWhileLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoWhileLoop_Fail() {
		String testHasBelowNoWhileLoop_Fail = "testHasBelowNoWhileLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoWhileLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - While-Statement was found:\n"
						+ "   - Between line 19 (column 3) and line 22 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoWhileLoop_Success() {
		String testHasAtNoWhileLoop_Success = "testHasAtNoWhileLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoWhileLoop_Success));
	}

	@TestTest
	void test_testHasAtNoWhileLoop_Fail() {
		String testHasAtNoWhileLoop_Fail = "testHasAtNoWhileLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoWhileLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - While-Statement was found:\n"
						+ "   - Between line 19 (column 3) and line 22 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="Do-While-Loop">

	@TestTest
	void test_testHasBelowNoDoWhileLoop_Success() {
		String testHasBelowNoDoWhileLoop_Success = "testHasBelowNoDoWhileLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoDoWhileLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoDoWhileLoop_Fail() {
		String testHasBelowNoDoWhileLoop_Fail = "testHasBelowNoDoWhileLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoDoWhileLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - Do-While-Statement was found:\n"
						+ "   - Between line 27 (column 3) and line 30 (column 19)"));
	}

	@TestTest
	void test_testHasAtNoDoWhileLoop_Success() {
		String testHasAtNoDoWhileLoop_Success = "testHasAtNoDoWhileLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoDoWhileLoop_Success));
	}

	@TestTest
	void test_testHasAtNoDoWhileLoop_Fail() {
		String testHasAtNoDoWhileLoop_Fail = "testHasAtNoDoWhileLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoDoWhileLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - Do-While-Statement was found:\n"
						+ "   - Between line 27 (column 3) and line 30 (column 19)"));
	}
	// </editor-fold>

	// <editor-fold desc="Any-For-Loop">
	@TestTest
	void test_testHasBelowNoAnyForLoop_Success() {
		String testHasBelowNoAnyForLoop_Success = "testHasBelowNoAnyForLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyForLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoAnyForLoop_Fail() {
		String testHasBelowNoAnyForLoop_Fail = "testHasBelowNoAnyForLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoAnyForLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Statement was found:\n" + "   - Between line 6 (column 3) and line 8 (column 3)\n"
						+ "  - For-Each-Statement was found:\n"
						+ "   - Between line 12 (column 3) and line 14 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoAnyForLoop_Success() {
		String testHasAtNoAnyForLoop_Success = "testHasAtNoAnyForLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAnyForLoop_Success));
	}

	@TestTest
	void test_testHasAtNoAnyForLoop_Fail() {
		String testHasAtNoAnyForLoop_Fail = "testHasAtNoAnyForLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoAnyForLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Statement was found:\n" + "   - Between line 6 (column 3) and line 8 (column 3)\n"
						+ "  - For-Each-Statement was found:\n"
						+ "   - Between line 12 (column 3) and line 14 (column 3)"

				));
	}
	// </editor-fold>

	// <editor-fold desc="Any-While-Loop">

	@TestTest
	void test_testHasBelowNoAnyWhileLoop_Success() {
		String testHasBelowNoAnyWhileLoop_Success = "testHasBelowNoAnyWhileLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyWhileLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoAnyWhileLoop_Fail() {
		String testHasBelowNoAnyWhileLoop_Fail = "testHasBelowNoAnyWhileLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoAnyWhileLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - While-Statement was found:\n"
						+ "   - Between line 19 (column 3) and line 22 (column 3)\n"
						+ "  - Do-While-Statement was found:\n"
						+ "   - Between line 27 (column 3) and line 30 (column 19)"));
	}

	@TestTest
	void test_testHasAtNoAnyWhileLoop_Success() {
		String testHasAtNoAnyWhileLoop_Success = "testHasAtNoAnyWhileLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAnyWhileLoop_Success));
	}

	@TestTest
	void test_testHasAtNoAnyWhileLoop_Fail() {
		String testHasAtNoAnyWhileLoop_Fail = "testHasAtNoAnyWhileLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoAnyWhileLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - While-Statement was found:\n"
						+ "   - Between line 19 (column 3) and line 22 (column 3)\n"
						+ "  - Do-While-Statement was found:\n"
						+ "   - Between line 27 (column 3) and line 30 (column 19)"));
	}
	// </editor-fold>

	// <editor-fold desc="Any-Loop">
	@TestTest
	void test_testHasBelowNoAnyLoop_Success() {
		String testHasBelowNoAnyLoop_Success = "testHasBelowNoAnyLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyLoop_Success));
	}

	@TestTest
	void test_testHasBelowNoAnyLoop_Fail() {
		String testHasBelowNoAnyLoop_Fail = "testHasBelowNoAnyLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoAnyLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Statement was found:\n" + "   - Between line 6 (column 3) and line 8 (column 3)\n"
						+ "  - For-Each-Statement was found:\n"
						+ "   - Between line 12 (column 3) and line 14 (column 3)\n"
						+ "  - While-Statement was found:\n"
						+ "   - Between line 19 (column 3) and line 22 (column 3)\n"
						+ "  - Do-While-Statement was found:\n"
						+ "   - Between line 27 (column 3) and line 30 (column 19)"));
	}

	@TestTest
	void test_testHasAtNoAnyLoop_Success() {
		String testHasAtNoAnyLoop_Success = "testHasAtNoAnyLoop_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAnyLoop_Success));
	}

	@TestTest
	void test_testHasAtNoAnyLoop_Fail() {
		String testHasAtNoAnyLoop_Fail = "testHasAtNoAnyLoop_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoAnyLoop_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
						+ "  - For-Statement was found:\n" + "   - Between line 6 (column 3) and line 8 (column 3)\n"
						+ "  - For-Each-Statement was found:\n"
						+ "   - Between line 12 (column 3) and line 14 (column 3)\n"
						+ "  - While-Statement was found:\n"
						+ "   - Between line 19 (column 3) and line 22 (column 3)\n"
						+ "  - Do-While-Statement was found:\n"
						+ "   - Between line 27 (column 3) and line 30 (column 19)"));
	}
	// </editor-fold>

	// <editor-fold desc="If-Conditional">

	@TestTest
	void test_testHasBelowNoIfConditional_Success() {
		String testHasBelowNoIfConditional_Success = "testHasBelowNoIfConditional_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoIfConditional_Success));
	}

	@TestTest
	void test_testHasBelowNoIfConditional_Fail() {
		String testHasBelowNoIfConditional_Fail = "testHasBelowNoIfConditional_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoIfConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
						+ "  - If-Statement was found:\n" + "   - Between line 9 (column 3) and line 15 (column 3)\n"
						+ "   - Between line 11 (column 10) and line 15 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoIfConditional_Success() {
		String testHasAtNoIfConditional_Success = "testHasAtNoIfConditional_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoIfConditional_Success));
	}

	@TestTest
	void test_testHasAtNoIfConditional_Fail() {
		String testHasAtNoIfConditional_Fail = "testHasAtNoIfConditional_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoIfConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
						+ "  - If-Statement was found:\n" + "   - Between line 9 (column 3) and line 15 (column 3)\n"
						+ "   - Between line 11 (column 10) and line 15 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="Switch-Conditional">

	@TestTest
	void test_testHasBelowNoSwitchConditional_Success() {
		String testHasBelowNoSwitchConditional_Success = "testHasBelowNoSwitchConditional_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoSwitchConditional_Success));
	}

	@TestTest
	void test_testHasBelowNoSwitchConditional_Fail() {
		String testHasBelowNoSwitchConditional_Fail = "testHasBelowNoSwitchConditional_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoSwitchConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
						+ "  - Switch-Statement was found:\n"
						+ "   - Between line 20 (column 3) and line 30 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoSwitchConditional_Success() {
		String testHasAtNoSwitchConditional_Success = "testHasAtNoSwitchConditional_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoSwitchConditional_Success));
	}

	@TestTest
	void test_testHasAtNoSwitchConditional_Fail() {
		String testHasAtNoSwitchConditional_Fail = "testHasAtNoSwitchConditional_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoSwitchConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
						+ "  - Switch-Statement was found:\n"
						+ "   - Between line 20 (column 3) and line 30 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="Any-Conditional">

	@TestTest
	void test_testHasBelowNoAnyConditional_Success() {
		String testHasBelowNoAnyConditional_Success = "testHasBelowNoAnyConditional_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyConditional_Success));
	}

	@TestTest
	void test_testHasBelowNoAnyConditional_Fail() {
		String testHasBelowNoAnyConditional_Fail = "testHasBelowNoAnyConditional_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoAnyConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
						+ "  - If-Statement was found:\n" + "   - Between line 9 (column 3) and line 15 (column 3)\n"
						+ "   - Between line 11 (column 10) and line 15 (column 3)\n"
						+ "  - Switch-Statement was found:\n"
						+ "   - Between line 20 (column 3) and line 30 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoAnyConditional_Success() {
		String testHasAtNoAnyConditional_Success = "testHasAtNoAnyConditional_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAnyConditional_Success));
	}

	@TestTest
	void test_testHasAtNoAnyConditional_Fail() {
		String testHasAtNoAnyConditional_Fail = "testHasAtNoAnyConditional_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoAnyConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
						+ "  - If-Statement was found:\n" + "   - Between line 9 (column 3) and line 15 (column 3)\n"
						+ "   - Between line 11 (column 10) and line 15 (column 3)\n"
						+ "  - Switch-Statement was found:\n" + "   - Between line 20 (column 3) and line 30 (column 3)"

				));
	}
	// </editor-fold>

	// <editor-fold desc="Any-Class">

	@TestTest
	void test_testHasBelowNoAnyClass_Success() {
		String testHasBelowNoAnyClass_Success = "testHasBelowNoAnyClass_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyClass_Success));
	}

	@TestTest
	void test_testHasBelowNoAnyClass_Fail() {
		String testHasBelowNoAnyClass_Fail = "testHasBelowNoAnyClass_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasBelowNoAnyClass_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAnyKindsOfClasses.java:\n"
						+ "  - Local-Class-Statement was found:\n"
						+ "   - Between line 6 (column 3) and line 7 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoAnyClass_Success() {
		String testHasAtNoAnyClass_Success = "testHasAtNoAnyClass_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAnyClass_Success));
	}

	@TestTest
	void test_testHasAtNoAnyClass_Fail() {
		String testHasAtNoAnyClass_Fail = "testHasAtNoAnyClass_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoAnyClass_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAnyKindsOfClasses.java:\n"
						+ "  - Local-Class-Statement was found:\n"
						+ "   - Between line 6 (column 3) and line 7 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="Assert-ExceptionHandling">

	@TestTest
	void test_testHasBelowNoAssertExceptionHandling_Success() {
		String testHasBelowNoAssertExceptionHandling_Success = "testHasBelowNoAssertExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAssertExceptionHandling_Success));
	}

	@TestTest
	void test_testHasBelowNoAssertExceptionHandling_Fail() {
		String testHasBelowNoAssertExceptionHandling_Fail = "testHasBelowNoAssertExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAssertExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Assert-Statement was found:\n"
						+ "   - Between line 7 (column 3) and line 7 (column 16)"));
	}

	@TestTest
	void test_testHasAtNoAssertExceptionHandling_Success() {
		String testHasAtNoAssertExceptionHandling_Success = "testHasAtNoAssertExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAssertExceptionHandling_Success));
	}

	@TestTest
	void test_testHasAtNoAssertExceptionHandling_Fail() {
		String testHasAtNoAssertExceptionHandling_Fail = "testHasAtNoAssertExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAssertExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Assert-Statement was found:\n"
						+ "   - Between line 7 (column 3) and line 7 (column 16)"));
	}
	// </editor-fold>

	// <editor-fold desc="Throw-ExceptionHandling">

	@TestTest
	void test_testHasBelowNoThrowExceptionHandling_Success() {
		String testHasBelowNoThrowExceptionHandling_Success = "testHasBelowNoThrowExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoThrowExceptionHandling_Success));
	}

	@TestTest
	void test_testHasBelowNoThrowExceptionHandling_Fail() {
		String testHasBelowNoThrowExceptionHandling_Fail = "testHasBelowNoThrowExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoThrowExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Throw-Statement was found:\n"
						+ "   - Between line 11 (column 3) and line 11 (column 54)"));
	}

	@TestTest
	void test_testHasAtNoThrowExceptionHandling_Success() {
		String testHasAtNoThrowExceptionHandling_Success = "testHasAtNoThrowExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoThrowExceptionHandling_Success));
	}

	@TestTest
	void test_testHasAtNoThrowExceptionHandling_Fail() {
		String testHasAtNoThrowExceptionHandling_Fail = "testHasAtNoThrowExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoThrowExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Throw-Statement was found:\n"
						+ "   - Between line 11 (column 3) and line 11 (column 54)"));
	}
	// </editor-fold>

	// <editor-fold desc="TryCatch-ExceptionHandling">

	@TestTest
	void test_testHasBelowNoTryCatchExceptionHandling_Success() {
		String testHasBelowNoTryCatchExceptionHandling_Success = "testHasBelowNoTryCatchExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoTryCatchExceptionHandling_Success));
	}

	@TestTest
	void test_testHasBelowNoTryCatchExceptionHandling_Fail() {
		String testHasBelowNoTryCatchExceptionHandling_Fail = "testHasBelowNoTryCatchExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoTryCatchExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Try-Statement was found:\n" + "   - Between line 15 (column 3) and line 18 (column 3)\n"
						+ "  - Catch-Statement was found:\n"
						+ "   - Between line 17 (column 5) and line 18 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoTryCatchExceptionHandling_Success() {
		String testHasAtNoTryCatchExceptionHandling_Success = "testHasAtNoTryCatchExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoTryCatchExceptionHandling_Success));
	}

	@TestTest
	void test_testHasAtNoTryCatchExceptionHandling_Fail() {
		String testHasAtNoTryCatchExceptionHandling_Fail = "testHasAtNoTryCatchExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoTryCatchExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Try-Statement was found:\n" + "   - Between line 15 (column 3) and line 18 (column 3)\n"
						+ "  - Catch-Statement was found:\n"
						+ "   - Between line 17 (column 5) and line 18 (column 3)"));
	}
	// </editor-fold>

	// <editor-fold desc="Any-ExceptionHandling">

	@TestTest
	void test_testHasBelowNoAnyExceptionHandling_Success() {
		String testHasBelowNoAnyExceptionHandling_Success = "testHasBelowNoAnyExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyExceptionHandling_Success));
	}

	@TestTest
	void test_testHasBelowNoAnyExceptionHandling_Fail() {
		String testHasBelowNoAnyExceptionHandling_Fail = "testHasBelowNoAnyExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAnyExceptionHandling_Fail,
				AssertionError.class,
				"Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Assert-Statement was found:\n"
						+ "   - Between line 7 (column 3) and line 7 (column 16)\n" + "  - Throw-Statement was found:\n"
						+ "   - Between line 11 (column 3) and line 11 (column 54)\n" + "  - Try-Statement was found:\n"
						+ "   - Between line 15 (column 3) and line 18 (column 3)\n"
						+ "  - Catch-Statement was found:\n"
						+ "   - Between line 17 (column 5) and line 18 (column 3)"));
	}

	@TestTest
	void test_testHasAtNoAnyExceptionHandling_Success() {
		String testHasAtNoAnyExceptionHandling_Success = "testHasAtNoAnyExceptionHandling_Success";
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAnyExceptionHandling_Success));
	}

	@TestTest
	void test_testHasAtNoAnyExceptionHandling_Fail() {
		String testHasAtNoAnyExceptionHandling_Fail = "testHasAtNoAnyExceptionHandling_Fail";
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHasAtNoAnyExceptionHandling_Fail, AssertionError.class, "Unwanted statement found:\n"
						+ " - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
						+ "  - Assert-Statement was found:\n"
						+ "   - Between line 7 (column 3) and line 7 (column 1)\n" + "  - Throw-Statement was found:\n"
						+ "   - Between line 11 (column 3) and line 11 (column 54)\n" + "  - Try-Statement was found:\n"
						+ "   - Between line 15 (column 3) and line 18 (column 3)\n"
						+ "  - Catch-Statement was found:\n"
						+ "   - Between line 17 (column 5) and line 18 (column 3)"));
	}
	// </editor-fold>
}
