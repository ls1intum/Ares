package de.tum.in.testuser;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.AddTrustedPackage;
import de.tum.in.test.api.WhitelistClass;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.testuser.subject.PathAccessPenguin;
import de.tum.in.testuser.subject.TrustedPackagePenguin;
import de.tum.in.testuser.subject.WhitelistedClassPenguin;

@Public
@AddTrustedPackage("**subject.TrustedPackageP*")
@WhitelistClass(WhitelistedClassPenguin.class)
public class TrustedClassesUser {

	private static final Path PATH = Path.of("pom.xml");

	@Test
	void testNotWhitelisted() throws IOException {
		PathAccessPenguin.accessPath(PATH);
	}

	@Test
	void testTrustedPackage() throws IOException {
		TrustedPackagePenguin.accessPath(PATH);
	}

	@Test
	void testWhitelistedClass() throws IOException {
		WhitelistedClassPenguin.accessPath(PATH);
	}
}
