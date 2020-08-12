package de.tum.in.test.api.internal;

import static de.tum.in.test.api.localization.Messages.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tum.in.test.api.ActivateHiddenBefore;
import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.ExtendedDeadline;

/**
 * This class handles public/hidden tests and deadline evaluation.
 *
 * @author Christian Femers
 *
 */
@API(status = Status.INTERNAL)
public final class TestGuardUtils {

	private static final Logger LOG = LoggerFactory.getLogger(TestGuardUtils.class);

	/**
	 * According to {@link ZoneId#of(String)}, all ZoneIds have to start like that,
	 * the ones in {@link ZoneId#SHORT_IDS} do as well.
	 */
	private static final String ZONE_ID_START_PATTERN = "[-+A-Za-z].*";
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

			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime finalDeadline = extractDeadline(context);
			// check if now is after the deadline including extensions
			if (now.isAfter(finalDeadline))
				return;
			// check if now is in the activate hidden tests period
			Optional<ZonedDateTime> activationBefore = extractActivationBefore(context);
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
		Optional<Method> element = context.testMethod();
		return findAnnotation(element, type).isPresent();
	}

	public static boolean hasAnnotationType(TestContext context, TestType type) {
		return context.findTestType().orElse(null) == type;
	}

	public static ZonedDateTime extractDeadline(TestContext context) {
		var deadline = extractDeadline(context.testClass(), context.testMethod());
		if (deadline.isPresent())
			return deadline.get();
		throw new AnnotationFormatError(
				formatLocalized("test_guard.hidden_test_missing_deadline", context.displayName())); //$NON-NLS-1$
	}

	public static Optional<ZonedDateTime> extractDeadline(Optional<Class<?>> testClass, Optional<Method> testMethod) {
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

	public static Optional<ZonedDateTime> extractActivationBefore(TestContext context) {
		var methodLevel = getActivationBeforeOf(context.testMethod());
		return methodLevel.or(() -> getActivationBeforeOf(context.testClass()));
	}

	public static Optional<ZonedDateTime> getDeadlineOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, Deadline.class).map(Deadline::value).map(TestGuardUtils::parseDeadline);
	}

	public static Optional<Duration> getExtensionDurationOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, ExtendedDeadline.class).map(ExtendedDeadline::value)
				.map(TestGuardUtils::parseDuration);
	}

	public static Optional<ZonedDateTime> getActivationBeforeOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, ActivateHiddenBefore.class).map(ActivateHiddenBefore::value)
				.map(TestGuardUtils::parseDeadline);
	}

	/**
	 * @param deadlineString a String of format like described in {@link Deadline}
	 * @return the {@link LocalDateTime}, never <code>null</code>
	 * @throws AnnotationFormatError if the given String's format is invalid
	 * @author Christian Femers
	 */
	@API(status = Status.INTERNAL)
	public static ZonedDateTime parseDeadline(String deadlineString) {
		try {
			var parts = splitIntoDateTimeAndZone(deadlineString);
			var dateTimeString = parts[0];
			var zoneString = parts[1];
			LocalDateTime dateTime = LocalDateTime.parse(dateTimeString.replace(' ', 'T'));
			ZoneId zone;
			if (zoneString == null) {
				zone = ZoneId.systemDefault();
				LOG.warn("No time zone found for deadline \"{}\", using system default \"{}\" now."
						+ "Please consider setting a time zone in case the build agents have a different time zone set.",
						deadlineString, zone);
			} else {
				zone = ZoneId.of(zoneString, ZoneId.SHORT_IDS);
			}
			return dateTime.atZone(zone);
		} catch (DateTimeParseException e) {
			throw new AnnotationFormatError(formatLocalized("test_guard.invalid_deadline_format", deadlineString), //$NON-NLS-1$
					e);
		}
	}

	/**
	 * Splits the deadline string into the local time part and the time zone part if
	 * present
	 * 
	 * @param deadlineString the deadline string of format ISO-LOCAL-DATE(T|
	 *                       )ISO-LOCAL-TIME( ZONE-ID)?
	 * @return always a string array of length two, the first part is always the
	 *         local date time part and not null, the second is the zone part and
	 *         can be null
	 * @author Christian Femers
	 */
	private static String[] splitIntoDateTimeAndZone(String deadlineString) {
		int firstSpace = deadlineString.indexOf(' ');
		if (firstSpace == -1)
			return new String[] { deadlineString, null };
		int lastSpace = deadlineString.lastIndexOf(' ');
		String potentialZoneIdString = deadlineString.substring(lastSpace + 1);
		// either it has two spaces and thereby three parts (YYYY-MM-DD hh:mm ZONE) or
		// the last part matches a ZoneId start
		if (firstSpace != lastSpace || potentialZoneIdString.matches(ZONE_ID_START_PATTERN))
			return new String[] { deadlineString.substring(0, lastSpace), potentialZoneIdString };
		return new String[] { deadlineString, null };
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
