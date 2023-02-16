package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;

import static de.tum.in.test.api.localization.Messages.localized;

@API(status = Status.MAINTAINED)
public enum LoopType implements Type {
	/**
	 * All loop types
	 */
	ANY(Map.of(localized("ast.enum.loop_type.for"), ForStmt.class, localized("ast.enum.loop_type.for_each"), ForEachStmt.class, localized("ast.enum.loop_type.while"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			WhileStmt.class, localized("ast.enum.loop_type.do_while"), DoStmt.class)), //$NON-NLS-1$
	/**
	 * All for-related types
	 */
	ANY_FOR(Map.of(localized("ast.enum.loop_type.for"), ForStmt.class, localized("ast.enum.loop_type.for_each"), ForEachStmt.class)), //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * The for type (statements of the form: "for (" + declaration + condition + statement + ")" + statement)
	 */
	FOR(Map.of(localized("ast.enum.loop_type.for"), ForStmt.class)), //$NON-NLS-1$
	/**
	 * The for each type (statements of the form: "for (" + declaration + ":" + iterateable: Iterateable ")" + statement)
	 */
	FOR_EACH(Map.of(localized("ast.enum.loop_type.for_each"), ForEachStmt.class)), //$NON-NLS-1$
	/**
	 * All while-related types
	 */
	ANY_WHILE(Map.of(localized("ast.enum.loop_type.while"), WhileStmt.class, localized("ast.enum.loop_type.do_while"), DoStmt.class)), //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * The while type (statements of the form: "while (" + condition + ")" + statement)
	 */
	WHILE(Map.of(localized("ast.enum.loop_type.while"), WhileStmt.class)), //$NON-NLS-1$
	/**
	 * The do while type (statements of the form: "do" + statement + "while (" + condition + ")")
	 */
	DO_WHILE(Map.of(localized("ast.enum.loop_type.do_while"), DoStmt.class)); //$NON-NLS-1$

	private final Map<String, Class<? extends Node>> nodeNameNodeMap;

	LoopType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
		this.nodeNameNodeMap = nodeNameNodeMap;
	}

	@Override
	public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
		return nodeNameNodeMap;
	}
}
