package de.tum.in.test.api.security;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import de.tum.in.test.api.util.PathRule;

final class ArtemisSecurityConfigurationImpl implements ArtemisSecurityConfiguration {
	private final Class<?> testClass;
	private final Method testMethod;
	private final Path executionPath;
	private final List<String> whitelistedClassNames;
	private final Optional<Set<PathRule>> whitelistedPaths;
	private final Set<PathRule> blacklistedPaths;
	private final OptionalInt allowedLocalPort;
	private final OptionalInt allowedThreadCount;

	ArtemisSecurityConfigurationImpl(Class<?> testClass, Method testMethod, Path executionPath,
			Collection<String> whitelistedClassNames, Optional<Collection<PathRule>> whitelistedPaths,
			Collection<PathRule> blacklistedPaths, OptionalInt allowedLocalPort, OptionalInt allowedThreadCount) {
		this.testClass = Objects.requireNonNull(testClass);
		this.testMethod = Objects.requireNonNull(testMethod);
		this.executionPath = executionPath.toAbsolutePath();
		this.whitelistedClassNames = List.copyOf(whitelistedClassNames);
		this.whitelistedPaths = whitelistedPaths.map(Set::copyOf);
		this.blacklistedPaths = Set.copyOf(blacklistedPaths);
		this.allowedLocalPort = Objects.requireNonNull(allowedLocalPort);
		this.allowedThreadCount = Objects.requireNonNull(allowedThreadCount);
	}

	@Override
	public Class<?> testClass() {
		return testClass;
	}

	@Override
	public Method testMethod() {
		return testMethod;
	}

	@Override
	public Path executionPath() {
		return executionPath;
	}

	@Override
	public List<String> whitelistedClassNames() {
		return whitelistedClassNames;
	}

	@Override
	public Optional<Set<PathRule>> whitelistedPaths() {
		return whitelistedPaths;
	}

	@Override
	public Set<PathRule> blacklistedPaths() {
		return blacklistedPaths;
	}

	@Override
	public OptionalInt allowedLocalPort() {
		return allowedLocalPort;
	}

	@Override
	public OptionalInt allowedThreadCount() {
		return allowedThreadCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArtemisSecurityConfigurationImpl))
			return false;
		ArtemisSecurityConfigurationImpl other = (ArtemisSecurityConfigurationImpl) obj;
		return Objects.equals(executionPath, other.executionPath) && Objects.equals(testClass, other.testClass)
				&& Objects.equals(testMethod, other.testMethod)
				&& Objects.equals(whitelistedClassNames, other.whitelistedClassNames)
				&& Objects.equals(allowedLocalPort, other.allowedLocalPort)
				&& Objects.equals(allowedThreadCount, other.allowedThreadCount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executionPath, testClass, testMethod, whitelistedClassNames, allowedThreadCount);
	}

	@Override
	public String toString() {
		return String.format("ArtemisSecurityConfigurationImpl [whitelistedClassNames=%s, executionPath=%s,"
				+ " testClass=%s, testMethod=%s, whitelistedPaths=%s, allowedLocalPort=%s, allowedThreadCount=%s]",
				whitelistedClassNames, executionPath, testClass, testMethod, whitelistedPaths, allowedLocalPort,
				allowedThreadCount);
	}

	@Override
	public String shortDesc() {
		return String.format(
				"ASC-Impl [testMethod=%s, executionPath=%s, whitelistedPaths=%s, allowedLocalPort=%s]",
				testMethod, executionPath, whitelistedPaths, allowedLocalPort);
	}
}
