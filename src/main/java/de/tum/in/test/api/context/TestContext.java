package de.tum.in.test.api.context;

import java.lang.reflect.*;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Adapter for different JUnit 5 test runner contexts. This interface provides
 * the common properties of a test context.
 * <p>
 * All properties are optional as different test types and phases in the test
 * life cycle of different test engines have different execution environments.
 * While you can expect most properties to be present most of the time, use them
 * defensively.
 *
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
public abstract class TestContext {

	public abstract Optional<Method> testMethod();

	public abstract Optional<Class<?>> testClass();

	public abstract Optional<Object> testInstance();

	public abstract Optional<String> displayName();

	public abstract Optional<AnnotatedElement> annotatedElement();

	public abstract Optional<TestType> findTestType();

	@Override
	public String toString() {
		return String.format("TestContext [testMethod()=%s, displayName()=%s, findTestType()=%s]", testMethod(), //$NON-NLS-1$
				displayName(), findTestType());
	}
}
