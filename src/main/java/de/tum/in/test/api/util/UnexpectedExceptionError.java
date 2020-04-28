package de.tum.in.test.api.util;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;
import static de.tum.in.test.api.internal.sanitization.ThrowableSanitizer.sanitize;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.sanitization.SanitizationError;

@API(status = Status.MAINTAINED)
public final class UnexpectedExceptionError extends Error {

	private static final long serialVersionUID = 1L;
	private final Class<? extends Throwable> originalType;

	private UnexpectedExceptionError(Throwable cause) throws SanitizationError {
		super(invoke(cause::toString), sanitize(invoke(cause::getCause)), true, true);
		originalType = cause.getClass();
		setStackTrace(invoke(cause::getStackTrace));
		for (Throwable sup : invoke(cause::getSuppressed))
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
