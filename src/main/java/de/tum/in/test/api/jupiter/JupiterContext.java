package de.tum.in.test.api.jupiter;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtensionContext;

import de.tum.in.test.api.context.*;

@API(status = Status.INTERNAL)
public class JupiterContext extends TestContext {
	private final ExtensionContext extensionContext;

	JupiterContext(ExtensionContext extensionContext) {
		this.extensionContext = extensionContext;
	}

	@Override
	public Optional<Method> testMethod() {
		return findOptionalsInHierarchy(ExtensionContext::getTestMethod).findFirst();
	}

	@Override
	public Optional<Class<?>> testClass() {
		return findOptionalsInHierarchy(ExtensionContext::getTestClass).findFirst();
	}

	@Override
	public Optional<Object> testInstance() {
		return findOptionalsInHierarchy(ExtensionContext::getTestInstance).findFirst();
	}

	@Override
	public Optional<AnnotatedElement> annotatedElement() {
		return findOptionalsInHierarchy(ExtensionContext::getElement).findFirst();
	}

	@Override
	public Optional<String> displayName() {
		return Optional.of(extensionContext.getDisplayName());
	}

	@Override
	public Optional<TestType> findTestType() {
		return TestContextUtils.findAnnotationIn(this, JupiterAresTest.class).map(JupiterAresTest::value);
	}

	public ExtensionContext getExtensionContext() {
		return extensionContext;
	}

	private <T> Stream<T> findOptionalsInHierarchy(Function<ExtensionContext, Optional<T>> getter) {
		return hierarchy().map(getter).filter(Optional::isPresent).map(Optional::get);
	}

	private Stream<ExtensionContext> hierarchy() {
		return Stream.iterate(extensionContext, Objects::nonNull, ec -> ec.getParent().orElse(null));
	}

	public static JupiterContext of(ExtensionContext extensionContext) {
		return new JupiterContext(extensionContext);
	}
}
