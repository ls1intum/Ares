package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum LoopType {
    /**
     * Returns a list with all loop types
     */
    ALL(new ArrayList<>() {{
        add(new Pair<>("For-Statement", ForStmt.class));
        add(new Pair<>("For-Each-Statement", ForEachStmt.class));
        add(new Pair<>("While-Statement", WhileStmt.class));
        add(new Pair<>("Do-While-Statement", DoStmt.class));
    }}),
    /**
     * Returns a list with all for-related loop types
     */
    ALLFOR(new ArrayList<>() {{
        add(new Pair<>("For-Statement", ForStmt.class));
        add(new Pair<>("For-Each-Statement", ForEachStmt.class));
    }}),
    /**
     * Returns the for loop type
     */
    FOR(new ArrayList<>() {{
        add(new Pair<>("For-Statement", ForStmt.class));
    }}),
    /**
     * Returns the for each loop type
     */
    FOREACH(new ArrayList<>() {{
        add(new Pair<>("For-Each-Statement", ForEachStmt.class));
    }}),
    /**
     * Returns a list with all while-related loop types
     */
    ALLWHILE(new ArrayList<>() {{
        add(new Pair<>("While-Statement", WhileStmt.class));
        add(new Pair<>("Do-While-Statement", DoStmt.class));
    }}),
    /**
     * Returns the while loop type
     */
    WHILE(new ArrayList<>() {{
        add(new Pair<>("While-Statement", WhileStmt.class));
    }}),
    /**
     * Returns the do while loop type
     */
    DOWHILE(new ArrayList<>() {{
        add(new Pair<>("Do-While-Statement", DoStmt.class));
    }});

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    LoopType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
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
