package de.tum.in.test.testutilities;

import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.*;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.Event;
import org.junit.platform.testkit.engine.EventConditions;

public final class CustomConditions {

	private CustomConditions() {

	}

	public static Condition<? super List<? extends Event>> deltaTimeIs(long millies, double accuracy) {
		return new Condition<>(list -> {
			Assertions.assertThat(list).hasSize(2);
			var deltaT = Duration.between(list.get(0).getTimestamp(), list.get(1).getTimestamp()).abs();
			return Math.abs(deltaT.toMillis() - millies) <= accuracy * millies;
		}, "took %s ms", millies);
	}

	public static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType)));
	}

	public static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType,
			String message) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType), message(message)));
	}

	public static Condition<Event> finishedSuccessfullyRep() {
		return EventConditions.finished(new Condition<>(ter -> {
			ter.getThrowable().ifPresent(Throwable::printStackTrace);
			return ter.getStatus() == TestExecutionResult.Status.SUCCESSFUL;
		}, "status is SUCCESSFUL"));
	}
}
