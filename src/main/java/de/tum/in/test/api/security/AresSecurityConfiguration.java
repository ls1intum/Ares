package de.tum.in.test.api.security;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.util.*;

@API(status = Status.INTERNAL)
public final class AresSecurityConfiguration {
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
	private final TrustScope threadTrustScope;
	private final boolean isThreadGroupCheckDisabled;

	AresSecurityConfiguration(Optional<Class<?>> testClass, Optional<Method> testMethod, Path executionPath, // NOSONAR
			Collection<String> whitelistedClassNames, Optional<Collection<PathRule>> whitelistedPaths,
			Collection<PathRule> blacklistedPaths, Set<Integer> allowedLocalPorts, OptionalInt allowLocalPortsAbove,
			Set<Integer> excludedLocalPorts, OptionalInt allowedThreadCount, Set<PackageRule> blacklistedPackages,
			Set<PackageRule> whitelistedPackages, Set<PackageRule> trustedPackages, TrustScope threadTrustScope,
			boolean isThreadGroupCheckDisabled) {
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
		this.threadTrustScope = threadTrustScope;
		this.isThreadGroupCheckDisabled = isThreadGroupCheckDisabled;
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

	public TrustScope threadTrustScope() {
		return threadTrustScope;
	}

	public boolean isThreadGroupCheckDisabled() {
		return isThreadGroupCheckDisabled;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AresSecurityConfiguration))
			return false;
		AresSecurityConfiguration other = (AresSecurityConfiguration) obj;
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
				&& Objects.equals(whitelistedPackages, other.whitelistedPackages)
				&& Objects.equals(threadTrustScope, other.threadTrustScope)
				&& Objects.equals(isThreadGroupCheckDisabled, other.isThreadGroupCheckDisabled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executionPath, testClass, testMethod, whitelistedClassNames, allowedThreadCount,
				whitelistedPaths, blacklistedPaths, blacklistedPackages, whitelistedPackages, threadTrustScope,
				isThreadGroupCheckDisabled);
	}

	@Override
	public String toString() {
		return String.format("AresSecurityConfiguration [whitelistedClassNames=%s, executionPath=%s," //$NON-NLS-1$
				+ " testClass=%s, testMethod=%s, whitelistedPaths=%s, blacklistedPaths=%s, allowedLocalPorts=%s," //$NON-NLS-1$
				+ " allowLocalPortsAbove=%s, excludedLocalPorts=%s, allowedThreadCount=%s," //$NON-NLS-1$
				+ " blacklistedPackages=%s, whitelistedPackages=%s, trustedPackages=%s, threadTrustScope=%s," //$NON-NLS-1$
				+ " isThreadGroupCheckDisabled=%b]", //$NON-NLS-1$
				whitelistedClassNames, executionPath, testClass, testMethod, whitelistedPaths, blacklistedPaths,
				allowedLocalPorts, allowLocalPortsAbove, excludedLocalPorts, allowedThreadCount, blacklistedPackages,
				whitelistedPackages, trustedPackages, threadTrustScope, isThreadGroupCheckDisabled);
	}

	public String shortDesc() {
		return String.format(
				"ASC-Impl [testMethod=%s, executionPath=%s, whitelistedPaths=%s, blacklistedPaths=%s, allowedLocalPorts=%s, trustedPackages=%s]", //$NON-NLS-1$
				testMethod, executionPath, whitelistedPaths, blacklistedPaths, allowedLocalPorts, trustedPackages);
	}
}
