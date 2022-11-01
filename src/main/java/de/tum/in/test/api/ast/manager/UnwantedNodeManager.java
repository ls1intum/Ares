package de.tum.in.test.api.ast.manager;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;
import de.tum.in.test.api.ast.data.UnwantedNodesInformation;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages the detection of unwanted nodes
 */
public class UnwantedNodeManager {
    /**
     * Detects a provided list of unwanted nodes in a Java-File at a given path
     * @param pathOfJavaFile Path to the Java-File, where unwanted nodes shall be detected
     * @param nodesDefinedAsUnwanted List of unwanted nodes
     * @return Pair of File-Path and their respective information about unwanted nodes
     */
    public static Optional<Pair<Path, UnwantedNodesInformation>>
    getPathAndUnwantedNodesInformationPairForUnwantedNodesForJavaFileAt(
            Path pathOfJavaFile, List<Pair<String, Class<? extends Node>>> nodesDefinedAsUnwanted
    ) {
        return ASTManager.getPathAndASTPairFromTheJavaFileAt(pathOfJavaFile)
                .flatMap(pathAndASTPair -> {
                    UnwantedNodesInformation unwantedNodesInformation = new UnwantedNodesInformation(pathAndASTPair.b, nodesDefinedAsUnwanted);
                    if (unwantedNodesInformation.getNodeNameAndNodePositionsPairsForUnwantedNodes().isEmpty()) {
                        return Optional.empty();
                    } else {
                        return Optional.of(new Pair<>(pathOfJavaFile, unwantedNodesInformation));
                    }
                });
    }

    /**
     * Detects a provided list of unwanted nodes in Java-Files below a given path
     * @param pathOfDirectory Path to the Directory, at and below where unwanted nodes shall be detected
     * @param nodesDefinedAsUnwanted List of unwanted nodes
     * @return List of pairs of File-Path and their respective information about unwanted nodes
     */
    public static List<Pair<Path, UnwantedNodesInformation>>
    getPathAndUnwantedNodesInformationPairForUnwantedNodesForJavaFilesBelow(
            Path pathOfDirectory, List<Pair<String, Class<? extends Node>>> nodesDefinedAsUnwanted
    ) {
        return ASTManager.getPathAndASTPairsFromAllJavaFilesBelow(pathOfDirectory)
                .stream()
                .map(pathAndAST -> getPathAndUnwantedNodesInformationPairForUnwantedNodesForJavaFileAt(pathOfDirectory, nodesDefinedAsUnwanted))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    /**
     * Creates an error message in case unwanted files are detected
     * @param pathOfJavaFile Path to the Java-File, where unwanted nodes shall be detected
     * @param nodesDefinedAsUnwanted List of unwanted nodes
     * @return Error message
     */
    public static Optional<String>
    getMessageForUnwantedNodesForJavaFileAt(
            Path pathOfJavaFile, List<Pair<String, Class<? extends Node>>> nodesDefinedAsUnwanted
    ) {
        return getPathAndUnwantedNodesInformationPairForUnwantedNodesForJavaFileAt(pathOfJavaFile, nodesDefinedAsUnwanted)
                .map(pathAndUnwantedNodesInformationPair ->
                        "- In " + pathOfJavaFile + ":\n" + pathAndUnwantedNodesInformationPair.b
                                .getNodeNameAndNodePositionsPairsForUnwantedNodes().stream()
                                .map(nodeNameAndNodePositionsPair ->
                                        "  - " +
                                                nodeNameAndNodePositionsPair.a +
                                                " was found:\n" +
                                                NodeManager.getPositionStringsOf(nodeNameAndNodePositionsPair.b)
                                                        .stream()
                                                        .reduce(String::concat)
                                                        .orElse(""))
                                .reduce(String::concat)
                                .orElse(""));
    }

    /**
     * Creates an error message in case unwanted files are detected
     * @param pathOfDirectory Path to the Directory, at and below where unwanted nodes shall be detected
     * @param unwantedNodeList List of unwanted nodes
     * @return Error message
     */
    public static Optional<String>
    getMessageForUnwantedNodesForAllJavaFilesBelow(
            Path pathOfDirectory, List<Pair<String, Class<? extends Node>>> unwantedNodeList
    ) {
        return ASTManager.getPathAndASTPairsFromAllJavaFilesBelow(pathOfDirectory)
                .stream()
                .map(pathAndAST -> getMessageForUnwantedNodesForJavaFileAt(pathAndAST.a, unwantedNodeList))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(String::concat);
    }
}

