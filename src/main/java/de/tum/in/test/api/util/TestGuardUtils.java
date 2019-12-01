package de.tum.in.test.api.util;

import static de.tum.in.test.api.localization.Messages.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.ActivateHiddenBefore;
import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.ExtendedDeadline;
import de.tum.in.test.api.TestType;

/**
 * This class handles public/hidden tests and deadline evaluation.
 *
 * @author Christian Femers
 *
 */
@API(status = Status.INTERNAL)
public class TestGuardUtils {

	private static final Pattern DURATION_PATTERN = Pattern
			.compile("(?:(?<d>\\d+)d)?\\s*(?:\\b(?<h>\\d+)h)?\\s*(?:\\b(?<m>\\d+)m)?", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	private TestGuardUtils() {

	}

	public static void checkForHidden(TestContext context) {
		if (hasAnnotationType(context, TestType.HIDDEN)) {
			// check if there are both, that would be a mistake
			if (hasAnnotationType(context, TestType.PUBLIC))
				throw new AnnotationFormatError(
						formatLocalized("test_guard.test_cannot_be_public_and_hidden", context.displayName())); //$NON-NLS-1$

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime finalDeadline = extractDeadline(context);
			// check if now is after the deadline including extensions
			if (now.isAfter(finalDeadline))
				return;
			// check if now is in the activate hidden tests period
			Optional<LocalDateTime> activationBefore = extractActivationBefore(context);
			if (activationBefore.map(now::isBefore).orElse(false))
				return;
			fail(localized("test_guard.hidden_test_before_deadline_message")); //$NON-NLS-1$
		} else {
			if (hasAnnotation(context, Deadline.class) || hasAnnotation(context, ExtendedDeadline.class))
				throw new AnnotationFormatError(
						formatLocalized("test_guard.public_test_cannot_have_deadline", context.displayName())); //$NON-NLS-1$
		}
	}

	public static boolean hasAnnotation(TestContext context, Class<? extends Annotation> type) {
		Optional<AnnotatedElement> element = context.annotatedElement();
		return findAnnotation(element, type).isPresent();
	}

	public static boolean hasAnnotationType(TestContext context, TestType type) {
		return context.findTestType().orElse(null) == type;
	}

	static LocalDateTime extractDeadline(TestContext context) {
		var deadline = extractDeadline(context.testClass(), context.testMethod());
		if (deadline.isPresent())
			return deadline.get();
		throw new AnnotationFormatError(
				formatLocalized("test_guard.hidden_test_missing_deadline", context.displayName())); //$NON-NLS-1$
	}

	public static Optional<LocalDateTime> extractDeadline(Optional<Class<?>> testClass, Optional<Method> testMethod) {
		var methodLevel = getDeadlineOf(testMethod);
		var methodDelta = getExtensionDurationOf(testMethod);
		// Then only the method counts ("override"), because it has its own deadline

		if (methodLevel.isPresent())
			return methodLevel.map(dl -> dl.plus(methodDelta.orElse(Duration.ZERO)));
		// look in the class otherwise
		var classLevel = findAnnotation(testClass, Deadline.class).map(Deadline::value)
				.map(TestGuardUtils::parseDeadline);
		var classDelta = findAnnotation(testClass, ExtendedDeadline.class).map(ExtendedDeadline::value)
				.map(TestGuardUtils::parseDuration);
		return classLevel.map(dl -> dl.plus(classDelta.orElse(Duration.ZERO)))
				.map(dl -> dl.plus(methodDelta.orElse(Duration.ZERO)));
	}

	static Optional<LocalDateTime> extractActivationBefore(TestContext context) {
		var methodLevel = getActivationBeforeOf(context.testMethod());
		return methodLevel.or(() -> getActivationBeforeOf(context.testClass()));
	}

	static Optional<LocalDateTime> getDeadlineOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, Deadline.class).map(Deadline::value).map(TestGuardUtils::parseDeadline);
	}

	static Optional<Duration> getExtensionDurationOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, ExtendedDeadline.class).map(ExtendedDeadline::value)
				.map(TestGuardUtils::parseDuration);
	}

	static Optional<LocalDateTime> getActivationBeforeOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, ActivateHiddenBefore.class).map(ActivateHiddenBefore::value)
				.map(TestGuardUtils::parseDeadline);
	}

	/**
	 * @param deadlineString a String of format like described in {@link Deadline}
	 *                       (which is like {@link LocalDateTime} ISO)
	 * @return the {@link LocalDateTime}, never <code>null</code>
	 * @throws AnnotationFormatError if the given String's format is invalid
	 * @author Christian Femers
	 */
	@API(status = Status.INTERNAL)
	public static LocalDateTime parseDeadline(String deadlineString) {
		try {
			return LocalDateTime.parse(deadlineString.replace(' ', 'T'));
		} catch (DateTimeParseException e) {
			throw new AnnotationFormatError(formatLocalized("test_guard.invalid_deadline_format", deadlineString), //$NON-NLS-1$
					e);
		}
	}

	/**
	 * @param durationString a String of format like described in
	 *                       {@link ExtendedDeadline}
	 * @return the {@link Duration}, never <code>null</code>
	 * @throws AnnotationFormatError if the given String's format is invalid
	 * @author Christian Femers
	 */
	@API(status = Status.INTERNAL)
	public static Duration parseDuration(String durationString) {
		Matcher matcher = DURATION_PATTERN.matcher(durationString);
		if (!matcher.matches())
			throw new AnnotationFormatError(
					formatLocalized("test_guard.invalid_extended_deadline_format", durationString)); //$NON-NLS-1$
		Integer d = Optional.ofNullable(matcher.group("d")).map(Integer::parseInt).orElse(0); //$NON-NLS-1$
		Integer h = Optional.ofNullable(matcher.group("h")).map(Integer::parseInt).orElse(0); //$NON-NLS-1$
		Integer m = Optional.ofNullable(matcher.group("m")).map(Integer::parseInt).orElse(0); //$NON-NLS-1$
		Duration duration = Duration.ofDays(d).plusHours(h).plusMinutes(m);
		if (duration.isZero() || duration.isNegative())
			throw new AnnotationFormatError(
					formatLocalized("test_guard.extended_deadline_zero_or_negative", durationString)); //$NON-NLS-1$
		return duration;
	}
}
