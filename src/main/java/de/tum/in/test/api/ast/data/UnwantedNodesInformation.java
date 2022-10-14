package de.tum.in.test.api.ast.data;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;
import de.tum.in.test.api.ast.manager.NodeManager;

import java.util.List;
import java.util.stream.Collectors;

public class UnwantedNodesInformation {
    private final List<Pair<String, List<NodePosition>>> nodeNameAndNodePositionsPairsForUnwantedNodes;

    public UnwantedNodesInformation(CompilationUnit ast, List<Pair<String, Class<? extends Node>>> nodesDefinedAsUnwanted) {
        nodeNameAndNodePositionsPairsForUnwantedNodes = nodesDefinedAsUnwanted.stream()
                .map(nodeDefinedAsUnwanted -> {
                    String nodeNameOfNodeDefinedAsUnwanted = nodeDefinedAsUnwanted.a;
                    Class<? extends Node> nodeClassOfNodeDefinedAsUnwanted = nodeDefinedAsUnwanted.b;
                    List<NodePosition> nodePositionsForNodeDefinedAsUnwanted = NodeManager.getPositionsOf(ast.findAll(nodeClassOfNodeDefinedAsUnwanted));
                    return new Pair<>(nodeNameOfNodeDefinedAsUnwanted, nodePositionsForNodeDefinedAsUnwanted);
                })
                .filter(nodeNameAndNodePositionsPairForUnwantedNodes ->
                        !nodeNameAndNodePositionsPairForUnwantedNodes.b.isEmpty()
                )
                .collect(Collectors.toList());
    }

    public List<Pair<String, List<NodePosition>>> getNodeNameAndNodePositionsPairsForUnwantedNodes() {
        return nodeNameAndNodePositionsPairsForUnwantedNodes;
    }

}
