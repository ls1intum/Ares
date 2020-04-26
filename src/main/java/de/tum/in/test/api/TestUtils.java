package de.tum.in.test.api;

import java.util.concurrent.Callable;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.PrivilegedException;

@API(status = Status.EXPERIMENTAL)
public final class TestUtils {

	private TestUtils() {
	}

	public static void privilegedThrow(Runnable possiblyThrowingAction) {
		try {
			possiblyThrowingAction.run();
		} catch (Throwable t) {
			throw new PrivilegedException(t);
		}
	}

	public static <R> R privilegedThrow(Callable<R> possiblyThrowingAction) throws Exception {
		try {
			return possiblyThrowingAction.call();
		} catch (Throwable t) {
			throw new PrivilegedException(t);
		}
	}

	public static void privilegedFail(String message) {
		throw new PrivilegedException(new AssertionError(message));
	}
}
