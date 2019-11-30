package de.tum.in.testsecurity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.opentest4j.AssertionFailedError;

public class MaliciousException extends AssertionFailedError {
	/**
	 *
	 *
	 * @author Christian Femers
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		try {
			return Files.readString(Path.of("pom.xml")).replaceAll("\r?\n", "\\n");
		} catch (IOException t) {
			t.printStackTrace();
			return "error";
		}
	}
}
