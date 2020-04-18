package de.tum.in.test.api;

import static de.tum.in.testutil.CustomConditions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.testutil.TestTest;

public class StrictTimeoutTest {

	private final String testOneSecondFail = "testOneSecondFail";
	private final String testOneSecondSuccess = "testOneSecondSuccess";
	private final String testMethodFailNormal = "testMethodFailNormal";
	private final String testMethodFailLoop = "testMethodFailLoop";
	private final String testMethodSuccess = "testMethodSuccess";
	private final String testClassFailLoop = "testClassFailLoop";
	private final String testClassFailNormal = "testClassFailNormal";
	private final String testClassSuccess = "testClassSuccess";
	private static Events tests;

	@BeforeAll
	@Tag("test-test")
	static void verifyStrictTimeout() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(StrictTimeoutUser.class)).execute();
		tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(8));
	}

	@TestTest
	void test_testOneSecondSuccess() {
		tests.assertThatEvents().haveExactly(1, event(test(testOneSecondSuccess), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testMethodSuccess() {
		tests.assertThatEvents().haveExactly(1, event(test(testMethodSuccess), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testClassSuccess() {
		tests.assertThatEvents().haveExactly(1, event(test(testClassSuccess), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testOneSecondFail() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testOneSecondFail, AssertionFailedError.class));
	}

	@TestTest
	void test_testMethodFailNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMethodFailNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testMethodFailLoop() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMethodFailLoop, AssertionFailedError.class));
	}

	@TestTest
	void test_testClassFailNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassFailNormal, AssertionFailedError.class));
	}

	@TestTest
	void test_testClassFailLoop() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassFailLoop, AssertionFailedError.class));
	}

	@TestTest
	void test_time_testOneSecondFail() {
		tests.assertThatEvents().filteredOn(event(test(testOneSecondFail))).satisfies(deltaTimeIs(1000, 0.3));
	}

	@TestTest
	void test_time_testMethodFailNormal() {
		tests.assertThatEvents().filteredOn(event(test(testMethodFailNormal))).satisfies(deltaTimeIs(300, 0.5));
	}

	@TestTest
	void test_time_testMethodFailLoop() {
		tests.assertThatEvents().filteredOn(event(test(testMethodFailLoop))).satisfies(deltaTimeIs(300, 0.5));
	}

	@TestTest
	void test_time_testClassFailNormal() {
		tests.assertThatEvents().filteredOn(event(test(testClassFailNormal))).satisfies(deltaTimeIs(100, 0.5));
	}

	@TestTest
	void test_time_testClassFailLoop() {
		tests.assertThatEvents().filteredOn(event(test(testClassFailLoop))).satisfies(deltaTimeIs(100, 0.5));
	}

}
