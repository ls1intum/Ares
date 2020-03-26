package de.tum.in.test.api.util.sanitization;

interface SpecificThrowableSanitizer {

	boolean canSanitize(Throwable t);

	Throwable sanitize(Throwable t) throws SanitizationError;
}
