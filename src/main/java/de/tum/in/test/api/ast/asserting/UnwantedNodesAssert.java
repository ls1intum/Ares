package de.tum.in.test.api.ast.asserting;

import static de.tum.in.test.api.localization.Messages.localized;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.*;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

import net.jqwik.api.Tuple;

import de.tum.in.test.api.ast.model.UnwantedNode;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.structural.testutils.ClassNameScanner;

/**
 * Checks whole Java files for unwanted nodes
 */
@API(status = Status.MAINTAINED)
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Tuple.Tuple2<String, String>> {

	/**
	 * Provides a path to the build files pom.xml or build.gradle file in case it is
	 * located differently, else it provides an empty optional
	 */
	private final Optional<Optional<Path>> delocatedBuildFile;

	/**
	 * The language level for the Java parser
	 */
	private final Optional<LanguageLevel> level;

	public UnwantedNodesAssert(Tuple.Tuple2<String, String> pathComponents, Optional<Optional<Path>> delocatedBuildFile,
			Optional<LanguageLevel> level) {
		super(pathComponents, UnwantedNodesAssert.class);
		this.delocatedBuildFile = delocatedBuildFile;
		this.level = level;
	}

	/**
	 * Creates an unwanted node assertion object (language level is automatically
	 * set to Java 11 and the path points to a file)
	 *
	 * @param expectedClassName   The class name of the expected class that is
	 *                            currently being searched after.
	 * @param expectedPackageName The package name of the expected class that is
	 *                            currently being searched after.
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThat(String expectedClassName, String expectedPackageName) {
		return new UnwantedNodesAssert(Tuple.of(expectedClassName, expectedPackageName), Optional.empty(),
				Optional.empty());
	}

	/**
	 * Configures the build files to be un-delocated
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asUndelocatedBuildFile() {
		return new UnwantedNodesAssert(actual, Optional.of(Optional.empty()), level);
	}

	/**
	 * Configures the build files to be delocated
	 *
	 * @param path The Path to the delocated build file
	 * @return
	 */
	public UnwantedNodesAssert asDelocatedBuildFile(String path) {
		return new UnwantedNodesAssert(actual, Optional.of(Optional.of(Path.of(path))), level);
	}

	/**
	 * Configures the language level used by the Java parser
	 *
	 * @param level The language level for the Java parser
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withLanguageLevel(LanguageLevel level) {
		return new UnwantedNodesAssert(actual, delocatedBuildFile, Optional.of(level));
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
		if (delocatedBuildFile.isPresent()) {
			if (level.isPresent()) {
				StaticJavaParser.getParserConfiguration().setLanguageLevel(level.get());
				Tuple.Tuple2<String, String> temporaryStorage = prepareClassNameScanner();
				Path packagePath = Path.of("", Arrays.stream(actual.get2().split("\\.")).toArray(String[]::new));
				Path filePath = Path.of(actual.get1());
				Path postAssignmentFolderPath = Path.of(packagePath.toString(), filePath.toString());
				Path fullPath = (new ClassNameScanner(actual.get1(), actual.get2())).getAssignmentFolderName()
						.map(assignmentFolderPath -> Path.of(".", assignmentFolderPath))
						.map(preAssignmentFolderPath -> Path.of(preAssignmentFolderPath.toString(),
								postAssignmentFolderPath.toString()))
						.orElseThrow();
				Optional<String> errorMessage = UnwantedNode.getMessageForUnwantedNodesForAllFilesBelow(fullPath,
						type.getNodeNameNodeMap());
				restoreClassNameScanner(temporaryStorage);
				errorMessage.ifPresent(unwantedNodeMessageForAllJavaFiles -> failWithMessage(
						localized("ast.method.has_no") + "\n" + unwantedNodeMessageForAllJavaFiles));
				return this;
			} else {
				failWithMessage(
						"The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel).");
			}
		} else {
			failWithMessage(
					"The 'delocatedBuildFile' is not set. Please use UnwantedNodesAssert.asUndelocatedBuildFile() or UnwantedNodesAssert.asDelocatedBuildFile(Path).");
		}
		throw new RuntimeException();
	}

	private Tuple.Tuple2<String, String> prepareClassNameScanner() {
		String temporaryStorageForBuildGradlePath = ClassNameScanner.getBuildGradlePath();
		ClassNameScanner.setBuildGradlePath(getPath(true, temporaryStorageForBuildGradlePath));
		String temporaryStorageForPomXmlPath = ClassNameScanner.getPomXmlPath();
		ClassNameScanner.setPomXmlPath(getPath(false, temporaryStorageForPomXmlPath));
		return Tuple.of(temporaryStorageForBuildGradlePath, temporaryStorageForPomXmlPath);
	}

	private void restoreClassNameScanner(Tuple.Tuple2<String, String> temporaryStorage) {
		ClassNameScanner.setBuildGradlePath(temporaryStorage.get1());
		ClassNameScanner.setPomXmlPath(temporaryStorage.get2());
	}

	private String getPath(boolean isGradle, String tempPath) {
		return delocatedBuildFile.flatMap(buildFile -> buildFile.map(path -> {
			if (!path.toAbsolutePath().endsWith(isGradle ? "build.gradle" : "pom.xml")) {
				return Path.of(path.toAbsolutePath().toString(), isGradle ? "build.gradle" : "pom.xml").toString();
			} else {
				return path.toAbsolutePath().toString();
			}
		})).orElse(tempPath);
	}
}
