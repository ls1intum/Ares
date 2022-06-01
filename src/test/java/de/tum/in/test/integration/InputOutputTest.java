package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.ComparisonFailure;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.InputOutputUser;
import de.tum.in.test.testutilities.*;

@UserBased(InputOutputUser.class)
class InputOutputTest {

	@UserTestResults
	private static Events tests;

	private final String customStringBuilderManager = "customStringBuilderManager";
	private final String makeUTF8Error = "makeUTF8Error";
	private final String noneManagerInvalidParameter = "noneManagerInvalidParameter";
	private final String testLinesMatch = "testLinesMatch";
	private final String testPenguin1 = "testPenguin1";
	private final String testPenguin2 = "testPenguin2";
	private final String testPolarBear = "testPolarBear";
	private final String testSquareCorrect = "testSquareCorrect";
	private final String testSquareWrong = "testSquareWrong";
	private final String testTooManyChars = "testTooManyChars";
	private final String testTooManyReads = "testTooManyReads";
	private final String wrongCustomManager = "wrongCustomManager";

	@TestTest
	void test_customStringBuilderManager() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(customStringBuilderManager));
	}

	@TestTest
	void test_makeUTF8Error() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(makeUTF8Error, IllegalArgumentException.class));
	}

	@TestTest
	void test_noneManagerInvalidParameter() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(noneManagerInvalidParameter, ParameterResolutionException.class));
	}

	@TestTest
	void test_testLinesMatch() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testLinesMatch));
	}

	@TestTest
	void test_testPenguin1() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testPenguin1));
	}

	@TestTest
	void test_testPenguin2() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPenguin2, ComparisonFailure.class));
	}

	@TestTest
	void test_testPolarBear() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testPolarBear));
	}

	@TestTest
	void test_testSquareCorrect() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testSquareCorrect));
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

	@TestTest
	void test_wrongCustomManager() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(wrongCustomManager, AnnotationFormatError.class));
	}
}
