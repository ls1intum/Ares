package de.tum.in.test.api.ast.asserting;

import java.nio.file.Path;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.*;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;

import de.tum.in.test.api.ast.model.UnwantedNode;
import de.tum.in.test.api.ast.type.*;

/**
 * Checks whole Java files for unwanted nodes
 */
@API(status = Status.MAINTAINED)
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Path> {

	/**
	 * true when the path points to a directory, false if it points to a file
	 */
	private final boolean assertDirectory;

	/**
	 * The language level for the Java parser
	 */
	private final LanguageLevel level;

	private UnwantedNodesAssert(Path actual, boolean assertDirectory, LanguageLevel level) {
		super(actual, UnwantedNodesAssert.class);
		this.assertDirectory = assertDirectory;
		this.level = level;
	}

	/**
	 * Creates an unwanted node assertion object (language level is automatically
	 * set to Java 11 and the path points to a file)
	 *
	 * @param actual The path from which on the Java files are checked
	 * @return An unwanted node assertion object (for chaining)
	 */
	public static UnwantedNodesAssert assertThat(Path actual) {
		return new UnwantedNodesAssert(actual, false, LanguageLevel.JAVA_11);
	}

	/**
	 * Configures the path to be treated as a file
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asFile() {
		return new UnwantedNodesAssert(actual, false, level);
	}

	/**
	 * Configures the path to be treated as a directory
	 *
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert asDirectory() {
		return new UnwantedNodesAssert(actual, true, level);
	}

	/**
	 * Configures the language level used by the Java parser
	 *
	 * @param level The language level for the Java parser
	 * @return An unwanted node assertion object (for chaining)
	 */
	public UnwantedNodesAssert withLanguageLevel(LanguageLevel level) {
		return new UnwantedNodesAssert(actual, assertDirectory, level);
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
		StaticJavaParser.getParserConfiguration().setLanguageLevel(level);
		Optional<String> errorMessage = assertDirectory
				? UnwantedNode.getMessageForUnwantedNodesForAllFilesBelow(this.actual, type.getNodeNameNodeMap())
				: UnwantedNode.getMessageForUnwantedNodesForFileAt(this.actual, type.getNodeNameNodeMap());
		errorMessage.ifPresent(unwantedNodeMessageForAllJavaFiles -> failWithMessage(
				"Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles));
		return this;
	}
}
