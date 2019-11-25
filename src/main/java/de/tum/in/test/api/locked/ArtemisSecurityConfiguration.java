package de.tum.in.test.api.locked;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

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

	default Optional<Set<PathMatcher>> whitelistedPaths() {
		return Optional.empty();
	}

	default Set<PathMatcher> blacklistedPaths() {
		return Set.of();
	}
}
