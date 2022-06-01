package de.tum.in.test.api.jqwik;

import java.lang.reflect.*;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import net.jqwik.api.lifecycle.PropertyLifecycleContext;

import de.tum.in.test.api.context.*;

@API(status = Status.INTERNAL)
public class JqwikContext extends TestContext {
	private final PropertyLifecycleContext lifecycleContext;

	JqwikContext(PropertyLifecycleContext lifecycleContext) {
		this.lifecycleContext = lifecycleContext;
	}

	@Override
	public Optional<Method> testMethod() {
		return Optional.ofNullable(lifecycleContext.targetMethod());
	}

	@Override
	public Optional<Class<?>> testClass() {
		return Optional.ofNullable(lifecycleContext.containerClass());
	}

	@Override
	public Optional<Object> testInstance() {
		return Optional.ofNullable(lifecycleContext.testInstance());
	}

	@Override
	public Optional<AnnotatedElement> annotatedElement() {
		return lifecycleContext.optionalElement();
	}

	@Override
	public Optional<String> displayName() {
		return Optional.of(lifecycleContext.label());
	}

	public PropertyLifecycleContext getPropertyLifecycleContext() {
		return lifecycleContext;
	}

	public static JqwikContext of(PropertyLifecycleContext lifecycleContext) {
		return new JqwikContext(lifecycleContext);
	}

	@Override
	public Optional<TestType> findTestType() {
		return TestContextUtils.findAnnotationIn(this, JqwikAresTest.class).map(JqwikAresTest::value);
	}
}
