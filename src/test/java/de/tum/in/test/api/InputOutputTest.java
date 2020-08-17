package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.ComparisonFailure;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.InputOutputUser;

@UserBased(InputOutputUser.class)
class InputOutputTest {

	@UserTestResults
	private static Events tests;

	private final String makeUTF8Error = "makeUTF8Error";
	private final String testPenguin1 = "testPenguin1";
	private final String testPenguin2 = "testPenguin2";
	private final String testPolarBear = "testPolarBear";
	private final String testSquareCorrect = "testSquareCorrect";
	private final String testSquareWrong = "testSquareWrong";
	private final String testTooManyChars = "testTooManyChars";
	private final String testTooManyReads = "testTooManyReads";

	@TestTest
	void test_makeUTF8Error() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(makeUTF8Error, IllegalArgumentException.class));
	}

	@TestTest
	void test_testPenguin1() {
		tests.assertThatEvents().haveExactly(1, event(test(testPenguin1), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testPenguin2() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPenguin2, ComparisonFailure.class));
	}

	@TestTest
	void test_testPolarBear() {
		tests.assertThatEvents().haveExactly(1, event(test(testPolarBear), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testSquareCorrect() {
		tests.assertThatEvents().haveExactly(1, event(test(testSquareCorrect), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testSquareWrong() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testSquareWrong, IllegalStateException.class));
	}

	@TestTest
	void test_testTooManyChars() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testTooManyChars, SecurityException.class));
	}

	@TestTest
	void test_testTooManyReads() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testTooManyReads, IllegalStateException.class));
	}
}
