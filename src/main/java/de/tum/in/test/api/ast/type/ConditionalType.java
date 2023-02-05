package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

@API(status = Status.MAINTAINED)
public enum ConditionalType implements Type {
	/**
	 * Returns a list with all conditional types
	 */
	ANY(Map.of("If-Statement", IfStmt.class, "Switch-Statement", SwitchStmt.class)),
	/**
	 * Returns the if condition type
	 */
	IF(Map.of("If-Statement", IfStmt.class)),
	/**
	 * Returns the switch condition type
	 */
	SWITCH(Map.of("Switch-Statement", SwitchStmt.class));

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	ConditionalType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
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
