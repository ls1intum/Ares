package de.tum.in.test.testutilities;

import static org.assertj.core.api.Assertions.allOf;
import static org.junit.platform.commons.util.FunctionUtils.where;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		return event(testWithSegments(testName), finishedWithFailure(instanceOf(errorType), messageLocalized()));
	}

	public static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType,
			String message) {
		return event(testWithSegments(testName),
				finishedWithFailure(instanceOf(errorType), message(message), messageLocalized()));
	}

	public static Condition<Event> finishedSuccessfullyRep() {
		return EventConditions.finished(new Condition<>(ter -> {
			ter.getThrowable().ifPresent(Throwable::printStackTrace);
			return ter.getStatus() == TestExecutionResult.Status.SUCCESSFUL;
		}, "status is SUCCESSFUL"));
	}

	public static Condition<Event> testWithSegments(String segementsSeparatedWithSlash) {
		return allOf(Stream.of(segementsSeparatedWithSlash.split("/")).map(EventConditions::test)
				.collect(Collectors.toList()));
	}

	public static Condition<Throwable> messageLocalized() {
		return new Condition<>(where(Throwable::getMessage, m -> !m.startsWith("!") || !m.endsWith("!")),
				"message is correctly localized");
	}
}
