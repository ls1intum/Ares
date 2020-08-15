package de.tum.in.test.api;

import static de.tum.in.testutil.CustomConditions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;

import java.lang.annotation.AnnotationFormatError;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.testutil.TestTest;

class DeadlineTest {

	private final String testHiddenCustomDeadlineFuture = "testHiddenCustomDeadlineFuture";
	private final String testHiddenCustomDeadlinePast = "testHiddenCustomDeadlinePast";
	private final String testHiddenNormal = "testHiddenNormal";
	private final String testPublicCustomDeadline = "testPublicCustomDeadline";
	private final String testPublicNormal = "testPublicNormal";
	private final String testHiddenTimeZoneUtc = "testHiddenTimeZoneUtc";
	private final String testHiddenTimeZoneBerlin = "testHiddenTimeZoneBerlin";

	private static Events tests;

	@BeforeAll
	@Tag("test-test")
	static void verifyDeadline() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(DeadlineUser.class)).execute();
		tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(7));
	}

	@TestTest
	void test_testPublicNormal() {
		tests.assertThatEvents().haveExactly(1, event(test(testPublicNormal), finishedSuccessfullyRep()));
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
	void test_testPublicCustomDeadline() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPublicCustomDeadline, AnnotationFormatError.class));
	}

	@TestTest
	void test_testHiddenCustomDeadlineFuture() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testHiddenCustomDeadlineFuture, AssertionFailedError.class));
	}

	@TestTest
	void test_testHiddenTimeZoneUtc() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenTimeZoneUtc), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testHiddenTimeZoneBerlin() {
		tests.assertThatEvents().haveExactly(1, event(test(testHiddenTimeZoneBerlin), finishedSuccessfullyRep()));
	}
}
