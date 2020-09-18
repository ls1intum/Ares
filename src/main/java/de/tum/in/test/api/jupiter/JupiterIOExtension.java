package de.tum.in.test.api.jupiter;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.tum.in.test.api.internal.IOTesterManager;
import de.tum.in.test.api.io.IOTester;

@API(status = Status.INTERNAL)
public class JupiterIOExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	private IOTesterManager ioTesterManager;

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getParameter().getType().equals(IOTester.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return ioTesterManager.getIOTester();
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		ioTesterManager = new IOTesterManager(JupiterContext.of(context));
		ioTesterManager.beforeTestExecution();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		ioTesterManager.afterTestExecution();
	}
}
