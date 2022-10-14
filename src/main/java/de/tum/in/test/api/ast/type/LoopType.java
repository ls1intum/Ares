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
    ALL(new ArrayList<>() {{
        add(new Pair<>("For-Statement", ForStmt.class));
        add(new Pair<>("For-Each-Statement", ForEachStmt.class));
        add(new Pair<>("While-Statement", WhileStmt.class));
        add(new Pair<>("Do-While-Statement", DoStmt.class));
    }}),
    ALLFOR(new ArrayList<>() {{
        add(new Pair<>("For-Statement", ForStmt.class));
        add(new Pair<>("For-Each-Statement", ForEachStmt.class));
    }}),
    FOR(new ArrayList<>() {{
        add(new Pair<>("For-Statement", ForStmt.class));
    }}),
    FOREACH(new ArrayList<>() {{
        add(new Pair<>("For-Each-Statement", ForEachStmt.class));
    }}),
    ALLWHILE(new ArrayList<>() {{
        add(new Pair<>("While-Statement", WhileStmt.class));
        add(new Pair<>("Do-While-Statement", DoStmt.class));
    }}),
    WHILE(new ArrayList<>() {{
        add(new Pair<>("While-Statement", WhileStmt.class));
    }}),
    DOWHILE(new ArrayList<>() {{
        add(new Pair<>("Do-While-Statement", DoStmt.class));
    }});

    private final List<Pair<String, Class<? extends Node>>> nodeNamePairs;

    LoopType(List<Pair<String, Class<? extends Node>>> nodeNamePairs) {
        this.nodeNamePairs = nodeNamePairs;
    }

    public List<Pair<String, Class<? extends Node>>> getNodeNamePairs() {
        return nodeNamePairs;
    }
}
