package de.tum.in.test.api.jqwik;

import java.time.Duration;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.engine.TestDescriptor;

import net.jqwik.api.domains.DomainContext;
import net.jqwik.api.lifecycle.*;
import net.jqwik.engine.execution.lifecycle.*;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.internal.TimeoutUtils;

/**
 * This class manages the {@link StrictTimeout} annotation and how it is
 * processed, similar to
 * {@link Assertions#assertTimeoutPreemptively(Duration, org.junit.jupiter.api.function.ThrowingSupplier)}
 * <p>
 * <i>Adaption for jqwik.</i>
 * <p>
 * Use <code>@AddLifecycleHook(JqwikStrictTimeoutExtension.class)</code> only on
 * test methods or classes that are not marked {@link Public} or {@link Hidden}
 * to use {@link StrictTimeout}. <b>Doing otherwise will break the tests
 * completely because the extension will get registered and executed twice!</b>
 *
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
public class JqwikStrictTimeoutExtension implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 40;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		DomainContext domainContext = CurrentDomainContext.get();
		TestDescriptor desc = CurrentTestDescriptor.get();
		return TimeoutUtils.performTimeoutExecution(
				() -> CurrentDomainContext.runWithContext(domainContext,
						() -> CurrentTestDescriptor.runWithDescriptor(desc, property::execute)),
				JqwikContext.of(context));
	}
}
