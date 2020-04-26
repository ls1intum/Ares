package de.tum.in.test.api.internal;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.security.ArtemisSecurityManager;

@API(status = Status.INTERNAL)
public final class PrivilegedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Throwable priviledgedThrowable;

	public PrivilegedException(Throwable priviledgedThrowable) {
		super("priviledged " + priviledgedThrowable.getClass(), null, false, false);
		ArtemisSecurityManager.checkCurrentStack(() -> "caller cannot throw priviledged");
		this.priviledgedThrowable = priviledgedThrowable;
	}

	public Throwable getPriviledgedThrowable() {
		return priviledgedThrowable;
	}
}
