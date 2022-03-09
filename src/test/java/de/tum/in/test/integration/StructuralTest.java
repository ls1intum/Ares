package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.event;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.integration.testuser.StructuralUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;

@UserBased(StructuralUser.class)
class StructuralTest {

	@UserTestResults
	private static Events tests;

	private final String testAttributesSomeInterface = "testAttributes()/dynamic-test:#1";
	private final String testAttributesSomeClass = "testAttributes()/dynamic-test:#2";
	private final String testAttributesSomeEnum = "testAttributes()/dynamic-test:#3";
	private final String testAttributesSomeAbstractClass = "testAttributes()/dynamic-test:#4";
	private final String testAttributesSomeFailingClass = "testAttributes()/dynamic-test:#5";
	private final String testClassDoesNotExist = "testClasses()/dynamic-test:#1";
	private final String testClassSomeInterface = "testClasses()/dynamic-test:#2";
	private final String testClassMisspelledClas = "testClasses()/dynamic-test:#3";
	private final String testClassSomeClass = "testClasses()/dynamic-test:#4";
	private final String testClassSomeEnum = "testClasses()/dynamic-test:#5";
	private final String testClassSomeAbstractClass = "testClasses()/dynamic-test:#6";
	private final String testClassMisspelledclass = "testClasses()/dynamic-test:#7";
	private final String testClassSomeFailingClass = "testClasses()/dynamic-test:#8";
	private final String testConstructorsSomeClass = "testConstructors()/dynamic-test:#1";
	private final String testConstructorsSomeEnum = "testConstructors()/dynamic-test:#2";
	private final String testConstructorsSomeAbstractClass = "testConstructors()/dynamic-test:#3";
	private final String testConstructorsSomeFailingClass = "testConstructors()/dynamic-test:#4";
	private final String testMethodsSomeInterface = "testMethods()/dynamic-test:#1";
	private final String testMethodsSomeClass = "testMethods()/dynamic-test:#2";
	private final String testMethodsSomeEnum = "testMethods()/dynamic-test:#3";
	private final String testMethodsSomeAbstractClass = "testMethods()/dynamic-test:#4";
	private final String testMethodsSomeFailingClass = "testMethods()/dynamic-test:#5";

	@TestTest
	void test_testAttributesSomeInterface() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testAttributesSomeInterface, AssertionFailedError.class,
				"The type of the expected attribute 'ANOTHER_CONSTANT' of the class 'SomeInterface' is not implemented as expected."));
	}

	@TestTest
	void test_testAttributesSomeClass() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testAttributesSomeClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testAttributesSomeEnum() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testAttributesSomeEnum), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testAttributesSomeAbstractClass() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testAttributesSomeAbstractClass, AssertionFailedError.class,
						"The modifier(s) (access type, abstract, etc.) of the expected attribute 'someInt' "
								+ "of the class 'SomeAbstractClass' are not implemented as expected."));
	}

	@TestTest
	void test_testAttributesSomeFailingClass() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testAttributesSomeFailingClass,
				IllegalArgumentException.class, "Invalid entry for modifier: 'penguin: final'"));
	}

	@TestTest
	void test_testClassDoesNotExist() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassDoesNotExist, AssertionFailedError.class,
				"The exercise expects a class with the name DoesNotExist in the package de.tum.in.test.integration.testuser.subject.structural. "
						+ "You did not implement the class in the exercise."));
	}

	@TestTest
	void test_testClassSomeInterface() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testClassSomeInterface), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testClassMisspelledClas() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassMisspelledClas, AssertionFailedError.class,
				"The exercise expects a class with the name MisspelledClas. "
						+ "We found that you implemented a class MisspelledClass, which deviates from the expectation. "
						+ "Check for typos in the class name."));
	}

	@TestTest
	void test_testClassSomeClass() {
		tests.assertThatEvents().haveExactly(1, event(testWithSegments(testClassSomeClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testClassSomeEnum() {
		tests.assertThatEvents().haveExactly(1, event(testWithSegments(testClassSomeEnum), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testClassSomeAbstractClass() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testClassSomeAbstractClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testClassMisspelledclass() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassMisspelledclass, AssertionFailedError.class,
				"The exercise expects a class with the name Misspelledclass. "
						+ "We found that you implemented a class MisspelledClass, which deviates from the expectation. "
						+ "Check for wrong upper case / lower case lettering."));
	}

	@TestTest
	void test_testClassSomeFailingClass() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testClassSomeFailingClass, AssertionFailedError.class,
				"The modifier(s) (access type, abstract, etc.) of SomeFailingClass are not implemented as expected."));
	}

	@TestTest
	void test_testConstructorsSomeClass() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testConstructorsSomeClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testConstructorsSomeEnum() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testConstructorsSomeEnum, AssertionFailedError.class,
				"The parameters of the expected constructor of the class 'SomeEnum' with the parameters: [\"int\"] are not implemented as expected."));
	}

	@TestTest
	void test_testConstructorsSomeAbstractClass() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testConstructorsSomeAbstractClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testConstructorsSomeFailingClass() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testConstructorsSomeFailingClass,
				AssertionFailedError.class,
				"The parameters of the expected constructor of the class 'SomeFailingClass' with the parameters: [\"int\",\"String\"] are not implemented as expected."));
	}

	@TestTest
	void test_testMethodsSomeInterface() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testMethodsSomeInterface), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testMethodsSomeClass() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testMethodsSomeClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testMethodsSomeEnum() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMethodsSomeEnum, AssertionFailedError.class,
				"the expected method 'doesNotExist' of the class 'SomeEnum' with no parameters was not found or is named wrongly."));
	}

	@TestTest
	void test_testMethodsSomeAbstractClass() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testMethodsSomeAbstractClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testMethodsSomeFailingClass() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testMethodsSomeFailingClass, AssertionFailedError.class,
				"The parameters of the expected method 'someMethodWithWrongParameterOrder' of the class 'SomeFailingClass' with the parameters: [\"int\",\"double\"] are not implemented as expected."));
	}
}
