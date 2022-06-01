package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;
import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;
import static org.junit.platform.testkit.engine.EventConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import net.jqwik.engine.JqwikTestEngine;

import de.tum.in.test.integration.testuser.JqwickUser;
import de.tum.in.test.testutilities.*;

@UserBased(value = JqwickUser.class, testEngineId = JqwikTestEngine.ENGINE_ID)
class JqwickTest {

	@UserTestResults
	private static Events tests;

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
	private final String testHiddenIncomplete = "testHiddenIncomplete";
	private final String testLocaleDe = "testLocaleDe";
	private final String testPublicIncomplete = "testPublicIncomplete";

	@TestTest
	void test_exampleHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(exampleHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_exampleHiddenCustomDeadlinePast() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(exampleHiddenCustomDeadlinePast));
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
	void test_examplePublicNormal() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(examplePublicNormal));
	}

	@TestTest
	void test_propertyHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(propertyHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_propertyHiddenCustomDeadlinePast() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(propertyHiddenCustomDeadlinePast));
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
	void test_propertyPublicNormal() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(propertyPublicNormal));
	}

	@TestTest
	void test_propertyUseIOTesterCorrect() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(propertyUseIOTesterCorrect));
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
	void test_provokeTimeoutSleepProperty() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(provokeTimeoutSleepProperty, AssertionFailedError.class));
	}

	@TestTest
	void test_provokeTimeoutSleepTries() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutSleepTries, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenIncomplete() {
		tests.assertThatEvents().doNotHave(event(test(testHiddenIncomplete)));
	}

	@TestTest
	void test_testLocaleDe() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testLocaleDe));
	}

	@TestTest
	void test_testPublicIncomplete() {
		tests.assertThatEvents().doNotHave(event(test(testPublicIncomplete)));
	}
}
