package de.tum.in.test.api.jupiter;

import static de.tum.in.test.api.internal.TimeoutUtils.performTimeoutExecution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import de.tum.in.test.api.StrictTimeout;

/**
 * This class manages the {@link StrictTimeout} annotation and how it is
 * processed, using
 * {@link Assertions#assertTimeoutPreemptively(Duration, org.junit.jupiter.api.function.ThrowingSupplier)}
 *
 * @author Christian Femers
 *
 */
public class JupiterStrictTimeoutExtension implements InvocationInterceptor {

	@Override
	public <T> T interceptTestClassConstructor(Invocation<T> invocation,
			ReflectiveInvocationContext<Constructor<T>> invocationContext, ExtensionContext extensionContext)
			throws Throwable {
		return performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptBeforeAllMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptBeforeEachMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public <T> T interceptTestFactoryMethod(Invocation<T> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		return performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptTestTemplateMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptAfterEachMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}

	@Override
	public void interceptAfterAllMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}
}
