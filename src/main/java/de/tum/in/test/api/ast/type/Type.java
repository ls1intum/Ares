package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;

@API(status = Status.MAINTAINED)
public interface Type {

	/**
	 * Returns the list of all node-name/node-type pairs
	 *
	 * @return List of all node-name/node-type pairs
	 */
	Map<String, Class<? extends Node>> getNodeNameNodeMap();
}
