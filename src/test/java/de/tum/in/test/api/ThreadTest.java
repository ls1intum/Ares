package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.*;

import java.util.concurrent.ForkJoinPool;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.ThreadUser;

@UserBased(ThreadUser.class)
class ThreadTest {

	@UserTestResults
	private static Events tests;

	private final String commonPoolInterruptable = "commonPoolInterruptable";
	private final String testThreadBomb = "testThreadBomb";
	private final String testThreadGroup = "testThreadGroup";
	private final String threadLimitExceeded = "threadLimitExceeded";
	private final String threadWhitelistingWithPathCorrect = "threadWhitelistingWithPathCorrect";
	private final String threadWhitelistingWithPathFail = "threadWhitelistingWithPathFail";
	private final String threadWhitelistingWithPathPenguin = "threadWhitelistingWithPathPenguin";

	@TestTest
	void test_commonPoolInterruptable() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(commonPoolInterruptable, AssertionError.class, "execution timed out after 300 ms"));
		assertTrue(ForkJoinPool.commonPool().isQuiescent());
	}

	@Disabled("Currently unused because this is very inconsistent depending on the CI environment")
	@TestTest
	void test_testThreadBomb() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testThreadBomb, SecurityException.class));
	}

	@TestTest
	void test_testThreadGroup() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testThreadGroup, SecurityException.class));
	}

	@TestTest
	void test_threadLimitExceeded() {
		tests.assertThatEvents().haveExactly(1, event(test(threadLimitExceeded),
				finishedWithFailure(instanceOf(SecurityException.class), message(m -> m.contains("2 (max: 1)")))));
	}

	@TestTest
	void test_threadWhitelistingWithPathCorrect() {
		tests.assertThatEvents().haveExactly(1,
				event(test(threadWhitelistingWithPathCorrect), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_threadWhitelistingWithPathFail() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(threadWhitelistingWithPathFail, SecurityException.class));
	}

	@TestTest
	void test_threadWhitelistingWithPathPenguin() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(threadWhitelistingWithPathPenguin, SecurityException.class));
	}
}
