package de.tum.in.test.api.jupiter;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.util.GeneralTestExtension;
import de.tum.in.test.api.util.JupiterContext;

public final class JupiterTestExtension
		implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

	private GeneralTestExtension testExtension;

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getParameter().getType().equals(IOTester.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return testExtension.getIOTester();
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		testExtension = new GeneralTestExtension(JupiterContext.of(context));
		testExtension.beforeTestExecution();
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		testExtension.afterTestExecution();
	}
}
