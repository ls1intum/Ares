package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum SynchronisationType {
    /**
     * Returns a list with all synchronisation types
     */
    ALL(new ArrayList<>() {
        {
            add(new Pair<>("Synchronised-Statement", SynchronizedStmt.class));
        }
    });

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    SynchronisationType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
        this.nodeNamePairs = nodeNamePairs;
    }

    /**
     * Returns the list of all node-name/node-type pairs
     * @return List of all node-name/node-type pairs
     */
    public List<Pair<String, Class<? extends Node>>> getNodeNamePairs() {
        return nodeNamePairs;
    }
}
