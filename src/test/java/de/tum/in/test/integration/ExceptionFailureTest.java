package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;

import org.assertj.core.api.*;
import org.assertj.core.error.*;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.*;

import de.tum.in.test.api.util.UnexpectedExceptionError;
import de.tum.in.test.integration.testuser.ExceptionFailureUser;
import de.tum.in.test.testutilities.*;
import de.tum.in.test.testutilities.CustomConditions.Option;

@UserBased(ExceptionFailureUser.class)
class ExceptionFailureTest {

	@UserTestResults
	private static Events tests;

	private final String assertJMultipleFailures = "assertJMultipleFailures";
	private final String assertionFailOnly = "assertionFailOnly";
	private final String assertionFailed = "assertionFailed";
	private final String customException = "customException";
	private final String exceptionInInitializer = "exceptionInInitializer";
	private final String faultyGetCauseException = "faultyGetCauseException";
	private final String faultyToStringException = "faultyToStringException";
	private final String multipleAssertions = "multipleAssertions";
	private final String multipleFailures = "multipleFailures";
	private final String nullPointer = "nullPointer";
	private final String softAssertion = "softAssertion";
	private final String throwExceptionInInitializerError = "throwExceptionInInitializerError";
	private final String throwNullPointerException = "throwNullPointerException";

	@TestTest
	void test_assertJMultipleFailures() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(assertJMultipleFailures,
				AssertJMultipleFailuresError.class, "Multiple Failures \\(2 failures\\)\n" + //
						"-- failure 1 --A\n" + //
						"at ExceptionFailureUser.+\n" + //
						"-- failure 2 --B\n" + //
						"at ExceptionFailureUser.+",
				new Condition<>(t -> t.getSuppressed().length == 2, "failures added as suppressed"),
				Option.MESSAGE_NORMALIZE_NEWLINE, Option.MESSAGE_REGEX));
	}

	@TestTest
	void test_assertionFailOnly() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(assertionFailOnly, AssertionFailedError.class,
				"This test failed. Penguin.", new Condition<>(t -> {
					var afe = (AssertionFailedError) t;
					return afe.getActual() == null && afe.getExpected() == null;
				}, "expected and actual are both null")));
	}

	@TestTest
	void test_assertionFailed() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(assertionFailed, AssertionFailedError.class,
				"expected: <1> but was: <2>", new Condition<>(t -> {
					var afe = (AssertionFailedError) t;
					return "2".equals(afe.getActual().getStringRepresentation())
							&& "1".equals(afe.getExpected().getStringRepresentation());
				}, "expected and actual are correct")));
	}

	@TestTest
	void test_customException() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(customException, UnexpectedExceptionError.class,
						"de.tum.in.test.integration.testuser.subject.CustomException: ABC",
						new Condition<>(t -> t.getCause() instanceof ArrayIndexOutOfBoundsException,
								"cause is ArrayIndexOutOfBoundsException")));
	}

	@TestTest
	void test_exceptionInInitializer() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(exceptionInInitializer,
				ExceptionInInitializerError.class, CustomConditions.NO_MSG,
				new Condition<>(t -> t.getCause() instanceof ArithmeticException, "cause is ArithmeticException")));
	}

	@TestTest
	void test_faultyGetCauseException() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(faultyGetCauseException, SecurityException.class, //
				"java.lang.AssertionError" + //
						" thrown, but cannot be displayed: " + //
						"class de.tum.in.test.integration.testuser.ExceptionFailureUser$FaultyGetCauseException" + //
						" threw an exception when retrieving information about it. (java.lang.NullPointerException: Faulty)",
				Option.MESSAGE_NORMALIZE_NEWLINE));
	}

	@TestTest
	void test_faultyToStringException() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(faultyToStringException, SecurityException.class, //
				"de.tum.in.test.integration.testuser.ExceptionFailureUser$FaultyToStringException" + //
						" thrown, but cannot be displayed:" + //
						" class de.tum.in.test.integration.testuser.ExceptionFailureUser$FaultyToStringException"
						+ " threw an exception when retrieving information about it. (java.lang.IllegalStateException: Faulty)",
				Option.MESSAGE_NORMALIZE_NEWLINE));
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
		tests.assertThatEvents().haveExactly(1, testFailedWith(multipleAssertions, MultipleAssertionsError.class, //
				"[Failed with 5] \n" + //
						"The following assertion failed:\n" + //
						"1) X\n", //
				hasOneCorrectlySanitizedAssertionError, Option.MESSAGE_NORMALIZE_NEWLINE));
	}

	@TestTest
	void test_multipleFailures() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(multipleFailures, MultipleFailuresError.class, //
				"Multiple Failures (2 failures)\n" + //
						"\tjava.lang.AssertionError: A\n" + //
						"\tjava.lang.AssertionError: B", //
				new Condition<>(t -> t.getSuppressed().length == 2, "two suppressed exceptions"),
				Option.MESSAGE_NORMALIZE_NEWLINE));
	}

	@TestTest
	void test_nullPointer() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(nullPointer, NullPointerException.class, "XYZ", Option.MESSAGE_CONTAINS));
	}

	@TestTest
	void test_softAssertion() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(softAssertion, SoftAssertionError.class, //
				"\n" + //
						"The following 2 assertions failed:\n" + //
						"1) A\n" + //
						"2) B\n",
				Option.MESSAGE_NORMALIZE_NEWLINE));
	}

	@TestTest
	void test_throwExceptionInInitializerError() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(throwExceptionInInitializerError,
				ExceptionInInitializerError.class,
				"abc\n" + "/// potential problem location: de.tum.in.test.integration.testuser.subject.ExceptionFailurePenguin.throwExceptionInInitializerError(ExceptionFailurePenguin.java:13) ///"));
	}

	@TestTest
	void test_throwNullPointerException() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(throwNullPointerException, NullPointerException.class,
				"xyz\n" + "/// potential problem location: de.tum.in.test.integration.testuser.subject.ExceptionFailurePenguin.throwNullPointerException(ExceptionFailurePenguin.java:9) ///"));
	}
}
