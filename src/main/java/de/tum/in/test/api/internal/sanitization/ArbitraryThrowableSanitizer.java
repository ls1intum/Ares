package de.tum.in.test.api.internal.sanitization;

import de.tum.in.test.api.util.UnexpectedExceptionError;

enum ArbitraryThrowableSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	@Override
	public boolean canSanitize(Throwable t) {
		return true;
	}

	@Override
	public Throwable sanitize(Throwable t, MessageTransformer messageTransformer) {
		var info = ThrowableInfo.getEssentialInfosSafeFrom(t);
		String className = t.getClass().getName();
		String message = messageTransformer.apply(info);
		String combinedMessage = message == null ? className : className + ": " + message; //$NON-NLS-1$
		return UnexpectedExceptionError.create(t.getClass(), combinedMessage, info.getCause(), info.getStackTrace(),
				info.getSuppressed());
	}
}
