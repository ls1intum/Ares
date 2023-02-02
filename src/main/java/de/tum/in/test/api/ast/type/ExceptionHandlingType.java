package de.tum.in.test.api.ast.type;

import java.util.Map;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

public enum ExceptionHandlingType implements Type {
	/**
	 * Returns a list with all exception handling types
	 */
	ANY(Map.of("Assert-Statement", AssertStmt.class, "Throw-Statement", ThrowStmt.class, "Catch-Statement",
			CatchClause.class)),
	/**
	 * Returns to assert type
	 */
	ASSERT(Map.of("Assert-Statement", AssertStmt.class)),
	/**
	 * Returns the throw type
	 */
	THROW(Map.of("Throw-Statement", ThrowStmt.class)),
	/**
	 * Returns the try-catch exception handling type
	 */
	CATCH(Map.of("Catch-Statement", CatchClause.class));

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	ExceptionHandlingType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	/**
	 * Returns the list of all node-name/node-type pairs
	 *
	 * @return List of all node-name/node-type pairs
	 */
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
