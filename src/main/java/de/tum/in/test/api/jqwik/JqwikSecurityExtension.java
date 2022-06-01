package de.tum.in.test.api.jqwik;

import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import net.jqwik.api.lifecycle.*;

import de.tum.in.test.api.internal.ConfigurationUtils;
import de.tum.in.test.api.security.ArtemisSecurityManager;

/**
 * <p>
 * <i>Adaption for jqwik.</i>
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
public final class JqwikSecurityExtension implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 30;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		var testContext = JqwikContext.of(context);
		var configuration = ConfigurationUtils.generateConfiguration(testContext);
		var accessToken = ArtemisSecurityManager.install(configuration);
		PropertyExecutionResult result;
		Throwable error = null;
		try {
			/*
			 * Note that the only Throwable not caught and collected is OutOfMemoryError
			 */
			result = property.execute();
		} finally {
			try {
				ArtemisSecurityManager.uninstall(accessToken);
			} catch (Exception e) {
				error = e;
			}
		}
		// Fix for issue #1, add as suppressed exception
		if (error != null) {
			Optional<Throwable> propExecError = result.throwable();
			if (propExecError.isPresent())
				propExecError.get().addSuppressed(error);
			else
				result = result.mapToFailed(error);
		}
		return result;
	}
}
