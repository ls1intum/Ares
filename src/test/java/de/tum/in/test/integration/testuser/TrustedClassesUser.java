package de.tum.in.test.integration.testuser;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.*;

@Public
@UseLocale("en")
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
