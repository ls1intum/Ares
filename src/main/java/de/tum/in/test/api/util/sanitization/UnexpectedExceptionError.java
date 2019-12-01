package de.tum.in.test.api.util.sanitization;

import static de.tum.in.test.api.util.sanitization.ThrowableSanitizer.sanitize;

public final class UnexpectedExceptionError extends Error {

	private static final long serialVersionUID = 1L;
	private final Class<? extends Throwable> originalType;

	private UnexpectedExceptionError(Throwable cause) throws SanitizationError {
		super(cause.toString(), sanitize(cause.getCause()), true, true);
		originalType = cause.getClass();
		setStackTrace(cause.getStackTrace());
		for (Throwable sup : cause.getSuppressed())
			addSuppressed(sanitize(sup));
	}

	public Class<? extends Throwable> getOriginalType() {
		return originalType;
	}

	public static UnexpectedExceptionError wrap(Throwable t) throws SanitizationError {
		if (t == null)
			return null;
		return new UnexpectedExceptionError(t);
	}
}
