package de.tum.in.test.api.jupiter;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.*;

import de.tum.in.test.api.internal.IOExtensionUtils;

@API(status = Status.INTERNAL)
public class JupiterIOExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	private IOExtensionUtils ioTesterManager;

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return ioTesterManager.canProvideControllerFor(parameterContext.getParameter().getType());
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return ioTesterManager.getControllerInstance();
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		ioTesterManager = new IOExtensionUtils(JupiterContext.of(context));
		ioTesterManager.beforeTestExecution();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		// If this is null, there was an exception in before, so ignore it here
		if (ioTesterManager != null)
			ioTesterManager.afterTestExecution();
	}
}
