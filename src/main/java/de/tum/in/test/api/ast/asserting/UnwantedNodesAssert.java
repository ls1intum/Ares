package de.tum.in.test.api.ast.asserting;

import static de.tum.in.test.api.localization.Messages.localized;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.*;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

import de.tum.in.test.api.ast.model.UnwantedNode;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.structural.testutils.ClassNameScanner;

/**
 * Checks whole Java files for unwanted nodes
 */
@API(status = Status.MAINTAINED)
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Path> {

	/**
	 * The language level for the Java parser
	 */
	private final LanguageLevel level;

	private UnwantedNodesAssert(Path path, LanguageLevel level) {
		super(path, UnwantedNodesAssert.class);
		this.level = level;
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param delocatedBuildFile Path to a delocated build file (or '' if the build
	 *                           file is not delocated), which describes part of the
	 *                           relative path
	 * @param relativePackage    The part of the relative path, which is described
	 *                           by the package
	 * @param relativePath       The part of the relative path, which is described
	 *                           by the file path
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatProjectSources(String delocatedBuildFile, String relativePackage,
			String relativePath) {
		UnwantedNodesAssert empty = (new UnwantedNodesAssert(null, null));
		if (delocatedBuildFile == null) {
			empty.failWithMessage("The 'delocatedBuildFile' must not be null.");
		}
		if (relativePackage == null) {
			empty.failWithMessage("The 'relativePackage' must not be null.");
		}
		if (relativePath == null) {
			empty.failWithMessage("The 'relativePath' must not be null.");
		}
		assert delocatedBuildFile != null;
		if (!delocatedBuildFile.equals("")) {
			Path delocatedBuildFilePath = Path.of(delocatedBuildFile);
			if (!Files.exists(delocatedBuildFilePath)) {
				empty.failWithMessage("The 'delocatedBuildFile' " + delocatedBuildFile + " does not exist.");
			}
			if (Files.isDirectory(delocatedBuildFilePath)) {
				empty.failWithMessage("The 'delocatedBuildFile' " + delocatedBuildFile + " must not be a directory.");
			}
			if (!delocatedBuildFilePath.endsWith("pom.xml") && !delocatedBuildFilePath.endsWith("build.gradle")) {
				empty.failWithMessage("The 'delocatedBuildFile' must be a 'pom.xml' or a 'build.gradle' file.");
			}
		}
		Path fullPath = getFullPath(delocatedBuildFile, relativePackage, relativePath);
		if (!Files.exists(fullPath)) {
			empty.failWithMessage("The path " + fullPath + " (resulting from the relativePackage " + relativePackage
					+ ", the relativePath " + relativePath + " and the delocatedBuildFile " + delocatedBuildFile
					+ ") does not exist.");
		}

		return new UnwantedNodesAssert(fullPath, null);
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param relativePackage The part of the relative path, which is described by
	 *                        the package
	 * @param relativePath    The part of the relative path, which is described by
	 *                        the file path
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatProjectSources(String relativePackage, String relativePath) {
		return UnwantedNodesAssert.assertThatProjectSources("", relativePackage, relativePath);
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param delocatedBuildFile Path to a delocated build file (or '' if the build
	 *                           file is not delocated), which describes part of the
	 *                           relative path
	 * @param relativePackage    The part of the relative path, which is described
	 *                           by the package
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatAllProjectSourcesAtPackage(String delocatedBuildFile,
			String relativePackage) {
		return UnwantedNodesAssert.assertThatProjectSources(delocatedBuildFile, relativePackage, "");
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param relativePackage The part of the relative path, which is described by
	 *                        the package
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatAllProjectSourcesAtPackage(String relativePackage) {
		return UnwantedNodesAssert.assertThatProjectSources("", relativePackage, "");
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param delocatedBuildFile Path to a delocated build file (or '' if the build
	 *                           file is not delocated), which describes part of the
	 *                           relative path
	 * @param relativePath       The part of the relative path, which is described
	 *                           by the file path
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatAllProjectSourcesAtPath(String delocatedBuildFile,
			String relativePath) {
		return UnwantedNodesAssert.assertThatProjectSources(delocatedBuildFile, "", relativePath);
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param relativePath The part of the relative path, which is described by the
	 *                     file path
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatAllProjectSourcesAtPath(String relativePath) {
		return UnwantedNodesAssert.assertThatProjectSources("", "", relativePath);
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @param delocatedBuildFile Path to a delocated build file (or '' if the build
	 *                           file is not delocated), which describes part of the
	 *                           relative path
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatAllProjectSources(String delocatedBuildFile) {
		return UnwantedNodesAssert.assertThatProjectSources(delocatedBuildFile, "", "");
	}

	/**
	 * Creates an unwanted node assertion object for all files below the relative
	 * path
	 * 
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThatAllProjectSources() {
		return UnwantedNodesAssert.assertThatProjectSources("", "", "");
	}

	/**
	 * Creates an unwanted node assertion object for all files below the absolute
	 * path
	 * 
	 * @param absolutePath Path under which all files are considered
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThat(String absolutePath) {
		return new UnwantedNodesAssert(Path.of(absolutePath), null);
	}

	/**
	 * Configures the language level used by the Java parser
	 *
	 * @param level The language level for the Java parser
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withLanguageLevel(LanguageLevel level) {
		return new UnwantedNodesAssert(actual, level);
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
		if (level == null) {
			failWithMessage("The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel).");
		}
		StaticJavaParser.getParserConfiguration().setLanguageLevel(level);
		Optional<String> errorMessage = UnwantedNode.getMessageForUnwantedNodesForAllFilesBelow(actual,
				type.getNodeNameNodeMap());
		errorMessage.ifPresent(unwantedNodeMessageForAllJavaFiles -> failWithMessage(
				localized("ast.method.has_no") + System.lineSeparator() + unwantedNodeMessageForAllJavaFiles));
		return this;
	}

	private static Path getFullPath(String delocatedBuildFile, String relativePackage, String relativePath) {
		String oldPomXmlPath = ClassNameScanner.getPomXmlPath();
		String oldBuildGradlePath = ClassNameScanner.getBuildGradlePath();
		if (!delocatedBuildFile.equals("")) {
			if (delocatedBuildFile.endsWith("pom.xml")) {
				ClassNameScanner.setPomXmlPath(delocatedBuildFile);
				ClassNameScanner.setBuildGradlePath(null);
			} else {
				ClassNameScanner.setPomXmlPath(null);
				ClassNameScanner.setBuildGradlePath(delocatedBuildFile);
			}
		}
		Path fullPath = (new ClassNameScanner(relativePath, relativePackage)).getAssignmentFolderName()
				.map(assignmentFolderName -> Path.of(".", assignmentFolderName,
						String.join(File.separator, relativePackage.split("\\.")), relativePath))
				.orElseThrow();
		if (!delocatedBuildFile.equals("")) {
			ClassNameScanner.setBuildGradlePath(oldPomXmlPath);
			ClassNameScanner.setPomXmlPath(oldBuildGradlePath);
		}
		return fullPath;
	}
}
