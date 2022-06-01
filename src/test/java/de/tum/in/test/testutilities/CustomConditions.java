package de.tum.in.test.testutilities;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.commons.util.FunctionUtils.where;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;

import java.time.Duration;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.*;
import org.opentest4j.AssertionFailedError;

public final class CustomConditions {

	public static final String NO_MSG = new String();

	private static final int CONDITION_FAIL_STACK_LIMIT = 10;
	private static boolean directFail = !Boolean.getBoolean("ares.test.softfail"); //$NON-NLS-1$

	private CustomConditions() {
	}

	public enum Option {
		MESSAGE_CONTAINS,
		MESSAGE_REGEX,
		MESSAGE_NORMALIZE_NEWLINE
	}

	public static Condition<? super List<? extends Event>> deltaTimeIs(long millies, double accuracy) {
		return new Condition<>(list -> {
			Assertions.assertThat(list).hasSize(2);
			var deltaT = Duration.between(list.get(0).getTimestamp(), list.get(1).getTimestamp()).abs();
			return Math.abs(deltaT.toMillis() - millies) <= accuracy * millies;
		}, "took %s ms", millies); //$NON-NLS-1$
	}

	public static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType,
			Option... options) {
		return testFailedWith(testName, errorType, NO_MSG, null, options);
	}

	public static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType,
			String message, Option... options) {
		return testFailedWith(testName, errorType, message, null, options);
	}

	public static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType,
			String message, Condition<Throwable> customCondition, Option... options) {
		var conditions = new ArrayList<Condition<Throwable>>();
		var optionSet = EnumSet.noneOf(Option.class);
		Collections.addAll(optionSet, options);

		conditions.add(instanceOf(errorType));
		conditions.add(messageLocalized());
		if (message != NO_MSG) {
			conditions.add(createMessageCondition(message, optionSet));
		}
		if (customCondition != null) {
			conditions.add(customCondition);
		}
		return event(testWithSegments(testName), EventConditions.finished(
				new Condition<>(testFailRespectingDirectFailure(testName, conditions), "status is not SUCCESSFUL"))); //$NON-NLS-1$
	}

	private static Condition<Throwable> createMessageCondition(String message, EnumSet<Option> optionSet) {
		var messageContains = optionSet.contains(Option.MESSAGE_CONTAINS);
		var messageRegex = optionSet.contains(Option.MESSAGE_REGEX);
		if (messageContains && messageRegex)
			throw new UnsupportedOperationException("regex matching any substring in the message is not implemented"); //$NON-NLS-1$
		if (messageContains)
			return createMessageCondition(message, optionSet,
					messageForComparison -> assertThat(messageForComparison).contains(message),
					messageForComparison -> messageForComparison != null && messageForComparison.contains(message),
					"message contains '%s'"); //$NON-NLS-1$
		if (messageRegex)
			return createMessageCondition(message, optionSet,
					messageForComparison -> assertThat(messageForComparison).matches(message),
					messageForComparison -> messageForComparison != null && messageForComparison.matches(message),
					"message matches the regex '%s'"); //$NON-NLS-1$
		return createMessageCondition(message, optionSet,
				messageForComparison -> assertThat(messageForComparison).isEqualTo(message),
				messageForComparison -> Objects.equals(messageForComparison, message), "message is '%s'"); //$NON-NLS-1$
	}

	private static Condition<Throwable> createMessageCondition(String message, EnumSet<Option> optionSet,
			Consumer<String> directlyFailingAssertion, Predicate<String> assertionAsBoolean,
			String descriptionWithPlaceholder) {
		return new Condition<>(where(Throwable::getMessage, actualMessage -> {
			var messageForComparison = normalizeMessageNewLines(optionSet, actualMessage);
			if (shouldDirectlyFail())
				directlyFailingAssertion.accept(messageForComparison);
			return assertionAsBoolean.test(messageForComparison);
		}), descriptionWithPlaceholder, message); // $NON-NLS-1$
	}

	private static String normalizeMessageNewLines(EnumSet<Option> optionSet, String actualMessage) {
		if (actualMessage == null)
			return null;
		return optionSet.contains(Option.MESSAGE_NORMALIZE_NEWLINE) ? actualMessage.replaceAll("\\R", "\n") //$NON-NLS-1$ //$NON-NLS-2$
				: actualMessage;
	}

	private static Predicate<TestExecutionResult> testFailRespectingDirectFailure(String testName,
			ArrayList<Condition<Throwable>> conditions) {
		return testExecutionResult -> {
			var requirements = allOf(conditions);
			testExecutionResult.getThrowable().ifPresentOrElse(t -> {
				boolean match;
				try {
					match = requirements.matches(t);
				} catch (AssertionError e) {
					/*
					 * in case the conditions fail, we expect the message to be more important or
					 * specially formatted, so only add the test case information to the suppressed.
					 */
					truncateStackTrace(e);
					e.addSuppressed(generateFailure(testName, testExecutionResult, requirements, t));
					throw e;
				}
				/*
				 * In case we don't have a match, fail here directly to produce more
				 * intelligible and direct failure messages.
				 */
				if (!match && shouldDirectlyFail()) {
					throw generateFailure(testName, testExecutionResult, requirements, t);
				}
			}, () -> {
				if (shouldDirectlyFail())
					fail("Expected test '" + testName + "' to fail, but is was successful."); //$NON-NLS-1$ //$NON-NLS-2$
			});
			return testExecutionResult.getStatus() != TestExecutionResult.Status.SUCCESSFUL;
		};
	}

	public static Condition<Event> finishedSuccessfully(String testName) {
		return event(testWithSegments(testName), EventConditions
				.finished(new Condition<>(testSuccessRespectingDirectFailure(testName), "status is SUCCESSFUL"))); //$NON-NLS-1$
	}

	private static Predicate<TestExecutionResult> testSuccessRespectingDirectFailure(String testName) {
		return testExecutionResult -> {
			testExecutionResult.getThrowable().ifPresent(t -> {
				if (shouldDirectlyFail())
					throw generateFailure("Expected test '" + testName + "' to be successful. Failure:\n\n" + t, t); //$NON-NLS-1$ //$NON-NLS-2$
				t.printStackTrace();
			});
			return testExecutionResult.getStatus() == TestExecutionResult.Status.SUCCESSFUL;
		};
	}

	public static Condition<Event> testWithSegments(String segementsSeparatedWithSlash) {
		return allOf(Stream.of(segementsSeparatedWithSlash.split("/")).map(EventConditions::test) //$NON-NLS-1$
				.collect(Collectors.toList()));
	}

	public static Condition<Throwable> messageLocalized() {
		return new Condition<>(where(Throwable::getMessage, m -> m == null || !m.startsWith("!") || !m.endsWith("!")), //$NON-NLS-1$ //$NON-NLS-2$
				"message is correctly localized"); //$NON-NLS-1$
	}

	private static AssertionFailedError generateFailure(String testName, TestExecutionResult testExecutionResult,
			Condition<Throwable> requirements, Throwable t) {
		var descr = evaluateWithoutDirectFail(requirements::conditionDescriptionWithStatus, t);
		var message = "Test '" + testName + "' failed in an unexpected way:\n\n" + testExecutionResult //$NON-NLS-1$ //$NON-NLS-2$
				+ "\n\nExpectations:\n" + descr.value(); //$NON-NLS-1$
		return generateFailure(message, t);
	}

	private static AssertionFailedError generateFailure(String message, Throwable t) {
		var failure = new AssertionFailedError(message, t);
		failure.fillInStackTrace();
		truncateStackTrace(failure);
		return failure;
	}

	private static void truncateStackTrace(AssertionError e) {
		e.setStackTrace(
				Stream.of(e.getStackTrace()).limit(CONDITION_FAIL_STACK_LIMIT).toArray(StackTraceElement[]::new));
	}

	private static <T, R> R evaluateWithoutDirectFail(Function<T, R> evaluation, T input) {
		boolean oldDirectFail = shouldDirectlyFail();
		try {
			directFail = false;
			return evaluation.apply(input);
		} finally {
			directFail = oldDirectFail;
		}
	}

	public static boolean shouldDirectlyFail() {
		return directFail;
	}
}
