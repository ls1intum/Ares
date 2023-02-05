package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

@API(status = Status.MAINTAINED)
public enum ExceptionHandlingType implements Type {
	/**
	 * All exception handling types
	 */
	ANY(Map.of("Assert-Statement", AssertStmt.class, "Throw-Statement", ThrowStmt.class, "Catch-Statement", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			CatchClause.class)),
	/**
	 * The assert type
	 */
	ASSERT(Map.of("Assert-Statement", AssertStmt.class)), //$NON-NLS-1$
	/**
	 * The throw type
	 */
	THROW(Map.of("Throw-Statement", ThrowStmt.class)), //$NON-NLS-1$
	/**
	 * The try-catch exception handling type
	 */
	CATCH(Map.of("Catch-Statement", CatchClause.class)); //$NON-NLS-1$

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	ExceptionHandlingType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
