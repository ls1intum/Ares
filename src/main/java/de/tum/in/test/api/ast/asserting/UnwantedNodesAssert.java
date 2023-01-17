package de.tum.in.test.api.ast.asserting;

import java.nio.file.Path;
import java.util.Optional;

import org.assertj.core.api.AbstractAssert;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

import de.tum.in.test.api.ast.model.UnwantedNode;
import de.tum.in.test.api.ast.type.*;

/**
 * Checks whole Java-Files for unwanted nodes
 */
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Path> {

	/**
	 * The flag, determining whether the path points to a file or to a directory
	 */
	private final boolean assertDirectory;

	/**
	 * The language level on which the JavaParser shall operate
	 */
	private final LanguageLevel level;

	/**
	 * (Designated constructor) Non-statically creates an unwanted node assertion
	 * object
	 *
	 * @param actual          The path from which on the Java-Files are checked
	 * @param assertDirectory A flag, determining whether the path points to a file
	 *                        or to a directory
	 * @param level           The language level on which the JavaParser operates
	 */
	public UnwantedNodesAssert(Path actual, boolean assertDirectory, LanguageLevel level) {
		super(actual, UnwantedNodesAssert.class);
		this.assertDirectory = assertDirectory;
		this.level = level;
	}

	/**
	 * (Convenience constructor) Non-statically creates an unwanted node assertion
	 * object (language level is automatically set to Java 11)
	 *
	 * @param actual          The path from which on the Java-Files are checked
	 * @param assertDirectory A flag, determining whether the path points to a file
	 *                        or to a directory
	 */
	public UnwantedNodesAssert(Path actual, boolean assertDirectory) {
		this(actual, assertDirectory, LanguageLevel.JAVA_11);
	}

	/**
	 * (Convenience constructor) Non-statically creates an unwanted node assertion
	 * object (language level is automatically set to Java 11 and the file/directory
	 * flag is automatically set to file)
	 *
	 * @param actual The path from which on the Java-Files are checked
	 */
	public UnwantedNodesAssert(Path actual) {
		this(actual, false, LanguageLevel.JAVA_11);
	}

	/**
	 * Statically creates an unwanted node assertion object (language level is
	 * automatically set to Java 11 and the file/directory flag is automatically set
	 * to file)
	 *
	 * @param actual The path from which on the Java-Files are checked
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThat(Path actual) {
		return new UnwantedNodesAssert(actual);
	}

	/**
	 * Determines the path to point to a File
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asFile() {
		return new UnwantedNodesAssert(actual, false);
	}

	/**
	 * Determines the path to point to a Directory
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asDirectory() {
		return new UnwantedNodesAssert(actual, true);
	}

	/**
	 * Determines the language level on which the JavaParser operate
	 *
	 * @param level The language level on which the JavaParser shall operate
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withConfiguration(LanguageLevel level) {
		return new UnwantedNodesAssert(actual, assertDirectory, level);
	}

	/**
	 * Checks if the Java-files at and/or below the given path do not contain any
	 * unwanted statement
	 *
	 * @param type Unwanted statements
	 * @return This unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert hasNo(Type type) {
		isNotNull();
		StaticJavaParser.getParserConfiguration().setLanguageLevel(level);
		Optional<String> errorMessage = assertDirectory
				? UnwantedNode.getMessageForUnwantedNodesForAllFilesBelow(this.actual, type.getNodeNameNodeMap())
				: UnwantedNode.getMessageForUnwantedNodesForFileAt(this.actual, type.getNodeNameNodeMap());
		errorMessage.ifPresent(unwantedNodeMessageForAllJavaFiles -> failWithMessage(
				"Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles));
		return this;
	}
}
