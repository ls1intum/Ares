package de.tum.in.test.api.jupiter;

import static de.tum.in.test.api.internal.TimeoutUtils.performTimeoutExecution;

import java.time.Duration;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;

import de.tum.in.test.api.StrictTimeout;

/**
 * This class manages the {@link StrictTimeout} annotation and how it is
 * processed, similar to
 * {@link Assertions#assertTimeoutPreemptively(Duration, org.junit.jupiter.api.function.ThrowingSupplier)}
 * <p>
 * Use <code>@ExtendWith(JupiterStrictTimeoutExtension.class)</code> only on
 * test methods or classes that are not marked {@link Public} or {@link Hidden}
 * to use {@link StrictTimeout}. <b>Doing otherwise will break the tests
 * completely because the extension will get registered and executed twice!</b>
 *
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
public class JupiterStrictTimeoutExtension implements UnifiedInvocationInterceptor {

	@Override
	public <T> T interceptGenericInvocation(Invocation<T> invocation, ExtensionContext extensionContext,
			Optional<ReflectiveInvocationContext<?>> invocationContext) throws Throwable {
		return performTimeoutExecution(invocation::proceed, JupiterContext.of(extensionContext));
	}
}
