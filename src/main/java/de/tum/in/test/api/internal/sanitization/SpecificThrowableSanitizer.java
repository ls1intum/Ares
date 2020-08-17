package de.tum.in.test.api.internal.sanitization;

interface SpecificThrowableSanitizer {

	boolean canSanitize(Throwable t);

	Throwable sanitize(Throwable t);
}
