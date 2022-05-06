package de.tum.in.test.api.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.tum.in.test.integration.testuser.subject.structural.AbstractClassExtension;
import de.tum.in.test.integration.testuser.subject.structural.SomeAbstractClass;
import de.tum.in.test.integration.testuser.subject.structural.SomeInterface;

class ClassMemberAccessorTest {

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getPublicMethod(boolean findPrivate) throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "declaredMethod", findPrivate,
				new Class[] {});
		assertThat(method).isNotNull();
	}

	@Test
	void getMethodMatchingParameters() throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "declaredMethod", true,
				new Class[] { int.class });
		assertThat(Modifier.isPrivate(method.getModifiers())).isTrue();
		assertThat(method.getParameterTypes()).containsExactly(int.class);
	}

	@Test
	void getProtectedInheritedMethod() throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "nonAbstractProtected", true,
				new Class[] {});
		assertThat(method).isNotNull();
	}

	@Test
	void getProtectedInheritedMethodNoForcedAccess() {
		assertThrows(NoSuchMethodException.class, () -> ClassMemberAccessor.getMethod(AbstractClassExtension.class,
				"nonAbstractProtected", false, new Class[] {}));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getInheritedInterfaceDefaultMethod(boolean findPrivate) throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "doSomethingElse", findPrivate,
				new Class[] { int.class });
		assertThat(method.getDeclaringClass()).isEqualTo(SomeInterface.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getNonAccessiblePrivateSuperclassMethod(boolean findPrivate) {
		assertThrows(NoSuchMethodException.class, () -> ClassMemberAccessor.getMethod(AbstractClassExtension.class,
				"nonAbstractPrivate", findPrivate, new Class[] {}));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getPublicInheritedAttribute(boolean findPrivate) throws NoSuchFieldException {
		Field field = ClassMemberAccessor.getAttribute(AbstractClassExtension.class, "someInt", findPrivate);
		assertThat(field.getDeclaringClass()).isEqualTo(SomeAbstractClass.class);
	}

	@Test
	void getProtectedInheritedAttribute() throws NoSuchFieldException {
		Field field = ClassMemberAccessor.getAttribute(AbstractClassExtension.class, "someProtectedAttribute", true);
		assertThat(field.getDeclaringClass()).isEqualTo(SomeAbstractClass.class);
	}

	@Test
	void getProtectedInheritedAttributeNoForcedAccess() {
		assertThrows(NoSuchFieldException.class,
				() -> ClassMemberAccessor.getAttribute(AbstractClassExtension.class, "someProtectedAttribute", false));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getInterfaceAttribute(boolean findPrivate) throws NoSuchFieldException {
		Field field = ClassMemberAccessor.getAttribute(AbstractClassExtension.class, "ANOTHER_CONSTANT", findPrivate);
		assertThat(field.getDeclaringClass()).isEqualTo(SomeInterface.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getNonAccessiblePrivateSuperclassField(boolean findPrivate) {
		assertThrows(NoSuchFieldException.class, () -> ClassMemberAccessor.getAttribute(AbstractClassExtension.class,
				"somePrivateAttribute", findPrivate));
	}
}
