package de.tum.in.test.api.jqwik;

import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.GeneralTestExtension;
import de.tum.in.test.api.internal.JqwikContext;
import de.tum.in.test.api.io.IOTester;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.lifecycle.AroundPropertyHook;
import net.jqwik.api.lifecycle.PropertyExecutionResult;
import net.jqwik.api.lifecycle.PropertyExecutor;
import net.jqwik.api.lifecycle.PropertyLifecycleContext;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;
import net.jqwik.engine.providers.RegisteredArbitraryProviders;

/**
 * <p>
 * <i>Adaption for jqwik.</i>
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
public final class JqwikTestExtension implements AroundPropertyHook {

	private GeneralTestExtension testExtension;
	private IOTesterProvider ioTesterProvider;

	@Override
	public int aroundPropertyProximity() {
		return 20;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		testExtension = new GeneralTestExtension(JqwikContext.of(context));
		testExtension.beforeTestExecution();
		ioTesterProvider = new IOTesterProvider(testExtension.getIOTester());
		RegisteredArbitraryProviders.register(ioTesterProvider);

		PropertyExecutionResult result;
		Throwable error = null;
		try {
			/*
			 * Note that the only Throwable not caught and collected is OutOfMemoryError
			 */
			result = property.execute();
		} finally {
			RegisteredArbitraryProviders.unregister(ioTesterProvider);
			try {
				testExtension.afterTestExecution();
			} catch (Throwable t) {
				error = t;
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

	private static class IOTesterProvider implements ArbitraryProvider {
		private final IOTester ioTester;

		public IOTesterProvider(IOTester ioTester) {
			this.ioTester = ioTester;
		}

		@Override
		public boolean canProvideFor(TypeUsage targetType) {
			return targetType.getRawType().equals(IOTester.class);
		}

		@Override
		public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
			return Set.of(Arbitraries.constant(ioTester));
		}

	}
}
