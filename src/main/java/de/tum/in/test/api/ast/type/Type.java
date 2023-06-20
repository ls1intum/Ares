package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;

import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;

/**
 * Describes a type of syntactic construct in Java in the bread sense (e.g., all
 * loop statements together can also be regarded as "the loop type"). Can be
 * checked using {@link UnwantedNodesAssert}.
 *
 * @author Markus Paulsen
 * @since 1.12.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
public interface Type {

	/**
	 * Returns the list of all node-name/node-type pairs
	 *
	 * @return Map of all node-name/node-type pairs
	 */
	Map<String, Class<? extends Node>> getNodeNameNodeMap();
}
