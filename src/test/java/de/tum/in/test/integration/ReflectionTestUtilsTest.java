package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.integration.testuser.ReflectionTestUtilsUser;
import de.tum.in.test.testutilities.*;

@UserBased(ReflectionTestUtilsUser.class)
class ReflectionTestUtilsTest {

	@UserTestResults
	private static Events tests;

	private final String testGetConstructor_noSuchMethod = "testGetConstructor_noSuchMethod";
	private final String testGetConstructor_success = "testGetConstructor_success";
	private final String testGetMethod_noMethodName = "testGetMethod_noMethodName";
	private final String testGetMethod_noSuchMethod_noParameters = "testGetMethod_noSuchMethod_noParameters";
	private final String testGetMethod_noSuchMethod_withParameters = "testGetMethod_noSuchMethod_withParameters";
	private final String testGetMethod_success = "testGetMethod_success";
	private final String testInvokeMethod_classNotVisible = "testInvokeMethod_classNotVisible";
	private final String testInvokeMethod_invocationTarget = "testInvokeMethod_invocationTarget";
	private final String testInvokeMethod_success = "testInvokeMethod_success";
	private final String testInvokeMethodRethrowing_illegalAccess = "testInvokeMethodRethrowing_illegalAccess";
	private final String testInvokeMethodRethrowing_illegalArgument = "testInvokeMethodRethrowing_illegalArgument";
	private final String testInvokeMethodRethrowing_nullPointer = "testInvokeMethodRethrowing_nullPointer";
	private final String testInvokePrivateMethodByName_success = "testInvokePrivateMethodByName_success";
	private final String testInvokePrivateMethodRethrowing_success = "testInvokePrivateMethodRethrowing_success";
	private final String testNewInstance_classNotFound = "testNewInstance_classNotFound";
	private final String testNewInstance_classNotVisible = "testNewInstance_classNotVisible";
	private final String testNewInstance_exceptionInInitializer = "testNewInstance_exceptionInInitializer";
	private final String testNewInstance_illegalAccess = "testNewInstance_illegalAccess";
	private final String testNewInstance_illegalArguments = "testNewInstance_illegalArguments";
	private final String testNewInstance_instantiation = "testNewInstance_instantiation";
	private final String testNewInstance_invocationTarget = "testNewInstance_invocationTarget";
	private final String testNewInstance_noSuchMethod = "testNewInstance_noSuchMethod";
	private final String testNewInstance_success = "testNewInstance_success";
	private final String testNewInstancePrivateConstructor_success = "testNewInstancePrivateConstructor_success";
	private final String testSetValueOfAttribute_final = "testSetValueOfAttribute_final";
	private final String testSetValueOfAttribute_illegalAccess = "testSetValueOfAttribute_illegalAccess";
	private final String testSetValueOfAttribute_success = "testSetValueOfAttribute_success";
	private final String testSetValueOfNonPublicAttribute_final = "testSetValueOfNonPublicAttribute_final";
	private final String testSetValueOfNonPublicAttribute_success = "testSetValueOfNonPublicAttribute_success";
	private final String testValueForAttribute_classNotVisible = "testValueForAttribute_classNotVisible";
	private final String testValueForAttribute_illegalAccess = "testValueForAttribute_illegalAccess";
	private final String testValueForAttribute_noSuchField = "testValueForAttribute_noSuchField";
	private final String testValueForAttribute_success = "testValueForAttribute_success";
	private final String testValueForPrivateAttribute_success = "testValueForPrivateAttribute_success";
	private final String testNewInstanceRethrowing_invocationTarget = "testNewInstanceRethrowing_invocationTarget";
	private final String testNewInstanceFromNonPublicConstructorRethrowing_success = "testNewInstanceFromNonPublicConstructorRethrowing_success";
	private final String testGetNonPublicConstructor_noSuchMethod = "testGetNonPublicConstructor_noSuchMethod";

