package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;
import static de.tum.in.test.testutilities.CustomConditions.testFailedWith;
import static org.junit.platform.testkit.engine.EventConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.integration.testuser.HiddenPublicUser;
import de.tum.in.test.testutilities.*;

@UserBased(HiddenPublicUser.class)
class HiddenPublicTest {

	@UserTestResults
	private static Events tests;

	private final String testHiddenCustomDeadlineFuture = "testHiddenCustomDeadlineFuture";
	private final String testHiddenCustomDeadlinePast = "testHiddenCustomDeadlinePast";
	private final String testHiddenIncomplete = "testHiddenIncomplete";
	private final String testHiddenNormal = "testHiddenNormal";
	private final String testPublicCustomDeadline = "testPublicCustomDeadline";
	private final String testPublicIncomplete = "testPublicIncomplete";
	private final String testPublicNormal = "testPublicNormal";

	@TestTest
	void test_testHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenCustomDeadlinePast() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHiddenCustomDeadlinePast));
	}

	@TestTest
	void test_testHiddenIncomplete() {
		tests.assertThatEvents().doNotHave(event(test(testHiddenIncomplete)));
	}

	@TestTest
	void test_testHiddenNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testHiddenNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testPublicCustomDeadline() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicCustomDeadline, AnnotationFormatError.class));
	}

	@TestTest
	void test_testPublicIncomplete() {
		tests.assertThatEvents().doNotHave(event(test(testPublicIncomplete)));
	}

	@TestTest
	void test_testPublicNormal() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testPublicNormal));
	}
}
