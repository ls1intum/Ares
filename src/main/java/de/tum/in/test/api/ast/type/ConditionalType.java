package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

@API(status = Status.MAINTAINED)
public enum ConditionalType implements Type {
	/**
	 * All conditional types
	 */
	ANY(Map.of("If-Statement", IfStmt.class, "Switch-Statement", SwitchStmt.class)), //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * The if condition type
	 */
	IF(Map.of("If-Statement", IfStmt.class)), //$NON-NLS-1$
	/**
	 * The switch condition type
	 */
	SWITCH(Map.of("Switch-Statement", SwitchStmt.class)); //$NON-NLS-1$

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	ConditionalType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
