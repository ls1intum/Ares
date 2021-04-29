package de.tum.in.testuser;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.BlacklistPackage;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPackage;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.testuser.subject.PackageAccessPenguin;

@UseLocale("en")
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class PackageAccessUser {

	@BlacklistPackage("java.util.regex")
	@PublicTest
	void package_aBlacklistingRegex() {
		PackageAccessPenguin.useArrayList();
	}

	@BlacklistPackage("java.**")
	@PublicTest
	void package_bBlacklistingJava() {
		PackageAccessPenguin.useArrayList();
	}

	@BlacklistPackage("**")
	@PublicTest
	void package_cBlacklistingAll() {
		PackageAccessPenguin.useArrayList();
	}

	@BlacklistPackage("**")
	@WhitelistPackage("java.util.regex")
	@PublicTest
	void package_dBlackAndWhitelisting() {
		PackageAccessPenguin.useArrayList();
	}

	@BlacklistPackage("java.util.regex")
	@PublicTest
	void package_eBlackPenguinAgain() {
		PackageAccessPenguin.useArrayList();
	}
}
