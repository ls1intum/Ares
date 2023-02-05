package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

@API(status = Status.MAINTAINED)
public enum LoopType implements Type {
	/**
	 * Returns a list with all loop types
	 */
	ANY(Map.of("For-Statement", ForStmt.class, "For-Each-Statement", ForEachStmt.class, "While-Statement",
			WhileStmt.class, "Do-While-Statement", DoStmt.class)),
	/**
	 * Returns a list with all for-related loop types
	 */
	ANYFOR(Map.of("For-Statement", ForStmt.class, "For-Each-Statement", ForEachStmt.class)),
	/**
	 * Returns the for loop type
	 */
	FOR(Map.of("For-Statement", ForStmt.class)),
	/**
	 * Returns the for each loop type
	 */
	FOREACH(Map.of("For-Each-Statement", ForEachStmt.class)),
	/**
	 * Returns a list with all while-related loop types
	 */
	ANYWHILE(Map.of("While-Statement", WhileStmt.class, "Do-While-Statement", DoStmt.class)),
	/**
	 * Returns the while loop type
	 */
	WHILE(Map.of("While-Statement", WhileStmt.class)),
	/**
	 * Returns the do while loop type
	 */
	DOWHILE(Map.of("Do-While-Statement", DoStmt.class));

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	LoopType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	/**
	 * Returns the list of all node-name/node-type pairs
	 *
	 * @return List of all node-name/node-type pairs
	 */
	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
