package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.ComparisonFailure;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.SecurityUser;

@UserBased(SecurityUser.class)
class SecurityTest {

	@UserTestResults
	private static Events tests;

	private final String doSystemExit = "doSystemExit";
	private final String longOutputJUnit4 = "longOutputJUnit4";
	private final String longOutputJUnit5 = "longOutputJUnit5";
	private final String testEvilPermission = "testEvilPermission";
	private final String testExecuteGit = "testExecuteGit";
	private final String testMaliciousExceptionA = "testMaliciousExceptionA";
	private final String testMaliciousExceptionB = "testMaliciousExceptionB";
	private final String trySetSecurityManager = "trySetSecurityManager";
	private final String trySetSystemOut = "trySetSystemOut";
	private final String useReflectionNormal = "useReflectionNormal";
	private final String useReflectionPrivileged = "useReflectionPrivileged";
	private final String useReflectionTrick = "useReflectionTrick";
	private final String weUseReflection = "weUseReflection";

	@TestTest
	void test_doSystemExit() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(doSystemExit, SecurityException.class));
	}

	@TestTest
	void test_longOutputJUnit4() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(longOutputJUnit4, ComparisonFailure.class));
	}

	@TestTest
	void test_longOutputJUnit5() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(longOutputJUnit5, AssertionFailedError.class));
	}

	@TestTest
	void test_testEvilPermission() {
		tests.assertThatEvents().haveExactly(1, event(test(testEvilPermission), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testExecuteGit() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testExecuteGit, SecurityException.class));
	}

	@TestTest
	void test_testMaliciousExceptionA() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMaliciousExceptionA, SecurityException.class));
	}

	@TestTest
	void test_testMaliciousExceptionB() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMaliciousExceptionB, SecurityException.class));
	}

	@TestTest
	void test_trySetSecurityManager() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(trySetSecurityManager, SecurityException.class));
	}

	@TestTest
	void test_trySetSystemOut() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(trySetSystemOut, SecurityException.class));
	}

	@TestTest
	void test_useReflectionNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionNormal, SecurityException.class));
	}

	@TestTest
	void test_useReflectionPrivileged() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionPrivileged, SecurityException.class));
	}

	@TestTest
	void test_useReflectionTrick() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionTrick, SecurityException.class));
	}

	@TestTest
	void test_weUseReflection() {
		tests.assertThatEvents().haveExactly(1, event(test(weUseReflection), finishedSuccessfullyRep()));
	}
}
