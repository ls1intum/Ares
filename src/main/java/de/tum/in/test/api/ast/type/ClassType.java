package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.LocalRecordDeclarationStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum ClassType {
    /**
     * Returns a list with all class types
     */
    ALL(new ArrayList<>() {
        {
            add(new Pair<>("Local-Class-Statement", LocalClassDeclarationStmt.class));
            add(new Pair<>("Local-Record-Statement", LocalRecordDeclarationStmt.class));
        }
    }),

    /**
     *
     */
    LOCALCLASS(new ArrayList<>() {
        {
            add(new Pair<>("Local-Class-Statement", LocalClassDeclarationStmt.class));
        }
    }),

    /**
     *
     */
    LOCALRECORD(new ArrayList<>() {
        {
            add(new Pair<>("Local-Record-Statement", LocalRecordDeclarationStmt.class));
        }
    });

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    ClassType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
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
