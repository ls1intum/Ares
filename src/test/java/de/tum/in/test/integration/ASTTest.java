package de.tum.in.test.integration;

import de.tum.in.test.integration.testuser.ITASTTestUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import org.junit.platform.testkit.engine.Events;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;
import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;

@UserBased(ITASTTestUser.class)
public class ASTTest {

    @UserTestResults
    private static Events tests;

    public static void setTests(Events tests) {
        ASTTest.tests = tests;
    }

    //<editor-fold desc="For-Loop">
    @TestTest
    void test_testHasBelowNoForLoop_Success() {
        String testHasBelowNoForLoop_Success = "testHasBelowNoForLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoForLoop_Fail() {
        String testHasBelowNoForLoop_Fail = "testHasBelowNoForLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoForLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                        """));
    }

    @TestTest
    void test_testHasAtNoForLoop_Success() {
        String testHasAtNoForLoop_Success = "testHasAtNoForLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoForLoop_Success));
    }

    @TestTest
    void test_testHasAtNoForLoop_Fail() {
        String testHasAtNoForLoop_Fail = "testHasAtNoForLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoForLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                        """));
    }
    //</editor-fold>

    // <editor-fold desc="For-Each-Loop">
    @TestTest
    void test_testHasBelowNoForEachLoop_Success() {
        String testHasBelowNoForEachLoop_Success = "testHasBelowNoForEachLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForEachLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoForEachLoop_Fail() {
        String testHasBelowNoForEachLoop_Fail = "testHasBelowNoForEachLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoForEachLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                        """));
    }

    @TestTest
    void test_testHasAtNoForEachLoop_Success() {
        String testHasAtNoForEachLoop_Success = "testHasAtNoForEachLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoForEachLoop_Success));
    }

    @TestTest
    void test_testHasAtNoForEachLoop_Fail() {
        String testHasAtNoForEachLoop_Fail = "testHasAtNoForEachLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoForEachLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="While-Loop">

    @TestTest
    void test_testHasBelowNoWhileLoop_Success() {
        String testHasBelowNoWhileLoop_Success = "testHasBelowNoWhileLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoWhileLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoWhileLoop_Fail() {
        String testHasBelowNoWhileLoop_Fail = "testHasBelowNoWhileLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoWhileLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                        """));
    }

    @TestTest
    void test_testHasAtNoWhileLoop_Success() {
        String testHasAtNoWhileLoop_Success = "testHasAtNoWhileLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoWhileLoop_Success));
    }

    @TestTest
    void test_testHasAtNoWhileLoop_Fail() {
        String testHasAtNoWhileLoop_Fail = "testHasAtNoWhileLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoWhileLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="Do-While-Loop">

    @TestTest
    void test_testHasBelowNoDoWhileLoop_Success() {
        String testHasBelowNoDoWhileLoop_Success = "testHasBelowNoDoWhileLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoDoWhileLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoDoWhileLoop_Fail() {
        String testHasBelowNoDoWhileLoop_Fail = "testHasBelowNoDoWhileLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoDoWhileLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        """));
    }

    @TestTest
    void test_testHasAtNoDoWhileLoop_Success() {
        String testHasAtNoDoWhileLoop_Success = "testHasAtNoDoWhileLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoDoWhileLoop_Success));
    }

    @TestTest
    void test_testHasAtNoDoWhileLoop_Fail() {
        String testHasAtNoDoWhileLoop_Fail = "testHasAtNoDoWhileLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoDoWhileLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-For-Loop">
    @TestTest
    void test_testHasBelowNoAllForLoop_Success() {

        String testHasBelowNoAllForLoop_Success = "testHasBelowNoAllForLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllForLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoAllForLoop_Fail() {
        String testHasBelowNoAllForLoop_Fail = "testHasBelowNoAllForLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllForLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllForLoop_Success() {
        String testHasAtNoAllForLoop_Success = "testHasAtNoAllForLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllForLoop_Success));
    }

    @TestTest
    void test_testHasAtNoAllForLoop_Fail() {
        String testHasAtNoAllForLoop_Fail = "testHasAtNoAllForLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllForLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-While-Loop">

    @TestTest
    void test_testHasBelowNoAllWhileLoop_Success() {
        String testHasBelowNoAllWhileLoop_Success = "testHasBelowNoAllWhileLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllWhileLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoAllWhileLoop_Fail() {
        String testHasBelowNoAllWhileLoop_Fail = "testHasBelowNoAllWhileLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllWhileLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllWhileLoop_Success() {
        String testHasAtNoAllWhileLoop_Success = "testHasAtNoAllWhileLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllWhileLoop_Success));
    }

    @TestTest
    void test_testHasAtNoAllWhileLoop_Fail() {
        String testHasAtNoAllWhileLoop_Fail = "testHasAtNoAllWhileLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllWhileLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-Loop">
    @TestTest
    void test_testHasBelowNoAllLoop_Success() {
        String testHasBelowNoAllLoop_Success = "testHasBelowNoAllLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllLoop_Success));
    }

    @TestTest
    void test_testHasBelowNoAllLoop_Fail() {
        String testHasBelowNoAllLoop_Fail = "testHasBelowNoAllLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops2.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllLoop_Success() {
        String testHasAtNoAllLoop_Success = "testHasAtNoAllLoop_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllLoop_Success));
    }

    @TestTest
    void test_testHasAtNoAllLoop_Fail() {
        String testHasAtNoAllLoop_Fail = "testHasAtNoAllLoop_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllLoop_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java:
                          - For-Statement was found:
                            -  Between line 6 (column 9) and line 8 (column 9)
                          - For-Each-Statement was found:
                            -  Between line 12 (column 9) and line 14 (column 9)
                          - While-Statement was found:
                            -  Between line 19 (column 9) and line 22 (column 9)
                          - Do-While-Statement was found:
                            -  Between line 28 (column 9) and line 31 (column 25)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="If-Conditional">

    @TestTest
    void test_testHasBelowNoIfConditional_Success() {
        String testHasBelowNoIfConditional_Success = "testHasBelowNoIfConditional_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoIfConditional_Success));
    }

    @TestTest
    void test_testHasBelowNoIfConditional_Fail() {
        String testHasBelowNoIfConditional_Fail = "testHasBelowNoIfConditional_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoIfConditional_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals2.java:
                          - If-Statement was found:
                            -  Between line 9 (column 9) and line 15 (column 9)
                            -  Between line 11 (column 16) and line 15 (column 9)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java:
                          - If-Statement was found:
                            -  Between line 9 (column 9) and line 15 (column 9)
                            -  Between line 11 (column 16) and line 15 (column 9)
                        """));
    }

    @TestTest
    void test_testHasAtNoIfConditional_Success() {
        String testHasAtNoIfConditional_Success = "testHasAtNoIfConditional_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoIfConditional_Success));
    }

    @TestTest
    void test_testHasAtNoIfConditional_Fail() {
        String testHasAtNoIfConditional_Fail = "testHasAtNoIfConditional_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoIfConditional_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java:
                          - If-Statement was found:
                            -  Between line 9 (column 9) and line 15 (column 9)
                            -  Between line 11 (column 16) and line 15 (column 9)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="Switch-Conditional">

    @TestTest
    void test_testHasBelowNoSwitchConditional_Success() {
        String testHasBelowNoSwitchConditional_Success = "testHasBelowNoSwitchConditional_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoSwitchConditional_Success));
    }

    @TestTest
    void test_testHasBelowNoSwitchConditional_Fail() {
        String testHasBelowNoSwitchConditional_Fail = "testHasBelowNoSwitchConditional_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoSwitchConditional_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals2.java:
                          - Switch-Statement was found:
                            -  Between line 20 (column 9) and line 30 (column 9)
                          - Switch-Entry-Statement was found:
                            -  Between line 21 (column 13) and line 23 (column 22)
                            -  Between line 24 (column 13) and line 26 (column 22)
                            -  Between line 27 (column 13) and line 29 (column 22)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java:
                          - Switch-Statement was found:
                            -  Between line 20 (column 9) and line 30 (column 9)
                          - Switch-Entry-Statement was found:
                            -  Between line 21 (column 13) and line 23 (column 22)
                            -  Between line 24 (column 13) and line 26 (column 22)
                            -  Between line 27 (column 13) and line 29 (column 22)
                        """));
    }

    @TestTest
    void test_testHasAtNoSwitchConditional_Success() {
        String testHasAtNoSwitchConditional_Success = "testHasAtNoSwitchConditional_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoSwitchConditional_Success));
    }

    @TestTest
    void test_testHasAtNoSwitchConditional_Fail() {
        String testHasAtNoSwitchConditional_Fail = "testHasAtNoSwitchConditional_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoSwitchConditional_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java:
                          - Switch-Statement was found:
                            -  Between line 20 (column 9) and line 30 (column 9)
                          - Switch-Entry-Statement was found:
                            -  Between line 21 (column 13) and line 23 (column 22)
                            -  Between line 24 (column 13) and line 26 (column 22)
                            -  Between line 27 (column 13) and line 29 (column 22)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-Conditional">

    @TestTest
    void test_testHasBelowNoAllConditional_Success() {
        String testHasBelowNoAllConditional_Success = "testHasBelowNoAllConditional_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllConditional_Success));
    }

    @TestTest
    void test_testHasBelowNoAllConditional_Fail() {
        String testHasBelowNoAllConditional_Fail = "testHasBelowNoAllConditional_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllConditional_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals2.java:
                          - If-Statement was found:
                            -  Between line 9 (column 9) and line 15 (column 9)
                            -  Between line 11 (column 16) and line 15 (column 9)
                          - Switch-Statement was found:
                            -  Between line 20 (column 9) and line 30 (column 9)
                          - Switch-Entry-Statement was found:
                            -  Between line 21 (column 13) and line 23 (column 22)
                            -  Between line 24 (column 13) and line 26 (column 22)
                            -  Between line 27 (column 13) and line 29 (column 22)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java:
                          - If-Statement was found:
                            -  Between line 9 (column 9) and line 15 (column 9)
                            -  Between line 11 (column 16) and line 15 (column 9)
                          - Switch-Statement was found:
                            -  Between line 20 (column 9) and line 30 (column 9)
                          - Switch-Entry-Statement was found:
                            -  Between line 21 (column 13) and line 23 (column 22)
                            -  Between line 24 (column 13) and line 26 (column 22)
                            -  Between line 27 (column 13) and line 29 (column 22)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllConditional_Success() {
        String testHasAtNoAllConditional_Success = "testHasAtNoAllConditional_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllConditional_Success));
    }

    @TestTest
    void test_testHasAtNoAllConditional_Fail() {
        String testHasAtNoAllConditional_Fail = "testHasAtNoAllConditional_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllConditional_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java:
                          - If-Statement was found:
                            -  Between line 9 (column 9) and line 15 (column 9)
                            -  Between line 11 (column 16) and line 15 (column 9)
                          - Switch-Statement was found:
                            -  Between line 20 (column 9) and line 30 (column 9)
                          - Switch-Entry-Statement was found:
                            -  Between line 21 (column 13) and line 23 (column 22)
                            -  Between line 24 (column 13) and line 26 (column 22)
                            -  Between line 27 (column 13) and line 29 (column 22)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="New-Function">

    @TestTest
    void test_testHasBelowNoExplicitConstructorInvocationFunction_Success() {
        String testHasBelowNoExplicitConstructorInvocationFunction_Success = "testHasBelowNoExplicitConstructorInvocationFunction_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoExplicitConstructorInvocationFunction_Success));
    }

    @TestTest
    void test_testHasBelowNoExplicitConstructorInvocationFunction_Fail() {
        String testHasBelowNoExplicitConstructorInvocationFunction_Fail = "testHasBelowNoExplicitConstructorInvocationFunction_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoExplicitConstructorInvocationFunction_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java:
                          - Explicit-Constructor-Invocation-Statement was found:
                            -  Between line 8 (column 9) and line 8 (column 21)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions2.java:
                          - Explicit-Constructor-Invocation-Statement was found:
                            -  Between line 8 (column 9) and line 8 (column 21)
                        """));
    }

    @TestTest
    void test_testHasAtNoExplicitConstructorInvocationFunction_Success() {
        String testHasAtNoExplicitConstructorInvocationFunction_Success = "testHasAtNoExplicitConstructorInvocationFunction_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoExplicitConstructorInvocationFunction_Success));
    }

    @TestTest
    void test_testHasAtNoExplicitConstructorInvocationFunction_Fail() {
        String testHasAtNoExplicitConstructorInvocationFunction_Fail = "testHasAtNoExplicitConstructorInvocationFunction_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoExplicitConstructorInvocationFunction_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java:
                          - Explicit-Constructor-Invocation-Statement was found:
                            -  Between line 8 (column 9) and line 8 (column 21)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="Return-Function">

    @TestTest
    void test_testHasBelowNoReturnFunction_Success() {
        String testHasBelowNoReturnFunction_Success = "testHasBelowNoReturnFunction_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoReturnFunction_Success));
    }

    @TestTest
    void test_testHasBelowNoReturnFunction_Fail() {
        String testHasBelowNoReturnFunction_Fail = "testHasBelowNoReturnFunction_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoReturnFunction_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java:
                          - Return-Statement was found:
                            -  Between line 12 (column 9) and line 12 (column 17)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions2.java:
                          - Return-Statement was found:
                            -  Between line 12 (column 9) and line 12 (column 17)
                        """));
    }

    @TestTest
    void test_testHasAtNoReturnFunction_Success() {
        String testHasAtNoReturnFunction_Success = "testHasAtNoReturnFunction_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoReturnFunction_Success));
    }

    @TestTest
    void test_testHasAtNoReturnFunction_Fail() {
        String testHasAtNoReturnFunction_Fail = "testHasAtNoReturnFunction_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoReturnFunction_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java:
                          - Return-Statement was found:
                            -  Between line 12 (column 9) and line 12 (column 17)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-Function">

    @TestTest
    void test_testHasBelowNoAllFunction_Success() {
        String testHasBelowNoAllFunction_Success = "testHasBelowNoAllFunction_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllFunction_Success));
    }

    @TestTest
    void test_testHasBelowNoAllFunction_Fail() {
        String testHasBelowNoAllFunction_Fail = "testHasBelowNoAllFunction_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllFunction_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java:
                          - Explicit-Constructor-Invocation-Statement was found:
                            -  Between line 8 (column 9) and line 8 (column 21)
                          - Return-Statement was found:
                            -  Between line 12 (column 9) and line 12 (column 17)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions2.java:
                          - Explicit-Constructor-Invocation-Statement was found:
                            -  Between line 8 (column 9) and line 8 (column 21)
                          - Return-Statement was found:
                            -  Between line 12 (column 9) and line 12 (column 17)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllFunction_Success() {
        String testHasAtNoAllFunction_Success = "testHasAtNoAllFunction_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllFunction_Success));
    }

    @TestTest
    void test_testHasAtNoAllFunction_Fail() {
        String testHasAtNoAllFunction_Fail = "testHasAtNoAllFunction_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllFunction_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java:
                          - Explicit-Constructor-Invocation-Statement was found:
                            -  Between line 8 (column 9) and line 8 (column 21)
                          - Return-Statement was found:
                            -  Between line 12 (column 9) and line 12 (column 17)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="LocalClass-Class">

    @TestTest
    void test_testHasBelowNoLocalClassClass_Success() {
        String testHasBelowNoLocalClassClass_Success = "testHasBelowNoLocalClassClass_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoLocalClassClass_Success));
    }

    @TestTest
    void test_testHasBelowNoLocalClassClass_Fail() {
        String testHasBelowNoLocalClassClass_Fail = "testHasBelowNoLocalClassClass_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoLocalClassClass_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java:
                          - Local-Class-Statement was found:
                            -  Between line 5 (column 43) and line 5 (column 61)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses2.java:
                          - Local-Class-Statement was found:
                            -  Between line 5 (column 43) and line 5 (column 61)
                        """));
    }

    @TestTest
    void test_testHasAtNoLocalClassClass_Success() {
        String testHasAtNoLocalClassClass_Success = "testHasAtNoLocalClassClass_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoLocalClassClass_Success));
    }

    @TestTest
    void test_testHasAtNoLocalClassClass_Fail() {
        String testHasAtNoLocalClassClass_Fail = "testHasAtNoLocalClassClass_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoLocalClassClass_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java:
                          - Local-Class-Statement was found:
                            -  Between line 5 (column 43) and line 5 (column 61)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="LocalRecord-Class">

    @TestTest
    void test_testHasBelowNoLocalRecordClass_Success() {
        String testHasBelowNoLocalRecordClass_Success = "testHasBelowNoLocalRecordClass_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoLocalRecordClass_Success));
    }

    @TestTest
    void test_testHasBelowNoLocalRecordClass_Fail() {
        String testHasBelowNoLocalRecordClass_Fail = "testHasBelowNoLocalRecordClass_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoLocalRecordClass_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java:
                          - Local-Record-Statement was found:
                            -  Between line 7 (column 44) and line 7 (column 66)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses2.java:
                          - Local-Record-Statement was found:
                            -  Between line 7 (column 44) and line 7 (column 66)
                        """));
    }

    @TestTest
    void test_testHasAtNoLocalRecordClass_Success() {
        String testHasAtNoLocalRecordClass_Success = "testHasAtNoLocalRecordClass_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoLocalRecordClass_Success));
    }

    @TestTest
    void test_testHasAtNoLocalRecordClass_Fail() {
        String testHasAtNoLocalRecordClass_Fail = "testHasAtNoLocalRecordClass_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoLocalRecordClass_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java:
                          - Local-Record-Statement was found:
                            -  Between line 7 (column 44) and line 7 (column 66)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-Class">

    @TestTest
    void test_testHasBelowNoAllClass_Success() {
        String testHasBelowNoAllClass_Success = "testHasBelowNoAllClass_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllClass_Success));
    }

    @TestTest
    void test_testHasBelowNoAllClass_Fail() {
        String testHasBelowNoAllClass_Fail = "testHasBelowNoAllClass_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllClass_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java:
                          - Local-Class-Statement was found:
                            -  Between line 5 (column 43) and line 5 (column 61)
                          - Local-Record-Statement was found:
                            -  Between line 7 (column 44) and line 7 (column 66)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses2.java:
                          - Local-Class-Statement was found:
                            -  Between line 5 (column 43) and line 5 (column 61)
                          - Local-Record-Statement was found:
                            -  Between line 7 (column 44) and line 7 (column 66)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllClass_Success() {
        String testHasAtNoAllClass_Success = "testHasAtNoAllClass_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllClass_Success));
    }

    @TestTest
    void test_testHasAtNoAllClass_Fail() {
        String testHasAtNoAllClass_Fail = "testHasAtNoAllClass_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllClass_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java:
                          - Local-Class-Statement was found:
                            -  Between line 5 (column 43) and line 5 (column 61)
                          - Local-Record-Statement was found:
                            -  Between line 7 (column 44) and line 7 (column 66)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="Assert-ExceptionHandling">

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
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Assert-Statement was found:
                            -  Between line 7 (column 9) and line 7 (column 22)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings2.java:
                          - Assert-Statement was found:
                            -  Between line 7 (column 9) and line 7 (column 22)
                        """));
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
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Assert-Statement was found:
                            -  Between line 7 (column 9) and line 7 (column 22)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="Throw-ExceptionHandling">

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
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Throw-Statement was found:
                            -  Between line 11 (column 9) and line 11 (column 60)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings2.java:
                          - Throw-Statement was found:
                            -  Between line 11 (column 9) and line 11 (column 60)
                        """));
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
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Throw-Statement was found:
                            -  Between line 11 (column 9) and line 11 (column 60)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="TryCatch-ExceptionHandling">

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
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Try-Statement was found:
                            -  Between line 15 (column 9) and line 17 (column 32)
                          - Catch-Statement was found:
                            -  Between line 17 (column 11) and line 17 (column 32)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings2.java:
                          - Try-Statement was found:
                            -  Between line 15 (column 9) and line 17 (column 32)
                          - Catch-Statement was found:
                            -  Between line 17 (column 11) and line 17 (column 32)
                        """));
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
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Try-Statement was found:
                            -  Between line 15 (column 9) and line 17 (column 32)
                          - Catch-Statement was found:
                            -  Between line 17 (column 11) and line 17 (column 32)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-ExceptionHandling">

    @TestTest
    void test_testHasBelowNoAllExceptionHandling_Success() {
        String testHasBelowNoAllExceptionHandling_Success = "testHasBelowNoAllExceptionHandling_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllExceptionHandling_Success));
    }

    @TestTest
    void test_testHasBelowNoAllExceptionHandling_Fail() {
        String testHasBelowNoAllExceptionHandling_Fail = "testHasBelowNoAllExceptionHandling_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllExceptionHandling_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Assert-Statement was found:
                            -  Between line 7 (column 9) and line 7 (column 22)
                          - Throw-Statement was found:
                            -  Between line 11 (column 9) and line 11 (column 60)
                          - Try-Statement was found:
                            -  Between line 15 (column 9) and line 17 (column 32)
                          - Catch-Statement was found:
                            -  Between line 17 (column 11) and line 17 (column 32)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings2.java:
                          - Assert-Statement was found:
                            -  Between line 7 (column 9) and line 7 (column 22)
                          - Throw-Statement was found:
                            -  Between line 11 (column 9) and line 11 (column 60)
                          - Try-Statement was found:
                            -  Between line 15 (column 9) and line 17 (column 32)
                          - Catch-Statement was found:
                            -  Between line 17 (column 11) and line 17 (column 32)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllExceptionHandling_Success() {
        String testHasAtNoAllExceptionHandling_Success = "testHasAtNoAllExceptionHandling_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllExceptionHandling_Success));
    }

    @TestTest
    void test_testHasAtNoAllExceptionHandling_Fail() {
        String testHasAtNoAllExceptionHandling_Fail = "testHasAtNoAllExceptionHandling_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllExceptionHandling_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java:
                          - Assert-Statement was found:
                            -  Between line 7 (column 9) and line 7 (column 22)
                          - Throw-Statement was found:
                            -  Between line 11 (column 9) and line 11 (column 60)
                          - Try-Statement was found:
                            -  Between line 15 (column 9) and line 17 (column 32)
                          - Catch-Statement was found:
                            -  Between line 17 (column 11) and line 17 (column 32)
                        """));
    }
    //</editor-fold>

    //<editor-fold desc="All-Synchronisation">

    @TestTest
    void test_testHasBelowNoAllSynchronisation_Success() {
        String testHasBelowNoAllSynchronisation_Success = "testHasBelowNoAllSynchronisation_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAllSynchronisation_Success));
    }

    @TestTest
    void test_testHasBelowNoAllSynchronisation_Fail() {
        String testHasBelowNoAllSynchronisation_Fail = "testHasBelowNoAllSynchronisation_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAllSynchronisation_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/synchronisations/yes/ClassWithAllKindsOfSynchronisations2.java:
                          - Synchronised-Statement was found:
                            -  Between line 8 (column 9) and line 10 (column 9)
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/synchronisations/yes/ClassWithAllKindsOfSynchronisations.java:
                          - Synchronised-Statement was found:
                            -  Between line 8 (column 9) and line 10 (column 9)
                        """));
    }

    @TestTest
    void test_testHasAtNoAllSynchronisation_Success() {
        String testHasAtNoAllSynchronisation_Success = "testHasAtNoAllSynchronisation_Success";
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoAllSynchronisation_Success));
    }

    @TestTest
    void test_testHasAtNoAllSynchronisation_Fail() {
        String testHasAtNoAllSynchronisation_Fail = "testHasAtNoAllSynchronisation_Fail";
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoAllSynchronisation_Fail,
                AssertionError.class,
                """
                        Unwanted statement found:
                        - In src/test/java/de/tum/in/test/integration/testuser/subject/structural/astTestFiles/synchronisations/yes/ClassWithAllKindsOfSynchronisations.java:
                          - Synchronised-Statement was found:
                            -  Between line 8 (column 9) and line 10 (column 9)
                        """));
    }
    //</editor-fold>
}
