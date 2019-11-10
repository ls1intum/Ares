package de.tum.in.test.api.locked;

import static de.tum.in.test.api.localization.Messages.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.ExtendedDeadline;
import de.tum.in.test.api.HiddenTest;
import de.tum.in.test.api.PublicTest;

/**
 * This class' main purpose is to guard the {@link HiddenTest}s execution and
 * evaluate the {@link Deadline}.
 *
 * @author Christian Femers
 */
public final class ArtemisTestGuard implements InvocationInterceptor, DisplayNameGenerator {

	private static final Pattern DURATION_PATTERN = Pattern
			.compile("(?:(?<d>\\d+)d)?\\s*(?:\\b(?<h>\\d+)h)?\\s*(?:\\b(?<m>\\d+)m)?", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
	private final DisplayNameGenerator defaultNameGen = new DisplayNameGenerator.ReplaceUnderscores();

	@Override
	public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
		checkForHidden(extensionContext);
		doProceedAndPostProcess(invocation);
	}

	@Override
	public <T> T interceptTestFactoryMethod(Invocation<T> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		checkForHidden(extensionContext);
		return doProceedAndPostProcess(invocation);
	}

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		checkForHidden(extensionContext);
		doProceedAndPostProcess(invocation);
	}

	@Override
	public void interceptTestTemplateMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		checkForHidden(extensionContext);
		doProceedAndPostProcess(invocation);
	}

	private static <T> T doProceedAndPostProcess(Invocation<T> invocation) throws Throwable {
		/*
		 * Currently not needed anymore, because Artemis #993 is fixed, but might be
		 * useful in future (e.g. convert newlines/tabs to \n, \t or similar)
		 */
//		try {
		return invocation.proceed();
//		} catch (Throwable t) {
//			tryPostProcessFieldOrAddSuppressed(t, "detailMessage");
//			throw t;
//		}
	}

	@SuppressWarnings("unused")
	private static String tryPostProcessFieldOrAddSuppressed(Throwable t, String fieldName) {
		try {
			Field f = Throwable.class.getDeclaredField(fieldName);
			f.setAccessible(true);
			String value = postProcessMessage((String) f.get(t));
			f.set(t, value);
			return value;
		} catch (Exception e) {
			t.addSuppressed(e);
			return null;
		}
	}

	private static String postProcessMessage(String message) {
		return message.replace('<', '"').replace('>', '"');
	}

	private static void checkForHidden(ExtensionContext context) {
		if (hasAnnotation(context, HiddenTest.class)) {
			// check if there are both, that would be a mistake
			if (hasAnnotation(context, PublicTest.class))
				throw new AnnotationFormatError(
						formatLocalized("test_guard.test_cannot_be_public_and_hidden", context.getDisplayName())); //$NON-NLS-1$

			LocalDateTime finalDeadline = extractDeadline(context);
			// check if now is after the deadline including extensions
			if (LocalDateTime.now().isAfter(finalDeadline))
				return;
			fail(localized("test_guard.hidden_test_before_deadline_message")); //$NON-NLS-1$
		} else {
			if (hasAnnotation(context, Deadline.class) || hasAnnotation(context, ExtendedDeadline.class))
				throw new AnnotationFormatError(
						formatLocalized("test_guard.public_test_cannot_have_deadline", context.getDisplayName())); //$NON-NLS-1$
		}
	}

	static boolean hasAnnotation(ExtensionContext context, Class<? extends Annotation> annotation) {
		Optional<AnnotatedElement> element = context.getElement();
		return findAnnotation(element, annotation).isPresent();
	}

	static LocalDateTime extractDeadline(ExtensionContext context) {
		var deadline = extractDeadline(context.getTestClass(), context.getTestMethod());
		if (deadline.isPresent())
			return deadline.get();
		throw new AnnotationFormatError(
				formatLocalized("test_guard.hidden_test_missing_deadline", context.getDisplayName())); //$NON-NLS-1$
	}

	static Optional<LocalDateTime> extractDeadline(Optional<Class<?>> testClass, Optional<Method> testMethod) {
		var methodLevel = getDeadlineOf(testMethod);
		var methodDelta = getExtensionDurationOf(testMethod);
		// Then only the method counts ("override"), because it has its own deadline

		if (methodLevel.isPresent()) {
			return methodLevel.map(dl -> dl.plus(methodDelta.orElse(Duration.ZERO)));
		}
		// look in the class otherwise
		var classLevel = findAnnotation(testClass, Deadline.class).map(Deadline::value)
				.map(ArtemisTestGuard::parseDeadline);
		var classDelta = findAnnotation(testClass, ExtendedDeadline.class).map(ExtendedDeadline::value)
				.map(ArtemisTestGuard::parseDuration);
		return classLevel.map(dl -> dl.plus(classDelta.orElse(Duration.ZERO)))
				.map(dl -> dl.plus(methodDelta.orElse(Duration.ZERO)));
	}

	static Optional<LocalDateTime> getDeadlineOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, Deadline.class).map(Deadline::value).map(ArtemisTestGuard::parseDeadline);
	}

	static Optional<Duration> getExtensionDurationOf(Optional<? extends AnnotatedElement> element) {
		return findAnnotation(element, ExtendedDeadline.class).map(ExtendedDeadline::value)
				.map(ArtemisTestGuard::parseDuration);
	}

	static LocalDateTime parseDeadline(String deadlineString) {
		try {
			return LocalDateTime.parse(deadlineString.replace(' ', 'T'));
		} catch (DateTimeParseException e) {
			throw new AnnotationFormatError(formatLocalized("test_guard.invalid_deadline_format", deadlineString), //$NON-NLS-1$
					e);
		}
	}

	static Duration parseDuration(String durationString) {
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

	@Override
	public String generateDisplayNameForClass(Class<?> testClass) {
		return defaultNameGen.generateDisplayNameForClass(testClass);
	}

	@Override
	public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
		return defaultNameGen.generateDisplayNameForNestedClass(nestedClass);
	}

	@Override
	public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
		if (testMethod.isAnnotationPresent(HiddenTest.class)) {
			var deadline = extractDeadline(Optional.of(testClass), Optional.of(testMethod));
			String name = testMethod.toString();
			if (deadline.isEmpty())
				return formatLocalized("test_guard.obfuscate_hidden_test_missing_deadline", name); //$NON-NLS-1$
			if (LocalDateTime.now().isBefore(deadline.get()))
				return String.format("Hidden Test %08X", name.hashCode()); //$NON-NLS-1$
		}
		return defaultNameGen.generateDisplayNameForMethod(testClass, testMethod);
	}
}
