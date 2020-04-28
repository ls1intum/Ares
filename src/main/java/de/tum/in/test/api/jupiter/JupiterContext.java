package de.tum.in.test.api.jupiter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import de.tum.in.test.api.internal.TestContext;
import de.tum.in.test.api.internal.TestType;

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
		return AnnotationSupport.findAnnotation(annotatedElement(), JupiterArtemisTest.class)
				.map(JupiterArtemisTest::value);
	}
}