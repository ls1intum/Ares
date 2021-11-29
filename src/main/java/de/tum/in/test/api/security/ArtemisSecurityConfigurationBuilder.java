package de.tum.in.test.api.security;

import static de.tum.in.test.api.localization.Messages.localized;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.internal.TestContext;
import de.tum.in.test.api.util.PackageRule;
import de.tum.in.test.api.util.PathRule;

@API(status = Status.INTERNAL)
public final class ArtemisSecurityConfigurationBuilder {
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
	}

	private static void validatePortRange(int value) {
		if (value < AllowLocalPort.MINIMUM)
			throw new ConfigurationException(localized("security.configuration_invalid_port_negative")); //$NON-NLS-1$
		if (value > AllowLocalPort.MAXIMUM)
			throw new ConfigurationException(localized("security.configuration_invalid_port_over_max")); //$NON-NLS-1$
	}

	public static ArtemisSecurityConfigurationBuilder create() {
		return new ArtemisSecurityConfigurationBuilder();
	}
}
