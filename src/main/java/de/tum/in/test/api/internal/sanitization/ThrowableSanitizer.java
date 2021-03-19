package de.tum.in.test.api.internal.sanitization;

import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.UnexpectedExceptionError;

@API(status = Status.INTERNAL)
public final class ThrowableSanitizer {

	private ThrowableSanitizer() {
		// static methods only
	}

	static {
		ThrowableSets.init();
	}

	private static final List<SpecificThrowableSanitizer> SANITIZERS = List.of(SafeTypeThrowableSanitizer.INSTANCE,
			AssertionFailedErrorSanitizer.INSTANCE, PrivilegedExceptionSanitizer.INSTANCE,
			MultipleFailuresErrorSanitizer.INSTANCE, MultipleAssertionsErrorSanitizer.INSTANCE,
			ExceptionInInitializerErrorSanitizer.INSTANCE, SoftAssertionErrorSanitizer.INSTANCE);

	public static Throwable sanitize(final Throwable t) {
		return sanitize(t, MessageTransformer.IDENTITY);
	}

	public static Throwable sanitize(final Throwable t, MessageTransformer messageTransformer) {
		if (t == null)
			return null;
		return SanitizationUtils.sanitizeWithinScopeOf(t, () -> {
			if (UnexpectedExceptionError.class.equals(t.getClass()))
				return t;
			var firstPossibleSan = SANITIZERS.stream().filter(s -> s.canSanitize(t)).findFirst();
			return firstPossibleSan.orElse(ArbitraryThrowableSanitizer.INSTANCE).sanitize(t, messageTransformer);
		});
	}
}
