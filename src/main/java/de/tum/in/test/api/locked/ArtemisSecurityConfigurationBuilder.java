package de.tum.in.test.api.locked;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.tum.in.test.api.util.PathRule;
import de.tum.in.test.api.util.TestContext;
import de.tum.in.test.api.util.TimeoutUtils;

public final class ArtemisSecurityConfigurationBuilder {
	private Class<?> testClass;
	private Method testMethod;
	private Path executionPath;
	private Set<String> whitelistedClassNames = new HashSet<>();
	private Set<PathRule> whitelistedPaths;
	private Set<PathRule> blacklistedPaths = Set.of();
	private boolean whitelistFirstThread;

	private ArtemisSecurityConfigurationBuilder() {

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

	public boolean isWhitelistFirstThread() {
		return whitelistFirstThread;
	}

	public Set<PathRule> getWhitelistedPaths() {
		return whitelistedPaths;
	}

	public ArtemisSecurityConfigurationBuilder withCurrentPath() {
		this.executionPath = Path.of("");
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPath(Path executionPath) {
		this.executionPath = Objects.requireNonNull(executionPath);
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathWhitelist(Collection<PathRule> whitelistedPaths) {
		this.whitelistedPaths = whitelistedPaths.stream().collect(Collectors.toSet());
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withPathWhitelist(
			Optional<? extends Collection<PathRule>> whitelistedPaths) {
		whitelistedPaths.ifPresentOrElse(this::withPathWhitelist, () -> {
			this.whitelistedPaths = null;
		});
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

	public ArtemisSecurityConfigurationBuilder withWhitelistFirstThread(boolean whitelistFirstThread) {
		this.whitelistFirstThread = whitelistFirstThread;
		return this;
	}

	public ArtemisSecurityConfigurationBuilder withFirstThreadWhitelisted() {
		this.whitelistFirstThread = true;
		return this;
	}

	public ArtemisSecurityConfigurationBuilder configureFromContext(TestContext context) {
		this.testClass = context.testClass().get();
		this.testMethod = context.testMethod().get();
		this.whitelistFirstThread = TimeoutUtils.findTimeout(context).isPresent();
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
		return new ArtemisSecurityConfigurationImpl(testClass, testMethod, executionPath, whitelistedClassNames,
				whitelistFirstThread, Optional.ofNullable(whitelistedPaths), blacklistedPaths);
	}

	public static ArtemisSecurityConfigurationBuilder create() {
		return new ArtemisSecurityConfigurationBuilder();
	}
}
