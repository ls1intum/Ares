package de.tum.in.test.api.jqwik;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.util.JqwikContext;
import de.tum.in.test.api.util.TimeoutUtils;
import net.jqwik.api.domains.DomainContext;
import net.jqwik.api.lifecycle.AroundPropertyHook;
import net.jqwik.api.lifecycle.PropertyExecutionResult;
import net.jqwik.api.lifecycle.PropertyExecutor;
import net.jqwik.api.lifecycle.PropertyLifecycleContext;
import net.jqwik.engine.facades.DomainContextFacadeImpl;

/**
 * This class manages the {@link StrictTimeout} annotation and how it is
 * processed, using
 * {@link Assertions#assertTimeoutPreemptively(Duration, org.junit.jupiter.api.function.ThrowingSupplier)}
 * <p>
 * <i>Adaption for jqwick.</i>
 *
 * @author Christian Femers
 *
 */
public class JqwikStrictTimeoutExtension implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 30;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		DomainContext domainContext = DomainContextFacadeImpl.currentContext.get();
		return TimeoutUtils.performTimeoutExecution(() -> {
			DomainContextFacadeImpl.currentContext.set(domainContext);
			return property.execute();
		}, JqwikContext.of(context));
	}
}
