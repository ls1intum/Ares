package de.tum.in.test.api.ast.manager;

import com.github.javaparser.ast.Node;
import de.tum.in.test.api.ast.data.NodePosition;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the NodePosition creation for nodes
 */
public class NodeManager {

    /**
     * Determines the NodePosition for a node
     * @param node Node, for which the NodePosition shall be determined
     * @return NodePosition for the provided node
     */
    public static NodePosition getPositionOf(Node node) {
        return new NodePosition(node);
    }

    /**
     * Determines the NodePositions for a list of node
     * @param nodes List of nodes, for which the NodePositions shall be determined
     * @return List of NodePositions for the provided list of nodes
     */
    public static List<NodePosition> getPositionsOf(List<? extends Node> nodes) {
        return nodes.stream().map(NodeManager::getPositionOf).collect(Collectors.toList());
    }

    /**
     * Determines the NodePositions' textual description for a list of node
     * @param nodePositions List of nodes, for which the NodePositions' textual description shall be determined
     * @return NodePositions' textual description for a list of node
     */
    public static List<String> getPositionStringsOf(List<NodePosition> nodePositions) {
        return nodePositions.stream().map(NodePosition::toString).collect(Collectors.toList());
    }
}

