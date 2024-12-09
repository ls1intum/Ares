package de.tum.in.test.api.internal;

import java.lang.StackWalker.StackFrame;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.*;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.context.*;
import de.tum.in.test.api.security.*;
import de.tum.in.test.api.util.*;

@API(status = Status.INTERNAL)
public final class ConfigurationUtils {

	private ConfigurationUtils() {
	}

	public static AresSecurityConfiguration generateConfiguration(TestContext context) {
		var config = AresSecurityConfigurationBuilder.create();
		config.configureFromContext(context);
		config.withPath(Path.of("")); //$NON-NLS-1$
		config.addWhitelistedClassNames(generateClassWhiteList(context));
		config.withPathWhitelist(generatePathWhiteList(context));
		config.withPathBlacklist(generatePathBlackList(context));
		config.withAllowedThreadCount(getAllowedThreadCount(context));
		config.withPackageBlacklist(generatePackageBlackList(context));
		config.withPackageWhitelist(generatePackageWhiteList(context));
		config.withTrustedPackages(getTrustedPackages(context));
		config.withThreadTrustScope(getThreadTrustScope(context));
		config.withIsThreadGroupCheckDisabled(isThreadGroupCheckDisabled(context));
		configureAllowLocalPort(config, context);
		return config.build();
	}

	public static Set<PathRule> generatePathWhiteList(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, WhitelistPath.class).flatMap(PathRule::allOf)
				.collect(Collectors.toSet());
	}

	public static Set<PathRule> generatePathBlackList(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, BlacklistPath.class).flatMap(PathRule::allOf)
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
				.flatMap(Arrays::stream).map(Class::getName).collect(Collectors.toSet());
	}

	public static boolean shouldMirrorOutput(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, MirrorOutput.class).map(MirrorOutput::value)
				.map(MirrorOutputPolicy::isEnabled).orElse(false);
	}

	public static long getMaxStandardOutput(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, MirrorOutput.class).map(MirrorOutput::maxCharCount)
				.orElse(MirrorOutput.DEFAULT_MAX_STD_OUT);
	}

	public static void configureAllowLocalPort(AresSecurityConfigurationBuilder config, TestContext context) {
		TestContextUtils.findAnnotationIn(context, AllowLocalPort.class).ifPresent(allowLocalPort -> {
			config.withAllowedLocalPorts(IntStream.of(allowLocalPort.value()).boxed().collect(Collectors.toSet()));
			config.withAllowLocalPortsAbove(OptionalInt.of(allowLocalPort.allowPortsAbove()));
			config.withExcludedLocalPorts(IntStream.of(allowLocalPort.exclude()).boxed().collect(Collectors.toSet()));
		});
	}

	public static OptionalInt getAllowedThreadCount(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, AllowThreads.class).map(AllowThreads::maxActiveCount)
				.map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public static Set<PackageRule> generatePackageBlackList(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, BlacklistPackage.class).flatMap(PackageRule::allOf)
				.collect(Collectors.toSet());
	}

	public static Set<PackageRule> generatePackageWhiteList(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, WhitelistPackage.class).flatMap(PackageRule::allOf)
				.collect(Collectors.toSet());
	}

	public static Optional<String> getNonprivilegedFailureMessage(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, PrivilegedExceptionsOnly.class)
				.map(PrivilegedExceptionsOnly::value);
	}

	public static Set<PackageRule> getTrustedPackages(TestContext context) {
		return TestContextUtils.findRepeatableAnnotationsIn(context, AddTrustedPackage.class)
				.map(AddTrustedPackage::value)
				.flatMap(packagePatterns -> PackageRule.from(RuleType.WHITELIST, packagePatterns))
				.collect(Collectors.toSet());
	}

	private static TrustScope getThreadTrustScope(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, TrustedThreads.class).map(TrustedThreads::value)
				.orElse(TrustScope.MINIMAL);
	}

	public static boolean isThreadGroupCheckDisabled(TestContext context) {
		return TestContextUtils.findAnnotationIn(context, DisableThreadGroupCheck.class).isPresent();
	}
}
