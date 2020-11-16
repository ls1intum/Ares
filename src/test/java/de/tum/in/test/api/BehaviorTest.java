package de.tum.in.test.api;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.BehaviorUser;
import de.tum.in.testuser.StructuralUser;
import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.event;

@UserBased(BehaviorUser.class)
class BehaviorTest {

	@UserTestResults
	private static Events tests;

	private final String testNewInstance = "testNewInstance";
	private final String testNewInstance_cnf = "testNewInstance_classNotFound";
	private final String testNewInstance_eii = "testNewInstance_exceptionInInitializer";
	private final String testNewInstance_nsm = "testNewInstance_noSuchMethod";
	private final String testNewInstance_iacc = "testNewInstance_illegalAccess";
	private final String testNewInstance_iarg = "testNewInstance_illegalArguments";
	private final String testNewInstance_inst = "testNewInstance_instantiation";
	private final String testNewInstance_inv = "testNewInstance_invocationTarget";
	private final String testValueForAttribute = "testValueForAttribute";
	private final String testValueForAttribute_nsf = "testValueForAttribute_noSuchField";
	private final String testValueForAttribute_iacc = "testValueForAttribute_illegalAccess";
	private final String testGetMethod = "testGetMethod";
	private final String testGetMethod_nsm = "testGetMethod_noSuchMethod";
	private final String testGetMethod_nsm_np = "testGetMethod_noSuchMethod_noParameters";
	private final String testGetMethod_nmn = "testGetMethod_noMethodName";
	private final String testInvokeMethod = "testInvokeMethod";
	private final String testInvokeMethod_inv = "testInvokeMethod_invocationTarget";
	private final String testInvokeMethodRe_iacc = "testInvokeMethodRethrowing_illegalAccess";
	private final String testInvokeMethodRe_iarg = "testInvokeMethodRethrowing_illegalArgument";
	private final String testInvokeMethodRe_npe = "testInvokeMethodRethrowing_nullPointer";
	private final String testGetConstructor = "testGetConstructor";
	private final String testGetConstructor_nsf = "testGetConstructor_noSuchMethod";

	@TestTest
	void test_testNewInstance() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testNewInstance), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testNewInstance_classNotFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_cnf, AssertionFailedError.class,
				"The class 'DoesNotExist' was not found within the submission. Make sure to implement it properly."));
	}

	@TestTest
	void test_testNewInstance_exceptionInInitializer() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_eii, AssertionFailedError.class,
				"The class 'SomeFailingClass' could not be initialized because an exception was thrown in a static initializer block. Make sure to implement the static initialization without errors."));
	}

	@TestTest
	void test_testNewInstance_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_nsm, AssertionFailedError.class,
				"Could not instantiate the class SomeClass because the class does not have a constructor with the arguments: [ Boolean, Boolean ]. Make sure to implement this constructor properly."));
	}

	@TestTest
	void test_testNewInstance_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_iacc, AssertionFailedError.class,
				"Could not instantiate the class SomeClass because access to its constructor with the parameters: [ String ] was denied. Make sure to check the modifiers of the constructor."));
	}

	@TestTest
	void test_testNewInstance_illegalArguments() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_iarg, AssertionFailedError.class,
				"Could not instantiate the class SomeClass because the actual constructor or none of the actual constructors of this class match the expected one. We expect, amongst others, one with [ Integer ] parameters, which does not exist. Make sure to implement this constructor correctly."));
	}

	@TestTest
	void test_testNewInstance_instantiation() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_inst, AssertionFailedError.class,
				"Could not instantiate the class SomeAbstractClass because the class is abstract and should not have a constructor. Make sure to remove the constructor of the class."));
	}

	@TestTest
	void test_testNewInstance_invocationTarget() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testNewInstance_inv, AssertionFailedError.class,
				"Could not instantiate the class SomeClass because the constructor with 1 parameters threw an exception and could not be initialized. Make sure to check the constructor implementation."));
	}

	@TestTest
	void test_testValueForAttribute() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testValueForAttribute), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testValueForAttribute_noSuchField() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testValueForAttribute_nsf, AssertionFailedError.class,
				"Could not retrieve the attribute 'noSuchField' from the class SomeClass because the attribute does not exist. Make sure to implement the attribute correctly."));
	}

	@TestTest
	void test_testValueForAttribute_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testValueForAttribute_iacc, AssertionFailedError.class,
				"Could not retrieve the attribute 'someAttribute' from the class SomeClass because access to the attribute was denied. Make sure to check the modifiers of the attribute."));
	}

	@TestTest
	void test_testGetMethod() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testGetMethod), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testGetMethod_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_nsm, AssertionFailedError.class,
				"Could not find the method 'someMethod' with the parameters: [ String ] in the class SomeClass because the method does not exist. Make sure to implement this method properly."));
	}

	@TestTest
	void test_testGetMethod_noSuchMethod_noParameters() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_nsm_np, AssertionFailedError.class,
				"Could not find the method 'someMethod' from the class SomeClass because the method does not exist. Make sure to implement this method properly."));
	}

	@TestTest
	void test_testGetMethod_noMethodName() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetMethod_nmn, AssertionFailedError.class,
				"Could not find the method 'null' from the class SomeClass because the name of the method is null. Make sure to check the name of the method."));
	}

	@TestTest
	void test_invokeMethod() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testInvokeMethod), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testInvokeMethod_invocationTarget() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethod_inv, AssertionFailedError.class,
				"Could not invoke the method 'throwException' in the class SomeClass because of an exception within the method: java.lang.RuntimeException"));
	}

	@TestTest
	void test_testInvokeMethodRethrowing_illegalAccess() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethodRe_iacc, AssertionFailedError.class,
				"Could not invoke the method 'superSecretMethod' in the class SomeClass because access to the method was denied. Make sure to check the modifiers of the method."));
	}

	@TestTest
	void test_testInvokeMethodRethrowing_illegalArgument() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethodRe_iarg, AssertionFailedError.class,
				"Could not invoke the method 'getAnotherAttribute' in the class SomeClass because the parameters are not implemented right. Make sure to check the parameters of the method."));
	}

	@TestTest
	void test_testInvokeMethodRethrowing_nullPointer() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testInvokeMethodRe_npe, AssertionFailedError.class,
				"Could not invoke the method 'getAnotherAttribute' in the class SomeClass because the object was null and the method is an instance method. Make sure to check the static modifier of the method."));
	}

	@TestTest
	void test_testGetConstructor() {
		tests.assertThatEvents().haveExactly(1,
				event(testWithSegments(testGetConstructor), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_testGetConstructor_noSuchMethod() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(testGetConstructor_nsf, AssertionFailedError.class,
				"Could not find the constructor with the parameters: [ String, Boolean ] in the class SomeClass because the constructor does not exist. Make sure to implement this constructor properly."));
	}

}
