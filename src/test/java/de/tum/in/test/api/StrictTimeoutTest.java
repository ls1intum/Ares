package de.tum.in.test.api;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Event;
import org.opentest4j.AssertionFailedError;

public class StrictTimeoutTest {

	private final String testOneSecondFail = "testOneSecondFail";
	private final String testOneSecondSuccess = "testOneSecondSuccess";
	private final String testMethodFailNormal = "testMethodFailNormal";
	private final String testMethodFailLoop = "testMethodFailLoop";
	private final String testMethodSuccess = "testMethodSuccess";
	private final String testClassFailLoop = "testClassFailLoop";
	private final String testClassFailNormal = "testClassFailNormal";
	private final String testClassSuccess = "testClassSuccess";

	@Test
	void verifyStrictTimeout() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(StrictTimeoutUser.class)).execute();
		var tests = results.tests();

		results.containers().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(8).succeeded(3).failed(5));

		tests.assertThatEvents().haveExactly(1, event(test(testOneSecondSuccess), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testMethodSuccess), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testClassSuccess), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(testOneSecondFail, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMethodFailNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMethodFailLoop, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassFailNormal, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassFailLoop, AssertionFailedError.class));

		tests.assertThatEvents().filteredOn(event(test(testOneSecondFail))).satisfies(deltaTimeIs(1000, 0.1));
		tests.assertThatEvents().filteredOn(event(test(testMethodFailNormal))).satisfies(deltaTimeIs(300, 0.2));
		tests.assertThatEvents().filteredOn(event(test(testMethodFailLoop))).satisfies(deltaTimeIs(300, 0.2));
		tests.assertThatEvents().filteredOn(event(test(testClassFailNormal))).satisfies(deltaTimeIs(100, 0.2));
		tests.assertThatEvents().filteredOn(event(test(testClassFailLoop))).satisfies(deltaTimeIs(100, 0.2));
	}

	private static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType)));
	}

	private static Condition<? super List<? extends Event>> deltaTimeIs(long millies, double accuracy) {
		return new Condition<>(list -> {
			Assertions.assertThat(list).hasSize(2);
			var deltaT = Duration.between(list.get(0).getTimestamp(), list.get(1).getTimestamp()).abs();
			return Math.abs(deltaT.toMillis() - millies) <= accuracy * millies;
		}, "took %s ms", millies);
	}
}
