package de.tum.in.test.api.security;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.MAINTAINED)
public final class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}
}
