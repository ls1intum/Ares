package de.tum.in.test.api.ast.type;

import static de.tum.in.test.api.localization.Messages.localized;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;

/**
 * Enumerates exception handling Java constructs which can be checked using
 * {@link UnwantedNodesAssert}.
 *
 * @author Markus Paulsen
 * @since 1.12.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
public enum ExceptionHandlingType implements Type {
	/**
	 * All exception handling types
	 */
	ANY(Map.of(localized("ast.enum.exception_handling_type.assert"), AssertStmt.class, //$NON-NLS-1$
			localized("ast.enum.exception_handling_type.throw"), ThrowStmt.class, //$NON-NLS-1$
			localized("ast.enum.exception_handling_type.catch"), //$NON-NLS-1$
			CatchClause.class)),
	/**
	 * The assert type (statements of the form: "assert" + condition: boolean +
	 * errorMessage: String)
	 */
	ASSERT(Map.of(localized("ast.enum.exception_handling_type.assert"), AssertStmt.class)), //$NON-NLS-1$
	/**
	 * The throw type (statements of the form: "throw" + exception: Exception)
	 */
	THROW(Map.of(localized("ast.enum.exception_handling_type.throw"), ThrowStmt.class)), //$NON-NLS-1$
	/**
	 * The catch type (statements of the form: "catch (" + exception declaration +
	 * ")" + statement)
	 */
	CATCH(Map.of(localized("ast.enum.exception_handling_type.catch"), CatchClause.class)); //$NON-NLS-1$

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	ExceptionHandlingType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
