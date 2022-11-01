package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum FunctionType {
    /**
     * Returns a list with all function types
     */
    ALL(new ArrayList<>() {
        {
            add(new Pair<>("Explicit-Constructor-Invocation-Statement", ExplicitConstructorInvocationStmt.class));
            add(new Pair<>("Return-Statement", ReturnStmt.class));
        }
    }),
    /**
     * Returns the function explicit constructor invocation type (super(), this(), ...)
     */
    EXPLICITCONSTRUCTORINVOCATION(new ArrayList<>() {
        {
            add(new Pair<>("Explicit-Constructor-Invocation-Statement", ExplicitConstructorInvocationStmt.class));
        }
    }),
    /**
     * Returns the function return type
     */
    RETURN(new ArrayList<>() {
        {
            add(new Pair<>("Return-Statement", ReturnStmt.class));
        }
    });

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    FunctionType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
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
