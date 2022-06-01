package de.tum.in.test.integration.testuser;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.*;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.PathAccessPenguin;

@UseLocale("en")
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class PathAccessUser {

	@PublicTest
	@WhitelistPath("")
	void accessPathAllFiles() {
		PathAccessPenguin.askForFilePermission("<<ALL FILES>>");
	}

	@PublicTest
	@WhitelistPath("")
	void accessPathAllowed() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml"));
	}

	@PublicTest
	void accessPathNormal() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml"));
	}

	@WhitelistPath(value = "../*r*e*s**", type = PathType.GLOB)
	@PublicTest
	void accessPathRelativeGlobA() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml").toAbsolutePath());
	}

	@WhitelistPath(value = "./pom.xml", type = PathType.GLOB)
	@PublicTest
	void accessPathRelativeGlobB() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml").toAbsolutePath());
	}

	@WhitelistPath(value = "../*r*e*s**", type = PathType.GLOB)
	@PublicTest
	void accessPathRelativeGlobDirectChildrenAllowed() {
		PathAccessPenguin.askForFilePermission("src/*");
	}

	@WhitelistPath(value = "../*r*e*s**", type = PathType.GLOB)
	@BlacklistPath(value = "abc")
	@PublicTest
	void accessPathRelativeGlobDirectChildrenBlacklist() {
		PathAccessPenguin.askForFilePermission("*");
	}

	@PublicTest
	void accessPathRelativeGlobDirectChildrenForbidden() {
		PathAccessPenguin.askForFilePermission("*");
	}

	@WhitelistPath(value = "../*r*e*s**", type = PathType.GLOB)
	@PublicTest
	void accessPathRelativeGlobRecursiveAllowed() {
		PathAccessPenguin.askForFilePermission("-");
	}

	@WhitelistPath(value = "../*r*e*s**", type = PathType.GLOB)
	@BlacklistPath(value = "abc")
	@PublicTest
	void accessPathRelativeGlobRecursiveBlacklist() {
		PathAccessPenguin.askForFilePermission("src/-");
	}

	@PublicTest
	void accessPathRelativeGlobRecursiveForbidden() {
		PathAccessPenguin.askForFilePermission("-");
	}

	@PublicTest
	@WhitelistPath("")
	@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
	void accessPathTest() throws IOException {
		Path file = Path.of("src/test/java/de/tum/in/test/integration/SecurityTest.java");
		if (!Files.exists(file))
			fail("File not present: " + file.toAbsolutePath());
		PathAccessPenguin.accessPath(file);
	}

	@PublicTest
	void weAccessPath() throws IOException {
		Files.readString(Path.of("pom.xml"));
	}
}
