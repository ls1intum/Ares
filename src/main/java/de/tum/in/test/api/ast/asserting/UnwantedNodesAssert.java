package de.tum.in.test.api.ast.asserting;

import java.nio.file.Path;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.AbstractAssert;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

import de.tum.in.test.api.ast.model.UnwantedNode;
import de.tum.in.test.api.ast.type.Type;

/**
 * Checks whole Java-Files for unwanted nodes
 */
@API(status = Status.MAINTAINED)
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Path> {

	/**
	 * true when the path points to a directory, false if it points to a file
	 */
	private final boolean assertDirectory;

	/**
	 * The language level on which the JavaParser shall operate
	 */
	private final LanguageLevel level;

	/**
	 * Private constructor: Non-statically creates an unwanted node assertion object
	 *
	 * @param actual          The path from which on the Java-Files are checked
	 * @param assertDirectory true when the path points to a directory, false if it
	 *                        points to a file
	 * @param level           The language level on which the JavaParser operates
	 */
	private UnwantedNodesAssert(Path actual, boolean assertDirectory, LanguageLevel level) {
		super(actual, UnwantedNodesAssert.class);
		this.assertDirectory = assertDirectory;
		this.level = level;
	}

	/**
	 * Statically creates an unwanted node assertion object (language level is
	 * automatically set to Java 11 and the path points to a file)
	 *
	 * @param actual The path from which on the Java-Files are checked
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThat(Path actual) {
		return new UnwantedNodesAssert(actual, false, LanguageLevel.JAVA_11);
	}

	/**
	 * Determines the path to point to a File
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asFile() {
		return new UnwantedNodesAssert(actual, false, level);
	}

	/**
	 * Determines the path to point to a Directory
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asDirectory() {
		return new UnwantedNodesAssert(actual, true, level);
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
