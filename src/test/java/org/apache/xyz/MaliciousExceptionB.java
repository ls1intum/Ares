package org.apache.xyz;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.opentest4j.AssertionFailedError;

public class MaliciousExceptionB extends AssertionFailedError {

	private static final long serialVersionUID = 1L;

	private AtomicBoolean ab;

	public MaliciousExceptionB(AtomicBoolean ab) {
		this.ab = ab;
	}

	@Override
	public String getMessage() {
		try {
			Files.readString(Path.of("pom.xml")).replaceAll("\r?\n", "\\n");
			ab.set(true);
			return "success";
		} catch (IOException t) {
			t.printStackTrace();
			return "error";
		}
	}
}
