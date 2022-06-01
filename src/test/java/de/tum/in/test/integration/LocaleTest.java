package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfully;

import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.LocaleUser;
import de.tum.in.test.integration.testuser.LocaleUser.*;
import de.tum.in.test.testutilities.*;

@UserBased({ LocaleUser.class, LocaleEn.class, LocaleUnsupported.class })
class LocaleTest {

	@UserTestResults
	private static Events tests;

	private final String testLocaleEn = "testLocaleEn";
	private final String testLocaleUnsupported = "testLocaleUnsupported";
	private final String testLocaleDe = "testLocaleDe";
	private final String testUnknownFormatted = "testUnknownFormatted";
	private final String testUnknownNormal = "testUnknownNormal";

	@TestTest
	void test_testLocaleEn() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testLocaleEn));
	}

	@TestTest
	void test_testLocaleUnsupported() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testLocaleUnsupported));
	}

	@TestTest
	void test_testLocaleDe() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testLocaleDe));
	}

	@TestTest
	void test_testUnknownFormatted() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testUnknownFormatted));
	}

	@TestTest
	void test_testUnknownNormal() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testUnknownNormal));
	}
}
