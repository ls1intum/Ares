package de.tum.in.test.api.locked;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

final class ArtemisSecurityConfigurationImpl implements ArtemisSecurityConfiguration {
	private final Class<?> testClass;
	private final Path executionPath;
	private final List<String> whitelistedClassNames;

	ArtemisSecurityConfigurationImpl(Class<?> testClass, Path executionPath, Collection<String> whitelistedClassNames) {
		this.testClass = Objects.requireNonNull(testClass);
		this.executionPath = executionPath.toAbsolutePath();
		this.whitelistedClassNames = List.copyOf(whitelistedClassNames);
	}

	@Override
	public final Class<?> testClass() {
		return testClass;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArtemisSecurityConfigurationImpl))
			return false;
		ArtemisSecurityConfigurationImpl other = (ArtemisSecurityConfigurationImpl) obj;
		return Objects.equals(executionPath, other.executionPath) && Objects.equals(testClass, other.testClass)
				&& Objects.equals(whitelistedClassNames, other.whitelistedClassNames);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executionPath, testClass, whitelistedClassNames);
	}

	@Override
	public String toString() {
		return String.format(
				"ArtemisSecurityConfigurationImpl [whitelistedClassNames=%s, executionPath=%s, testClass=%s]",
				whitelistedClassNames, executionPath, testClass);
	}

	@Override
	public String shortDesc() {
		return String.format("ASC-Impl [executionPath=%s, testClass=%s]", executionPath, testClass);
	}
}
