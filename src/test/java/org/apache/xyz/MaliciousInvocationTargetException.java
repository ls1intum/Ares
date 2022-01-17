package org.apache.xyz;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MaliciousInvocationTargetException extends InvocationTargetException {

	private static final long serialVersionUID = 1L;

	@Override
	public Throwable getTargetException() {
		try {
			Files.readString(Path.of("pom.xml"));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return new Error("succeeded");
	}
}
