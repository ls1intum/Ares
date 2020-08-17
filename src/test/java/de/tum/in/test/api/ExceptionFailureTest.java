package de.tum.in.test.api;

import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.*;

import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.assertj.core.error.MultipleAssertionsError;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.MultipleFailuresError;

import de.tum.in.test.api.util.UnexpectedExceptionError;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.ExceptionFailureUser;

@UserBased(ExceptionFailureUser.class)
class ExceptionFailureTest {

	@UserTestResults
	private static Events tests;

	private final String assertionFailed = "assertionFailed";
	private final String assertJMultipleFailures = "assertJMultipleFailures";
	private final String customException = "customException";
	private final String exceptionInInitializer = "exceptionInInitializer";
	private final String faultyGetCauseException = "faultyGetCauseException";
	private final String faultyToStringException = "faultyToStringException";
	private final String multipleAssertions = "multipleAssertions";
	private final String multipleFailures = "multipleFailures";
	private final String nullPointer = "nullPointer";
	private final String softAssertion = "softAssertion";

	@TestTest
	void test_assertionFailed() {
		tests.assertThatEvents().haveExactly(1,
				event(test(assertionFailed), finishedWithFailure(instanceOf(AssertionError.class),
						message(m -> m.contains("expected: <1> but was: <2>")))));
	}

	@TestTest
	void test_assertJMultipleFailures() {
		tests.assertThatEvents().haveExactly(1,
				event(test(assertJMultipleFailures),
						finishedWithFailure(instanceOf(AssertJMultipleFailuresError.class),
								message(m -> m.contains("Multiple Failures (2 failures)") //
										&& m.contains("-- failure 1 --A") //
										&& m.contains("-- failure 2 --B")),
								new Condition<>(t -> t.getSuppressed().length == 0, "no suppressed exceptions"))));
	}

	@TestTest
	void test_customException() {
		tests.assertThatEvents().haveExactly(1,
				event(test(customException),
						finishedWithFailure(instanceOf(UnexpectedExceptionError.class),
								message("de.tum.in.testuser.subject.CustomException: ABC"),
								new Condition<>(t -> t.getCause() instanceof ArrayIndexOutOfBoundsException,
										"cause is ArrayIndexOutOfBoundsException"))));
	}

	@TestTest
	void test_exceptionInInitializer() {
		tests.assertThatEvents().haveExactly(1, event(test(exceptionInInitializer), finishedWithFailure(
				instanceOf(ExceptionInInitializerError.class),
				new Condition<>(t -> t.getCause() instanceof ArithmeticException, "cause is ArithmeticException"))));
	}

	@TestTest
	void test_faultyGetCauseException() {
		tests.assertThatEvents().haveExactly(1, event(test(faultyGetCauseException), finishedWithFailure(
				instanceOf(SecurityException.class),
				message(m -> m.contains("AssertionError thrown, but cannot be displayed:") && m.contains(
						"FaultyGetCauseException threw an exception when retrieving information about it. (java.lang.NullPointerException: Faulty)")))));
	}

	@TestTest
	void test_faultyToStringException() {
		tests.assertThatEvents().haveExactly(1, event(test(faultyToStringException), finishedWithFailure(
				instanceOf(SecurityException.class),
				message(m -> m.contains("FaultyToStringException thrown, but cannot be displayed:") && m.contains(
						"FaultyToStringException threw an exception when retrieving information about it. (java.lang.IllegalStateException: Faulty)")))));
	}

	@TestTest
	void test_multipleAssertions() {
		Condition<Throwable> hasOneCorrectlySanitizedAssertionError = new Condition<>(t -> {
			if (((MultipleAssertionsError) t).getErrors().size() != 1)
				return false;
			var error = ((MultipleAssertionsError) t).getErrors().get(0);
			return "X".equals(error.getMessage()) //
					&& error.getCause() instanceof UnexpectedExceptionError //
					&& error.getCause().getMessage().contains("ABC");

		}, "has one correctly sanitized AssertionError");
		tests.assertThatEvents().haveExactly(1,
				event(test(multipleAssertions),
						finishedWithFailure(instanceOf(MultipleAssertionsError.class),
								message(m -> m.contains("[Failed with 5]") //
										&& m.contains("The following assertion failed:") //
										&& m.contains("1) X")),
								hasOneCorrectlySanitizedAssertionError)));
	}

	@TestTest
	void test_multipleFailures() {
		tests.assertThatEvents().haveExactly(1,
				event(test(multipleFailures),
						finishedWithFailure(instanceOf(MultipleFailuresError.class),
								message(m -> m.contains("Multiple Failures (2 failures)") //
										&& m.contains("java.lang.AssertionError: A") //
										&& m.contains("java.lang.AssertionError: B")),
								new Condition<>(t -> t.getSuppressed().length == 2, "two suppressed exceptions"))));
	}

	@TestTest
	void test_nullPointer() {
		tests.assertThatEvents().haveExactly(1, event(test(nullPointer),
				finishedWithFailure(instanceOf(NullPointerException.class), message(m -> m.contains("XYZ")))));
	}

	@TestTest
	void test_softAssertion() {
		tests.assertThatEvents().haveExactly(1,
				event(test(softAssertion), finishedWithFailure(instanceOf(SoftAssertionError.class),
						message(m -> m.contains("The following 2 assertions failed:  1) A  2) B")))));
	}
}
