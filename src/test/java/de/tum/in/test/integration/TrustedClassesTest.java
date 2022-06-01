package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.TrustedClassesUser;
import de.tum.in.test.testutilities.*;

@UserBased(TrustedClassesUser.class)
class TrustedClassesTest {

	@UserTestResults
	private static Events tests;

	private final String testNotWhitelisted = "testNotWhitelisted";
	private final String testTrustedPackage = "testTrustedPackage";
	private final String testWhitelistedClass = "testWhitelistedClass";

	@TestTest
	void test_allowAndExcludeLocalPortIntersect() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNotWhitelisted, SecurityException.class));
	}

	@TestTest
	void test_allowLocalPortInsideAllowAboveRange() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testTrustedPackage));
	}

	@TestTest
	void test_excludeLocalPortValueToSmall() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testWhitelistedClass));
	}
}
