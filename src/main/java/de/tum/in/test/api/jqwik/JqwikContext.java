package de.tum.in.test.api.jqwik;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.TestContext;
import de.tum.in.test.api.internal.TestContextUtils;
import de.tum.in.test.api.internal.TestType;
import net.jqwik.api.lifecycle.PropertyLifecycleContext;

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
		return TestContextUtils.findAnnotationIn(this, JqwikArtemisTest.class).map(JqwikArtemisTest::value);
	}
}