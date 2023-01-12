package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import java.util.Map;

public interface Type {

    Map<String, Class<? extends Node>> getNodeNameNodeMap();
}
