package de.tum.in.test.api.internal.sanitization;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
@FunctionalInterface
public interface MessageTransformer {

	MessageTransformer IDENTITY = ThrowableInfo::getMessage;

	String apply(ThrowableInfo throwableInfo);
}
