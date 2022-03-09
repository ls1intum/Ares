package de.tum.in.test.integration.testuser.subject;

import de.tum.in.test.api.TestUtils;

public final class PrivilegedExceptionPenguin {

	private PrivilegedExceptionPenguin() {
	}

	public static void throwNullPointerException() {
		throw new NullPointerException("xyz");
	}

	public static void throwPrivilegedNullPointerException() {
		TestUtils.privilegedThrow((Runnable) () -> {
			throw new NullPointerException("xyz");
		});
	}
}
