package de.tum.in.test.api.ast.model;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import org.apiguardian.api.API;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@API(status = API.Status.INTERNAL)
public class RecursionCheck {

    private RecursionCheck() {
        // Hide the implicit public constructor
        throw new IllegalStateException();
    }

    /**
     * Check if the startingNode has a recursive call
     *
     * @param pathToSrcRoot Path to the source root
     * @param level         JavaParser Language Level
     * @param startingNode  Method to start the recursion check from, which may be {@code null}
     * @return Optional.empty() if recursive call is detected, otherwise an error message
     */
    public static Optional<String> hasCycle(Path pathToSrcRoot, ParserConfiguration.LanguageLevel level, Method startingNode, Method... excludedMethods) {
        MethodCallGraph graph = createMethodCallGraph(pathToSrcRoot, level, excludedMethods);
        return !checkCycle(graph, startingNode).isEmpty() ? Optional.empty() : Optional.of("No recursive call detected");
    }

    /**
     * Check if the startingNode has no recursive call
     *
     * @param pathToSrcRoot Path to the source root
     * @param level         JavaParser Language Level
     * @param startingNode  Method to start the recursion check from, which may be {@code null}
     * @return Optional.empty() if no recursive call is detected, otherwise an error message with methods in the detected cycle
     */
    public static Optional<String> hasNoCycle(Path pathToSrcRoot, ParserConfiguration.LanguageLevel level, Method startingNode, Method... excludedMethods) {
        MethodCallGraph graph = createMethodCallGraph(pathToSrcRoot, level, excludedMethods);
        return checkCycle(graph, startingNode).stream().reduce((s1, s2) -> String.join(", ", s1, s2));
    }

    /**
     * Check if the graph has a cycle
     *
     * @param graph        Method call graph
     * @param startingNode Method to start the recursion check from, which may be {@code null}
     * @return Set of methods in the detected cycle
     */
    private static Set<String> checkCycle(MethodCallGraph graph, Method startingNode) {
        // Convert Method to Node name
        String nodeName = startingNode != null ? startingNode.getDeclaringClass().getName() + "." + startingNode.getName() + getParametersOfMethod(startingNode) : null;

        if (nodeName != null) {
            Graph<String, DefaultEdge> subgraph = graph.extractSubgraph(nodeName);
            return new CycleDetector<>(subgraph).findCycles();
        } else {
            return new CycleDetector<>(graph.getGraph()).findCycles();
        }
    }

    /**
     * Get the parameters of the method
     *
     * @param method Method
     * @return String representation of the parameters
     */
    public static String getParametersOfMethod(Method method) {
        return "(" + Arrays.stream(method.getParameterTypes()).map(Class::getCanonicalName).collect(Collectors.joining(", ")) + ")";
    }

    /**
     * Create a method call graph from the source root
     *
     * @param pathToSrcRoot Path to the source root
     * @param level         JavaParser Language Level
     * @return Method call graph
     */
    public static MethodCallGraph createMethodCallGraph(Path pathToSrcRoot, ParserConfiguration.LanguageLevel level, Method... excludedMethods) {
        MethodCallGraph methodCallGraph = new MethodCallGraph(excludedMethods);
        List<Optional<CompilationUnit>> asts = parseFromSourceRoot(pathToSrcRoot, level);
        for (Optional<CompilationUnit> ast : asts) {
            ast.ifPresent(methodCallGraph::createGraph);
        }

        return methodCallGraph;
    }

    /**
     * Parse all Java files in the source root
     *
     * @param pathToSourceRoot Path to the source root
     * @param level            JavaParser Language Level
     * @return List of CompilationUnit
     */
    public static List<Optional<CompilationUnit>> parseFromSourceRoot(Path pathToSourceRoot, ParserConfiguration.LanguageLevel level) {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(new File(pathToSourceRoot.toString())));
        combinedTypeSolver.add(new ClassLoaderTypeSolver(MethodCallGraph.class.getClassLoader()));

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        SourceRoot sourceRoot = new SourceRoot(pathToSourceRoot, new ParserConfiguration().setSymbolResolver(symbolSolver).setLanguageLevel(level));

        List<ParseResult<CompilationUnit>> parseResults;
        try {
            parseResults = sourceRoot.tryToParse();
        } catch (IOException e) {
            throw new AssertionError(String.format("The file %s could not be read:", e));
        }

        return parseResults.stream().map(ParseResult::getResult).collect(Collectors.toList());
    }
}
