package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum ExceptionHandlingType {
    ALL(new ArrayList<>() {{
        add(new Pair<>("Assert-Statement", AssertStmt.class));
        add(new Pair<>("If-Statement", IfStmt.class));
        add(new Pair<>("Try-Statement", TryStmt.class));
        add(new Pair<>("Catch-Clause", CatchClause.class));
    }}),
    ASSERT(new ArrayList<>() {{
        add(new Pair<>("Assert-Statement", AssertStmt.class));
    }}),
    THROW(new ArrayList<>() {{
        add(new Pair<>("If-Statement", IfStmt.class));
    }}),
    TRYCATCH(new ArrayList<>() {{
        add(new Pair<>("Try-Statement", TryStmt.class));
        add(new Pair<>("Catch-Clause", CatchClause.class));
    }});

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    ExceptionHandlingType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
        this.nodeNamePairs = nodeNamePairs;
    }

    public List<Pair<String, Class<? extends Node>>> getNodeNamePairs() {
        return nodeNamePairs;
    }
}

