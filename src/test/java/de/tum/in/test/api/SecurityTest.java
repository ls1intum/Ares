package de.tum.in.test.api;

import static de.tum.in.testutil.CustomConditions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;

import java.util.concurrent.ForkJoinPool;

import org.junit.ComparisonFailure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.testuser.SecurityUser;
import de.tum.in.testutil.TestTest;

class SecurityTest {

	private final String doSystemExit = "doSystemExit";
	private final String exceedTimeLimit = "exceedTimeLimit";
	private final String longOutputJUnit4 = "longOutputJUnit4";
	private final String longOutputJUnit5 = "longOutputJUnit5";
	private final String makeUTF8Error = "makeUTF8Error";
	private final String testPenguin1 = "testPenguin1";
	private final String testPenguin2 = "testPenguin2";
	private final String testPolarBear = "testPolarBear";
	private final String testSquareCorrect = "testSquareCorrect";
	private final String testSquareWrong = "testSquareWrong";
	private final String testTooManyReads = "testTooManyReads";
	private final String testTooManyChars = "testTooManyChars";
	private final String useReflectionNormal = "useReflectionNormal";
	private final String useReflectionTrick = "useReflectionTrick";
	private final String weUseReflection = "weUseReflection";
	private final String accessPathNormal = "accessPathNormal";
	private final String accessPathAllowed = "accessPathAllowed";
	private final String accessPathTest = "accessPathTest";
	private final String weAccessPath = "weAccessPath";
	private final String testMaliciousExceptionA = "testMaliciousExceptionA";
	private final String testMaliciousExceptionB = "testMaliciousExceptionB";
	private final String testExecuteGit = "testExecuteGit";
	private final String testThreadGroup = "testThreadGroup";
	private final String testEvilPermission = "testEvilPermission";
	private final String testThreadBomb = "testThreadBomb";
	private final String threadWhitelistingWithPathFail = "threadWhitelistingWithPathFail";
	private final String threadWhitelistingWithPathCorrect = "threadWhitelistingWithPathCorrect";
	private final String threadWhitelistingWithPathPenguin = "threadWhitelistingWithPathPenguin";
	private final String package_aBlacklistingPenguinUtil = "package_aBlacklistingPenguinUtil";
	private final String package_bBlacklistingPenguinJava = "package_bBlacklistingPenguinJava";
	private final String package_cBlacklistingPenguinAll = "package_cBlacklistingPenguinAll";
	private final String package_dBlackAndWhitelistingPenguin = "package_dBlackAndWhitelistingPenguin";
	private final String package_eBlackPenguinAgain = "package_eBlackPenguinAgain";
	private final String accessPathRelativeGlobA = "accessPathRelativeGlobA";
	private final String accessPathRelativeGlobB = "accessPathRelativeGlobB";
	private final String nonprivilegedExceptionExtern = "nonprivilegedExceptionExtern";
	private final String nonprivilegedExceptionIntern = "nonprivilegedExceptionIntern";
	private final String privilegedExceptionNormal = "privilegedExceptionNormal";
	private final String privilegedExceptionFail = "privilegedExceptionFail";
	private final String nonprivilegedExceptionTry = "nonprivilegedExceptionTry";
	private final String commonPoolInterruptable = "commonPoolInterruptable";

	private static Events tests;

