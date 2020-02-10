package de.tum.in.test.api.util.sanitization;

import static de.tum.in.test.api.util.BlacklistedInvoker.invoke;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import de.tum.in.test.api.locked.ArtemisSecurityManager;

public enum SimpleThrowableSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	@Override
	public boolean canSanitize(Throwable t) {
		return ThrowableSets.SAFE_TYPES.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		Throwable causeVal = invoke(t::getCause);
		Throwable[] supprVal = invoke(t::getSuppressed);
		try {
			Field cause;
			cause = Throwable.class.getDeclaredField("cause");
			cause.setAccessible(true);
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
							ArtemisSecurityManager.getOnModification()));
		} catch (ReflectiveOperationException e) {
			throw new SanitizationError(e);
		}
		return t;
	}
}