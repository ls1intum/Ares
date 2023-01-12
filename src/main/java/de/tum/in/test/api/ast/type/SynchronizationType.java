package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.SynchronizedStmt;

import java.util.Map;

public enum SynchronizationType implements Type {
    /**
     * Returns a list with all synchronization types
     */
    ANY(Map.of(
            "Synchronized-Statement", SynchronizedStmt.class
    ));

    private final Map<String, Class<? extends Node>> nodeNameNodeMap;

    SynchronizationType(Map<String, Class<? extends Node>> nodeNameNodeMap) {
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