	@TestTest
	void test_invokeMethod_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testInvokeMethod_success));
	}

	@TestTest
	void test_invokePrivateMethodByName_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testInvokePrivateMethodByName_success));
	}

	@TestTest
	void test_testGetConstructor_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetConstructor_noSuchMethod,
				AssertionFailedError.class,
				"Could not find the constructor with the parameters: [ String, Boolean ] in the class SomeClass because the constructor does not exist. Make sure to implement this constructor properly."));
	}

	@TestTest
	void test_testGetConstructor_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testGetConstructor_success));
	}

	@TestTest
	void test_testGetMethod_noMethodName() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_noMethodName, AssertionFailedError.class,
				"Could not find the method 'null' in the class SomeClass because the name of the method is null. Make sure to check the name of the method."));
	}

	@TestTest
	void test_testGetMethod_noSuchMethod_noParameters() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_noSuchMethod_noParameters,
				AssertionFailedError.class,
				"Could not find the method 'someMethod' in the class SomeClass because the method does not exist. Make sure to implement this method properly."));
	}

	@TestTest
	void test_testGetMethod_noSuchMethod_withParameters() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_noSuchMethod_withParameters,
				AssertionFailedError.class,
				"Could not find the method 'someMethod' with the parameters: [ String ] in the class SomeClass because the method does not exist. Make sure to implement this method properly."));
	}

	@TestTest
	void test_testGetMethod_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testGetMethod_success));
	}

	@TestTest
	void test_testInvokeMethod_classNotVisible() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethod_classNotVisible,
				AssertionFailedError.class,
				"Could not invoke the method 'publicMethod' in the class Nested because access to the method was denied. Make sure to check the modifiers of the class."));
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
	void test_testInvokePrivateMethodRethrowing_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testInvokePrivateMethodRethrowing_success));
	}

	@TestTest
	void test_testNewInstance_classNotFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_classNotFound,
				AssertionFailedError.class,
				"The class 'DoesNotExist' was not found within the submission. Make sure to implement it properly."));
	}

	@TestTest
	void test_testNewInstance_classNotVisible() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_classNotVisible,
				AssertionFailedError.class,
				"Could not instantiate the class Nested because access to its constructor with the parameters: none was denied. Make sure to check the modifiers of the class."));
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
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testNewInstance_success));
	}

	@TestTest
	void test_testNewInstancePrivateConstructor_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testNewInstancePrivateConstructor_success));
	}

	@TestTest
	void test_testSetValueOfAttribute_final() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testSetValueOfAttribute_final,
				AssertionFailedError.class,
				"Could not set the value of attribute 'SOME_CONSTANT' from the class SomeClass because it is final. Make sure to check the modifiers of the attribute."));
	}

	@TestTest
	void test_testSetValueOfAttribute_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testSetValueOfAttribute_illegalAccess,
				AssertionFailedError.class,
				"Could not retrieve the attribute 'someAttribute' from the class SomeClass because access to the attribute was denied. Make sure to check the modifiers of the attribute."));
	}

	@TestTest
	void test_testSetValueOfAttribute_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testSetValueOfAttribute_success));
	}

	@TestTest
	void test_testSetValueOfNonPublicAttribute_final() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testSetValueOfNonPublicAttribute_final,
				AssertionFailedError.class,
				"Could not set the value of attribute 'someFinalAttribute' from the class SomeClass because it is final. Make sure to check the modifiers of the attribute."));
	}

	@TestTest
	void test_testSetValueOfNonPublicAttribute_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testSetValueOfNonPublicAttribute_success));
	}

	@TestTest
	void test_testValueForAttribute_classNotVisible() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testValueForAttribute_classNotVisible,
				AssertionFailedError.class,
				"Could not retrieve the attribute 'innerValue' from the class Nested because access to the attribute was denied. Make sure to check the modifiers of the class."));
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
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testValueForAttribute_success));
	}

	@TestTest
	void test_testValueForPrivateAttribute_success() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testValueForPrivateAttribute_success));
	}

	@TestTest
	void test_testNewInstanceRethrowing_invocationTarget() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(testNewInstanceRethrowing_invocationTarget, RuntimeException.class));
	}

	@TestTest
	void test_testNewInstanceFromNonPublicConstructorRethrowing_success() {
		tests.assertThatEvents().haveExactly(1,
				finishedSuccessfully(testNewInstanceFromNonPublicConstructorRethrowing_success));
	}

	@TestTest
	void test_testGetNonPublicConstructor_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetNonPublicConstructor_noSuchMethod,
				AssertionFailedError.class,
				"Could not find the constructor with the parameters: [ String, Boolean ] in the class SomeClass because the constructor does not exist. Make sure to implement this constructor properly."));
	}
}
