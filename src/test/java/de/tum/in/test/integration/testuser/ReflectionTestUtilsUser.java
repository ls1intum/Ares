package de.tum.in.test.integration.testuser;

import static de.tum.in.test.api.util.ReflectionTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.structural.SomeClass;

@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
public class ReflectionTestUtilsUser {

	private static final String SUBJECT_PACKAGE = "de.tum.in.test.integration.testuser.subject.structural";
	private static final String CLASS_NAME = SUBJECT_PACKAGE + ".SomeClass";
	private static final String ABSTRACT_CLASS_NAME = SUBJECT_PACKAGE + ".SomeAbstractClass";
	private static final String FAILING_CLASS_NAME = SUBJECT_PACKAGE + ".SomeFailingClass";

	private static final SomeClass CLASS_INSTANCE = new SomeClass(1);

	@Test
	void testGetConstructor_noSuchMethod() {
		getConstructor(CLASS_INSTANCE.getClass(), String.class, Boolean.class);
	}

	@Test
	void testGetConstructor_success() {
		var constructor = getConstructor(CLASS_INSTANCE.getClass());
		assertThat(constructor).isInstanceOf(Constructor.class);
	}

	@Test
	void testGetMethod_noMethodName() {
		getMethod(CLASS_INSTANCE, null);
	}

	@Test
	void testGetMethod_noSuchMethod_withParameters() {
		getMethod(CLASS_INSTANCE, "someMethod", String.class);
	}

	@Test
	void testGetMethod_noSuchMethod_noParameters() {
		getMethod(CLASS_INSTANCE, "someMethod");
	}

	@Test
	void testGetMethod_success() {
		var method = getMethod(CLASS_INSTANCE, "getAnotherAttribute");
		assertThat(method).isInstanceOf(Method.class);
	}

	@Test
	void testInvokeMethod_invocationTarget() {
		invokeMethod(CLASS_INSTANCE, "throwException");
	}

	@Test
	void testInvokeMethod_success() {
		var returnValue = invokeMethod(CLASS_INSTANCE, "getAnotherAttribute");
		assertThat(returnValue).isEqualTo(CLASS_INSTANCE.getAnotherAttribute());
	}

	@Test
	void testInvokePrivateMethodByName_success() {
		invokePrivateMethod(CLASS_INSTANCE, "superSecretMethod");
	}

	@Test
	void testInvokeMethodRethrowing_illegalAccess() throws NoSuchMethodException {
		var privateMethod = CLASS_INSTANCE.getClass().getDeclaredMethod("superSecretMethod");
		invokeMethod(CLASS_INSTANCE, privateMethod);
	}

	@Test
	void testInvokePrivateMethodRethrowing_success() throws NoSuchMethodException {
		var privateMethod = CLASS_INSTANCE.getClass().getDeclaredMethod("superSecretMethod");
		invokePrivateMethod(CLASS_INSTANCE, privateMethod);
	}

	@Test
	void testInvokeMethodRethrowing_illegalArgument() throws NoSuchMethodException {
		var method = CLASS_INSTANCE.getClass().getMethod("getAnotherAttribute");
		invokeMethod(CLASS_INSTANCE, method, "Illegal");
	}

	@Test
	void testInvokeMethodRethrowing_nullPointer() throws NoSuchMethodException {
		var method = CLASS_INSTANCE.getClass().getMethod("getAnotherAttribute");
		invokeMethod(null, method);
	}

	@Test
	void testNewInstance_classNotFound() {
		newInstance("DoesNotExist");
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
	void testNewInstancePrivateConstructor_success() {
		newInstanceFromPrivateConstructor(CLASS_NAME, "");
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
	void testValueForAttribute_illegalAccess() {
		valueForAttribute(CLASS_INSTANCE, "someAttribute");
	}

	@Test
	void testValueForPrivateAttribute_success() {
		valueForPrivateAttribute(CLASS_INSTANCE, "someAttribute");
	}

	@Test
	void testValueForAttribute_noSuchField() {
		valueForAttribute(CLASS_INSTANCE, "noSuchField");
	}

	@Test
	void testValueForAttribute_success() {
		var value = valueForAttribute(CLASS_INSTANCE, "SOME_CONSTANT");
		assertThat(value).isEqualTo(SomeClass.SOME_CONSTANT);
	}
}
