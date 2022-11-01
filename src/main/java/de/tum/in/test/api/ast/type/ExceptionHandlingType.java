package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum ExceptionHandlingType {
    /**
     * Returns a list with all exception handling types
     */
    ALL(new ArrayList<>() {{
        add(new Pair<>("Assert-Statement", AssertStmt.class));
        add(new Pair<>("Throw-Statement", ThrowStmt.class));
        add(new Pair<>("Try-Statement", TryStmt.class));
        add(new Pair<>("Catch-Statement", CatchClause.class));
    }}),
    /**
     * Returns the assert type
     */
    ASSERT(new ArrayList<>() {{
        add(new Pair<>("Assert-Statement", AssertStmt.class));
    }}),
    /**
     * Returns the throw type
     */
    THROW(new ArrayList<>() {{
        add(new Pair<>("Throw-Statement", ThrowStmt.class));
    }}),
    /**
     * Returns the try-catch exception handling type
     */
    TRYCATCH(new ArrayList<>() {{
        add(new Pair<>("Try-Statement", TryStmt.class));
        add(new Pair<>("Catch-Statement", CatchClause.class));
    }});

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    ExceptionHandlingType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
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

