package de.tum.in.test.api.ast.data;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;
import de.tum.in.test.api.ast.manager.NodeManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores all unwanted nodes of an abstract syntax tree of a Java-file
 */
public class UnwantedNodesInformation {
    /**
     * List of node-name / node-position pairs of unwanted nodes
     */
    private final List<Pair<String, List<NodePosition>>> nodeNameAndNodePositionsPairsForUnwantedNodes;

    /**
     * Finds all unwanted nodes in an abstract syntax tree of a Java-file
     * @param ast Abstract syntax tree of a Java-file
     * @param nodesDefinedAsUnwanted List of node-name / node-type pairs of nodes, which are unwanted
     */
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

    /**
     * Returns the list of node-name / node-position pairs of unwanted nodes
     * @return List of node-name / node-position pairs of unwanted nodes
     */
    public List<Pair<String, List<NodePosition>>> getNodeNameAndNodePositionsPairsForUnwantedNodes() {
        return nodeNameAndNodePositionsPairsForUnwantedNodes;
    }

}
