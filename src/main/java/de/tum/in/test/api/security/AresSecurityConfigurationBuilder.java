package de.tum.in.test.api.security;

import static de.tum.in.test.api.localization.Messages.localized;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.*;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.context.TestContext;
import de.tum.in.test.api.util.*;

@API(status = Status.INTERNAL)
public final class AresSecurityConfigurationBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(AresSecurityConfigurationBuilder.class);

	private static final Path EXPECTED_MAVEN_POM_PATH = Path
			.of(System.getProperty(AresSystemProperties.ARES_MAVEN_POM, "pom.xml")); //$NON-NLS-1$
	private static final Path EXPECTED_GRADLE_BUILD_PATH = Path
			.of(System.getProperty(AresSystemProperties.ARES_GRADLE_BUILD, "build.gradle")); //$NON-NLS-1$
	private static final String MAVEN_ENFORCER_FILE_ENTRY = "<file>${project.build.outputDirectory}%s</file>"; //$NON-NLS-1$
	private static final String GRADLE_ENFORCER_FILE_ENTRY = "\"$studentOutputDir%s\""; //$NON-NLS-1$
	private static final boolean IS_MAVEN;
	private static final boolean IS_GRADLE;
	static {
		// Check if we are in a maven environment and don't intend to ignore that fact
		IS_MAVEN = (StackWalker.getInstance().walk(sfs -> sfs.anyMatch(sf -> sf.getClassName().contains("maven"))) //$NON-NLS-1$
				|| Files.exists(EXPECTED_MAVEN_POM_PATH))
				&& !Boolean.getBoolean(AresSystemProperties.ARES_MAVEN_IGNORE);
		IS_GRADLE = (StackWalker.getInstance().walk(sfs -> sfs.anyMatch(sf -> sf.getClassName().contains("gradle"))) //$NON-NLS-1$
				|| Files.exists(EXPECTED_GRADLE_BUILD_PATH))
				&& !Boolean.getBoolean(AresSystemProperties.ARES_GRADLE_IGNORE);
	}
	/**
	 * Cache for the content of the build file so that we don't need to read it each
	 * time. It must not change during program execution, anyway.
	 */
	private static String buildConfigurationFileContent;

	private Optional<Class<?>> testClass;
	private Optional<Method> testMethod;
	private Path executionPath;
	private Set<String> whitelistedClassNames;
	private Set<PathRule> whitelistedPaths;
	private Set<PathRule> blacklistedPaths;
	private Set<PackageRule> blacklistedPackages;
	private Set<PackageRule> whitelistedPackages;
	private Set<Integer> allowedLocalPorts;
	private OptionalInt allowLocalPortsAbove;
	private Set<Integer> excludedLocalPorts;
	private OptionalInt allowedThreadCount;
	private Set<PackageRule> trustedPackages;
	private TrustScope threadTrustScope;
	private boolean isThreadGroupCheckDisabled;

	private AresSecurityConfigurationBuilder() {
		testClass = Optional.empty();
		testMethod = Optional.empty();
		whitelistedClassNames = new HashSet<>();
		blacklistedPaths = Set.of();
		blacklistedPackages = Set.of();
		whitelistedPackages = Set.of();
		allowedLocalPorts = Set.of();
		allowLocalPortsAbove = OptionalInt.empty();
		excludedLocalPorts = Set.of();
		allowedThreadCount = OptionalInt.empty();
		trustedPackages = Set.of();
		threadTrustScope = TrustScope.MINIMAL;
		isThreadGroupCheckDisabled = false;
	}

	public AresSecurityConfigurationBuilder withPath(Path executionPath) {
		this.executionPath = Objects.requireNonNull(executionPath);
		return this;
	}

	public AresSecurityConfigurationBuilder withPathWhitelist(Collection<PathRule> whitelistedPaths) {
		this.whitelistedPaths = Set.copyOf(whitelistedPaths);
		return this;
	}

	public AresSecurityConfigurationBuilder withPathBlacklist(Collection<PathRule> blacklistedPaths) {
		this.blacklistedPaths = Set.copyOf(blacklistedPaths);
		return this;
	}

	public AresSecurityConfigurationBuilder withAllowedLocalPorts(Set<Integer> allowedLocalPorts) {
		this.allowedLocalPorts = Objects.requireNonNull(allowedLocalPorts);
		return this;
	}

	public AresSecurityConfigurationBuilder withAllowLocalPortsAbove(OptionalInt allowLocalPortsAbove) {
		this.allowLocalPortsAbove = Objects.requireNonNull(allowLocalPortsAbove);
		return this;
	}

	public AresSecurityConfigurationBuilder withExcludedLocalPorts(Set<Integer> excludedLocalPorts) {
		this.excludedLocalPorts = Objects.requireNonNull(excludedLocalPorts);
		return this;
	}

	public AresSecurityConfigurationBuilder withAllowedThreadCount(OptionalInt allowedThreadCount) {
		this.allowedThreadCount = Objects.requireNonNull(allowedThreadCount);
		return this;
	}

	public AresSecurityConfigurationBuilder configureFromContext(TestContext context) {
		testClass = Objects.requireNonNull(context.testClass());
		testMethod = Objects.requireNonNull(context.testMethod());
		return this;
	}

	public AresSecurityConfigurationBuilder addWhitelistedClassNames(Collection<String> classNames) {
		whitelistedClassNames.addAll(classNames);
		return this;
	}

	public AresSecurityConfigurationBuilder withPackageBlacklist(Collection<PackageRule> packageBlacklist) {
		blacklistedPackages = Set.copyOf(packageBlacklist);
		return this;
	}

	public AresSecurityConfigurationBuilder withPackageWhitelist(Collection<PackageRule> packageWhitelist) {
		whitelistedPackages = Set.copyOf(packageWhitelist);
		return this;
	}

	public AresSecurityConfigurationBuilder withTrustedPackages(Set<PackageRule> trustedPackages) {
		this.trustedPackages = Set.copyOf(trustedPackages);
		return this;
	}

	public AresSecurityConfigurationBuilder withThreadTrustScope(TrustScope threadTrustScope) {
		this.threadTrustScope = Objects.requireNonNull(threadTrustScope);
		return this;
	}

	public AresSecurityConfigurationBuilder withIsThreadGroupCheckDisabled(boolean isThreadGroupCheckDisabled) {
		this.isThreadGroupCheckDisabled = isThreadGroupCheckDisabled;
		return this;
	}

	public AresSecurityConfiguration build() {
		validate();
		return new AresSecurityConfiguration(testClass, testMethod, executionPath, whitelistedClassNames,
				Optional.ofNullable(whitelistedPaths), blacklistedPaths, allowedLocalPorts, allowLocalPortsAbove,
				excludedLocalPorts, allowedThreadCount, blacklistedPackages, whitelistedPackages, trustedPackages,
				threadTrustScope, isThreadGroupCheckDisabled);
	}

	private void validate() {
		if (allowedThreadCount.orElse(0) < 0)
			throw new ConfigurationException(localized("security.configuration_invalid_negative_threads")); //$NON-NLS-1$
		if (!Collections.disjoint(allowedLocalPorts, excludedLocalPorts))
			throw new ConfigurationException(localized("security.configuration_invalid_port_rule_intersection")); //$NON-NLS-1$
		allowedLocalPorts.forEach(AresSecurityConfigurationBuilder::validatePortRange);
		excludedLocalPorts.forEach(AresSecurityConfigurationBuilder::validatePortRange);
		allowLocalPortsAbove.ifPresent(value -> {
			validatePortRange(value);
			if (allowedLocalPorts.stream().anyMatch(allowed -> allowed > value))
				throw new ConfigurationException(localized("security.configuration_invalid_port_allowed_in_rage")); //$NON-NLS-1$
			if (excludedLocalPorts.stream().anyMatch(exclusion -> exclusion <= value))
				throw new ConfigurationException(localized("security.configuration_invalid_port_exclude_outside_rage")); //$NON-NLS-1$
		});
		validateTrustedPackages(trustedPackages);
	}

	private static void validatePortRange(int value) {
		if (value < AllowLocalPort.MINIMUM)
			throw new ConfigurationException(localized("security.configuration_invalid_port_negative")); //$NON-NLS-1$
		if (value > AllowLocalPort.MAXIMUM)
			throw new ConfigurationException(localized("security.configuration_invalid_port_over_max")); //$NON-NLS-1$
	}

	private static void validateTrustedPackages(Set<PackageRule> trustedPackages) {
		String enforcerFileEntryFormat;
		Path expectedProjectBuildFilePath;
		if (IS_MAVEN) {
			enforcerFileEntryFormat = MAVEN_ENFORCER_FILE_ENTRY;
			expectedProjectBuildFilePath = EXPECTED_MAVEN_POM_PATH;
		} else if (IS_GRADLE) {
			enforcerFileEntryFormat = GRADLE_ENFORCER_FILE_ENTRY;
			expectedProjectBuildFilePath = EXPECTED_GRADLE_BUILD_PATH;
		} else {
			return;
		}
		try {
			if (buildConfigurationFileContent == null)
				buildConfigurationFileContent = Files.readString(expectedProjectBuildFilePath);
			var enforcerFileRules = Stream.concat(
					// include all package prefixes that are statically whitelisted
					SecurityConstants.STACK_WHITELIST.stream(),
					// include the part before the first wildcard of all trusted packages
					trustedPackages.stream().map(packageRule -> packageRule.getPackagePattern().split("\\*", 2)[0]) //$NON-NLS-1$
							// Ignore rules starting with wildcards. We cannot enforce those.
							.filter(Predicate.not(String::isEmpty)))
					// Transform package name prefixes like com.example. to paths like /com/example/
					.map(packagePrefix -> "/" + String.join("/", packagePrefix.split("\\.")) + "/") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					// And finally wrap the paths info file rules for maven enforcer
					.map(packagePath -> String.format(enforcerFileEntryFormat, packagePath));
			// all must be contained in the build file, find the missing ones
			var missing = enforcerFileRules.filter(Predicate.not(buildConfigurationFileContent::contains)).sorted()
					.collect(Collectors.toList());
			LOG.debug("Validated build configuration regarding trusted package rules, {} are missing.", missing.size()); //$NON-NLS-1$
			// If nothing is missing, we're good. Otherwise tell the user what is missing
			if (missing.isEmpty())
				return;
			throw new ConfigurationException("Ares has detected that the build configuration is probably incomplete." //$NON-NLS-1$
					+ " The following file-must-not-exist rules seem to be missing:\n    " //$NON-NLS-1$
					+ String.join("\n    ", missing) //$NON-NLS-1$
					+ "\n    See https://github.com/ls1intum/Ares#what-you-need-to-do-outside-ares for more information."); //$NON-NLS-1$
		} catch (IOException e) {
			LOG.error("Ares cannot read pom.xml", e); //$NON-NLS-1$
			throw new ConfigurationException("Ares cannot read pom.xml and validate the configuration." //$NON-NLS-1$
					+ " Please make sure " + expectedProjectBuildFilePath.getFileName() //$NON-NLS-1$
					+ " can be read or otherwise set the 'ares.maven.ignore'/'ares.gradle.ignore' system property to true"); //$NON-NLS-1$
		}
	}

	public static AresSecurityConfigurationBuilder create() {
		return new AresSecurityConfigurationBuilder();
	}
}
