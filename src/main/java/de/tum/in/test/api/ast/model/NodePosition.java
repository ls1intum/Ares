package de.tum.in.test.api.ast.model;

import static de.tum.in.test.api.localization.Messages.localized;

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

	private static final Comparator<NodePosition> COMPARATOR = Comparator.comparing(NodePosition::getBeginLine)
			.thenComparing(NodePosition::getBeginColumn);

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
		return localized("ast.method.to_string",
				(hasBegin ? localized("ast.check.has_begin_end", begin.a, begin.b)
						: localized("ast.check.not_has_begin")),
				(hasEnd ? localized("ast.check.has_begin_end", end.a, end.b) : localized("ast.check.not_has_end")));
	}

	public static NodePosition getPositionOf(Node node) {
		return new NodePosition(node);
	}

	@Override
	public int compareTo(NodePosition o) {
		return COMPARATOR.compare(this, o);
	}
}
