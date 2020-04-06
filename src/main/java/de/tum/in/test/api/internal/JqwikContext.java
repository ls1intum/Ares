package de.tum.in.test.api.internal;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.platform.commons.support.AnnotationSupport;

import de.tum.in.test.api.TestType;
import de.tum.in.test.api.jqwik.JqwikArtemisTest;
import net.jqwik.api.lifecycle.PropertyLifecycleContext;

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
	public String displayName() {
		return lifecycleContext.label();
	}

	public PropertyLifecycleContext getPropertyLifecycleContext() {
		return lifecycleContext;
	}

	public static JqwikContext of(PropertyLifecycleContext lifecycleContext) {
		return new JqwikContext(lifecycleContext);
	}

	@Override
	public Optional<TestType> findTestType() {
		return AnnotationSupport.findAnnotation(annotatedElement(), JqwikArtemisTest.class)
				.map(JqwikArtemisTest::value);
	}
}