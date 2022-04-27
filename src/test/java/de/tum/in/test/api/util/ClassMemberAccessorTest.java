package de.tum.in.test.api.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import de.tum.in.test.integration.testuser.subject.structural.AbstractClassExtension;

class ClassMemberAccessorTest {
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
	void getMethodMatchingParameters() throws NoSuchMethodException {
		Method method = ClassMemberAccessor.getMethod(AbstractClassExtension.class, "declaredMethod", true,
				new Class[] { int.class });
		assertThat(Modifier.isPrivate(method.getModifiers())).isTrue();
		assertThat(method.getParameterTypes()).containsExactly(int.class);
	}
}
