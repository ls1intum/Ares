package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfullyRep;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.LocaleUser;
import de.tum.in.testuser.LocaleUser.LocaleEn;
import de.tum.in.testuser.LocaleUser.LocaleUnsupported;

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
		tests.assertThatEvents().haveExactly(1, event(test(testLocaleEn), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testLocaleUnsupported() {
		tests.assertThatEvents().haveExactly(1, event(test(testLocaleUnsupported), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testLocaleDe() {
		tests.assertThatEvents().haveExactly(1, event(test(testLocaleDe), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testUnknownFormatted() {
		tests.assertThatEvents().haveExactly(1, event(test(testUnknownFormatted), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testUnknownNormal() {
		tests.assertThatEvents().haveExactly(1, event(test(testUnknownNormal), finishedSuccessfullyRep()));
	}
}
