package de.tum.in.test.api;

import org.assertj.core.api.Condition;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.Event;
import org.junit.platform.testkit.engine.EventConditions;

public final class CustomConditions {

	private CustomConditions() {

	}

	public static Condition<Event> finishedSuccessfullyRep() {
		return EventConditions.finished(new Condition<>(ter -> {
			ter.getThrowable().ifPresent(Throwable::printStackTrace);
			return ter.getStatus() == TestExecutionResult.Status.SUCCESSFUL;
		}, "status is SUCCESSFUL"));
	}
}
