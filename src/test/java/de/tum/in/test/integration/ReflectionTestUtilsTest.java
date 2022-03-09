package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.integration.testuser.ReflectionTestUtilsUser;
import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;

@UserBased(ReflectionTestUtilsUser.class)
class ReflectionTestUtilsTest {

	@UserTestResults
	private static Events tests;

	private final String testGetConstructor_noSuchMethod = "testGetConstructor_noSuchMethod";
	private final String testGetConstructor_success = "testGetConstructor_success";
	private final String testGetMethod_noMethodName = "testGetMethod_noMethodName";
	private final String testGetMethod_noSuchMethod = "testGetMethod_noSuchMethod";
	private final String testGetMethod_noSuchMethod_noParameters = "testGetMethod_noSuchMethod_noParameters";
	private final String testGetMethod_success = "testGetMethod_success";
	private final String testInvokeMethod_invocationTarget = "testInvokeMethod_invocationTarget";
	private final String testInvokeMethod_success = "testInvokeMethod_success";
	private final String testInvokeMethodRethrowing_illegalAccess = "testInvokeMethodRethrowing_illegalAccess";
	private final String testInvokeMethodRethrowing_illegalArgument = "testInvokeMethodRethrowing_illegalArgument";
	private final String testInvokeMethodRethrowing_nullPointer = "testInvokeMethodRethrowing_nullPointer";
	private final String testNewInstance_classNotFound = "testNewInstance_classNotFound";
	private final String testNewInstance_exceptionInInitializer = "testNewInstance_exceptionInInitializer";
	private final String testNewInstance_illegalAccess = "testNewInstance_illegalAccess";
	private final String testNewInstance_illegalArguments = "testNewInstance_illegalArguments";
	private final String testNewInstance_instantiation = "testNewInstance_instantiation";
	private final String testNewInstance_invocationTarget = "testNewInstance_invocationTarget";
	private final String testNewInstance_noSuchMethod = "testNewInstance_noSuchMethod";
	private final String testNewInstance_success = "testNewInstance_success";
	private final String testValueForAttribute_illegalAccess = "testValueForAttribute_illegalAccess";
	private final String testValueForAttribute_noSuchField = "testValueForAttribute_noSuchField";
	private final String testValueForAttribute_success = "testValueForAttribute_success";

	@TestTest
	void test_invokeMethod_success() {
		tests.assertThatEvents().haveExactly(1, event(test(testInvokeMethod_success), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testGetConstructor_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetConstructor_noSuchMethod,
				AssertionFailedError.class,
				"Could not find the constructor with the parameters: [ String, Boolean ] in the class SomeClass because the constructor does not exist. Make sure to implement this constructor properly."));
	}

	@TestTest
	void test_testGetConstructor_success() {
		tests.assertThatEvents().haveExactly(1, event(test(testGetConstructor_success), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testGetMethod_noMethodName() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_noMethodName, AssertionFailedError.class,
				"Could not find the method 'null' from the class SomeClass because the name of the method is null. Make sure to check the name of the method."));
	}

	@TestTest
	void test_testGetMethod_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_noSuchMethod, AssertionFailedError.class,
				"Could not find the method 'someMethod' with the parameters: [ String ] in the class SomeClass because the method does not exist. Make sure to implement this method properly."));
	}

	@TestTest
	void test_testGetMethod_noSuchMethod_noParameters() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_noSuchMethod_noParameters,
				AssertionFailedError.class,
				"Could not find the method 'someMethod' from the class SomeClass because the method does not exist. Make sure to implement this method properly."));
	}

	@TestTest
	void test_testGetMethod_success() {
		tests.assertThatEvents().haveExactly(1, event(test(testGetMethod_success), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testInvokeMethod_invocationTarget() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethod_invocationTarget,
				AssertionFailedError.class,
				"Could not invoke the method 'throwException' in the class SomeClass because of an exception within the method: java.lang.RuntimeException"));
	}

	@TestTest
	void test_testInvokeMethodRethrowing_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethodRethrowing_illegalAccess,
				AssertionFailedError.class,
				"Could not invoke the method 'superSecretMethod' in the class SomeClass because access to the method was denied. Make sure to check the modifiers of the method."));
	}

	@TestTest
	void test_testInvokeMethodRethrowing_illegalArgument() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethodRethrowing_illegalArgument,
				AssertionFailedError.class,
				"Could not invoke the method 'getAnotherAttribute' in the class SomeClass because the parameters are not implemented right. Make sure to check the parameters of the method."));
	}

	@TestTest
	void test_testInvokeMethodRethrowing_nullPointer() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethodRethrowing_nullPointer,
				AssertionFailedError.class,
				"Could not invoke the method 'getAnotherAttribute' in the class SomeClass because the object was null and the method is an instance method. Make sure to check the static modifier of the method."));
	}

	@TestTest
	void test_testNewInstance_classNotFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_classNotFound,
				AssertionFailedError.class,
				"The class 'DoesNotExist' was not found within the submission. Make sure to implement it properly."));
	}

	@TestTest
	void test_testNewInstance_exceptionInInitializer() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_exceptionInInitializer,
				AssertionFailedError.class,
				"The class 'SomeFailingClass' could not be initialized because an exception was thrown in a static initializer block. Make sure to implement the static initialization without errors."));
	}

	@TestTest
	void test_testNewInstance_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_illegalAccess,
				AssertionFailedError.class,
				"Could not instantiate the class SomeClass because access to its constructor with the parameters: [ String ] was denied. Make sure to check the modifiers of the constructor."));
	}

	@TestTest
	void test_testNewInstance_illegalArguments() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_illegalArguments,
				AssertionFailedError.class,
				"Could not instantiate the class SomeClass because the actual constructor or none of the actual constructors of this class match the expected one. We expect, amongst others, one with [ Integer ] parameters, which does not exist. Make sure to implement this constructor correctly."));
	}

	@TestTest
	void test_testNewInstance_instantiation() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_instantiation,
				AssertionFailedError.class,
				"Could not instantiate the class SomeAbstractClass because the class is abstract and should not have a constructor. Make sure to remove the constructor of the class."));
	}

	@TestTest
	void test_testNewInstance_invocationTarget() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_invocationTarget,
				AssertionFailedError.class,
				"Could not instantiate the class SomeClass because the constructor with 1 parameters threw an exception and could not be initialized. Make sure to check the constructor implementation."));
	}

	@TestTest
	void test_testNewInstance_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_noSuchMethod, AssertionFailedError.class,
				"Could not instantiate the class SomeClass because the class does not have a constructor with the arguments: [ Boolean, Boolean ]. Make sure to implement this constructor properly."));
	}

	@TestTest
	void test_testNewInstance_success() {
		tests.assertThatEvents().haveExactly(1, event(test(testNewInstance_success), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testValueForAttribute_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testValueForAttribute_illegalAccess,
				AssertionFailedError.class,
				"Could not retrieve the attribute 'someAttribute' from the class SomeClass because access to the attribute was denied. Make sure to check the modifiers of the attribute."));
	}

	@TestTest
	void test_testValueForAttribute_noSuchField() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testValueForAttribute_noSuchField,
				AssertionFailedError.class,
				"Could not retrieve the attribute 'noSuchField' from the class SomeClass because the attribute does not exist. Make sure to implement the attribute correctly."));
	}

	@TestTest
	void test_testValueForAttribute_success() {
		tests.assertThatEvents().haveExactly(1, event(test(testValueForAttribute_success), finishedSuccessfullyRep()));
	}
}
