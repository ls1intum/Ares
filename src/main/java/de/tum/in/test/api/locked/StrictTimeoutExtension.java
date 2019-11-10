package de.tum.in.test.api.locked;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.platform.commons.util.AnnotationUtils;

import de.tum.in.test.api.StrictTimeout;

/**
 * This class manages the {@link StrictTimeout} annotation and how it is
 * processed, using
 * {@link Assertions#assertTimeoutPreemptively(Duration, org.junit.jupiter.api.function.ThrowingSupplier)}
 * 
 * @author Christian Femers
 *
 */
public class StrictTimeoutExtension implements InvocationInterceptor {

	@Override
	public <T> T interceptTestClassConstructor(Invocation<T> invocation,
			ReflectiveInvocationContext<Constructor<T>> invocationContext, ExtensionContext extensionContext)
			throws Throwable {
		return applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptBeforeAllMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptBeforeEachMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	@Override
	public <T> T interceptTestFactoryMethod(Invocation<T> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		return applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptTestTemplateMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptAfterEachMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	@Override
	public void interceptAfterAllMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		applyTimeout(invocation, extensionContext);
	}

	private static <T> T applyTimeout(Invocation<T> invocation, ExtensionContext context) throws Throwable {
		var timeout = findTimeout(context);
		if (timeout.isEmpty())
			return invocation.proceed();
		return Assertions.assertTimeoutPreemptively(timeout.get(), invocation::proceed);
	}

	private static Optional<Duration> findTimeout(ExtensionContext context) {
		var methodLevel = AnnotationUtils.findAnnotation(context.getTestMethod(), StrictTimeout.class);
		var classLevel = AnnotationUtils.findAnnotation(context.getTestClass(), StrictTimeout.class);
		return methodLevel.or(() -> classLevel).map(st -> Duration.of(st.value(), st.unit().toChronoUnit()));
	}
}
