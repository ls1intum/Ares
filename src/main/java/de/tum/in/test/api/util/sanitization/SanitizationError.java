package de.tum.in.test.api.util.sanitization;

import static de.tum.in.test.api.util.BlacklistedInvoker.invoke;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public final class SanitizationError extends Error {

	private static final long serialVersionUID = 1L;

	public SanitizationError() {
	}

	public SanitizationError(String message) {
		super(message);
	}

	public SanitizationError(Throwable cause) {
		super(invoke(cause::toString));
	}
}
