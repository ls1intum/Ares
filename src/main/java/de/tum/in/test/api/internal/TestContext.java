package de.tum.in.test.api.internal;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Adapter for different JUnit 5 test runner contexts. This interface provides
 * the common properties of a test context.
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
public abstract class TestContext {

	public abstract Optional<Method> testMethod();

	public abstract Optional<Class<?>> testClass();

	public abstract Optional<Object> testInstance();

	public abstract String displayName();

	public abstract Optional<AnnotatedElement> annotatedElement();

	public abstract Optional<TestType> findTestType();

	@Override
	public String toString() {
		return String.format("TestContext [testMethod()=%s, displayName()=%s, findTestType()=%s]", testMethod(), //$NON-NLS-1$
				displayName(), findTestType());
	}
}
