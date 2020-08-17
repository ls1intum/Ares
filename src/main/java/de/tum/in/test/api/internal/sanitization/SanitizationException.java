package de.tum.in.test.api.internal.sanitization;

import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.BlacklistedInvoker;

@API(status = Status.INTERNAL)
class SanitizationException extends RuntimeException {

	private static final long serialVersionUID = -5196693239081725334L;

	private final Class<?> originClass;
	private final Throwable unsafeCause;

	public SanitizationException(Class<?> originClass, Throwable cause) {
		super(generateMessage(originClass, cause));
		this.originClass = Objects.requireNonNull(originClass, "sanitization failure origin class must not be null");
		this.unsafeCause = Objects.requireNonNull(cause, "sanitization failure cause must not be null");
	}

	public Class<?> getOriginClass() {
		return originClass;
	}

	public Throwable getUnsafeCause() {
		return unsafeCause;
	}

	private static String generateMessage(Class<?> originClass, Throwable cause) {
		StringBuilder message = new StringBuilder();
		message.append(originClass.toString());
		message.append(" threw an exception when retrieving information about it. (");
		message.append(BlacklistedInvoker.invoke(cause::toString));
		message.append(")");
		return message.toString();
	}

	@Override
	public String toString() {
		return getMessage();
	}
}
