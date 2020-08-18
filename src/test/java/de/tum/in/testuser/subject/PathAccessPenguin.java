package de.tum.in.testuser.subject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathAccessPenguin {

	private PathAccessPenguin() {

	}

	public static void accessPath(Path p) throws IOException {
		Files.readString(p);
	}
}
