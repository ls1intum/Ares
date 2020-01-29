package de.tum.in.testsecurity;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;

import org.assertj.core.api.Condition;
import org.junit.ComparisonFailure;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Event;
import org.opentest4j.AssertionFailedError;

public class SecurityTest {

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
	private final String testTooManyreads = "testTooManyreads";
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

	@Test
	@Tag("test-test")
	void verifySecurity() {
		var results = EngineTestKit.engine("junit-jupiter").selectors(selectClass(SecurityUser.class)).execute();
		var tests = results.tests();

		results.containers().assertStatistics(stats -> stats.started(2).succeeded(2));
		tests.assertStatistics(stats -> stats.started(23));

		tests.assertThatEvents().haveExactly(1, event(test(testPenguin1), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testPolarBear), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testSquareCorrect), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(weUseReflection), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(weAccessPath), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(accessPathAllowed), finishedSuccessfully()));
		tests.assertThatEvents().haveExactly(1, event(test(testEvilPermission), finishedSuccessfully()));

		tests.assertThatEvents().haveExactly(1, testFailedWith(doSystemExit, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionNormal, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(useReflectionTrick, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(accessPathNormal, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(accessPathTest, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMaliciousExceptionA, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testExecuteGit, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testThreadGroup, SecurityException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMaliciousExceptionB, SecurityException.class));
//		tests.assertThatEvents().haveExactly(1, testFailedWith(testThreadBomb, SecurityException.class));

		tests.assertThatEvents().haveExactly(1, testFailedWith(exceedTimeLimit, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(longOutputJUnit4, ComparisonFailure.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(longOutputJUnit5, AssertionFailedError.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(makeUTF8Error, IllegalArgumentException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testPenguin2, ComparisonFailure.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testSquareWrong, IllegalStateException.class));
		tests.assertThatEvents().haveExactly(1, testFailedWith(testTooManyreads, IllegalStateException.class));
	}

	private static Condition<? super Event> testFailedWith(String testName, Class<? extends Throwable> errorType) {
		return event(test(testName), finishedWithFailure(instanceOf(errorType)));
	}
}
