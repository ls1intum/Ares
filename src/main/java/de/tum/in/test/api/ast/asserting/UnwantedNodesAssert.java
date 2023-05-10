package de.tum.in.test.api.ast.asserting;

import static de.tum.in.test.api.localization.Messages.localized;

import java.nio.file.Files;
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
	 * located differently than in the root folder, else it provides null
	 */
	private final Path delocatedBuildFile;

	/**
	 * True in case the pom.xml is located differently than in the root folder, else
	 * false
	 */
	private final boolean pomXmlIsDelocated;

	/**
	 * True in case the build.gradle is located differently than in the root folder,
	 * else false
	 */
	private final boolean buildGradleIsDelocated;

	/**
	 * The language level for the Java parser
	 */
	private final LanguageLevel level;

	public UnwantedNodesAssert(Tuple.Tuple2<String, String> pathComponents, Path delocatedBuildFile,
			boolean pomXmlIsDelocated, boolean buildGradleIsDelocated, LanguageLevel level) {
		super(pathComponents, UnwantedNodesAssert.class);
		this.delocatedBuildFile = delocatedBuildFile;
		this.pomXmlIsDelocated = pomXmlIsDelocated;
		this.buildGradleIsDelocated = buildGradleIsDelocated;
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
		return new UnwantedNodesAssert(Tuple.of(expectedClassName, expectedPackageName), null, false, false, null);
	}

	/**
	 * Configures the build files to be un-delocated
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withoutDelocatedBuildFile() {
		return new UnwantedNodesAssert(actual, Path.of(""), false, false, level);
	}

	/**
	 * Configures the build files to be delocated
	 *
	 * @param path The Path to the delocated build file
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withDelocatedPomXmlFile(String path) {
		return new UnwantedNodesAssert(actual, Path.of(path), true, false, level);
	}

	/**
	 * Configures the build files to be delocated
	 *
	 * @param path The Path to the delocated build file
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withDelocatedBuildGradleFile(String path) {
		return new UnwantedNodesAssert(actual, path != null ? Path.of(path) : null, false, true, level);
	}

	/**
	 * Configures the language level used by the Java parser
	 *
	 * @param level The language level for the Java parser
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withLanguageLevel(LanguageLevel level) {
		return new UnwantedNodesAssert(actual, delocatedBuildFile, pomXmlIsDelocated, buildGradleIsDelocated, level);
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
		if (actual.get1() != null) {
			if (actual.get2() != null) {
				if (delocatedBuildFile != null) {
					if (!(pomXmlIsDelocated && buildGradleIsDelocated)) {
						if (Files.exists(delocatedBuildFile)) {
							Path fullPath = getFullPath();
							if (Files.exists(fullPath)) {
								if (level != null) {
									StaticJavaParser.getParserConfiguration().setLanguageLevel(level);
									Optional<String> errorMessage = UnwantedNode
											.getMessageForUnwantedNodesForAllFilesBelow(fullPath,
													type.getNodeNameNodeMap());
									errorMessage.ifPresent(unwantedNodeMessageForAllJavaFiles -> failWithMessage(
											localized("ast.method.has_no") + System.lineSeparator()
													+ unwantedNodeMessageForAllJavaFiles));
									return this;
								} else {
									failWithMessage(
											"The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel).");
								}
							} else {
								failWithMessage("The path " + getFullPath() + " (resulting from the expectedClassName "
										+ actual.get1() + " and the expectedPackageName " + actual.get2()
										+ ") does not exist.");
							}
						} else {
							failWithMessage("The delocatedBuildFile " + delocatedBuildFile + " does not exist.");
						}
					} else {
						failWithMessage("Either the pom.xml or the build.gradle can be delocated, but not both.");
					}
				} else {
					failWithMessage(
							"The 'delocatedBuildFile' is not set. Please use UnwantedNodesAssert.withoutDelocatedBuildFile() or UnwantedNodesAssert.withDelocatedPomXmlFile(Path) or UnwantedNodesAssert.withDelocatedBuildGradleFile(Path).");
				}
			} else {
				failWithMessage("The 'expectedClassName' must not be null.");
			}
		} else {
			failWithMessage("The 'expectedPackageName' must not be null.");
		}

		throw new RuntimeException();
	}

	private Path getFullPath() {
		Tuple.Tuple2<String, String> temporaryStorage = prepareClassNameScanner();
		Path packagePath = Path.of("", Arrays.stream(actual.get2().split("\\.")).toArray(String[]::new));
		Path filePath = Path.of(actual.get1());
		Path postAssignmentFolderPath = Path.of(packagePath.toString(), filePath.toString());
		Path fullPath = (new ClassNameScanner(actual.get1(), actual.get2())).getAssignmentFolderName()
				.map(assignmentFolderPath -> Path.of(".", assignmentFolderPath)).map(preAssignmentFolderPath -> Path
						.of(preAssignmentFolderPath.toString(), postAssignmentFolderPath.toString()))
				.orElseThrow();
		restoreClassNameScanner(temporaryStorage);
		return fullPath;
	}

	private Tuple.Tuple2<String, String> prepareClassNameScanner() {
		String temporaryStorageForPomXmlPath = ClassNameScanner.getPomXmlPath();
		ClassNameScanner.setPomXmlPath(
				delocatedBuildFile != null ? (pomXmlIsDelocated ? delocatedBuildFile.toAbsolutePath().toString() : null)
						: temporaryStorageForPomXmlPath);
		String temporaryStorageForBuildGradlePath = ClassNameScanner.getBuildGradlePath();
		ClassNameScanner.setBuildGradlePath(delocatedBuildFile != null
				? (buildGradleIsDelocated ? delocatedBuildFile.toAbsolutePath().toString() : null)
				: temporaryStorageForPomXmlPath);
		return Tuple.of(temporaryStorageForBuildGradlePath, temporaryStorageForPomXmlPath);
	}

	private void restoreClassNameScanner(Tuple.Tuple2<String, String> temporaryStorage) {
		ClassNameScanner.setBuildGradlePath(temporaryStorage.get1());
		ClassNameScanner.setPomXmlPath(temporaryStorage.get2());
	}
}
