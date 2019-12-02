package de.tum.in.test.api.util.sanitization;

import static de.tum.in.test.api.util.BlacklistedInvoker.invoke;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ThrowableSanitizer {

	private ThrowableSanitizer() {
		// static methods only
	}

	private static final List<SpecificThrowableSanitizer> SANITIZERS = List.of(SimpleThrowableSanitizer.INSTANCE,
			AssertionFailedErrorSanitizer.INSTANCE, MultipleFailuresErrorSanitizer.INSTANCE,
			MultipleAssertionsErrorSanitizer.INSTANCE, SoftAssertionErrorSanitizer.INSTANCE);

	private static final Field STACKTRACE;
	private static final Field CAUSE;
	private static final Field SUPPRESSED;
	static {
		try {
			STACKTRACE = Throwable.class.getDeclaredField("stackTrace");
			STACKTRACE.setAccessible(true);
			CAUSE = Throwable.class.getDeclaredField("cause");
			CAUSE.setAccessible(true);
			SUPPRESSED = Throwable.class.getDeclaredField("suppressedExceptions");
			SUPPRESSED.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Throwable sanitize(final Throwable t) throws SanitizationError {
		if (t == null)
			return null;
		// use synchronized to prevent modification of t during sanitization
		synchronized (t) {
			if (UnexpectedExceptionError.class.equals(t.getClass()))
				return t;
			var firstPossibleSan = SANITIZERS.stream().filter(s -> s.canSanitize(t)).findFirst();
			if (firstPossibleSan.isPresent())
				return firstPossibleSan.get().sanitize(t);
			return UnexpectedExceptionError.wrap(t);
		}
	}

	static void copyThrowableInfo(Throwable from, Throwable to) {
		to.setStackTrace(to.getStackTrace());
		try {
			Throwable cause = (Throwable) CAUSE.get(from);
			if (cause == from)
				CAUSE.set(to, to);
			else
				CAUSE.set(to, sanitize(cause));
			SUPPRESSED.set(to, Arrays.stream(invoke(from::getSuppressed)).map(ThrowableSanitizer::sanitize)
					.collect(Collectors.toUnmodifiableList()));
		} catch (IllegalArgumentException | ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}
}
