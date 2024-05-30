package de.tum.in.test.api.ast.asserting;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import de.tum.in.test.api.AresConfiguration;
import de.tum.in.test.api.ast.model.RecursionCheck;
import de.tum.in.test.api.util.ProjectSourcesFinder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.AbstractAssert;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.fail;

/**
 * Checks whole Java files for unwanted simple recursion
 *
 * @author Markus Paulsen
 * @version 1.0.0
 * @since 1.12.0
 */
@API(status = Status.MAINTAINED)
public class UnwantedRecursionAssert extends AbstractAssert<UnwantedRecursionAssert, Path> {

    /**
     * The language level for the Java parser
     */
    private final LanguageLevel level;

    /**
     * The method to start the recursion check from
     */
    private final Method startingMethod;

    /**
     * The methods to exclude from the recursion check
     */
    private final Method[] excludedMethods;

    private UnwantedRecursionAssert(Path path, LanguageLevel level, Method startingMethod, Method... excludedMethods) {
        super(requireNonNull(path), UnwantedRecursionAssert.class);
        this.level = level;
        this.startingMethod = startingMethod;
        this.excludedMethods = excludedMethods;
        if (!Files.isDirectory(path)) {
            fail("The source directory %s does not exist", path); //$NON-NLS-1$
        }
    }

    private UnwantedRecursionAssert(Path path, LanguageLevel level) {
        this(path, level, null);
    }

    /**
     * Creates an unwanted simple recursion assertion object for all project source files.
     * <p>
     * The project source directory gets extracted from the build configuration, and
     * a <code>pom.xml</code> or <code>build.gradle</code> in the execution path is
     * the default build configuration location. The configuration here is the same
     * as the one in the structural tests and uses {@link AresConfiguration}.
     *
     * @return An unwanted simple recursion assertion object (for chaining)
     */
    public static UnwantedRecursionAssert assertThatProjectSources() {
        var path = ProjectSourcesFinder.findProjectSourcesPath().orElseThrow(() -> //$NON-NLS-1$
                new AssertionError("Could not find project sources folder." //$NON-NLS-1$
                        + " Make sure the build file is configured correctly." //$NON-NLS-1$
                        + " If it is not located in the execution folder directly," //$NON-NLS-1$
                        + " set the location using AresConfiguration methods.")); //$NON-NLS-1$
        return new UnwantedRecursionAssert(path, null);
    }

    /**
     * Creates an unwanted simple recursion node assertion object for all source files at and below
     * the given directory path.
     *
     * @param directory Path to a directory under which all files are considered
     * @return An unwanted simple recursion assertion object (for chaining)
     */
    public static UnwantedRecursionAssert assertThatSourcesIn(Path directory) {
        Objects.requireNonNull(directory, "The given source path must not be null."); //$NON-NLS-1$
        return new UnwantedRecursionAssert(directory, null);
    }

    /**
     * Creates an unwanted simple recursion assertion object for all source files in the given
     * package, including all of its sub-packages.
     *
     * @param packageName Java package name in the form of, e.g.,
     *                    <code>de.tum.in.test.api</code>, which is resolved
     *                    relative to the path of this UnwantedNodesAssert.
     * @return An unwanted simple recursion assertion object (for chaining)
     * @implNote The package is split at "." with the resulting segments being
     * interpreted as directory structure. So
     * <code>assertThatSourcesIn(Path.of("src/main/java")).withinPackage("net.example.test")</code>
     * will yield an assert for all source files located at and below the
     * relative path <code>src/main/java/net/example/test</code>
     */
    public UnwantedRecursionAssert withinPackage(String packageName) {
        Objects.requireNonNull(packageName, "The package name must not be null."); //$NON-NLS-1$
        var newPath = actual.resolve(Path.of("", packageName.split("\\."))); //$NON-NLS-1$ //$NON-NLS-2$
        return new UnwantedRecursionAssert(newPath, level, startingMethod, excludedMethods);
    }

    /**
     * Configures the language level used by the Java parser
     *
     * @param level The language level for the Java parser
     * @return An unwanted simple recursion assertion object (for chaining)
     */
    public UnwantedRecursionAssert withLanguageLevel(LanguageLevel level) {
        return new UnwantedRecursionAssert(actual, level, startingMethod, excludedMethods);
    }

    /**
     * Configures the method to start the recursion check from
     * @param node The method to start the recursion check from
     * @return An unwanted simple recursion assertion object (for chaining)
     */
    public UnwantedRecursionAssert startingWithMethod(Method node) {
        return new UnwantedRecursionAssert(actual, level, node, excludedMethods);
    }

    public UnwantedRecursionAssert excludeMethods(Method... methods) {
        return new UnwantedRecursionAssert(actual, level, startingMethod, methods);
    }

    /**
     * Verifies that the selected Java files do not contain any recursion.
     *
     * @return This unwanted simple recursion assertion object (for chaining)
     */
    public UnwantedRecursionAssert hasNoRecursion() {
        if (level == null) {
            failWithMessage("The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel)."); //$NON-NLS-1$
        }
        Optional<String> errorMessage = RecursionCheck.hasNoCycle(actual, level, startingMethod, excludedMethods);
        errorMessage.ifPresent(unwantedSimpleRecursionMessageForAllJavaFiles -> failWithMessage(
                "Unwanted recursion found in methods:" + System.lineSeparator() + unwantedSimpleRecursionMessageForAllJavaFiles)); //$NON-NLS-1$
        return this;
    }

    /**
     * Verifies that the selected Java files do contain any recursion.
     *
     * @return This unwanted simple recursion assertion object (for chaining)
     */
    public UnwantedRecursionAssert hasRecursion() {
        if (level == null) {
            failWithMessage("The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel)."); //$NON-NLS-1$
        }
        Optional<String> errorMessage = RecursionCheck.hasCycle(actual, level, startingMethod, excludedMethods);
        errorMessage.ifPresent(unwantedSimpleRecursionMessageForAllJavaFiles -> failWithMessage(
                "Wanted recursion not found:" + System.lineSeparator() + unwantedSimpleRecursionMessageForAllJavaFiles)); //$NON-NLS-1$
        return this;
    }
}


