package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import java.util.Map;

public enum ClassType implements Type {
    /**
     * Returns a list with the local class type
     */

    ANY(Map.of(
            "Local-Class-Statement", LocalClassDeclarationStmt.class
    ));

    private final Map<String, Class<? extends Node>> nodeNameNodeMap;

    ClassType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
        this.nodeNameNodeMap = nodeNameNodeMap;
    }

    /**
     * Returns the list of all node-name/node-type pairs
     *
     * @return List of all node-name/node-type pairs
     */
    public Map<String, Class<? extends Node>> getNodeNameNodeMap() {
        return nodeNameNodeMap;
    }
}
