package de.tum.in.test.api.ast.model;

import com.github.javaparser.ast.Node;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Stores all unwanted nodes of an abstract syntax tree of a Java-file
 */
public class UnwantedNode {

    private final String unwantedNodeName;
    private final List<NodePosition> unwantedNodePositions;

    public UnwantedNode(JavaFile javaFile, String unwantedNodeName, Class<? extends Node> nodeDefinedAsUnwanted) {
        this.unwantedNodeName = unwantedNodeName;
        this.unwantedNodePositions = javaFile
                .getJavaFileAST()
                .findAll(nodeDefinedAsUnwanted)
                .stream()
                .map(NodePosition::getPositionOf)
                .collect(Collectors.toList());
        this.unwantedNodePositions.sort(Comparator.comparingInt(NodePosition::getBeginLine).thenComparing(NodePosition::getBeginColumn));
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
     * @param nodesDefinedAsUnwanted List of unwanted node information (packed into UnwantedNode objects)
     */
    public static List<UnwantedNode> getUnwantedNodesInJavaFile(
            JavaFile javaFile,
            Map<String, Class<? extends Node>> nodesDefinedAsUnwanted
    ) {
        return nodesDefinedAsUnwanted
                .keySet()
                .stream()
                .map(unwantedNodeName -> new UnwantedNode(javaFile, unwantedNodeName, nodesDefinedAsUnwanted.get(unwantedNodeName)))
                .filter(unwantedNode -> !unwantedNode.getUnwantedNodePositions().isEmpty())
                .sorted(Comparator
                        .comparing(uwn -> ((UnwantedNode) uwn).getUnwantedNodePositions().get(0).getBeginLine())
                        .thenComparing(uwn -> ((UnwantedNode) uwn).getUnwantedNodePositions().get(0).getBeginLine())
                )
                .collect(Collectors.toList());
    }

    /**
     * Detects a provided list of unwanted nodes in a Java-File at a given path
     *
     * @param pathOfJavaFile         Path to the Java-File, where unwanted nodes shall be detected
     * @param nodesDefinedAsUnwanted List of unwanted nodes
     * @return Map of File-Paths and their respective list of unwanted node information (packed into UnwantedNode objects)
     */
    public static Map<Path, List<UnwantedNode>> getUnwantedNodesForFileAt(
            Path pathOfJavaFile,
            Map<String, Class<? extends Node>> nodesDefinedAsUnwanted
    ) {
        JavaFile javaFile = JavaFile.getJavaFileFromFileAt(pathOfJavaFile);
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
     * @param positionString Path to the Directory, at and below where unwanted nodes shall be detected
     * @return List of pairs of File-Path and their respective information about unwanted nodes
     * <p>
     * public static Map<Path, List<UnwantedNode>> getUnwantedNodesForFilesBelow(
     * Path pathOfDirectory,
     * Map<String, Class<? extends Node>> nodesDefinedAsUnwanted
     * ) {
     * return JavaFile.getJavaFilesFromFilesBelow(pathOfDirectory)
     * .stream()
     * .map(javaFile -> getUnwantedNodesForFileAt(javaFile.getJavaFilePath(), nodesDefinedAsUnwanted))
     * .filter(map -> !map.isEmpty())
     * .flatMap(map -> map.entrySet().stream())
     * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
     * }
     */

    public static String getFormatedPositionString(String positionString) {
        return "   - " + positionString;
    }

    public static String getFormatedUnwantedNodeString(UnwantedNode unwantedNode) {
        return unwantedNode.getUnwantedNodePositions()
                .stream()
                .map(String::valueOf)
                .map(UnwantedNode::getFormatedPositionString)
                .collect(Collectors.joining(
                        "\n",
                        "  - " + unwantedNode.getUnwantedNodeName() + " was found:\n",
                        ""
                ));
    }

    public static String getFormatedFileString(Path filePath, Map<Path, List<UnwantedNode>> unwantedNodes) {
        return unwantedNodes.get(filePath)
                .stream()
                .map(UnwantedNode::getFormatedUnwantedNodeString)
                .collect(Collectors.joining(
                        "\n",
                        " - In " + filePath + ":\n",
                        ""
                ));
    }


    /**
     * Creates an error message in case unwanted files are detected
     *
     * @param pathOfJavaFile          Path to the Java-File, where unwanted nodes shall be detected
     * @param nodeNameUnwantedNodeMap List of unwanted nodes
     * @return Error message
     */
    public static Optional<String> getMessageForUnwantedNodesForFileAt(
            Path pathOfJavaFile,
            Map<String, Class<? extends Node>> nodeNameUnwantedNodeMap
    ) {
        Map<Path, List<UnwantedNode>> unwantedNodes = getUnwantedNodesForFileAt(pathOfJavaFile, nodeNameUnwantedNodeMap);
        if (unwantedNodes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                unwantedNodes
                        .keySet()
                        .stream()
                        .map(filePath -> getFormatedFileString(filePath, unwantedNodes))
                        .reduce(String::concat)
                        .orElse("")
        );
    }

    /**
     * Creates an error message in case unwanted files are detected
     *
     * @param pathOfDirectory         Path to the Directory, at and below where unwanted nodes shall be detected
     * @param nodeNameUnwantedNodeMap List of unwanted nodes
     * @return Error message
     */
    public static Optional<String> getMessageForUnwantedNodesForAllFilesBelow(
            Path pathOfDirectory,
            Map<String, Class<? extends Node>> nodeNameUnwantedNodeMap
    ) {
        return JavaFile.getJavaFilesFromFilesBelow(pathOfDirectory)
                .stream()
                .sorted(Comparator.comparing(JavaFile::getJavaFilePath))
                .map(javaFile -> getMessageForUnwantedNodesForFileAt(javaFile.getJavaFilePath(), nodeNameUnwantedNodeMap))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(message -> message + "\n")
                .reduce(String::concat)
                .map(String::trim)
                .map(message -> " " + message);
    }

}