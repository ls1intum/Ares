package de.tum.in.test.api.jqwik;

import static de.tum.in.test.api.util.ReportingUtils.doProceedAndPostProcess;
import static de.tum.in.test.api.util.TestGuardUtils.checkForHidden;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.util.JqwikContext;
import net.jqwik.api.lifecycle.AroundPropertyHook;
import net.jqwik.api.lifecycle.PropertyExecutionResult;
import net.jqwik.api.lifecycle.PropertyExecutor;
import net.jqwik.api.lifecycle.PropertyLifecycleContext;

/**
 * This class' main purpose is to guard the {@link HiddenTest}s execution and
 * evaluate the {@link Deadline}.
 * <p>
 * <i>Adaption for jqwick.</i>
 *
 * @author Christian Femers
 */
public final class JqwikTestGuard implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 10;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		checkForHidden(JqwikContext.of(context));
		return doProceedAndPostProcess(property::execute);
	}

}
