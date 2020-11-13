package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.finishedSuccessfullyRep;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.AttributeTestUser;

@UserBased(AttributeTestUser.class)
class AttributeTestTest {

	@UserTestResults
	private static Events tests;

	private final String testAttributesContext = "dynamic-test:#1"; // "testAttributes[Context]";

	private final String testAttributesPolicy = "dynamic-test:#2"; // "testAttributes[Policy]";

	@TestTest
	void test_testAttributesContext() {
		tests.assertThatEvents().haveExactly(1, event(test(testAttributesContext),
				finishedWithFailure(instanceOf(AssertionFailedError.class), message(m -> m.contains(
						"The name of the expected attribute 'sortAlgorithm' of the class 'Context' is not implemented as expected.")))));
	}

	@TestTest
	void test_testAttributesPolicy() {
		tests.assertThatEvents().haveExactly(1, event(test(testAttributesPolicy), finishedSuccessfullyRep()));

	}
}
