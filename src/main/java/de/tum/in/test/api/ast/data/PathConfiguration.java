package de.tum.in.test.api.ast.data;

import java.nio.file.Path;

public class PathConfiguration {
    private final Path path;

    public PathConfiguration(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
