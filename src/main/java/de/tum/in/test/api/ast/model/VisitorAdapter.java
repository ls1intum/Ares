package de.tum.in.test.api.ast.model;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class VisitorAdapter extends VoidVisitorAdapter<Void> {
    private final Graph<String, DefaultEdge> graph;

    private final String[] excludedMethodIdentifiers;

    public VisitorAdapter(Graph<String, DefaultEdge> graph, String... excludedMethodIdentifiers) {
        this.graph = graph;
        this.excludedMethodIdentifiers = excludedMethodIdentifiers;
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        super.visit(md, arg);
        String vertexName = md.resolve().getQualifiedSignature();
        if (isExcluded(vertexName)) {
            return;
        }
        graph.addVertex(vertexName);
        md.findAll(MethodCallExpr.class).forEach(mce -> {
            String calleeVertexName = mce.resolve().getQualifiedSignature();
            graph.addVertex(calleeVertexName);
            graph.addEdge(vertexName, calleeVertexName);
        });
    }
    /**
     * Check if the given vertex is excluded from the cycle check
     * @param vertex Vertex to check
     * @return True if the vertex is excluded, false otherwise
     */
    private boolean isExcluded(String vertex) {
        for (String excludedIdentifier : excludedMethodIdentifiers) {
            if (vertex.equals(excludedIdentifier)) {
                return true;
            }
        }
        return false;
    }
}
