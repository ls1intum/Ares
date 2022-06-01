package de.tum.in.test.integration.testuser.subject;

import java.io.*;
import java.nio.file.*;

public final class PathAccessPenguin {

	private PathAccessPenguin() {
	}

	public static void accessPath(Path p) throws IOException {
		Files.readString(p);
	}

	public static void askForFilePermission(String path) {
		System.getSecurityManager().checkPermission(new FilePermission(path, "read"));
	}
}
