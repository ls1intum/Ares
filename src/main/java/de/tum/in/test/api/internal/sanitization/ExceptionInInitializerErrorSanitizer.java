package de.tum.in.test.api.internal.sanitization;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Optional;
import java.util.Set;

enum ExceptionInInitializerErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private static final String EXCEPTION_NAME = "exception";

	private final Set<Class<? extends Throwable>> types = Set.of(ExceptionInInitializerError.class);

	/**
	 * Since Java 12 the exception member has been removed, so it is possibly not
	 * present and cannot be sanitized.
	 */
	private static final Optional<VarHandle> EXCEPTION;

	static {
		VarHandle exceptionHandle;
		try {
			var lookup = MethodHandles.privateLookupIn(ExceptionInInitializerError.class, MethodHandles.lookup());
			exceptionHandle = lookup.findVarHandle(ExceptionInInitializerError.class, EXCEPTION_NAME, Throwable.class);
		} catch (@SuppressWarnings("unused") NoSuchFieldException e) {
			// expected for some Java versions
			exceptionHandle = null;
		} catch (SecurityException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
		EXCEPTION = Optional.ofNullable(exceptionHandle);
	}

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		try {
			if (EXCEPTION.isPresent()) {
				Throwable ex = (Throwable) EXCEPTION.get().getVolatile(t);
				EXCEPTION.get().setVolatile(t, ThrowableSanitizer.sanitize(ex));
			}
			SimpleThrowableSanitizer.INSTANCE.sanitize(t);
		} catch (IllegalArgumentException e) {
			throw new SanitizationError(e);
		}
		return t;
	}
}
