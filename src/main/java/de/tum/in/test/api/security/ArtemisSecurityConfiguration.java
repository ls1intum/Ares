package de.tum.in.test.api.security;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.PathRule;

@API(status = Status.INTERNAL)
public interface ArtemisSecurityConfiguration {

	Class<?> testClass();

	Method testMethod();

	Path executionPath();

	default List<String> whitelistedClassNames() {
		return List.of();
	}

	String shortDesc();

	boolean whitelistFirstThread();

	default Optional<Set<PathRule>> whitelistedPaths() {
		return Optional.empty();
	}

	default Set<PathRule> blacklistedPaths() {
		return Set.of();
	}

	OptionalInt allowedLocalPort();

	OptionalInt allowedThreadCount();
}
