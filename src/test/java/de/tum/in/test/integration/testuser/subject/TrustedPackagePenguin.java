package de.tum.in.test.integration.testuser.subject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TrustedPackagePenguin {

	private TrustedPackagePenguin() {
	}

	public static void accessPath(Path p) throws IOException {
		Files.readString(p);
	}
}
