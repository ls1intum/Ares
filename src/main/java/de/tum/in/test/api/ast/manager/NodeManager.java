package de.tum.in.test.api.ast.manager;

import com.github.javaparser.ast.Node;
import de.tum.in.test.api.ast.data.NodePosition;

import java.util.List;
import java.util.stream.Collectors;

public class NodeManager {

    public static NodePosition getPositionOf(Node node) {
        return new NodePosition(node);
    }

    public static List<NodePosition> getPositionsOf(List<? extends Node> nodes) {
        return nodes.stream().map(NodeManager::getPositionOf).collect(Collectors.toList());
    }

    public static List<String> getPositionStringsOf(List<NodePosition> nodePositions) {
        return nodePositions.stream().map(NodePosition::toString).collect(Collectors.toList());
    }
}

