package de.tum.in.test.api.ast.asserting;

import de.tum.in.test.api.ast.manager.UnwantedNodeManager;
import de.tum.in.test.api.ast.type.*;
import org.assertj.core.api.AbstractAssert;

import java.nio.file.Path;

/**
 * Checks whole Java-Files for unwanted nodes
 */
public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, Path> {

    /**
     * Non-statically creates an unwanted node assertion object with a path
     * @param actual The path from which on the Java-Files are checked
     */
    public UnwantedNodesAssert(Path actual) {
        super(actual, UnwantedNodesAssert.class);
    }

    /**
     * Statically creates an unwanted node assertion object with a path
     * @param actual The path from which on the Java-Files are checked
     * @return An unwanted node assertion object (for checks)
     */
    public static UnwantedNodesAssert assertThat(Path actual) {
        return new UnwantedNodesAssert(actual);
    }

    /**
     * Checks if the Java-files at and below the given path do not contain any unwanted class statement
     * @param classType Unwanted class statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasBelowNo(ClassType classType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual, classType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles -> 
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-files at and below the given path do not contain any unwanted function statement
     * @param functionType Unwanted function statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasBelowNo(FunctionType functionType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual, functionType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-files at and below the given path do not contain any unwanted conditional statement
     * @param conditionalType Unwanted conditional statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasBelowNo(ConditionalType conditionalType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual, conditionalType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-files at and below the given path do not contain any unwanted loop statement
     * @param loopType Unwanted loop statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasBelowNo(LoopType loopType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual, loopType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-files at and below the given path do not contain any unwanted exception handling statement
     * @param exceptionHandlingType Unwanted exception handling statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasBelowNo(ExceptionHandlingType exceptionHandlingType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual, exceptionHandlingType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-files at and below the given path do not contain any unwanted synchronisation statement
     * @param synchronisationType Unwanted synchronisation statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasBelowNo(SynchronisationType synchronisationType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual, synchronisationType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-file at the given path does not contain any unwanted class statement
     * @param classType Unwanted class statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasAtNo(ClassType classType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual, classType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-file at the given path does not contain any unwanted function statement
     * @param functionType Unwanted function statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasAtNo(FunctionType functionType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual, functionType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-file at the given path does not contain any unwanted conditional statement
     * @param conditionalType Unwanted conditional statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasAtNo(ConditionalType conditionalType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual, conditionalType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-file at the given path does not contain any unwanted loop statement
     * @param loopType Unwanted loop statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasAtNo(LoopType loopType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual, loopType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-file at the given path does not contain any unwanted exception handling statement
     * @param exceptionHandlingType Unwanted exception handling statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasAtNo(ExceptionHandlingType exceptionHandlingType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual, exceptionHandlingType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    /**
     * Checks if the Java-file at the given path does not contain any unwanted synchronisation statement
     * @param synchronisationType Unwanted synchronisation statements
     * @return This unwanted node assertion object (for chaining)
     */
    public UnwantedNodesAssert hasAtNo(SynchronisationType synchronisationType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual, synchronisationType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }
}
