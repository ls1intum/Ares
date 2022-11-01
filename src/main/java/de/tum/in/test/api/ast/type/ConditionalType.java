package de.tum.in.test.api.ast.type;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.YieldStmt;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public enum ConditionalType {
    /**
     * Returns a list with all conditional types
     */
    ALL(new ArrayList<>() {{
        add(new Pair<>("If-Statement", IfStmt.class));
        add(new Pair<>("Switch-Statement", SwitchStmt.class));
        add(new Pair<>("Switch-Entry-Statement", SwitchEntry.class));
        add(new Pair<>("Yield-Statement", YieldStmt.class));
    }}),
    /**
     * Returns the if condition type
     */
    IF(new ArrayList<>() {{
        add(new Pair<>("If-Statement", IfStmt.class));
    }}),
    /**
     * Returns the switch condition type
     */
    SWITCH(new ArrayList<>() {{
        add(new Pair<>("Switch-Statement", SwitchStmt.class));
        add(new Pair<>("Switch-Entry-Statement", SwitchEntry.class));
        add(new Pair<>("Yield-Statement", YieldStmt.class));
    }});

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    ConditionalType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
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
