package de.tum.in.test.api.jqwik;

import java.util.Set;
import java.util.function.Predicate;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;
import net.jqwik.api.providers.*;
import net.jqwik.engine.providers.RegisteredArbitraryProviders;

import de.tum.in.test.api.internal.IOExtensionUtils;

/**
 * <p>
 * <i>Adaption for jqwik.</i>
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
public final class JqwikIOExtension implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 20;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		IOExtensionUtils ioExtensionUtils = new IOExtensionUtils(JqwikContext.of(context));
		ioExtensionUtils.beforeTestExecution();
		// register controller if possible
		ControllerProvider controllerProvider = null;
		if (ioExtensionUtils.providesController()) {
			controllerProvider = new ControllerProvider(ioExtensionUtils::canProvideControllerFor,
					ioExtensionUtils.getControllerInstance());
			RegisteredArbitraryProviders.register(controllerProvider);
		}
		try {
			return property.execute();
		} finally {
			// unregister controller if necessary
			if (ioExtensionUtils.providesController())
				RegisteredArbitraryProviders.unregister(controllerProvider);
			ioExtensionUtils.afterTestExecution();
		}
	}

	private static class ControllerProvider implements ArbitraryProvider {

		private final Predicate<Class<?>> canProvideControllerFor;
		private final Object controllerInstance;

		private ControllerProvider(Predicate<Class<?>> canProvideControllerFor, Object controllerInstance) {
			this.canProvideControllerFor = canProvideControllerFor;
			this.controllerInstance = controllerInstance;
		}

		@Override
		public boolean canProvideFor(TypeUsage targetType) {
			return canProvideControllerFor.test(targetType.getRawType());
		}

		@Override
		public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
			return Set.of(Arbitraries.just(controllerInstance));
		}
	}
}
