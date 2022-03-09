package de.tum.in.test.integration.testuser.subject;

public final class ExceptionFailurePenguin {

	private ExceptionFailurePenguin() {
	}

	public static void throwNullPointerException() {
		throw new NullPointerException("xyz");
	}

	public static void throwExceptionInInitializerError() {
		throw new ExceptionInInitializerError("abc");
	}
}
