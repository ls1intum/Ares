package de.tum.in.test.api.security;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import de.tum.in.test.api.internal.TestContext;
import de.tum.in.test.api.util.PackageRule;
import de.tum.in.test.api.util.PathRule;

public final class ArtemisSecurityConfigurationBuilder {
	private Class<?> testClass;
	private Method testMethod;
	private Path executionPath;
	private Set<String> whitelistedClassNames;
	private Set<PathRule> whitelistedPaths;
	private Set<PathRule> blacklistedPaths;
	private Set<PackageRule> blacklistedPackages;
	private Set<PackageRule> whitelistedPackages;
	private OptionalInt allowedLocalPort;
	private OptionalInt allowedThreadCount;

	private ArtemisSecurityConfigurationBuilder() {
		whitelistedClassNames = new HashSet<>();
		blacklistedPaths = Set.of();
		blacklistedPackages = Set.of();
		whitelistedPackages = Set.of();
		allowedLocalPort = OptionalInt.empty();
		allowedThreadCount = OptionalInt.empty();
	}

	public Class<?> getTestClass() {
		return testClass;
	}

	public Method getTestMethod() {
		return testMethod;
	}

	public Path getExecutionPath() {
		return executionPath;
	}

	public Set<String> getWhitelistedClassNames() {
		return whitelistedClassNames;
	}

	public Set<PathRule> getWhitelistedPaths() {
		return whitelistedPaths;
	}

	public Set<PathRule> getBlacklistedPaths() {
		return blacklistedPaths;
	}

	public OptionalInt getAllowedLocalPort() {
		return allowedLocalPort;
	}

	public OptionalInt getAllowedThreadCount() {
		return allowedThreadCount;
	}

	public Set<PackageRule> getBlacklistedPackages() {
		return blacklistedPackages;
	}

	public Set<PackageRule> getWhitelistedPackages() {
		return whitelistedPackages;
	}

	public ArtemisSecurityConfigurationBuilder withCurrentPath() {
		executionPath = Path.of("");
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPath(Path executionPath) {
		this.executionPath = Objects.requireNonNull(executionPath);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathWhitelist(Collection<PathRule> whitelistedPaths) {
		this.whitelistedPaths = Set.copyOf(whitelistedPaths);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathWhitelist(
			Optional<? extends Collection<PathRule>> whitelistedPaths) {
		whitelistedPaths.ifPresentOrElse(this::withPathWhitelist, () -> this.whitelistedPaths = null);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathBlacklist(Collection<PathRule> blacklistedPaths) {
		this.blacklistedPaths = Set.copyOf(blacklistedPaths);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withTestClass(Class<?> testClass) {
		this.testClass = Objects.requireNonNull(testClass);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withTestMethod(Method testMethod) {
		this.testMethod = Objects.requireNonNull(testMethod);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withAllowedLocalPort(OptionalInt allowedLocalPort) {
		this.allowedLocalPort = Objects.requireNonNull(allowedLocalPort);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withAllowedThreadCount(OptionalInt allowedThreadCount) {
		this.allowedThreadCount = Objects.requireNonNull(allowedThreadCount);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder allowLocalPort(int port) {
		allowedLocalPort = OptionalInt.of(port);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder configureFromContext(TestContext context) {
		testClass = context.testClass().get();
		testMethod = context.testMethod().get();
		return this;
	}

	public ArtemisSecurityConfigurationBuilder addWhitelistedClassNames(String... classNames) {
		return addWhitelistedClassNames(List.of(classNames));
	}

	public ArtemisSecurityConfigurationBuilder addWhitelistedClassNames(Collection<String> classNames) {
		whitelistedClassNames.addAll(classNames);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPackageBlacklist(Collection<PackageRule> packageBlacklist) {
		this.blacklistedPackages = Set.copyOf(packageBlacklist);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPackageWhitelist(Collection<PackageRule> packageWhitelist) {
		this.whitelistedPackages = Set.copyOf(packageWhitelist);
		return this;
	}

	public ArtemisSecurityConfiguration build() {
		return new ArtemisSecurityConfigurationImpl(testClass, testMethod, executionPath, whitelistedClassNames,
				Optional.ofNullable(whitelistedPaths), blacklistedPaths, allowedLocalPort, allowedThreadCount,
				blacklistedPackages, whitelistedPackages);
	}

	public static ArtemisSecurityConfigurationBuilder create() {
		return new ArtemisSecurityConfigurationBuilder();
	}
}