	@BeforeAll
	@Tag("test-test")
	static void verifySecurity() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(SecurityUser.class)).execute();
		tests = results.testEvents();

		results.containerEvents().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(40));
	}

	@TestTest
	void test_testPenguin1() {
		tests.assertThatEvents().haveExactly(1, event(test(testPenguin1), finishedSuccessfullyRep()));
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
	void test_weUseReflection() {
		tests.assertThatEvents().haveExactly(1, event(test(weUseReflection), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_weAccessPath() {
		tests.assertThatEvents().haveExactly(1, event(test(weAccessPath), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_accessPathAllowed() {
		tests.assertThatEvents().haveExactly(1, event(test(accessPathAllowed), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testEvilPermission() {
		tests.assertThatEvents().haveExactly(1, event(test(testEvilPermission), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_threadWhitelistingWithPathCorrect() {
		tests.assertThatEvents().haveExactly(1,
				event(test(threadWhitelistingWithPathCorrect), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_package_dBlackAndWhitelistingPenguin() {
		tests.assertThatEvents().haveExactly(1,
				event(test(package_dBlackAndWhitelistingPenguin), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_accessPathRelativeGlobA() {
		tests.assertThatEvents().haveExactly(1, event(test(accessPathRelativeGlobA), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_accessPathRelativeGlobB() {
		tests.assertThatEvents().haveExactly(1, event(test(accessPathRelativeGlobB), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_doSystemExit() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(doSystemExit, SecurityException.class));
	}

	@TestTest
	void test_useReflectionNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionNormal, SecurityException.class));
	}

	@TestTest
	void test_useReflectionTrick() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionTrick, SecurityException.class));
	}

	@TestTest
	void test_accessPathNormal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(accessPathNormal, SecurityException.class));
	}

	@TestTest
	void test_accessPathTest() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(accessPathTest, SecurityException.class));
	}

	@TestTest
	void test_testMaliciousExceptionA() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMaliciousExceptionA, SecurityException.class));
	}

	@TestTest
	void test_testExecuteGit() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testExecuteGit, SecurityException.class));
	}

	@TestTest
	void test_testThreadGroup() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testThreadGroup, SecurityException.class));
	}

	@TestTest
	void test_testMaliciousExceptionB() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMaliciousExceptionB, SecurityException.class));
	}

	@Disabled("Currently unused because this is very inconsistent depending on the CI environment")
	@TestTest
	void test_testThreadBomb() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testThreadBomb, SecurityException.class));
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

	@TestTest
	void test_package_aBlacklistingPenguinUtil() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(package_aBlacklistingPenguinUtil, SecurityException.class));
	}

	@TestTest
	void test_package_bBlacklistingPenguinJava() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(package_bBlacklistingPenguinJava, SecurityException.class));
	}

	@TestTest
	void test_package_cBlacklistingPenguinAll() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(package_cBlacklistingPenguinAll, SecurityException.class));
	}

	@Disabled("Classes are only checked when they are loaded, so changing this again is impossible")
	@TestTest
	void test_package_eBlackPenguinAgain() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(package_eBlackPenguinAgain, SecurityException.class));
	}

	@TestTest
	void test_exceedTimeLimit() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(exceedTimeLimit, AssertionFailedError.class));
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
	void test_makeUTF8Error() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(makeUTF8Error, IllegalArgumentException.class));
	}

	@TestTest
	void test_testPenguin2() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPenguin2, ComparisonFailure.class));
	}

	@TestTest
	void test_testSquareWrong() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testSquareWrong, IllegalStateException.class));
	}

	@TestTest
	void test_testTooManyReads() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testTooManyReads, IllegalStateException.class));
	}

	@TestTest
	void test_testTooManyChars() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testTooManyChars, SecurityException.class));
	}

	@TestTest
	void test_nonprivilegedExceptionExtern() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(nonprivilegedExceptionExtern, AssertionError.class, "ABC"));
	}

	@TestTest
	void test_nonprivilegedExceptionIntern() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(nonprivilegedExceptionIntern, AssertionError.class, "ABC"));
	}

	@TestTest
	void test_privilegedExceptionNormal() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(privilegedExceptionNormal, NullPointerException.class, "xyz"));
	}

	@TestTest
	void test_privilegedExceptionFail() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(privilegedExceptionFail, AssertionError.class, "xyz"));
	}

	@TestTest
	void test_nonprivilegedExceptionTry() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(nonprivilegedExceptionTry, AssertionError.class, "ABC"));
	}

	@TestTest
	void test_commonPoolInterruptable() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(commonPoolInterruptable, AssertionError.class, "execution timed out after 300 ms"));
		assertTrue(ForkJoinPool.commonPool().isQuiescent());
	}
}
