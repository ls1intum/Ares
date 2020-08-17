package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.tum.in.test.api.internal.ThrowableUtils;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.test.api.util.IgnorantUnmodifiableList;

final class SanitizationUtils {

	private SanitizationUtils() {

	}

	static void copyThrowableInfoSafe(Throwable from, Throwable to) {
		// this is only needed for transfer
		if (from != to) {
			// the message and stack trace are safe
			ThrowableUtils.setDetailMessage(to, ThrowableUtils.getDetailMessage(from));
			to.setStackTrace(from.getStackTrace());
		}

		// this returns either null or another Throwable instance
		Throwable causeVal = invoke(from::getCause);
		Throwable[] supprVal = invoke(from::getSuppressed);

		// because causeVal is never t, this will lock the cause and calls to initCause
		var newCause = ThrowableSanitizer.sanitize(causeVal);
		ThrowableUtils.setCause(to, newCause);

		// this breaks addSuppressed by purpose
		var newSupressed = IgnorantUnmodifiableList.wrapWith(
				Arrays.stream(supprVal).map(ThrowableSanitizer::sanitize).collect(Collectors.toList()),
				ArtemisSecurityManager.getOnSuppressedModification());
		ThrowableUtils.setSuppressedException(to, newSupressed);
	}

	static <T> T sanitizeWithinScopeOf(Object scope, SanitizationAction<T> sanitizationAction) {
		return SanitizationUtils.sanitizeWithinScopeOf(scope.getClass(), sanitizationAction);
	}

	static <T> T sanitizeWithinScopeOf(Class<?> scope, SanitizationAction<T> sanitizationAction) {
		try {
			return sanitizationAction.executeSanitization();
		} catch (SanitizationException sanitizationException) {
			throw sanitizationException;
		} catch (Throwable t) {
			throw new SanitizationException(scope, t);
		}
	}
}
