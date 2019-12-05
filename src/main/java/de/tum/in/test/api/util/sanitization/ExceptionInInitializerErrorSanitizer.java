package de.tum.in.test.api.util.sanitization;

import java.lang.reflect.Field;
import java.util.Set;

enum ExceptionInInitializerErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(ExceptionInInitializerError.class);

	private static final Field EXCEPTION;
	static {
		try {
			EXCEPTION = ExceptionInInitializerError.class.getDeclaredField("exception");
			EXCEPTION.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		try {
			Throwable ex = (Throwable) EXCEPTION.get(t);
			EXCEPTION.set(t, ThrowableSanitizer.sanitize(ex));
		} catch (IllegalArgumentException | ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
		return t;
	}
}