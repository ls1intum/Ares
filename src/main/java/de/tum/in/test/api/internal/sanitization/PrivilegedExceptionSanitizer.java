package de.tum.in.test.api.internal.sanitization;

import de.tum.in.test.api.internal.PrivilegedException;

enum PrivilegedExceptionSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	@Override
	public boolean canSanitize(Throwable t) {
		return t instanceof PrivilegedException;
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		return ThrowableSanitizer.sanitize(((PrivilegedException) t).getPriviledgedThrowable());
	}
}