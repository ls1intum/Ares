package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.localization.Messages.localized;

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
		this.originClass = Objects.requireNonNull(originClass, "sanitization failure origin class must not be null"); //$NON-NLS-1$
		unsafeCause = Objects.requireNonNull(cause, "sanitization failure cause must not be null"); //$NON-NLS-1$
	}

	public Class<?> getOriginClass() {
		return originClass;
	}

	public Throwable getUnsafeCause() {
		return unsafeCause;
	}

	private static String generateMessage(Class<?> originClass, Throwable cause) {
		return localized("sanitization.sanitization_exception_message", originClass.toString(), //$NON-NLS-1$
				BlacklistedInvoker.invoke(cause::toString));
	}

	@Override
	public String toString() {
		return getMessage();
	}
}
