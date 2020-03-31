package de.tum.in.test.api.jqwik;

import static de.tum.in.test.api.internal.TestGuardUtils.checkForHidden;

import org.apiguardian.api.API;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.internal.JqwikContext;
import de.tum.in.test.api.internal.ReportingUtils;
import de.tum.in.test.api.jupiter.HiddenTest;
import net.jqwik.api.lifecycle.AroundPropertyHook;
import net.jqwik.api.lifecycle.PropertyExecutionResult;
import net.jqwik.api.lifecycle.PropertyExecutionResult.Status;
import net.jqwik.api.lifecycle.PropertyExecutor;
import net.jqwik.api.lifecycle.PropertyLifecycleContext;

/**
 * This class' main purpose is to guard the {@link HiddenTest}s execution and
 * evaluate the {@link Deadline}.
 * <p>
 * <i>Adaption for jqwik.</i>
 *
 * @author Christian Femers
 */
@API(status = API.Status.INTERNAL)
public final class JqwikTestGuard implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 10;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		checkForHidden(JqwikContext.of(context));
		return ReportingUtils.doProceedAndPostProcess(() -> postProcess(property.execute()));
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
