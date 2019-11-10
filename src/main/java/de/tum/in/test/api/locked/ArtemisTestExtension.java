package de.tum.in.test.api.locked;

import java.lang.StackWalker.StackFrame;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.AnnotationUtils;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.io.IOTester;

public final class ArtemisTestExtension
		implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

	private String token;
	private IOTester ioTester;

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getParameter().getType().equals(IOTester.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return ioTester;
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		boolean mirrorOutput = shouldMirrorOutput(context);
		ioTester = IOTester.installNew(mirrorOutput);
		try {
			token = ArtemisSecurityManager.install(generateConfiguration(context));
		} catch (Throwable t) {
			try {
				IOTester.uninstallCurrent();
			} catch (Exception e) {
				t.addSuppressed(e);
			}
			throw t;
		}
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		try {
			ArtemisSecurityManager.uninstall(token);
		} finally {
			IOTester.uninstallCurrent();
			ioTester = null;
		}
	}

	private static ArtemisSecurityConfiguration generateConfiguration(ExtensionContext context) {
		var config = ArtemisSecurityConfigurationBuilder.create();
		config.configureFromContext(context);
		config.withCurrentPath();
		config.addWhitelistedClassNames(generateWhiteList(context));
		return config.build();
	}

	private static Set<String> generateWhiteList(ExtensionContext context) {
		Set<String> entries = StackWalker.getInstance()
				.walk(s -> s.map(StackFrame::getClassName).distinct().collect(Collectors.toCollection(HashSet::new)));
		context.getTestClass().map(Class::getName).ifPresent(entries::add);
		return entries;
	}

	private static boolean shouldMirrorOutput(ExtensionContext context) {
		return AnnotationUtils.findAnnotation(context.getTestMethod(), MirrorOutput.class)
				.or(() -> AnnotationUtils.findAnnotation(context.getTestClass(), MirrorOutput.class))
				.map(MirrorOutput::value).map(MirrorOutputPolicy::isEnabled).orElse(false);
	}
}
