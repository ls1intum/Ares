package de.tum.in.test.integration.testuser.subject;

import java.io.IOException;
import java.nio.file.*;

public final class WhitelistedClassPenguin {

	private WhitelistedClassPenguin() {
	}

	public static void accessPath(Path p) throws IOException {
		Files.readString(p);
	}
}
