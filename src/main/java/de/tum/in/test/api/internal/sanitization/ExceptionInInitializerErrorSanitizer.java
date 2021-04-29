package de.tum.in.test.api.internal.sanitization;

import java.util.Set;

enum ExceptionInInitializerErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(ExceptionInInitializerError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t, MessageTransformer messageTransformer) {
		ExceptionInInitializerError eiie = (ExceptionInInitializerError) t;
		Throwable exception = eiie.getException();
		var info = ThrowableInfo.getEssentialInfosSafeFrom(t);
		info.setCause(exception);
		info.sanitize();
		String newMessage = messageTransformer.apply(info);
		ExceptionInInitializerError newEiie;
		/*
		 * Prioritize the message and add cause as suppressed if present. Normally both
		 * are mutually exclusive, but our messageTransformer might generate a message
		 * even if there was none before. Because we don't want wo loose the cause, we
		 * add it as suppressed. This, however, will be automatically done by
		 * SanitizationUtils.copyThrowableInfoSafe a cause is present and its call to
		 * initCause fails
		 */
		if (newMessage != null && !newMessage.isEmpty())
			newEiie = new ExceptionInInitializerError(newMessage);
		else if (exception != null)
			newEiie = new ExceptionInInitializerError(info.getCause());
		else
			newEiie = new ExceptionInInitializerError();
		SanitizationUtils.copyThrowableInfoSafe(info, newEiie);
		return newEiie;
	}
}
