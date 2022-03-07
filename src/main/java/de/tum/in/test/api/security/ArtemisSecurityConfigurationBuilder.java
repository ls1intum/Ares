package de.tum.in.test.api.security;

import static de.tum.in.test.api.localization.Messages.localized;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.internal.TestContext;
import de.tum.in.test.api.util.PackageRule;
import de.tum.in.test.api.util.PathRule;

@API(status = Status.INTERNAL)
public final class ArtemisSecurityConfigurationBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(ArtemisSecurityConfigurationBuilder.class);

	private static final Path EXPECTED_MAVEN_POM_PATH = Path.of(System.getProperty("ares.maven.pom", "pom.xml")); //$NON-NLS-1$ //$NON-NLS-2$
	private static final Path EXPECTED_GRADLE_BUILD_PATH = Path
			.of(System.getProperty("ares.gradle.build", "build.gradle"));
	private static final String MAVEN_ENFORCER_FILE_ENTRY = "<file>${project.build.outputDirectory}%s</file>";
	private static final boolean IS_MAVEN;
	private static final boolean IS_GRADLE;
	static {
		// Check if we are in a maven environment and don't intend to ignore that fact
		IS_MAVEN = (StackWalker.getInstance().walk(sfs -> sfs.anyMatch(sf -> sf.getClassName().contains("maven"))) //$NON-NLS-1$
				|| Files.exists(EXPECTED_MAVEN_POM_PATH))
				&& !Boolean.parseBoolean(System.getProperty("ares.maven.ignore")); //$NON-NLS-1$
		IS_GRADLE = (StackWalker.getInstance().walk(sfs -> sfs.anyMatch(sf -> sf.getClassName().contains("gradle"))) //$NON-NLS-1$
				|| Files.exists(EXPECTED_GRADLE_BUILD_PATH))
				&& !Boolean.parseBoolean(System.getProperty("ares.gradle.ignore")); //$NON-NLS-1$
	}

	/**
	 * Pattern/String for verifying that certain files are forced to not exist
	 */
	private static final String GRADLE_ENFORCER_TASK = "test {\n" + "    doFirst {\n"
			+ "        for (String fileName in filesToNotExist) {\n"
			+ "            assert !file(\"$studentOutputDir/$fileName\").exists(): \"$fileName must not be exist within the submission.\"\n"
			+ "        }\n" + "    }";
	private static final Pattern FILES_TO_NOT_EXIST_PATTERN = Pattern
			.compile("def\\s+filesToNotExist\\s+=\\s+\\[\"(?<files>.*)\"\\]");

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

	private ArtemisSecurityConfigurationBuilder() {
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
	}

	public ArtemisSecurityConfigurationBuilder withPath(Path executionPath) {
		this.executionPath = Objects.requireNonNull(executionPath);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathWhitelist(Collection<PathRule> whitelistedPaths) {
		this.whitelistedPaths = Set.copyOf(whitelistedPaths);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathBlacklist(Collection<PathRule> blacklistedPaths) {
		this.blacklistedPaths = Set.copyOf(blacklistedPaths);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withAllowedLocalPorts(Set<Integer> allowedLocalPorts) {
		this.allowedLocalPorts = Objects.requireNonNull(allowedLocalPorts);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withAllowLocalPortsAbove(OptionalInt allowLocalPortsAbove) {
		this.allowLocalPortsAbove = Objects.requireNonNull(allowLocalPortsAbove);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withExcludedLocalPorts(Set<Integer> excludedLocalPorts) {
		this.excludedLocalPorts = Objects.requireNonNull(excludedLocalPorts);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withAllowedThreadCount(OptionalInt allowedThreadCount) {
		this.allowedThreadCount = Objects.requireNonNull(allowedThreadCount);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder configureFromContext(TestContext context) {
		testClass = Objects.requireNonNull(context.testClass());
		testMethod = Objects.requireNonNull(context.testMethod());
		return this;
	}

	public ArtemisSecurityConfigurationBuilder addWhitelistedClassNames(Collection<String> classNames) {
		whitelistedClassNames.addAll(classNames);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPackageBlacklist(Collection<PackageRule> packageBlacklist) {
		blacklistedPackages = Set.copyOf(packageBlacklist);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPackageWhitelist(Collection<PackageRule> packageWhitelist) {
		whitelistedPackages = Set.copyOf(packageWhitelist);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withTrustedPackages(Set<PackageRule> trustedPackages) {
		this.trustedPackages = Set.copyOf(trustedPackages);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withThreadTrustScope(TrustScope threadTrustScope) {
		this.threadTrustScope = Objects.requireNonNull(threadTrustScope);
		return this;
	}

	public ArtemisSecurityConfiguration build() {
		validate();
		return new ArtemisSecurityConfiguration(testClass, testMethod, executionPath, whitelistedClassNames,
				Optional.ofNullable(whitelistedPaths), blacklistedPaths, allowedLocalPorts, allowLocalPortsAbove,
				excludedLocalPorts, allowedThreadCount, blacklistedPackages, whitelistedPackages, trustedPackages,
				threadTrustScope);
	}

	private void validate() {
		if (allowedThreadCount.orElse(0) < 0)
			throw new ConfigurationException(localized("security.configuration_invalid_negative_threads")); //$NON-NLS-1$
		if (!Collections.disjoint(allowedLocalPorts, excludedLocalPorts))
			throw new ConfigurationException(localized("security.configuration_invalid_port_rule_intersection")); //$NON-NLS-1$
		allowedLocalPorts.forEach(ArtemisSecurityConfigurationBuilder::validatePortRange);
		excludedLocalPorts.forEach(ArtemisSecurityConfigurationBuilder::validatePortRange);
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
			expectedProjectBuildFilePath = EXPECTED_MAVEN_POM_PATH;
		} else if (IS_GRADLE) {
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
					.map(packagePrefix -> "/" + String.join("/", packagePrefix.split("\\.")) + "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

			// And finally wrap the paths info file rules (only for maven enforcer)

			// .map(packagePath -> String.format(enforcerFileEntryFormat, packagePath));
			// all must be contained in the build file, find the missing ones
			List<String> missing;
			if (IS_MAVEN) {
				enforcerFileRules = enforcerFileRules
						.map(packagePath -> String.format(MAVEN_ENFORCER_FILE_ENTRY, packagePath));
				missing = enforcerFileRules.filter(Predicate.not(buildConfigurationFileContent::contains)).sorted()
						.collect(Collectors.toList());
			} else {
				if (!buildConfigurationFileContent.contains(GRADLE_ENFORCER_TASK))
					throw new ConfigurationException(
							generateEnforcingConfigurationException("Make sure to not change the test task at all.")); //$NON-NLS-1$

				Matcher fileNameMatcher = FILES_TO_NOT_EXIST_PATTERN.matcher(buildConfigurationFileContent);
				if (fileNameMatcher.find()) {
					List<String> filesToNotExist = Arrays.stream(fileNameMatcher.group("files").split(","))
							.map(name -> name.replaceAll("\"", "").trim()).collect(Collectors.toList());
					missing = enforcerFileRules.filter(Predicate.not(filesToNotExist::contains)).sorted()
							.collect(Collectors.toList());
				} else {
					throw new ConfigurationException(
							generateEnforcingConfigurationException("The attribute \"filesToNotExist\" is missing.")); //$NON-NLS-1$
				}
			}

			LOG.debug("Validated build configuration regarding trusted package rules, {} are missing.", missing.size()); //$NON-NLS-1$
			// If nothing is missing, we're good. Otherwise tell the user what is missing
			if (missing.isEmpty())
				return;
			throw new ConfigurationException(generateEnforcingConfigurationException(
					" The following file-must-not-exist rules seem to be missing:\n    " //$NON-NLS-1$
							+ String.join("\n    ", missing))); //$NON-NLS-1$
		} catch (IOException e) {
			LOG.error("Ares cannot read pom.xml", e); //$NON-NLS-1$
			throw new ConfigurationException("Ares cannot read pom.xml and validate the configuration." //$NON-NLS-1$
					+ " Please make sure Path.of(\"pom.xml\") can be read or otherwise "); //$NON-NLS-1$
		}
	}

	private static String generateEnforcingConfigurationException(String message) {
		return "Ares has detected that the build configuration is probably incomplete." //$NON-NLS-1$
				+ message
				+ "\n    See https://github.com/ls1intum/Ares#what-you-need-to-do-outside-ares for more information."; //$NON-NLS-1$
	}

	public static ArtemisSecurityConfigurationBuilder create() {
		return new ArtemisSecurityConfigurationBuilder();
	}
}
