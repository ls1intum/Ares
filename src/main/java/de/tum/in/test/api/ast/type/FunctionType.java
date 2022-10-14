package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum FunctionType {
    ALL(new ArrayList<>() {
        {
            add(new Pair<>("New-Statement", ExplicitConstructorInvocationStmt.class));
            add(new Pair<>("Return-Statement", ReturnStmt.class));
        }
    }),

    NEW(new ArrayList<>() {
        {
            add(new Pair<>("New-Statement", ExplicitConstructorInvocationStmt.class));
        }
    }),

    RETURN(new ArrayList<>() {
        {
            add(new Pair<>("Return-Statement", ReturnStmt.class));
        }
    });

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    FunctionType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
        this.nodeNamePairs = nodeNamePairs;
    }

    public List<Pair<String, Class<? extends Node>>> getNodeNamePairs() {
        return nodeNamePairs;
    }
}
