package de.tum.in.test.api.util.sanitization;

final class SanitizationError extends Error {

	private static final long serialVersionUID = 1L;

	public SanitizationError() {
	}

	public SanitizationError(String message) {
		super(message);
	}

	public SanitizationError(Throwable cause) {
		super(cause.toString());
	}
}