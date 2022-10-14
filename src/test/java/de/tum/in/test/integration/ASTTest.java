package de.tum.in.test.integration;

import de.tum.in.test.integration.testuser.ASTTestUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import org.junit.platform.testkit.engine.Events;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;
import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;

@UserBased(ASTTestUser.class)
public class ASTTest {

    @UserTestResults
    private static Events tests;

    private final String testHasBelowNoForLoop_Success = "testHasBelowNoForLoop_Success";
    private final String testHasBelowNoForLoop_Fail = "testHasBelowNoForLoop_Fail";
    private final String testHasAtNoForLoop_Success = "testHasAtNoForLoop_Success";
    private final String testHasAtNoForLoop_Fail = "testHasAtNoForLoop_Fail";

    @TestTest
    void test_testHasBelowNoForLoop_Success() {
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForLoop_Success));
    }

    @TestTest
    void test_testGetConstructor_noSuchMethod() {
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoForLoop_Fail,
                AssertionError.class,
                "Unwanted statement found:\n" +
                        "- In src/test/java/de/tum/in/test/integration/testuser/subject/structural/ClassWithAllKindsOfLoops.java:\n" +
                        "  - For-Statement was found:\n" +
                        "    -  Between line 6 (column 9) and line 8 (column 9)\n"));
    }

    @TestTest
    void test_testHasAtNoForLoop_Success() {
        tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasAtNoForLoop_Success));
    }

    @TestTest
    void test_testHasAtNoForLoop_Fail() {
        tests.assertThatEvents().haveExactly(1, testFailedWith(testHasAtNoForLoop_Fail,
                AssertionError.class,
                "Unwanted statement found:\n" +
                        "- In src/test/java/de/tum/in/test/integration/testuser/subject/structural/ClassWithAllKindsOfLoops.java:\n" +
                        "  - For-Statement was found:\n" +
                        "    -  Between line 6 (column 9) and line 8 (column 9)\n"));
    }
}
