package de.tum.in.test.api.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.tum.in.test.integration.testuser.subject.structural.*;
import de.tum.in.test.integration.testuser.subject.structural.subpackage.SubpackageClass;

class ClassMemberAccessorTest {

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getInheritedInterfaceDefaultMethod(boolean findNonPublic) throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "doSomethingElse", findNonPublic,
				new Class[] { int.class });
		assertThat(method.getDeclaringClass()).isEqualTo(SomeInterface.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getInterfaceAttribute(boolean findNonPublic) throws NoSuchFieldException {
		Field field = ClassMemberAccessor.getField(AbstractClassExtension.class, "ANOTHER_CONSTANT", findNonPublic);
		assertThat(field.getDeclaringClass()).isEqualTo(SomeInterface.class);
	}

	@Test
	void getMethodMatchingParameters() throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "declaredMethod", true,
				new Class[] { int.class });
		assertThat(Modifier.isPrivate(method.getModifiers())).isTrue();
		assertThat(method.getParameterTypes()).containsExactly(int.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getNonAccessiblePrivateSuperclassField(boolean findNonPublic) {
		assertThrows(NoSuchFieldException.class, () -> ClassMemberAccessor.getField(AbstractClassExtension.class,
				"somePrivateAttribute", findNonPublic));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getNonAccessiblePrivateSuperclassMethod(boolean findNonPublic) {
		assertThrows(NoSuchMethodException.class, () -> ClassMemberAccessor.getMethod(AbstractClassExtension.class,
				"nonAbstractPrivate", findNonPublic, new Class[] {}));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getPackagePrivateMethodNoAccessInSubpackage(boolean findNonPublic) {
		assertThrows(NoSuchMethodException.class, () -> ClassMemberAccessor.getMethod(SubpackageClass.class,
				"nonAbstractPackagePrivate", findNonPublic, new Class[] {}));
	}

	@Test
	void getProtectedInheritedAttribute() throws NoSuchFieldException {
		Field field = ClassMemberAccessor.getField(AbstractClassExtension.class, "someProtectedAttribute", true);
		assertThat(field.getDeclaringClass()).isEqualTo(SomeAbstractClass.class);
	}

	@Test
	void getProtectedInheritedAttributeNoForcedAccess() {
		assertThrows(NoSuchFieldException.class,
				() -> ClassMemberAccessor.getField(AbstractClassExtension.class, "someProtectedAttribute", false));
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

	@Test
	void getProtectedMethodAccessInSubpackage() throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(SubpackageClass.class, "nonAbstractProtected", true,
				new Class[] {});
		assertThat(method).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getPublicInheritedAttribute(boolean findNonPublic) throws NoSuchFieldException {
		Field field = ClassMemberAccessor.getField(AbstractClassExtension.class, "someInt", findNonPublic);
		assertThat(field.getDeclaringClass()).isEqualTo(SomeAbstractClass.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getPublicMethod(boolean findNonPublic) throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "declaredMethod", findNonPublic,
				new Class[] {});
		assertThat(method).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void getStaticMethodFromInterface(boolean findNonPublic) {
		assertThrows(NoSuchMethodException.class,
				() -> ClassMemberAccessor.getMethod(SomeClass.class, "getOne", findNonPublic, new Class[] {}));
	}
}
