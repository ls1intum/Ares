package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.integration.testuser.DeadlineUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;

@UserBased(DeadlineUser.class)
class DeadlineTest {

	@UserTestResults
	private static Events tests;

	private final String testHiddenCustomDeadlineFuture = "testHiddenCustomDeadlineFuture";
	private final String testHiddenCustomDeadlinePast = "testHiddenCustomDeadlinePast";
	private final String testHiddenNormal = "testHiddenNormal";
	private final String testHiddenTimeZoneBerlin = "testHiddenTimeZoneBerlin";
	private final String testHiddenTimeZoneUtc = "testHiddenTimeZoneUtc";
	private final String testPublicCustomDeadline = "testPublicCustomDeadline";
	private final String testPublicNormal = "testPublicNormal";

	@TestTest
	void test_testHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenCustomDeadlinePast() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenCustomDeadlinePast), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenTimeZoneBerlin() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenTimeZoneBerlin), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenTimeZoneUtc() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenTimeZoneUtc), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testPublicCustomDeadline() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicCustomDeadline, AnnotationFormatError.class));
	}

	@TestTest
	void test_testPublicNormal() {
		tests.assertThatEvents().haveExactly(1, event(test(testPublicNormal), finishedSuccessfullyRep()));
	}
}
