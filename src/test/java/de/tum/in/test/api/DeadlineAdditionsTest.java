package de.tum.in.test.api;

import static de.tum.in.testutil.CustomConditions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.testutil.TestTest;

class DeadlineAdditionsTest {
	private final String testHidden_CustomDeadlineFutureActive = "testHidden_CustomDeadlineFutureActive";
	private final String testHidden_CustomDeadlineFutureExtendedActive = "testHidden_CustomDeadlineFutureExtendedActive";
	private final String testHidden_CustomDeadlineFutureExtendedInactive = "testHidden_CustomDeadlineFutureExtendedInactive";
	private final String testHidden_CustomDeadlineFutureExtendedNormal = "testHidden_CustomDeadlineFutureExtendedNormal";
	private final String testHidden_CustomDeadlineFutureInactive = "testHidden_CustomDeadlineFutureInactive";
	private final String testHidden_CustomDeadlinePastExtendedActive = "testHidden_CustomDeadlinePastExtendedActive";
	private final String testHidden_CustomDeadlinePastExtendedInactive = "testHidden_CustomDeadlinePastExtendedInactive";
	private final String testHidden_CustomDeadlinePastExtendedNormal = "testHidden_CustomDeadlinePastExtendedNormal";
	private final String testHiddenActive = "testHiddenActive";
	private final String testHiddenExtendedNormal = "testHiddenExtendedNormal";
	private final String testHiddenExtendedActive = "testHiddenExtendedActive";
	private final String testHiddenExtendedInactive = "testHiddenExtendedInactive";
	private final String testHiddenInactive = "testHiddenInactive";
	private final String testHiddenNormal = "testHiddenNormal";
	private final String testPublicActive = "testPublicActive";
	private final String testPublicExtended = "testPublicExtended";

	private static Events tests;

	@BeforeAll
	@Tag("test-test")
	static void verifyExtendedDeadline() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineAdditionsUser.class))
				.execute();
		tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(16).succeeded(8).failed(8));
	}

	@TestTest
	void test_testHiddenNormal() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenNormal), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenExtendedNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenExtendedNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testPublicExtended() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicExtended, AnnotationFormatError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureExtendedNormal() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureExtendedNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlinePastExtendedNormal() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlinePastExtendedNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testPublicActive() {
		tests.assertThatEvents().haveExactly(1, event(test(testPublicActive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenActive() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenActive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenInactive() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenInactive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureActive() {
		tests.assertThatEvents().haveExactly(1,
				event(test(testHidden_CustomDeadlineFutureActive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureInactive() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenExtendedActive() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenExtendedActive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureExtendedActive() {
		tests.assertThatEvents().haveExactly(1,
				event(test(testHidden_CustomDeadlineFutureExtendedActive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHidden_CustomDeadlinePastExtendedActive() {
		tests.assertThatEvents().haveExactly(1,
				event(test(testHidden_CustomDeadlinePastExtendedActive), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenExtendedInactive() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenExtendedInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlineFutureExtendedInactive() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureExtendedInactive, AssertionFailedError.class));
	}

	@TestTest
	void test_testHidden_CustomDeadlinePastExtendedInactive() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlinePastExtendedInactive, AssertionFailedError.class));
	}

}
