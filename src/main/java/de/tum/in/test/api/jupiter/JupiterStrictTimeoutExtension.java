package de.tum.in.test.api.jupiter;

import static de.tum.in.test.api.internal.TimeoutUtils.performTimeoutExecution;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
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
public class JupiterStrictTimeoutExtension implements UnifiedInvocationInterceptor {

	@Override
	public <T> T interceptGenericInvocation(Invocation<T> invocation, ExtensionContext extensionContext,
			Optional<ReflectiveInvocationContext<?>> invocationContext) throws Throwable {
		return performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}
}
