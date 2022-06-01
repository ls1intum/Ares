package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.integration.testuser.DeadlineAdditionsUser;
import de.tum.in.test.testutilities.*;

@UserBased(DeadlineAdditionsUser.class)
class DeadlineAdditionsTest {

	@UserTestResults
	private static Events tests;

	private final String testHidden_CustomDeadlineFutureActive = "testHidden_CustomDeadlineFutureActive";
	private final String testHidden_CustomDeadlineFutureExtendedActive = "testHidden_CustomDeadlineFutureExtendedActive";
	private final String testHidden_CustomDeadlineFutureExtendedInactive = "testHidden_CustomDeadlineFutureExtendedInactive";
	private final String testHidden_CustomDeadlineFutureExtendedNormal = "testHidden_CustomDeadlineFutureExtendedNormal";
	private final String testHidden_CustomDeadlineFutureInactive = "testHidden_CustomDeadlineFutureInactive";
	private final String testHidden_CustomDeadlinePastExtendedActive = "testHidden_CustomDeadlinePastExtendedActive";
	private final String testHidden_CustomDeadlinePastExtendedInactive = "testHidden_CustomDeadlinePastExtendedInactive";
	private final String testHidden_CustomDeadlinePastExtendedNormal = "testHidden_CustomDeadlinePastExtendedNormal";
	private final String testHiddenActive = "testHiddenActive";
	private final String testHiddenExtendedActive = "testHiddenExtendedActive";
	private final String testHiddenExtendedInactive = "testHiddenExtendedInactive";
	private final String testHiddenExtendedNormal = "testHiddenExtendedNormal";
	private final String testHiddenInactive = "testHiddenInactive";
	private final String testHiddenNormal = "testHiddenNormal";
	private final String testPublicActive = "testPublicActive";
	private final String testPublicExtended = "testPublicExtended";

	@TestTest
	void test_testHidden_CustomDeadlineFutureActive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHidden_CustomDeadlineFutureActive));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureExtendedActive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHidden_CustomDeadlineFutureExtendedActive));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureExtendedInactive() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureExtendedInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureExtendedNormal() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureExtendedNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureInactive() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlinePastExtendedActive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHidden_CustomDeadlinePastExtendedActive));
	}

	@TestTest
	void test_testHidden_CustomDeadlinePastExtendedInactive() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlinePastExtendedInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlinePastExtendedNormal() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlinePastExtendedNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenActive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHiddenActive));
	}

	@TestTest
	void test_testHiddenExtendedActive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHiddenExtendedActive));
	}

	@TestTest
	void test_testHiddenExtendedInactive() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenExtendedInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenExtendedNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenExtendedNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenInactive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHiddenInactive));
	}

	@TestTest
	void test_testHiddenNormal() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHiddenNormal));
	}

	@TestTest
	void test_testPublicActive() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testPublicActive));
	}

	@TestTest
	void test_testPublicExtended() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicExtended, AnnotationFormatError.class));
	}
}
