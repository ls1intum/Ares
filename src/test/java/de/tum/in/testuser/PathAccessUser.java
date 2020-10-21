package de.tum.in.testuser;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.testuser.subject.PathAccessPenguin;

@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class PathAccessUser {

	@PublicTest
	@WhitelistPath("")
	void accessPathAllowed() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml"));
	}

	@PublicTest
	void accessPathNormal() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml"));
	}

	@WhitelistPath(value = "../artemis-java-test-sandbox**", type = PathType.GLOB)
	@PublicTest
	void accessPathRelativeGlobA() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml").toAbsolutePath());
	}

	@WhitelistPath(value = "./pom.xml", type = PathType.GLOB)
	@PublicTest
	void accessPathRelativeGlobB() throws IOException {
		PathAccessPenguin.accessPath(Path.of("pom.xml").toAbsolutePath());
	}

	@PublicTest
	@WhitelistPath("")
	void accessPathTest() throws IOException {
		if (!Files.exists(Path.of("target/test-classes/de/tum/in/test/api/SecurityTest.class")))
			fail("File not present");
		PathAccessPenguin.accessPath(Path.of("target/test-classes/de/tum/in/test/api/SecurityTest.class"));
	}

	@PublicTest
	void weAccessPath() throws IOException {
		Files.readString(Path.of("pom.xml"));
	}
}