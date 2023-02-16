package de.tum.in.test.api.ast.asserting;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

import de.tum.in.test.api.ast.tool.PathTool;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.*;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

import de.tum.in.test.api.ast.model.UnwantedNode;
import de.tum.in.test.api.ast.type.*;

import static de.tum.in.test.api.localization.Messages.localized;

/**
 * Checks whole Java files for unwanted nodes
 */
@API(status = Status.MAINTAINED)
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Path> {

    /**
     * true when the path is relative to the pom.xml or the build.gradle file, false if it is absolute to the file system's root
     */
    private final Optional<Boolean> relative;

    /**
     * true when the path points to a directory, false if it points to a file
     */
    private final Optional<Boolean> assertDirectory;

    /**
     * The language level for the Java parser
     */
    private final Optional<LanguageLevel> level;

    public UnwantedNodesAssert(Path path, Optional<Boolean> relative, Optional<Boolean> assertDirectory, Optional<LanguageLevel> level) {
        super(path, UnwantedNodesAssert.class);
        this.relative = relative;
        this.assertDirectory = assertDirectory;
        this.level = level;
    }

    /**
     * Creates an unwanted node assertion object (language level is automatically
     * set to Java 11 and the path points to a file)
     *
     * @param path The path from which on the Java files are checked
     * @return An unwanted node assertion object (for chaining)
     */
    public static UnwantedNodesAssert assertThat(Path path) {
        return new UnwantedNodesAssert(path, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Configures the path to be treated as relative to the pom.xml or the build.gradle file
     *
     * @return An unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert asRelative() {
        return new UnwantedNodesAssert(actual, Optional.of(true), assertDirectory, level);
    }

    /**
     * Configures the path to be treated as absolute to the file system's root
     *
     * @return An unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert asAbsolute() {
        return new UnwantedNodesAssert(actual, Optional.of(false), assertDirectory, level);
    }

    /**
     * Configures the path to be treated as a file
     *
     * @return An unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert asFile() {
        return new UnwantedNodesAssert(actual, relative, Optional.of(false), level);
    }

    /**
     * Configures the path to be treated as a directory
     *
     * @return An unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert asDirectory() {
        return new UnwantedNodesAssert(actual, relative, Optional.of(true), level);
    }

    /**
     * Configures the language level used by the Java parser
     *
     * @param level The language level for the Java parser
     * @return An unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert withLanguageLevel(LanguageLevel level) {
        return new UnwantedNodesAssert(actual, relative, assertDirectory, Optional.of(level));
    }

    /**
     * Verifies that the selected Java files do not contain any syntax tree nodes
     * (statements, expressions, ...) of the given type.
     *
     * @param type Unwanted statements
     * @return This unwanted node assertion object (for chaining)
     * @see ClassType
     * @see ConditionalType
     * @see ExceptionHandlingType
     * @see LoopType
     */
    public UnwantedNodesAssert hasNo(Type type) {
        isNotNull();
        try {
            if (relative.isPresent()) {
                if (assertDirectory.isPresent()) {
                    if (level.isPresent()) {
                        StaticJavaParser.getParserConfiguration().setLanguageLevel(level.get());
                        Optional<String> errorMessage = assertDirectory.get()
                                ? UnwantedNode.getMessageForUnwantedNodesForAllFilesBelow(
                                relative.get() ? PathTool.extractPath(this.actual, null) : this.actual,
                                type.getNodeNameNodeMap()
                        )
                                : UnwantedNode.getMessageForUnwantedNodesForFileAt(
                                relative.get() ? PathTool.extractPath(this.actual, null) : this.actual,
                                type.getNodeNameNodeMap()
                        );
                        errorMessage.ifPresent(unwantedNodeMessageForAllJavaFiles -> failWithMessage(
                                localized("ast.method.has_no") + "\n" + unwantedNodeMessageForAllJavaFiles
                        ));
                        return this;
                    } else {
                        failWithMessage("The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel).");
                    }
                } else {
                    failWithMessage("The 'assertDirectory' flag is not set. Please use UnwantedNodesAssert.asFile() or UnwantedNodesAssert.asDirectory().");
                }
            } else {
                failWithMessage("The 'relative' flag is not set. Please use UnwantedNodesAssert.asRelative() or UnwantedNodesAssert.asAbsolute().");
            }
            throw new RuntimeException();

        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
