package de.tum.in.test.integration.testuser;

import static de.tum.in.test.api.util.ReflectionTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.*;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.structural.*;
import de.tum.in.test.integration.testuser.subject.structural.subpackage.SubpackageClass;

@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
public class ReflectionTestUtilsUser {

	private static final String SUBJECT_PACKAGE = "de.tum.in.test.integration.testuser.subject.structural";
	private static final String CLASS_NAME = SUBJECT_PACKAGE + ".SomeClass";
	private static final String NESTED_CLASS_NAME = CLASS_NAME + "$Nested";
	private static final String ABSTRACT_CLASS_NAME = SUBJECT_PACKAGE + ".SomeAbstractClass";
	private static final String FAILING_CLASS_NAME = SUBJECT_PACKAGE + ".SomeFailingClass";

	private final SomeClass classInstance = new SomeClass(1);

	@Test
	void testGetConstructor_noSuchMethod() {
		getConstructor(classInstance.getClass(), String.class, Boolean.class);
	}

	@Test
	void testGetNonPublicConstructor_noSuchMethod() {
		getNonPublicConstructor(classInstance.getClass(), String.class, Boolean.class);
	}

	@Test
	void testGetConstructor_success() {
		var constructor = getConstructor(classInstance.getClass());
		assertThat(constructor).isInstanceOf(Constructor.class);
	}

	@Test
	void testGetMethod_noMethodName() {
		getMethod(classInstance, null);
	}

	@Test
	void testGetMethod_noSuchMethod_noParameters() {
		getMethod(classInstance, "someMethod");
	}

	@Test
	void testGetMethod_noSuchMethod_withParameters() {
		getMethod(classInstance, "someMethod", String.class);
	}

	@Test
	void testGetMethod_success() {
		var method = getMethod(classInstance, "getAnotherAttribute");
		assertThat(method).isInstanceOf(Method.class);
	}

	@Test
	void testGetNonPublicMethod_success() {
		var method = getNonPublicMethod(classInstance, "superSecretMethod");
		assertThat(method).isInstanceOf(Method.class);
	}

	@Test
	void testInvokeMethod_classNotVisible() {
		var innerInstance = newInstanceFromNonPublicConstructor(NESTED_CLASS_NAME);
		invokeMethod(innerInstance, "publicMethod");
	}

	@Test
	void testInvokeMethod_invocationTarget() {
		invokeMethod(classInstance, "throwException");
	}

	@Test
	void testInvokeMethod_success() {
		var returnValue = invokeMethod(classInstance, "getAnotherAttribute");
		assertThat(returnValue).isEqualTo(classInstance.getAnotherAttribute());
	}

	@Test
	void testInvokeMethodRethrowing_illegalAccess() throws NoSuchMethodException {
		var privateMethod = classInstance.getClass().getDeclaredMethod("superSecretMethod");
		invokeMethod(classInstance, privateMethod);
	}

	@Test
	void testInvokeMethodRethrowing_illegalArgument() throws NoSuchMethodException {
		var method = classInstance.getClass().getMethod("getAnotherAttribute");
		invokeMethod(classInstance, method, "Illegal");
	}

	@Test
	void testInvokeMethodRethrowing_nullPointer() throws NoSuchMethodException {
		var method = classInstance.getClass().getMethod("getAnotherAttribute");
		invokeMethod(null, method);
	}

	@Test
	void testInvokePrivateMethodByName_success() {
		invokeNonPublicMethod(classInstance, "superSecretMethod");
	}

	@Test
	void testInvokePrivateMethodRethrowing_success() throws NoSuchMethodException {
		var privateMethod = classInstance.getClass().getDeclaredMethod("superSecretMethod");
		invokeNonPublicMethod(classInstance, privateMethod);
	}

	@Test
	void testNewInstance_classNotFound() {
		newInstance("DoesNotExist");
	}

	@Test
	void testNewInstance_classNotVisible() {
		newInstance(NESTED_CLASS_NAME);
	}

	@Test
	void testNewInstance_exceptionInInitializer() {
		newInstance(FAILING_CLASS_NAME);
	}

	@Test
	void testNewInstance_illegalAccess() {
		newInstance(CLASS_NAME, "");
	}

	@Test
	void testNewInstance_illegalArguments() {
		var constructor = getConstructor(getClazz(CLASS_NAME), Integer.class);
		newInstance(constructor, "");
	}

	@Test
	void testNewInstance_instantiation() {
		newInstance(ABSTRACT_CLASS_NAME);
	}

	@Test
	void testNewInstance_invocationTarget() {
		newInstance(CLASS_NAME, true);
	}

	@Test
	void testNewInstance_noSuchMethod() {
		newInstance(CLASS_NAME, true, true);
	}

	@Test
	void testNewInstance_success() {
		var instance = newInstance(CLASS_NAME, 1);
		assertThat(instance).isInstanceOf(SomeClass.class);
	}

	@Test
	void testNewInstancePrivateConstructor_success() {
		newInstanceFromNonPublicConstructor(CLASS_NAME, "");
	}

	@Test
	void testSetValueOfAttribute_final() {
		setValueOfAttribute(classInstance, "SOME_CONSTANT", 1);
	}

	@Test
	void testSetValueOfAttribute_illegalAccess() {
		setValueOfAttribute(classInstance, "someAttribute", "x");
	}

	@Test
	void testSetValueOfAttribute_success() {
		SomeAbstractClass.someInt = 2;
		setValueOfAttribute(new SubpackageClass(), "someInt", 1);
		assertThat(SomeAbstractClass.someInt).isEqualTo(1);
	}

	@Test
	void testSetValueOfNonPublicAttribute_final() {
		setValueOfNonPublicAttribute(classInstance, "someFinalAttribute", 1);
		assertThat(classInstance.doSomethingElse(0)).isEqualTo(1);
	}

	@Test
	void testSetValueOfNonPublicAttribute_success() {
		setValueOfNonPublicAttribute(classInstance, "someAttribute", "x");
		assertThat(classInstance.getSomeAttribute()).isEqualTo("x");
	}

	@Test
	void testValueForAttribute_classNotVisible() {
		var innerInstance = newInstanceFromNonPublicConstructor(NESTED_CLASS_NAME);
		valueForAttribute(innerInstance, "innerValue");
	}

	@Test
	void testValueForAttribute_illegalAccess() {
		valueForAttribute(classInstance, "someAttribute");
	}

	@Test
	void testValueForAttribute_noSuchField() {
		valueForAttribute(classInstance, "noSuchField");
	}

	@Test
	void testValueForAttribute_success() {
		var value = valueForAttribute(classInstance, "SOME_CONSTANT");
		assertThat(value).isEqualTo(SomeClass.SOME_CONSTANT);
	}

	@Test
	void testValueForPrivateAttribute_success() {
		valueForNonPublicAttribute(classInstance, "someAttribute");
	}

	@Test
	void testNewInstanceRethrowing_invocationTarget() throws Throwable {
		var constructor = getConstructor(SomeClass.class, Boolean.class);
		newInstanceRethrowing(constructor, true);
	}

	@Test
	void testNewInstanceFromNonPublicConstructorRethrowing_success() throws Throwable {
		var constructor = getNonPublicConstructor(SomeClass.class, String.class);
		newInstanceFromNonPublicConstructorRethrowing(constructor, "x");
	}
}
