package de.tum.in.test.api.jupiter;

import java.lang.reflect.*;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.extension.*;

/**
 * Combines all invocation interceptions into one method called
 * {@link #interceptGenericInvocation(org.junit.jupiter.api.extension.InvocationInterceptor.Invocation, ExtensionContext, Optional)
 * interceptGenericInvocation}.
 * <p>
 * For derivations from the generic interception for special cases, override the
 * corresponding default method.
 *
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
public interface UnifiedInvocationInterceptor extends InvocationInterceptor {

	@Override
	default <T> T interceptTestClassConstructor(Invocation<T> invocation,
			ReflectiveInvocationContext<Constructor<T>> invocationContext, ExtensionContext extensionContext)
			throws Throwable {
		return interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default void interceptBeforeAllMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default void interceptBeforeEachMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default <T> T interceptTestFactoryMethod(Invocation<T> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		return interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default void interceptTestTemplateMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default void interceptDynamicTest(Invocation<Void> invocation, DynamicTestInvocationContext invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.empty());
	}

	@Override
	default void interceptAfterEachMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	@Override
	default void interceptAfterAllMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		interceptGenericInvocation(invocation, extensionContext, Optional.of(invocationContext));
	}

	/**
	 * Called for all invocation types unless the default method of this interface
	 * is explicitly overridden.
	 *
	 * @param <T>               the result type of the invocation, often just
	 *                          {@link Void}
	 * @param invocation        the invocation that is being intercepted; never null
	 * @param extensionContext  the current extension context; never null
	 * @param invocationContext the context of the invocation that is being
	 *                          intercepted; never null; but may be empty for
	 *                          {@link DynamicTest}s
	 * @return the result of the invocation; potentially null
	 * @throws Throwable the invocation can potentially throw any throwable when it
	 *                   is executed
	 * @author Christian Femers
	 */
	<T> T interceptGenericInvocation(Invocation<T> invocation, ExtensionContext extensionContext,
			Optional<ReflectiveInvocationContext<?>> invocationContext) throws Throwable; // NOSONAR
}
