package de.tum.in.test.api.ast.model;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Stores all required information about a Java file to be analysed
 */
public class JavaFile {
	private static final PathMatcher JAVAFILEMATCHER = FileSystems.getDefault().getPathMatcher("glob:*.java");
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private final Path javaFilePath;
	private final CompilationUnit javaFileAST;

	public JavaFile(Path javaFilePath, CompilationUnit javaFileAST) {
		this.javaFilePath = javaFilePath;
		this.javaFileAST = javaFileAST;
	}

	public Path getJavaFilePath() {
		return javaFilePath;
	}

	public CompilationUnit getJavaFileAST() {
		return javaFileAST;
	}

	/**
	 * Turns the Java-file into an AST in case the provided path points to a
	 * Java.-file
	 *
	 * @param pathOfFile Path to the Java-file
	 * @return The information of the Java-file packed into a JavaFile object (null
	 *         if the file is not a Java-file)
	 */
	public static JavaFile convertFromFile(Path pathOfFile) {
		if (!JAVAFILEMATCHER.matches(pathOfFile.getFileName())) {
			return null;
		}
		try {
			return new JavaFile(pathOfFile, StaticJavaParser.parse(pathOfFile));
		} catch (IOException e) {
			LOGGER.severe(
					"IOException in JavaFile.convertFromFile(" + pathOfFile.toAbsolutePath() + "): " + e.getMessage());
			return null;
		}
	}

	/**
	 * Turns all Java-file below a certain path into ASTs
	 *
	 * @param pathOfDirectory Path to the highest analysis level
	 * @return List of Java-file information packed into JavaFile objects (empty
	 *         list if the directory does not exist or if none of the files in the
	 *         directory or its subdirectories is a Java-file)
	 */
	public static List<JavaFile> readFromDirectory(Path pathOfDirectory) {
		try (Stream<Path> directoryContentStream = Files.walk(pathOfDirectory)) {
			return directoryContentStream.map(JavaFile::convertFromFile).filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (IOException e) {
			LOGGER.severe("IOException in JavaFile.convertFromDirectory(" + pathOfDirectory.toAbsolutePath() + "): "
					+ e.getMessage());
			return List.of();
		}
	}
}
