package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.LocalRecordDeclarationStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum ClassType {
    ALL(new ArrayList<>() {
        {
            add(new Pair<>("Class-Statement", LocalClassDeclarationStmt.class));
            add(new Pair<>("Record-Statement", LocalRecordDeclarationStmt.class));
        }
    }),

    CLASS(new ArrayList<>() {
        {
            add(new Pair<>("Class-Statement", LocalClassDeclarationStmt.class));
        }
    }),

    RECORD(new ArrayList<>() {
        {
            add(new Pair<>("Record-Statement", LocalRecordDeclarationStmt.class));
        }
    });

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    ClassType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
        this.nodeNamePairs = nodeNamePairs;
    }

    public List<Pair<String, Class<? extends Node>>> getNodeNamePairs() {
        return nodeNamePairs;
    }
}
