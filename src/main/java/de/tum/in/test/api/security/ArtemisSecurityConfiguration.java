package de.tum.in.test.api.security;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.PackageRule;
import de.tum.in.test.api.util.PathRule;

@API(status = Status.INTERNAL)
public interface ArtemisSecurityConfiguration {

	Optional<Class<?>> testClass();

	Optional<Method> testMethod();

	Path executionPath();

	List<String> whitelistedClassNames();

	String shortDesc();

	Optional<Set<PathRule>> whitelistedPaths();

	Set<PathRule> blacklistedPaths();

	OptionalInt allowedLocalPort();

	OptionalInt allowedThreadCount();

	Set<PackageRule> blacklistedPackages();

	Set<PackageRule> whitelistedPackages();
}
