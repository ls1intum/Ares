package de.tum.in.test.api.ast.data;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;

import java.util.Optional;

public class NodePosition {
    private final boolean hasBegin;
    private final Pair<Integer, Integer> begin;
    private final boolean hasEnd;
    private final Pair<Integer, Integer> end;

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

