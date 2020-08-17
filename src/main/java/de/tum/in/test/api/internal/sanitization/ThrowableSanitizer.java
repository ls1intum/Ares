package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.ThrowableUtils;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.test.api.util.IgnorantUnmodifiableList;
import de.tum.in.test.api.util.UnexpectedExceptionError;

@API(status = Status.INTERNAL)
public final class ThrowableSanitizer {

	private ThrowableSanitizer() {
		// static methods only
	}

	static {
		// Initialize
		ThrowableSets.SAFE_TYPES.size();
	}

	private static final List<SpecificThrowableSanitizer> SANITIZERS = List.of(SimpleThrowableSanitizer.INSTANCE,
			AssertionFailedErrorSanitizer.INSTANCE, PrivilegedExceptionSanitizer.INSTANCE,
			MultipleFailuresErrorSanitizer.INSTANCE, MultipleAssertionsErrorSanitizer.INSTANCE,
			ExceptionInInitializerErrorSanitizer.INSTANCE, SoftAssertionErrorSanitizer.INSTANCE);

	public static Throwable sanitize(final Throwable t) throws SanitizationError {
		if (t == null)
			return null;
		if (UnexpectedExceptionError.class.equals(t.getClass()))
			return t;
		var firstPossibleSan = SANITIZERS.stream().filter(s -> s.canSanitize(t)).findFirst();
		if (firstPossibleSan.isPresent())
			return firstPossibleSan.get().sanitize(t);
		return UnexpectedExceptionError.wrap(t);
	}

	static void copyThrowableInfoSafe(Throwable from, Throwable to) throws SanitizationError {
		try {
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
		} catch (Exception e) {
			throw new SanitizationError(e);
		}
	}
}
