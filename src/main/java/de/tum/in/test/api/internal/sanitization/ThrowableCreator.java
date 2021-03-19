package de.tum.in.test.api.internal.sanitization;

@FunctionalInterface
interface ThrowableCreator {

	Throwable create(ThrowableInfo throwableInfo);
}
