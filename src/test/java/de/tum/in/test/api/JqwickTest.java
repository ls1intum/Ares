package de.tum.in.test.api;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;

import java.lang.annotation.AnnotationFormatError;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Event;
import org.opentest4j.AssertionFailedError;

public class JqwickTest {
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

	@Test
	@Tag("test-test")
	void verifyJqwik() {
		var results = EngineTestKit.engine("jqwik").selectors(selectClass(JqwickUser.class)).execute();
		var tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(16));

		tests.assertThatEvents().doNotHave(event(test(testHiddenIncomplete)));
		tests.assertThatEvents().doNotHave(event(test(testPublicIncomplete)));

		tests.assertThatEvents().haveExactly(1, event(test(examplePublicNormal), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(exampleHiddenCustomDeadlinePast), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(exampleHiddenNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(examplePublicCustomDeadline, AnnotationFormatError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(exampleHiddenCustomDeadlineFuture, AssertionFailedError.class));

		tests.assertThatEvents().haveExactly(1, event(test(propertyPublicNormal), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(propertyHiddenCustomDeadlinePast), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(propertyHiddenNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(propertyPublicCustomDeadline, AnnotationFormatError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(propertyHiddenCustomDeadlineFuture, AssertionFailedError.class));

		tests.assertThatEvents().haveExactly(1, event(test(propertyUseIOTesterCorrect), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, testFailedWith(propertyUseIOTesterWrong, AssertionError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutEndlessLoop, IllegalStateException.class)); // TODO:
																															// improve
																															// message
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutSleepExample, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(provokeTimeoutSleepTries, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(provokeTimeoutSleepProperty, IllegalStateException.class));

	}

	private static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType)));
	}
}
