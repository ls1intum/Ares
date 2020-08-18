package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.PackageAccessUser;

@UserBased(PackageAccessUser.class)
class PackageAccessTest {

	@UserTestResults
	private static Events tests;

	private final String package_aBlacklistingRegex = "package_aBlacklistingRegex";
	private final String package_bBlacklistingJava = "package_bBlacklistingJava";
	private final String package_cBlacklistingAll = "package_cBlacklistingAll";
	private final String package_dBlackAndWhitelisting = "package_dBlackAndWhitelisting";
	private final String package_eBlackAgain = "package_eBlackAgain";

	@TestTest
	void test_package_aBlacklistingRegex() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(package_aBlacklistingRegex, SecurityException.class));
	}

	@TestTest
	void test_package_bBlacklistingJava() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(package_bBlacklistingJava, SecurityException.class));
	}

	@TestTest
	void test_package_cBlacklistingAll() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(package_cBlacklistingAll, SecurityException.class));
	}

	@TestTest
	void test_package_dBlackAndWhitelisting() {
		tests.assertThatEvents().haveExactly(1, event(test(package_dBlackAndWhitelisting), finishedSuccessfullyRep()));
	}

	@Disabled("Classes are only checked when they are loaded, so changing this again is impossible")
	@TestTest
	void test_package_eBlackAgain() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(package_eBlackAgain, SecurityException.class));
	}
}
