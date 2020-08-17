package de.tum.in.test.api.internal.sanitization;

enum SimpleThrowableSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	@Override
	public boolean canSanitize(Throwable t) {
		return ThrowableSets.SAFE_TYPES.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) {
		ThrowableSanitizer.copyThrowableInfoSafe(t, t);
		return t;
	}
}
