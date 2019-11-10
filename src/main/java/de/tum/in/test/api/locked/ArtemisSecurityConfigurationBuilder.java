package de.tum.in.test.api.locked;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionContext;

public final class ArtemisSecurityConfigurationBuilder {
	private Class<?> testClass;
	private Path executionPath;
	private Set<String> whitelistedClassNames = new HashSet<>();

	private ArtemisSecurityConfigurationBuilder() {

	}

	public ArtemisSecurityConfigurationBuilder withCurrentPath() {
		this.executionPath = Path.of(".");
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPath(Path executionPath) {
		this.executionPath = Objects.requireNonNull(executionPath);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withTestClass(Class<?> testClass) {
		this.testClass = Objects.requireNonNull(testClass);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder configureFromContext(ExtensionContext context) {
		this.testClass = context.getRequiredTestClass();
		return this;
	}

	public ArtemisSecurityConfigurationBuilder addWhitelistedClassNames(String... classNames) {
		return addWhitelistedClassNames(List.of(classNames));
	}

	public ArtemisSecurityConfigurationBuilder addWhitelistedClassNames(Collection<String> classNames) {
		whitelistedClassNames.addAll(classNames);
		return this;
	}

	public ArtemisSecurityConfiguration build() {
		return new ArtemisSecurityConfigurationImpl(testClass, executionPath, whitelistedClassNames);
	}

	public static ArtemisSecurityConfigurationBuilder create() {
		return new ArtemisSecurityConfigurationBuilder();
	}
}
