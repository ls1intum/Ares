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

public class DeadlineTest {

	private final String testHiddenCustomDeadlineFuture = "testHiddenCustomDeadlineFuture";
	private final String testHiddenCustomDeadlinePast = "testHiddenCustomDeadlinePast";
	private final String testHiddenNormal = "testHiddenNormal";
	private final String testPublicCustomDeadline = "testPublicCustomDeadline";
	private final String testPublicNormal = "testPublicNormal";

	@Test
	@Tag("test-test")
	void verifyDeadline() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineUser.class)).execute();
		var tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(5));

		tests.assertThatEvents().haveExactly(1, event(test(testPublicNormal), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenCustomDeadlinePast), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicCustomDeadline, AnnotationFormatError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	private static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType)));
	}
}
