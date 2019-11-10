package de.tum.in.test.api.locked;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;

public interface ArtemisSecurityConfiguration {

	Class<?> testClass();
	
	Method testMethod();

	Path executionPath();

	default List<String> whitelistedClassNames() {
		return List.of();
	}

	String shortDesc();
}
