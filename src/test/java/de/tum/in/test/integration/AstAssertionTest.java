package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;
import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.AstAssertionUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;

@UserBased(AstAssertionUser.class)
public class AstAssertionTest {

    @UserTestResults
    private static Events tests;

    public static void setTests(Events tests) {
        AstAssertionTest.tests = tests;
    }

    @Nested
    @DisplayName("For-Loop-Test-Tests")
    class ForLoopTestTests {
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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - For-Statement was found:\n" + "   - Between line 9 (column 3) and line 11 (column 3)"));
        }

    }

    @Nested
    @DisplayName("For-Each-Loop-Test-Tests")
    class ForEachLoopTestTests {
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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - For-Each-Statement was found:\n"
                            + "   - Between line 15 (column 3) and line 17 (column 3)"));
        }

    }

    @Nested
    @DisplayName("While-Loop-Test-Tests")
    class WhileLoopTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - While-Statement was found:\n"
                            + "   - Between line 22 (column 3) and line 25 (column 3)"));
        }

    }

    @Nested
    @DisplayName("Do-While-Loop-Test-Tests")
    class DoWhileLoopTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - Do-While-Statement was found:\n"
                            + "   - Between line 30 (column 3) and line 33 (column 42)"));
        }

    }

    @Nested
    @DisplayName("Any-For-Loop-Test-Tests")
    class AnyForLoopTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - For-Statement was found:\n" + "   - Between line 9 (column 3) and line 11 (column 3)\n"
                            + "  - For-Each-Statement was found:\n"
                            + "   - Between line 15 (column 3) and line 17 (column 3)"));
        }

    }

    @Nested
    @DisplayName("Any-While-Loop-Test-Tests")
    class AnyWhileLoopTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - While-Statement was found:\n"
                            + "   - Between line 22 (column 3) and line 25 (column 3)\n"
                            + "  - Do-While-Statement was found:\n"
                            + "   - Between line 30 (column 3) and line 33 (column 42)"));
        }

    }

    @Nested
    @DisplayName("Any-Loop-Test-Tests")
    class AnyLoopTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java:\n"
                            + "  - For-Statement was found:\n" + "   - Between line 9 (column 3) and line 11 (column 3)\n"
                            + "  - For-Each-Statement was found:\n"
                            + "   - Between line 15 (column 3) and line 17 (column 3)\n"
                            + "  - While-Statement was found:\n"
                            + "   - Between line 22 (column 3) and line 25 (column 3)\n"
                            + "  - Do-While-Statement was found:\n"
                            + "   - Between line 30 (column 3) and line 33 (column 42)"));
        }

    }

    @Nested
    @DisplayName("If-Statement-Test-Tests")
    class IfStatementTestTests {

        @TestTest
        void test_testHasBelowNoIfStatement_Success() {
            String testHasBelowNoIfConditional_Success = "testHasBelowNoIfStatement_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoIfConditional_Success));
        }

        @TestTest
        void test_testHasBelowNoIfStatement_Fail() {
            String testHasBelowNoIfConditional_Fail = "testHasBelowNoIfStatement_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoIfConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - If-Statement was found:\n" + "   - Between line 9 (column 3) and line 15 (column 3)\n"
                            + "   - Between line 11 (column 10) and line 15 (column 3)"));
        }

    }

    @Nested
    @DisplayName("If-Expression-Test-Tests")
    class IfExpressionTestTests {

        @TestTest
        void test_testHasBelowNoIfExpression_Success() {
            String testHasBelowNoIfConditional_Success = "testHasBelowNoIfExpression_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoIfConditional_Success));
        }

        @TestTest
        void test_testHasBelowNoIfExpression_Fail() {
            String testHasBelowNoIfConditional_Fail = "testHasBelowNoIfExpression_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoIfConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - If-Expression was found:\n" + "   - Between line 20 (column 22) and line 20 (column 64)\n"
                            + "   - Between line 20 (column 42) and line 20 (column 63)"));
        }

    }

    @Nested
    @DisplayName("Switch-Statement-Test-Tests")
    class SwitchStatementTestTests {

        @TestTest
        void test_testHasBelowNoSwitchStatement_Success() {
            String testHasBelowNoSwitchConditional_Success = "testHasBelowNoSwitchStatement_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoSwitchConditional_Success));
        }

        @TestTest
        void test_testHasBelowNoSwitchStatement_Fail() {
            String testHasBelowNoSwitchConditional_Fail = "testHasBelowNoSwitchStatement_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoSwitchConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - Switch-Statement was found:\n"
                            + "   - Between line 25 (column 3) and line 35 (column 3)"));
        }

    }

    @Nested
    @DisplayName("Switch-Expression-Test-Tests")
    class SwitchExpressionTestTests {

        @TestTest
        void test_testHasBelowNoSwitchExpression_Success() {
            String testHasBelowNoSwitchConditional_Success = "testHasBelowNoSwitchExpression_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoSwitchConditional_Success));
        }

        /*@TestTest
        void test_testHasBelowNoSwitchExpression_Fail() {
            String testHasBelowNoSwitchConditional_Fail = "testHasBelowNoSwitchExpression_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoSwitchConditional_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - Switch-Expression was found:\n"
                            + "   - Between line 41 (column 5) and line 45 (column 5)"));
        }*/

    }

    @Nested
    @DisplayName("Any-If-Test-Tests")
    class AnyIfTestTests {

        @TestTest
        void test_testHasBelowNoAnyIf_Success() {
            String testHasBelowNoAnyClass_Success = "testHasBelowNoAnyIf_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyClass_Success));
        }

        @TestTest
        void test_testHasBelowNoAnyIf_Fail() {
            String testHasBelowNoAnyClass_Fail = "testHasBelowNoAnyIf_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoAnyClass_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - If-Statement was found:\n"
                            + "   - Between line 9 (column 3) and line 15 (column 3)\n"
                            + "   - Between line 11 (column 10) and line 15 (column 3)\n"
                            + "  - If-Expression was found:\n"
                            + "   - Between line 20 (column 22) and line 20 (column 64)\n"
                            + "   - Between line 20 (column 42) and line 20 (column 63)"));
        }

    }

    @Nested
    @DisplayName("Any-Switch-Test-Tests")
    class AnySwitchTestTests {

        @TestTest
        void test_testHasBelowNoAnySwitch_Success() {
            String testHasBelowNoAnyClass_Success = "testHasBelowNoAnySwitch_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyClass_Success));
        }

        @TestTest
        void test_testHasBelowNoAnySwitch_Fail() {
            String testHasBelowNoAnyClass_Fail = "testHasBelowNoAnySwitch_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoAnyClass_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - Switch-Statement was found:\n"
                            + "   - Between line 25 (column 3) and line 35 (column 3)"/*\n"
                            + "  - Switch-Expression was found:\n"
                            + "   - Between line 41 (column 5) and line 45 (column 5)"*/));
        }

    }

    @Nested
    @DisplayName("Any-Conditional-Test-Tests")
    class AnyConditionalTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java:\n"
                            + "  - If-Statement was found:\n"
                            + "   - Between line 9 (column 3) and line 15 (column 3)\n"
                            + "   - Between line 11 (column 10) and line 15 (column 3)\n"
                            + "  - If-Expression was found:\n"
                            + "   - Between line 20 (column 22) and line 20 (column 64)\n"
                            + "   - Between line 20 (column 42) and line 20 (column 63)\n"
                            + "  - Switch-Statement was found:\n"
                            + "   - Between line 25 (column 3) and line 35 (column 3)"/*\n"
                            + "  - Switch-Expression was found:\n"
                            + "   - Between line 41 (column 5) and line 45 (column 5)"*/
                    ));
        }

    }

    @Nested
    @DisplayName("Class-Test-Tests")
    class ClassTestTests {

        @TestTest
        void test_testHasBelowNoClass_Success() {
            String testHasBelowNoClass_Success = "testHasBelowNoClass_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoClass_Success));
        }

        @TestTest
        void test_testHasBelowNoClass_Fail() {
            String testHasBelowNoClass_Fail = "testHasBelowNoClass_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoClass_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAnyKindsOfClasses.java:\n"
                            + "  - Local-Class-Statement was found:\n"
                            + "   - Between line 6 (column 3) and line 7 (column 3)"));
        }

    }

    @Nested
    @DisplayName("Record-Test-Tests")
    class RecordTestTests {

        @TestTest
        void test_testHasBelowNoRecord_Success() {
            String testHasBelowNoRecord_Success = "testHasBelowNoRecord_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoRecord_Success));
        }

        /*@TestTest
        void test_testHasBelowNoRecord_Fail() {
            String testHasBelowNoRecord_Fail = "testHasBelowNoRecord_Fail";
            tests.assertThatEvents().haveExactly(1,
                    testFailedWith(testHasBelowNoRecord_Fail, AssertionError.class, "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAnyKindsOfClasses.java:\n"
                            + "  - Local-Record-Statement was found:\n"
                            + "   - Between line 11 (column 3) and line 13 (column 3)"));
        }*/

    }

    @Nested
    @DisplayName("Assert-Exception-Handling-Test-Tests")
    class AssertExceptionHandlingTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
                            + "  - Assert-Statement was found:\n"
                            + "   - Between line 8 (column 3) and line 8 (column 40)"));
        }

    }

    @Nested
    @DisplayName("Throw-Exception-Handling-Test-Tests")
    class ThrowExceptionHandlingTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
                            + "  - Throw-Statement was found:\n"
                            + "   - Between line 12 (column 3) and line 12 (column 54)"));
        }

    }

    @Nested
    @DisplayName("Catch-Exception-Handling-Test-Tests")
    class CatchExceptionHandlingTestTests {

        @TestTest
        void test_testHasBelowNoCatchExceptionHandling_Success() {
            String testHasBelowNoCatchExceptionHandling_Success = "testHasBelowNoCatchExceptionHandling_Success";
            tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoCatchExceptionHandling_Success));
        }

        @TestTest
        void test_testHasBelowNoCatchExceptionHandling_Fail() {
            String testHasBelowNoCatchExceptionHandling_Fail = "testHasBelowNoCatchExceptionHandling_Fail";
            tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoCatchExceptionHandling_Fail,
                    AssertionError.class,
                    "Unwanted statement found:\n"
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
                            + "  - Catch-Statement was found:\n"
                            + "   - Between line 18 (column 5) and line 19 (column 3)"));
        }

    }

    @Nested
    @DisplayName("Any-Exception-Handling-Test-Tests")
    class AnyExceptionHandlingTestTests {

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
                            + " - In ./src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java:\n"
                            + "  - Assert-Statement was found:\n"
                            + "   - Between line 8 (column 3) and line 8 (column 40)\n" + "  - Throw-Statement was found:\n"
                            + "   - Between line 12 (column 3) and line 12 (column 54)\n"
                            + "  - Catch-Statement was found:\n"
                            + "   - Between line 18 (column 5) and line 19 (column 3)"));
        }

    }
}
