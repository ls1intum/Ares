package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.LocalRecordDeclarationStmt;

import java.util.Map;

public enum ClassType implements Type {
    /**
     * Returns a list with all class types
     */

    ANY(Map.of(
            "Local-Class-Statement", LocalClassDeclarationStmt.class,
            "Local-Record-Statement", LocalRecordDeclarationStmt.class
    )),

    /**
     *
     */
    LOCALCLASS(Map.of(
            "Local-Class-Statement", LocalClassDeclarationStmt.class
    )),

    /**
     *
     */
    LOCALRECORD(Map.of(
            "Local-Record-Statement", LocalRecordDeclarationStmt.class
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
