package de.tum.in.test.api.locked;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class ArtemisSecurityConfigurationImpl implements ArtemisSecurityConfiguration {
	private final Class<?> testClass;
	private final Method testMethod;
	private final Path executionPath;
	private final List<String> whitelistedClassNames;
	private final Optional<Set<Path>> whitelistedPaths;
	private final boolean whitelistFirstThread;

	ArtemisSecurityConfigurationImpl(Class<?> testClass, Method testMethod, Path executionPath,
			Collection<String> whitelistedClassNames, boolean whitelistFirstThread,
			Optional<Collection<Path>> whitelistedPaths) {
		this.testClass = Objects.requireNonNull(testClass);
		this.testMethod = Objects.requireNonNull(testMethod);
		this.executionPath = executionPath.toAbsolutePath();
		this.whitelistedClassNames = List.copyOf(whitelistedClassNames);
		this.whitelistedPaths = whitelistedPaths.map(Set::copyOf);
		this.whitelistFirstThread = whitelistFirstThread;
	}

	@Override
	public final Class<?> testClass() {
		return testClass;
	}

	@Override
	public Method testMethod() {
		return testMethod;
	}

	@Override
	public final Path executionPath() {
		return executionPath;
	}

	@Override
	public final List<String> whitelistedClassNames() {
		return whitelistedClassNames;
	}

	@Override
	public boolean whitelistFirstThread() {
		return whitelistFirstThread;
	}

	@Override
	public Optional<Set<Path>> whitelistedPaths() {
		return whitelistedPaths;
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
				&& whitelistFirstThread == other.whitelistFirstThread;
	}

	@Override
	public int hashCode() {
		return Objects.hash(executionPath, testClass, testMethod, whitelistedClassNames, whitelistFirstThread);
	}

	@Override
	public String toString() {
		return String.format(
				"ArtemisSecurityConfigurationImpl [whitelistedClassNames=%s, executionPath=%s,"
						+ " testClass=%s, testMethod=%s, whitelistFirstThread=%s, whitelistedPaths=%s]",
				whitelistedClassNames, executionPath, testClass, testMethod, whitelistFirstThread, whitelistedPaths);
	}

	@Override
	public String shortDesc() {
		return String.format("ASC-Impl [testMethod=%s, executionPath=%s, whitelistFirstThread=%s, whitelistedPaths=%s]",
				testMethod, executionPath, whitelistFirstThread, whitelistedPaths);
	}

}
