package de.tum.in.test.api.ast.manager;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.Pair;


public class ASTManager {

    private static final PathMatcher javaFileMatcher = FileSystems.getDefault().getPathMatcher("glob:*.java");

    public static Optional<Pair<Path, CompilationUnit>> getPathAndASTPairFromTheJavaFileAt(Path pathOfFile) {
        return Optional.of(javaFileMatcher.matches(pathOfFile.getFileName()))
                .flatMap(matches -> {
                    if (matches) {
                        try {
                            return Optional.of(new Pair<>(pathOfFile, StaticJavaParser.parse(pathOfFile)));
                        } catch (IOException e) {
                            System.err.println("Error in ASTManager.getPathAndASTPairFromTheJavaFileAt: " + e);
                            return Optional.empty();
                        }
                    } else {
                        return Optional.empty();
                    }
                });
    }

    public static List<Pair<Path, CompilationUnit>> getPathAndASTPairsFromAllJavaFilesBelow(Path pathOfDirectory) {
        try (Stream<Path> directoryContentStream = Files.walk(pathOfDirectory)) {
            return directoryContentStream
                    .map(ASTManager::getPathAndASTPairFromTheJavaFileAt)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error in ASTManager.getPathAndASTPairsFromAllJavaFilesBelow: " + e);
            return List.of();
        }
    }
}


