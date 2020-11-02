package de.tum.in.test.api.security;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.PackageRule;
import de.tum.in.test.api.util.PathRule;

@API(status = Status.INTERNAL)
public final class ArtemisSecurityConfiguration {
	private final Optional<Class<?>> testClass;
	private final Optional<Method> testMethod;
	private final Path executionPath;
	private final List<String> whitelistedClassNames;
	private final Optional<Set<PathRule>> whitelistedPaths;
	private final Set<PathRule> blacklistedPaths;
	private final Set<Integer> allowedLocalPorts;
	private final OptionalInt allowLocalPortsAbove;
	private final Set<Integer> excludedLocalPorts;
	private final OptionalInt allowedThreadCount;
	private final Set<PackageRule> blacklistedPackages;
	private final Set<PackageRule> whitelistedPackages;
	private final Set<PackageRule> trustedPackages;

	ArtemisSecurityConfiguration(Optional<Class<?>> testClass, Optional<Method> testMethod, Path executionPath,
			Collection<String> whitelistedClassNames, Optional<Collection<PathRule>> whitelistedPaths,
			Collection<PathRule> blacklistedPaths, Set<Integer> allowedLocalPorts, OptionalInt allowLocalPortsAbove,
			Set<Integer> excludedLocalPorts, OptionalInt allowedThreadCount, Set<PackageRule> blacklistedPackages,
			Set<PackageRule> whitelistedPackages, Set<PackageRule> trustedPackages) {
		this.testClass = Objects.requireNonNull(testClass);
		this.testMethod = Objects.requireNonNull(testMethod);
		this.executionPath = executionPath.toAbsolutePath();
		this.whitelistedClassNames = List.copyOf(whitelistedClassNames);
		this.whitelistedPaths = whitelistedPaths.map(Set::copyOf);
		this.blacklistedPaths = Set.copyOf(blacklistedPaths);
		this.allowedLocalPorts = Set.copyOf(allowedLocalPorts);
		this.allowLocalPortsAbove = Objects.requireNonNull(allowLocalPortsAbove);
		this.excludedLocalPorts = Set.copyOf(excludedLocalPorts);
		this.allowedThreadCount = Objects.requireNonNull(allowedThreadCount);
		this.blacklistedPackages = Set.copyOf(blacklistedPackages);
		this.whitelistedPackages = Set.copyOf(whitelistedPackages);
		this.trustedPackages = Set.copyOf(trustedPackages);
	}

	public Optional<Class<?>> testClass() {
		return testClass;
	}

	public Optional<Method> testMethod() {
		return testMethod;
	}

	public Path executionPath() {
		return executionPath;
	}

	public List<String> whitelistedClassNames() {
		return whitelistedClassNames;
	}

	public Optional<Set<PathRule>> whitelistedPaths() {
		return whitelistedPaths;
	}

	public Set<PathRule> blacklistedPaths() {
		return blacklistedPaths;
	}

	public Set<Integer> allowedLocalPorts() {
		return allowedLocalPorts;
	}

	public OptionalInt allowLocalPortsAbove() {
		return allowLocalPortsAbove;
	}

	public Set<Integer> excludedLocalPorts() {
		return excludedLocalPorts;
	}

	public OptionalInt allowedThreadCount() {
		return allowedThreadCount;
	}

	public Set<PackageRule> blacklistedPackages() {
		return blacklistedPackages;
	}

	public Set<PackageRule> whitelistedPackages() {
		return whitelistedPackages;
	}

	public Set<PackageRule> trustedPackages() {
		return trustedPackages;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArtemisSecurityConfiguration))
			return false;
		ArtemisSecurityConfiguration other = (ArtemisSecurityConfiguration) obj;
		return Objects.equals(executionPath, other.executionPath) && Objects.equals(testClass, other.testClass)
				&& Objects.equals(testMethod, other.testMethod)
				&& Objects.equals(whitelistedClassNames, other.whitelistedClassNames)
				&& Objects.equals(allowedLocalPorts, other.allowedLocalPorts)
				&& Objects.equals(allowLocalPortsAbove, other.allowLocalPortsAbove)
				&& Objects.equals(excludedLocalPorts, other.excludedLocalPorts)
				&& Objects.equals(allowedThreadCount, other.allowedThreadCount)
				&& Objects.equals(whitelistedPaths, other.whitelistedPaths)
				&& Objects.equals(blacklistedPaths, other.blacklistedPaths)
				&& Objects.equals(blacklistedPackages, other.blacklistedPackages)
				&& Objects.equals(whitelistedPackages, other.whitelistedPackages);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executionPath, testClass, testMethod, whitelistedClassNames, allowedThreadCount,
				whitelistedPaths, blacklistedPaths, blacklistedPackages, whitelistedPackages);
	}

	@Override
	public String toString() {
		return String.format("ArtemisSecurityConfigurationImpl [whitelistedClassNames=%s, executionPath=%s,"
				+ " testClass=%s, testMethod=%s, whitelistedPaths=%s, blacklistedPaths=%s, allowedLocalPorts=%s,"
				+ " allowLocalPortsAbove=%s, excludedLocalPorts=%s, allowedThreadCount=%s,"
				+ " blacklistedPackages=%s, whitelistedPackages=%s, trustedPackages=%s]", whitelistedClassNames,
				executionPath, testClass, testMethod, whitelistedPaths, blacklistedPaths, allowedLocalPorts,
				allowLocalPortsAbove, excludedLocalPorts, allowedThreadCount, blacklistedPackages, whitelistedPackages,
				trustedPackages);
	}

	public String shortDesc() {
		return String.format(
				"ASC-Impl [testMethod=%s, executionPath=%s, whitelistedPaths=%s, blacklistedPaths=%s, allowedLocalPorts=%s, trustedPackages=%s]",
				testMethod, executionPath, whitelistedPaths, blacklistedPaths, allowedLocalPorts, trustedPackages);
	}
}
