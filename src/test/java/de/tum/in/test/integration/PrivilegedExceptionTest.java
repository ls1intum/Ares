package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;

import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.PrivilegedExceptionUser;
import de.tum.in.test.testutilities.*;

@UserBased(PrivilegedExceptionUser.class)
class PrivilegedExceptionTest {

	@UserTestResults
	private static Events tests;

	private final String nonprivilegedExceptionExtern = "nonprivilegedExceptionExtern";
	private final String nonprivilegedExceptionIntern = "nonprivilegedExceptionIntern";
	private final String nonprivilegedExceptionTry = "nonprivilegedExceptionTry";
	private final String privilegedExceptionFail = "privilegedExceptionFail";
	private final String privilegedExceptionNormal = "privilegedExceptionNormal";
	private final String privilegedTimeout = "privilegedTimeout";

	@TestTest
	void test_nonprivilegedExceptionExtern() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(nonprivilegedExceptionExtern, AssertionError.class, "ABC"));
	}

	@TestTest
	void test_nonprivilegedExceptionIntern() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(nonprivilegedExceptionIntern, AssertionError.class, "ABC"));
	}

	@TestTest
	void test_nonprivilegedExceptionTry() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(nonprivilegedExceptionTry, AssertionError.class, "ABC"));
	}

	@TestTest
	void test_privilegedExceptionFail() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(privilegedExceptionFail, AssertionError.class, "xyz"));
	}

	@TestTest
	void test_privilegedExceptionNormal() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(privilegedExceptionNormal, NullPointerException.class, "xyz"));
	}

	@TestTest
	void test_privilegedTimeout() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(privilegedTimeout, AssertionError.class, "execution timed out after 300 ms"));
	}
}
