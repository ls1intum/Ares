package de.tum.in.test.api.jqwik;

import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.IOTesterManager;
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
public final class JqwikIOExtension implements AroundPropertyHook {

	@Override
	public int aroundPropertyProximity() {
		return 20;
	}

	@Override
	public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property)
			throws Throwable {
		IOTesterManager ioTesterManager = new IOTesterManager(JqwikContext.of(context));
		ioTesterManager.beforeTestExecution();
		IOTesterProvider ioTesterProvider = new IOTesterProvider(ioTesterManager.getIOTester());
		RegisteredArbitraryProviders.register(ioTesterProvider);
		try {
			return property.execute();
		} finally {
			RegisteredArbitraryProviders.unregister(ioTesterProvider);
			ioTesterManager.afterTestExecution();
		}
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
			return Set.of(Arbitraries.just(ioTester));
		}
	}
}
