package de.tum.in.testuser.subject;

import java.io.FilePermission;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
