package de.tum.in.test.api.ast.data;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;

import java.util.Optional;

/**
 * Stores information about the beginning and the end of a node
 */
public class NodePosition {
    /**
     * Indicates whether the node has a beginning
     */
    private final boolean hasBegin;
    /**
     * Position of the node beginning (or null if the node has no beginning)
     */
    private final Pair<Integer, Integer> begin;
    /**
     * Indicates whether the node has an end
     */
    private final boolean hasEnd;
    /**
     * Position of the node end (or null if the node has no end)
     */
    private final Pair<Integer, Integer> end;

    /**
     * Creates the node beginning and end information
     * @param node Node, for which the information about the beginning and the end shall be stored
     */
    public NodePosition(Node node) {
        Optional<Position> nodeBegin = node.getBegin();
        hasBegin = nodeBegin.isPresent();
        begin = hasBegin ? new Pair<>(nodeBegin.get().line, nodeBegin.get().column) : null;
        Optional<Position> nodeEnd = node.getEnd();
        hasEnd = nodeEnd.isPresent();
        end = hasEnd ? new Pair<>(nodeEnd.get().line, nodeEnd.get().column) : null;
    }

    @Override
    public String toString() {
        return "    - " + " Between " +
                (hasBegin ? "line " + begin.a + " (column " + begin.b + ")" : "no begin available") +
                " and " +
                (hasEnd ? "line " + end.a + " (column " + end.b + ")" : "no end available") +
                "\n";
    }
}

