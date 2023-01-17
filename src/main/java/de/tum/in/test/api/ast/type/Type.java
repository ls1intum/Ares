package de.tum.in.test.api.ast.type;

import java.util.Map;

import com.github.javaparser.ast.Node;

public interface Type {

	Map<String, Class<? extends Node>> getNodeNameNodeMap();
}
