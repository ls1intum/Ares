package de.tum.in.test.api.util;

import static org.junit.platform.commons.util.AnnotationUtils.*;

import java.lang.StackWalker.StackFrame;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.locked.ArtemisSecurityConfiguration;
import de.tum.in.test.api.locked.ArtemisSecurityConfigurationBuilder;
import de.tum.in.test.api.locked.ArtemisSecurityManager;

public final class GeneralTestExtension {

	private final TestContext context;
	private String token;
	private IOTester ioTester;

	public GeneralTestExtension(TestContext context) {
		this.context = context;
	}

	public void beforeTestExecution() throws Exception {
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

	public void afterTestExecution() throws Exception {
		try {
			ArtemisSecurityManager.uninstall(token);
		} finally {
			IOTester.uninstallCurrent();
			ioTester = null;
		}
	}

	public IOTester getIOTester() {
		return ioTester;
	}

	private static ArtemisSecurityConfiguration generateConfiguration(TestContext context) {
		var config = ArtemisSecurityConfigurationBuilder.create();
		config.configureFromContext(context);
		config.withCurrentPath();
		config.addWhitelistedClassNames(generateClassWhiteList(context));
		config.withPathWhitelist(generatePathWhiteList(context));
		return config.build();
	}

	private static Optional<Set<Path>> generatePathWhiteList(TestContext context) {
		var methodLevel = findRepeatableAnnotations(context.testMethod(), WhitelistPath.class);
		var classLevel = findRepeatableAnnotations(context.testClass(), WhitelistPath.class);
		return Optional.of(Stream.concat(methodLevel.stream(), classLevel.stream()).map(WhitelistPath::value)
				.map(Path::of).collect(Collectors.toSet()));
	}

	private static Set<String> generateClassWhiteList(TestContext context) {
		Set<String> entries = StackWalker.getInstance()
				.walk(s -> s.map(StackFrame::getClassName).distinct().collect(Collectors.toCollection(HashSet::new)));
		context.testClass().map(Class::getName).ifPresent(entries::add);
		return entries;
	}

	private static boolean shouldMirrorOutput(TestContext context) {
		return findAnnotation(context.testMethod(), MirrorOutput.class)
				.or(() -> findAnnotation(context.testClass(), MirrorOutput.class)).map(MirrorOutput::value)
				.map(MirrorOutputPolicy::isEnabled).orElse(false);
	}
}
