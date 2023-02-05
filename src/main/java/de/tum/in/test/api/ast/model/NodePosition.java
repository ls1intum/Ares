package de.tum.in.test.api.ast.model;

import java.util.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Pair;

/**
 * Stores information about the beginning and the end of a node
 */
@API(status = Status.INTERNAL)
public class NodePosition implements Comparable<NodePosition> {

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

	public int getBeginLine() {
		return begin.a;
	}

	public int getBeginColumn() {
		return begin.b;
	}

	@Override
	public String toString() {
		return "Between " + (hasBegin ? "line " + begin.a + " (column " + begin.b + ")" : "no begin available")
				+ " and " + (hasEnd ? "line " + end.a + " (column " + end.b + ")" : "no end available");
	}

	public static NodePosition getPositionOf(Node node) {
		return new NodePosition(node);
	}

	@Override
	public int compareTo(NodePosition o) {
		return Comparator.comparing(NodePosition::getBeginLine).thenComparing(NodePosition::getBeginColumn)
				.compare(this, o);
	}
}
