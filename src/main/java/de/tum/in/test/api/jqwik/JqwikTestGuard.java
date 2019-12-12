package de.tum.in.test.api.jqwik;

import static de.tum.in.test.api.util.TestGuardUtils.checkForHidden;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.util.JqwikContext;
import de.tum.in.test.api.util.ReportingUtils;
import net.jqwik.api.lifecycle.AroundPropertyHook;
import net.jqwik.api.lifecycle.PropertyExecutionResult;
import net.jqwik.api.lifecycle.PropertyExecutionResult.Status;
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
		return postProcess(property.execute());
	}

	private static PropertyExecutionResult postProcess(PropertyExecutionResult per) {
		var t = per.getThrowable();
		if (t.isEmpty())
			return per;
		Throwable newT = ReportingUtils.processThrowable(t.get());
		if (newT instanceof Error && !(newT instanceof AssertionError))
			throw (Error) newT;
		if (per.getStatus() == Status.ABORTED)
			return PropertyExecutionResult.aborted(newT, per.getSeed().orElse(null));
		if (per.getStatus() == Status.FAILED)
			return PropertyExecutionResult.failed(newT, per.getSeed().orElse(null),
					per.getFalsifiedSample().orElse(null));
		return per;
	}
}
