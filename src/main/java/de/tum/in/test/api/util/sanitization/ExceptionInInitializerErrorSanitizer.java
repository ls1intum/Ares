package de.tum.in.test.api.util.sanitization;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

enum ExceptionInInitializerErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(ExceptionInInitializerError.class);

	/**
	 * Since Java 12 the exception member has been removed, so it is possibly not
	 * present and cannot be sanitized.
	 */
	private static final Optional<Field> EXCEPTION;
	static {
		Field exField;
		try {
			exField = ExceptionInInitializerError.class.getDeclaredField("exception");
			exField.setAccessible(true);
		} catch (@SuppressWarnings("unused") NoSuchFieldException e) {
			exField = null;
		} catch (SecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
		EXCEPTION = Optional.ofNullable(exField);
	}

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		try {
			if (EXCEPTION.isPresent()) {
				Throwable ex = (Throwable) EXCEPTION.get().get(t);
				EXCEPTION.get().set(t, ThrowableSanitizer.sanitize(ex));
			}
			SimpleThrowableSanitizer.INSTANCE.sanitize(t);
		} catch (IllegalArgumentException | ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
		return t;
	}
}