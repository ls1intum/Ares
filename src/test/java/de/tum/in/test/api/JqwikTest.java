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

public class JqwikTest {
	private final String testHiddenIncomplete = "testHiddenIncomplete";
	private final String testPublicIncomplete = "testPublicIncomplete";

	private final String exampleHiddenCustomDeadlineFuture = "exampleHiddenCustomDeadlineFuture";
	private final String exampleHiddenCustomDeadlinePast = "exampleHiddenCustomDeadlinePast";
	private final String exampleHiddenNormal = "exampleHiddenNormal";
	private final String examplePublicCustomDeadline = "examplePublicCustomDeadline";
	private final String examplePublicNormal = "examplePublicNormal";

	private final String propertyHiddenCustomDeadlineFuture = "propertyHiddenCustomDeadlineFuture";
	private final String propertyHiddenCustomDeadlinePast = "propertyHiddenCustomDeadlinePast";
	private final String propertyHiddenNormal = "propertyHiddenNormal";
	private final String propertyPublicCustomDeadline = "propertyPublicCustomDeadline";
	private final String propertyPublicNormal = "propertyPublicNormal";

	private final String propertyUseIOTesterCorrect = "propertyUseIOTesterCorrect";
	private final String propertyUseIOTesterWrong = "propertyUseIOTesterWrong";

	private final String provokeTimeoutEndlessLoop = "provokeTimeoutEndlessLoop";
	private final String provokeTimeoutSleepExample = "provokeTimeoutSleepExample";
	private final String provokeTimeoutSleepProperty = "provokeTimeoutSleepProperty";
	private final String provokeTimeoutSleepTries = "provokeTimeoutSleepTries";
	private static Events tests;

	@BeforeAll
	@Tag("test-test")
	static void verifyJqwik() {
		var results = EngineTestKit.engine("jqwik").selectors(selectClass(JqwikUser.class)).execute();
		tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(16));
	}

	@TestTest
	void test_testHiddenIncomplete() {
		tests.assertThatEvents().doNotHave(event(test(testHiddenIncomplete)));
	}

	@TestTest
	void test_testPublicIncomplete() {
		tests.assertThatEvents().doNotHave(event(test(testPublicIncomplete)));
	}

	@TestTest
	void test_examplePublicNormal() {
		tests.assertThatEvents().haveExactly(1, event(test(examplePublicNormal), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_exampleHiddenCustomDeadlinePast() {
		tests.assertThatEvents().haveExactly(1,
				event(test(exampleHiddenCustomDeadlinePast), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_exampleHiddenNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(exampleHiddenNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_examplePublicCustomDeadline() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(examplePublicCustomDeadline, AnnotationFormatError.class));
	}

	@TestTest
	void test_exampleHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(exampleHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_propertyPublicNormal() {
		tests.assertThatEvents().haveExactly(1, event(test(propertyPublicNormal), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_propertyHiddenCustomDeadlinePast() {
		tests.assertThatEvents().haveExactly(1,
				event(test(propertyHiddenCustomDeadlinePast), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_propertyHiddenNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(propertyHiddenNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_propertyPublicCustomDeadline() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(propertyPublicCustomDeadline, AnnotationFormatError.class));
	}

	@TestTest
	void test_propertyHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(propertyHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_propertyUseIOTesterCorrect() {
		tests.assertThatEvents().haveExactly(1, event(test(propertyUseIOTesterCorrect), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_propertyUseIOTesterWrong() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(propertyUseIOTesterWrong, AssertionError.class));
	}

	@TestTest
	void test_provokeTimeoutEndlessLoop() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutEndlessLoop, AssertionFailedError.class));
	}

	@TestTest
	void test_provokeTimeoutSleepExample() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutSleepExample, AssertionFailedError.class));
	}

	@TestTest
	void test_provokeTimeoutSleepTries() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutSleepTries, AssertionFailedError.class));
	}

	@TestTest
	void test_provokeTimeoutSleepProperty() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(provokeTimeoutSleepProperty, AssertionFailedError.class));
	}

}
