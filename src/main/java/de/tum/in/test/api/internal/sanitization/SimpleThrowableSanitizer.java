package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.test.api.util.IgnorantUnmodifiableList;

enum SimpleThrowableSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	@Override
	public boolean canSanitize(Throwable t) {
		return ThrowableSets.SAFE_TYPES.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		// this returns either null or another Throwable instance
		Throwable causeVal = invoke(t::getCause);
		Throwable[] supprVal = invoke(t::getSuppressed);
		try {
			Field cause = Throwable.class.getDeclaredField("cause");
			cause.setAccessible(true);
			// because causeVal is never t, this will lock the cause and calls to initCause
			cause.set(t, ThrowableSanitizer.sanitize(causeVal));
		} catch (ReflectiveOperationException e) {
			throw new SanitizationError(e);
		}
		try {
			Field suppr = Throwable.class.getDeclaredField("suppressedExceptions");
			suppr.setAccessible(true);
			// this breaks addSuppressed by purpose
			suppr.set(t,
					IgnorantUnmodifiableList.wrapWith(
							Arrays.stream(supprVal).map(ThrowableSanitizer::sanitize).collect(Collectors.toList()),
							ArtemisSecurityManager.getOnSuppressedModification()));
		} catch (ReflectiveOperationException e) {
			throw new SanitizationError(e);
		}
		return t;
	}
}
