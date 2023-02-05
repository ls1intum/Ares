package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

@API(status = Status.MAINTAINED)
public enum LoopType implements Type {
	/**
	 * All loop types
	 */
	ANY(Map.of("For-Statement", ForStmt.class, "For-Each-Statement", ForEachStmt.class, "While-Statement", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			WhileStmt.class, "Do-While-Statement", DoStmt.class)), //$NON-NLS-1$
	/**
	 * All for-related loop types
	 */
	ANY_FOR(Map.of("For-Statement", ForStmt.class, "For-Each-Statement", ForEachStmt.class)), //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * The for loop type
	 */
	FOR(Map.of("For-Statement", ForStmt.class)), //$NON-NLS-1$
	/**
	 * The for each loop type
	 */
	FOR_EACH(Map.of("For-Each-Statement", ForEachStmt.class)), //$NON-NLS-1$
	/**
	 * All while-related loop types
	 */
	ANY_WHILE(Map.of("While-Statement", WhileStmt.class, "Do-While-Statement", DoStmt.class)), //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * The while loop type
	 */
	WHILE(Map.of("While-Statement", WhileStmt.class)), //$NON-NLS-1$
	/**
	 * The do while loop type
	 */
	DO_WHILE(Map.of("Do-While-Statement", DoStmt.class)); //$NON-NLS-1$

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	LoopType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
