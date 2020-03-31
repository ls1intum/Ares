package de.tum.in.test.api.internal;

import java.lang.StackWalker.StackFrame;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.WhitelistClass;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.security.ArtemisSecurityConfiguration;
import de.tum.in.test.api.security.ArtemisSecurityConfigurationBuilder;
import de.tum.in.test.api.util.PathRule;

public final class ConfigurationUtils {

	private static final long DEFAULT_MAX_STD_OUT = 10_000_000L;

	private ConfigurationUtils() {

	}

	public static ArtemisSecurityConfiguration generateConfiguration(TestContext context) {
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

	public static Set<PathRule> generatePathWhiteList(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, WhitelistPath.class).map(PathRule::of)
				.collect(Collectors.toSet());
	}

	public static Set<PathRule> generatePathBlackList(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, BlacklistPath.class).map(PathRule::of)
				.collect(Collectors.toSet());
	}

	public static Set<String> generateClassWhiteList(TestContext context) {
		Set<String> entries = StackWalker.getInstance()
				.walk(s -> s.map(StackFrame::getClassName).collect(Collectors.toCollection(HashSet::new)));
		TestContextUtils.getClassNestingInnermostFirst(context).map(Class::getName).forEach(entries::add);
		entries.addAll(getWhitelistedClasses(context));
		return entries;
	}

	public static Set<String> getWhitelistedClasses(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, WhitelistClass.class).map(WhitelistClass::value)
				.map(Class::getName).collect(Collectors.toSet());
	}

	public static boolean shouldMirrorOutput(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, MirrorOutput.class).map(MirrorOutput::value)
				.map(MirrorOutputPolicy::isEnabled).orElse(false);
	}

	public static long getMaxStandardOutput(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, MirrorOutput.class).map(MirrorOutput::maxCharCount)
				.orElse(DEFAULT_MAX_STD_OUT);
	}

	public static OptionalInt getAllowedLocalPort(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, AllowLocalPort.class).map(AllowLocalPort::value)
				.map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public static OptionalInt getAllowedThreadCount(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, AllowThreads.class).map(AllowThreads::maxActiveCount)
				.map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

}
