package de.tum.in.test.api.ast.manager;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;
import de.tum.in.test.api.ast.data.UnwantedNodesInformation;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnwantedNodeManager {
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

    public static Optional<String>
    getMessageForUnwantedNodesForAllJavaFilesBelow(
            Path path, List<Pair<String, Class<? extends Node>>> unwantedNodeList
    ) {
        return ASTManager.getPathAndASTPairsFromAllJavaFilesBelow(path)
                .stream()
                .map(pathAndAST -> getMessageForUnwantedNodesForJavaFileAt(pathAndAST.a, unwantedNodeList))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(String::concat);
    }
}

