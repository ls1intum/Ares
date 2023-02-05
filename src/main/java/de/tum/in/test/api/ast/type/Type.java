package de.tum.in.test.api.ast.type;

import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.javaparser.ast.Node;

@API(status = Status.MAINTAINED)
public interface Type {

	Map<String, Class<? extends Node>> getNodeNameNodeMap();
}
