package de.tum.in.test.api.ast.model;

import static de.tum.in.test.api.localization.Messages.localized;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;

/**
 * Stores all unwanted nodes of an abstract syntax tree of a Java-file
 */
@API(status = Status.INTERNAL)
public class UnwantedNode {

	private final String unwantedNodeName;
	private final List<NodePosition> unwantedNodePositions;

	public UnwantedNode(JavaFile javaFile, String unwantedNodeName, Class<? extends Node> nodeDefinedAsUnwanted) {
		this.unwantedNodeName = unwantedNodeName;
		this.unwantedNodePositions = javaFile.getJavaFileAST().findAll(nodeDefinedAsUnwanted).stream()
				.map(NodePosition::getPositionOf).collect(Collectors.toList());
		this.unwantedNodePositions.sort(NodePosition::compareTo);
	}

	public String getUnwantedNodeName() {
		return unwantedNodeName;
	}

	public List<NodePosition> getUnwantedNodePositions() {
		return unwantedNodePositions;
	}

	/**
	 * Finds all unwanted nodes in an abstract syntax tree of a Java-file
	 *
	 * @param javaFile               Abstract syntax tree of a Java-file
	 * @param nodesDefinedAsUnwanted List of unwanted node information (packed into
	 *                               UnwantedNode objects)
	 */
	public static List<UnwantedNode> getUnwantedNodesInJavaFile(JavaFile javaFile,
			Map<String, Class<? extends Node>> nodesDefinedAsUnwanted) {
		return nodesDefinedAsUnwanted.keySet().stream()
				.map(unwantedNodeName -> new UnwantedNode(javaFile, unwantedNodeName,
						nodesDefinedAsUnwanted.get(unwantedNodeName)))
				.filter(unwantedNode -> !unwantedNode.getUnwantedNodePositions().isEmpty())
				.sorted(Comparator.comparing(uwn -> uwn.getUnwantedNodePositions().get(0)))
				.collect(Collectors.toList());
	}

	/**
	 * Detects a provided list of unwanted nodes in a Java-File at a given path
	 *
	 * @param pathOfJavaFile         Path to the Java-File, where unwanted nodes
	 *                               shall be detected
	 * @param nodesDefinedAsUnwanted List of unwanted nodes
	 * @return Map of File-Paths and their respective list of unwanted node
	 *         information (packed into UnwantedNode objects)
	 */
	public static Map<Path, List<UnwantedNode>> getUnwantedNodesForFileAt(Path pathOfJavaFile,
			Map<String, Class<? extends Node>> nodesDefinedAsUnwanted) {
		JavaFile javaFile = JavaFile.convertFromFile(pathOfJavaFile);
		if (javaFile == null) {
			return Map.of();
		}
		List<UnwantedNode> unwantedNodes = getUnwantedNodesInJavaFile(javaFile, nodesDefinedAsUnwanted);
		if (unwantedNodes.isEmpty()) {
			return Map.of();
		}
		return Map.of(pathOfJavaFile, unwantedNodes);
	}

	/**
	 * Detects a provided list of unwanted nodes in Java-Files below a given path
	 *
	 * @param positionString Path to the Directory, at and below where unwanted
	 *                       nodes shall be detected
	 * @return List of pairs of File-Path and their respective information about
	 *         unwanted nodes
	 */

	public static String getFormattedPositionString(String positionString) {
		return "   - " + positionString;
	}

	public static String getFormattedUnwantedNodeString(UnwantedNode unwantedNode) {
		return unwantedNode
				.getUnwantedNodePositions().stream().map(String::valueOf).map(
						UnwantedNode::getFormattedPositionString)
				.collect(Collectors.joining(System.lineSeparator(),
						localized("ast.method.get_formatted_unwanted_node_string_prefix",
								unwantedNode.getUnwantedNodeName()) + System.lineSeparator(),
						""));
	}

	public static String getFormattedFileString(Path filePath, Map<Path, List<UnwantedNode>> unwantedNodes) {
		return unwantedNodes.get(filePath).stream().map(UnwantedNode::getFormattedUnwantedNodeString)
				.collect(Collectors.joining(System.lineSeparator(),
						localized("ast.method.get_formatted_file_string_prefix", filePath) + System.lineSeparator(),
						""));
	}

	/**
	 * Creates an error message in case unwanted files are detected
	 *
	 * @param pathOfJavaFile          Path to the Java-File, where unwanted nodes
	 *                                shall be detected
	 * @param nodeNameUnwantedNodeMap List of unwanted nodes
	 * @return Error message
	 */
	public static Optional<String> getMessageForUnwantedNodesForFileAt(Path pathOfJavaFile,
			Map<String, Class<? extends Node>> nodeNameUnwantedNodeMap) {
		Map<Path, List<UnwantedNode>> unwantedNodes = getUnwantedNodesForFileAt(pathOfJavaFile,
				nodeNameUnwantedNodeMap);
		if (unwantedNodes.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(unwantedNodes.keySet().stream()
				.map(filePath -> getFormattedFileString(filePath, unwantedNodes)).reduce(String::concat).orElse(""));
	}

	/**
	 * Creates an error message in case unwanted files are detected
	 *
	 * @param pathOfDirectory         Path to the Directory, at and below where
	 *                                unwanted nodes shall be detected
	 * @param nodeNameUnwantedNodeMap List of unwanted nodes
	 * @return Error message
	 */
	public static Optional<String> getMessageForUnwantedNodesForAllFilesBelow(Path pathOfDirectory,
			Map<String, Class<? extends Node>> nodeNameUnwantedNodeMap) {
		return JavaFile.readFromDirectory(pathOfDirectory).stream()
				.sorted(Comparator.comparing(JavaFile::getJavaFilePath))
				.map(javaFile -> getMessageForUnwantedNodesForFileAt(javaFile.getJavaFilePath(),
						nodeNameUnwantedNodeMap))
				.filter(Optional::isPresent).map(Optional::get).map(message -> message + System.lineSeparator())
				.reduce(String::concat).map(String::trim).map(message -> " " + message);
	}
}
