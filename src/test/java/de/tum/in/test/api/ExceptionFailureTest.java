package de.tum.in.test.api;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.*;

import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.MultipleFailuresError;

import de.tum.in.test.api.util.UnexpectedExceptionError;
import de.tum.in.testuser.ExceptionFailureUser;
import de.tum.in.testutil.TestTest;

class ExceptionFailureTest {

	private final String assertionFailed = "assertionFailed";
	private final String multipleFailures = "multipleFailures";
	private final String exceptionInInitializer = "exceptionInInitializer";
	private final String multipleAssertions = "multipleAssertions";
	private final String softAssertion = "softAssertion";
	private final String nullPointer = "nullPointer";
	private final String customException = "customException";

	private static Events tests;

	@BeforeAll
	@Tag("test-test")
	static void verifyDeadline() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(ExceptionFailureUser.class))
				.execute();
		tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(7));
	}

	@TestTest
	void test_assertionFailed() {
		tests.assertThatEvents().haveExactly(1,
				event(test(assertionFailed), finishedWithFailure(instanceOf(AssertionError.class),
						message(m -> m.contains("expected: <1> but was: <2>")))));
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
	void test_exceptionInInitializer() {
		tests.assertThatEvents().haveExactly(1, event(test(exceptionInInitializer), finishedWithFailure(
				instanceOf(ExceptionInInitializerError.class),
				new Condition<>(t -> t.getCause() instanceof ArithmeticException, "cause is ArithmeticException"))));
	}

	@TestTest
	void test_multipleAssertions() {
		tests.assertThatEvents().haveExactly(1,
				event(test(multipleAssertions),
						finishedWithFailure(instanceOf(AssertJMultipleFailuresError.class),
								message(m -> m.contains("Multiple Failures (2 failures)") //
										&& m.contains("-- failure 1 --A") //
										&& m.contains("-- failure 2 --B")),
								new Condition<>(t -> t.getSuppressed().length == 0, "no suppressed exceptions"))));
	}

	@TestTest
	void test_softAssertion() {
		tests.assertThatEvents().haveExactly(1,
				event(test(softAssertion), finishedWithFailure(instanceOf(SoftAssertionError.class),
						message(m -> m.contains("The following 2 assertions failed:  1) A  2) B")))));
	}

	@TestTest
	void test_nullPointer() {
		tests.assertThatEvents().haveExactly(1, event(test(nullPointer),
				finishedWithFailure(instanceOf(NullPointerException.class), message(m -> m.contains("XYZ")))));
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
}