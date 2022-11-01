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

import static com.github.javaparser.ParserConfiguration.LanguageLevel.*;


/**
 * Manages the creation of ASTs
 */
public class ASTManager {

    /**
     * Path-matcher for Java-files
     */
    private static final PathMatcher javaFileMatcher = FileSystems.getDefault().getPathMatcher("glob:*.java");

    /**
     * Turns the Java-file into an AST in case the provided path points to a Java.-file
     * @param pathOfFile Path to the Java-file
     * @return The pair of the provided path and the AST as an optional (null if the file is not a Java-file)
     */
    public static Optional<Pair<Path, CompilationUnit>> getPathAndASTPairFromTheJavaFileAt(Path pathOfFile) {
        // Sets parser level to Java 17
        StaticJavaParser.getConfiguration().setLanguageLevel(JAVA_17);
        return Optional.of(javaFileMatcher.matches(pathOfFile.getFileName()))
                .flatMap(matches -> {
                    if (matches) {
                        try {
                            return Optional.of(new Pair<>(pathOfFile, StaticJavaParser.parse(pathOfFile)));
                        } catch (IOException e) {
                            System.err.println("IOException in ASTManager.getPathAndASTPairFromTheJavaFileAt(" + pathOfFile.toAbsolutePath() + "): " + e.getMessage());
                            return Optional.empty();
                        }
                    } else {
                        return Optional.empty();
                    }
                });
    }

    /**
     * Turns all Java-file below a certain path into ASTs
     * @param pathOfDirectory Path to the highest analysis level
     * @return List of all pairs of Java-files and the respective AST
     */
    public static List<Pair<Path, CompilationUnit>> getPathAndASTPairsFromAllJavaFilesBelow(Path pathOfDirectory) {
        try (Stream<Path> directoryContentStream = Files.walk(pathOfDirectory)) {
            return directoryContentStream
                    .map(ASTManager::getPathAndASTPairFromTheJavaFileAt)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("IOException in ASTManager.getPathAndASTPairsFromAllJavaFilesBelow(" + pathOfDirectory.toAbsolutePath() + "): " + e.getMessage());
            return List.of();
        }
    }
}


