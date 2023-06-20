package de.tum.in.test.api.ast.type;

import static de.tum.in.test.api.localization.Messages.localized;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;

import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;

/**
 * Enumerates all conditional Java statements and expressions which can be
 * checked using {@link UnwantedNodesAssert}.
 *
 * @author Markus Paulsen
 * @since 1.12.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
public enum ConditionalType implements Type {
	/**
	 * All conditional types
	 */
	ANY(Map.of(localized("ast.enum.conditional_type.if"), IfStmt.class, //$NON-NLS-1$
			localized("ast.enum.conditional_type.conditional_expression"), ConditionalExpr.class, //$NON-NLS-1$
			localized("ast.enum.conditional_type.switch"), SwitchStmt.class,
			localized("ast.enum.conditional_type.switch_expression"), SwitchExpr.class)),
	/**
	 * All if-related types
	 */
	ANY_IF(Map.of(localized("ast.enum.conditional_type.if"), IfStmt.class,
			localized("ast.enum.conditional_type.conditional_expression"), ConditionalExpr.class)),
	/**
	 * The if statement type (statements of the form: "if (" + condition + ")" +
	 * statement)
	 */
	IFSTMT(Map.of(localized("ast.enum.conditional_type.if"), IfStmt.class)), //$NON-NLS-1$
	/**
	 * The conditional expression type (expression of the form: condition + "?" +
	 * expression + ":" + expression)
	 */
	CONDITIONALEXPR(Map.of(localized("ast.enum.conditional_type.conditional_expression"), ConditionalExpr.class)),
	/**
	 * All switch-related types
	 */
	ANY_SWITCH(Map.of(localized("ast.enum.conditional_type.switch"), SwitchStmt.class,
			localized("ast.enum.conditional_type.switch_expression"), SwitchExpr.class)),
	/**
	 * The switch statement type (statements of the form: "switch (" + name + ")" +
	 * statement)
	 */
	SWITCHSTMT(Map.of(localized("ast.enum.conditional_type.switch"), SwitchStmt.class)), //$NON-NLS-1$
	/**
	 * The switch expression type (expression of the form: "switch (" + name + ")" +
	 * statement)
	 */
	SWITCHEXPR(Map.of(localized("ast.enum.conditional_type.switch_expression"), SwitchExpr.class));

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	ConditionalType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
