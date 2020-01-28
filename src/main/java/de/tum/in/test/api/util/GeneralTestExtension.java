package de.tum.in.test.api.util;

import static org.junit.platform.commons.support.AnnotationSupport.*;

import java.lang.StackWalker.StackFrame;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.locked.ArtemisSecurityConfiguration;
import de.tum.in.test.api.locked.ArtemisSecurityConfigurationBuilder;
import de.tum.in.test.api.locked.ArtemisSecurityManager;

public final class GeneralTestExtension {

	private static final long DEFAULT_MAX_STD_OUT = 10_000_000L;
	private final TestContext context;
	private String token;
	private IOTester ioTester;

	public GeneralTestExtension(TestContext context) {
		this.context = context;
	}

	public void beforeTestExecution() throws Exception {
		boolean mirrorOutput = shouldMirrorOutput(context);
		long maxStdOut = getMaxStandardOutput(context);
		ioTester = IOTester.installNew(mirrorOutput, maxStdOut);
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
		config.withPathBlacklist(generatePathBlackList(context));
		config.withAllowedLocalPort(getAllowedLocalPort(context));
		config.withAllowedThreadCount(getAllowedThreadCount(context));
		return config.build();
	}

	private static Optional<Set<PathRule>> generatePathWhiteList(TestContext context) {
		var methodLevel = findRepeatableAnnotations(context.testMethod(), WhitelistPath.class);
		var classLevel = findRepeatableAnnotations(context.testClass(), WhitelistPath.class);
		return Optional.of(
				Stream.concat(methodLevel.stream(), classLevel.stream()).map(PathRule::of).collect(Collectors.toSet()));
	}

	private static Set<PathRule> generatePathBlackList(TestContext context) {
		var methodLevel = findRepeatableAnnotations(context.testMethod(), BlacklistPath.class);
		var classLevel = findRepeatableAnnotations(context.testClass(), BlacklistPath.class);
		return Stream.concat(methodLevel.stream(), classLevel.stream()).map(PathRule::of).collect(Collectors.toSet());
	}

	private static Set<String> generateClassWhiteList(TestContext context) {
		Set<String> entries = StackWalker.getInstance()
				.walk(s -> s.map(StackFrame::getClassName).distinct().collect(Collectors.toCollection(HashSet::new)));
		context.testClass().map(Class::getName).ifPresent(entries::add);
		context.testClass().map(Class::getEnclosingClass).map(Class::getName).ifPresent(entries::add);
		return entries;
	}

	private static boolean shouldMirrorOutput(TestContext context) {
		return findAnnotationIn(context, MirrorOutput.class).map(MirrorOutput::value).map(MirrorOutputPolicy::isEnabled)
				.orElse(false);
	}

	private static long getMaxStandardOutput(TestContext context) {
		return findAnnotationIn(context, MirrorOutput.class).map(MirrorOutput::maxCharCount)
				.orElse(DEFAULT_MAX_STD_OUT);
	}

	private static OptionalInt getAllowedLocalPort(TestContext context) {
		return findAnnotationIn(context, AllowLocalPort.class).map(AllowLocalPort::value).map(OptionalInt::of)
				.orElseGet(OptionalInt::empty);
	}

	private static OptionalInt getAllowedThreadCount(TestContext context) {
		return findAnnotationIn(context, AllowThreads.class).map(AllowThreads::maxActiveCount).map(OptionalInt::of)
				.orElseGet(OptionalInt::empty);
	}

	private static <A extends Annotation> Optional<A> findAnnotationIn(TestContext context, Class<A> annotation) {
		return findAnnotation(context.testMethod(), annotation)
				.or(() -> findAnnotation(context.annotatedElement(), annotation))
				.or(() -> findAnnotation(context.testClass(), annotation));
	}
}
