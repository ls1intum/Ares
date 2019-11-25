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

public class DeadlineAdditionsTest {
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

	@Test
	@Tag("test-test")
	void verifyExtendedDeadline() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineAdditionsUser.class))
				.execute();
		var tests = results.tests();

		results.containers().assertStatistics(stats -> stats.started(2).succeeded(2));

		tests.assertThatEvents().haveExactly(1, event(test(testHiddenNormal), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenExtendedNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicExtended, AnnotationFormatError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureExtendedNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlinePastExtendedNormal, AssertionFailedError.class));

	}

	@Test
	@Tag("test-test")
	void verifyActivateHiddenBefore() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineAdditionsUser.class))
				.execute();
		var tests = results.tests();

		results.containers().assertStatistics(stats -> stats.started(2).succeeded(2));

		tests.assertThatEvents().haveExactly(1, event(test(testPublicActive), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenActive), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenInactive), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1,
				event(test(testHidden_CustomDeadlineFutureActive), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureInactive, AssertionFailedError.class));

	}

	@Test
	@Tag("test-test")
	void verifyDeadlineAdditionsMixed() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineAdditionsUser.class))
				.execute();
		var tests = results.tests();

		results.containers().assertStatistics(stats -> stats.started(2).succeeded(2));

		tests.assertThatEvents().haveExactly(1, event(test(testHiddenExtendedActive), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1,
				event(test(testHidden_CustomDeadlineFutureExtendedActive), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1,
				event(test(testHidden_CustomDeadlinePastExtendedActive), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenExtendedInactive, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlineFutureExtendedInactive, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHidden_CustomDeadlinePastExtendedInactive, AssertionFailedError.class));

	}

	@Test
	@Tag("test-test")
	void verifyStatistics() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineAdditionsUser.class))
				.execute();
		var tests = results.tests();
		results.containers().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(16).succeeded(8).failed(8));
	}

	private static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType)));
	}
}
