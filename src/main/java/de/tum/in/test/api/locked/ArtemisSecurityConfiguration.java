package de.tum.in.test.api.locked;

import java.nio.file.Path;
import java.util.List;

public interface ArtemisSecurityConfiguration {

	Class<?> testClass();

	Path executionPath();

	default List<String> whitelistedClassNames() {
		return List.of();
	}
}
