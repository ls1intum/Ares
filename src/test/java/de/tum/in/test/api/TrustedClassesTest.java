package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.TrustedClassesUser;

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
		tests.assertThatEvents().haveExactly(1, event(test(testTrustedPackage), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_excludeLocalPortValueToSmall() {
		tests.assertThatEvents().haveExactly(1, event(test(testWhitelistedClass), finishedSuccessfullyRep()));
	}
}
