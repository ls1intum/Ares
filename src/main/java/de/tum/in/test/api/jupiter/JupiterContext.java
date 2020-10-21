package de.tum.in.test.api.jupiter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtensionContext;

import de.tum.in.test.api.internal.TestContext;
import de.tum.in.test.api.internal.TestContextUtils;
import de.tum.in.test.api.internal.TestType;

@API(status = Status.INTERNAL)
public class JupiterContext extends TestContext {
	private final ExtensionContext extensionContext;

	JupiterContext(ExtensionContext extensionContext) {
		this.extensionContext = extensionContext;
	}

	@Override
	public Optional<Method> testMethod() {
		return extensionContext.getTestMethod();
	}

	@Override
	public Optional<Class<?>> testClass() {
		return extensionContext.getTestClass();
	}

	@Override
	public Optional<Object> testInstance() {
		return extensionContext.getTestInstance();
	}

	@Override
	public Optional<AnnotatedElement> annotatedElement() {
		return extensionContext.getElement();
	}

	@Override
	public String displayName() {
		return extensionContext.getDisplayName();
	}

	public ExtensionContext getExtensionContext() {
		return extensionContext;
	}

	public static JupiterContext of(ExtensionContext extensionContext) {
		return new JupiterContext(extensionContext);
	}

	@Override
	public Optional<TestType> findTestType() {
		return TestContextUtils.findAnnotationIn(this, JupiterArtemisTest.class).map(JupiterArtemisTest::value);
	}
}