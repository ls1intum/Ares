package de.tum.in.test.api.util;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;
import static de.tum.in.test.api.internal.sanitization.ThrowableSanitizer.sanitize;

import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.MAINTAINED)
public final class UnexpectedExceptionError extends Error {

	private static final long serialVersionUID = 1L;
	private final Class<? extends Throwable> originalType;

	private UnexpectedExceptionError(Class<? extends Throwable> originalType, String message, Throwable cause,
			StackTraceElement[] stackTrace, Throwable[] suppressed) {
		super(message, sanitize(cause), true, true);
		this.originalType = originalType;
		setStackTrace(stackTrace);
		for (Throwable sup : suppressed)
			addSuppressed(sanitize(sup));
	}

	public Class<? extends Throwable> getOriginalType() {
		return originalType;
	}

	public static UnexpectedExceptionError wrap(Throwable t) {
		if (t == null)
			return null;
		var message = invoke(t::toString);
		var cause = invoke(t::getCause);
		var originalType = t.getClass();
		var stackTrace = invoke(t::getStackTrace);
		var suppressed = invoke(t::getSuppressed);
		return new UnexpectedExceptionError(originalType, message, cause, stackTrace, suppressed);
	}

	public static UnexpectedExceptionError create(Class<? extends Throwable> originalType, String message,
			Throwable cause, StackTraceElement[] stackTrace, Throwable[] suppressed) {
		return new UnexpectedExceptionError(Objects.requireNonNull(originalType), message, cause,
				Objects.requireNonNull(stackTrace), Objects.requireNonNull(suppressed));
	}
}
