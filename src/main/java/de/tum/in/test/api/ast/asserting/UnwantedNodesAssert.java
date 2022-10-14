package de.tum.in.test.api.ast.asserting;

import de.tum.in.test.api.ast.data.UnwantedNodesSetting;
import de.tum.in.test.api.ast.manager.UnwantedNodeManager;
import de.tum.in.test.api.ast.type.*;
import org.assertj.core.api.AbstractAssert;

public class UnwantedNodesAssert extends AbstractAssert<UnwantedNodesAssert, UnwantedNodesSetting> {

    protected UnwantedNodesAssert(UnwantedNodesSetting actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static UnwantedNodesAssert assertThat(UnwantedNodesSetting actual, Class<?> selfType) {
        return new UnwantedNodesAssert(actual, selfType);
    }

    public UnwantedNodesAssert hasBelowNo(ClassType classType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual.getPath(), classType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles -> 
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasBelowNo(FunctionType functionType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual.getPath(), functionType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasBelowNo(ConditionalType conditionalType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual.getPath(), conditionalType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasBelowNo(LoopType loopType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual.getPath(), loopType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasBelowNo(ExceptionHandlingType exceptionHandlingType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual.getPath(), exceptionHandlingType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasBelowNo(SynchronisationType synchronisationType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForAllJavaFilesBelow(this.actual.getPath(), synchronisationType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasAtNo(ClassType classType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual.getPath(), classType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasAtNo(FunctionType functionType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual.getPath(), functionType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasAtNo(ConditionalType conditionalType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual.getPath(), conditionalType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasAtNo(LoopType loopType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual.getPath(), loopType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasAtNo(ExceptionHandlingType exceptionHandlingType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual.getPath(), exceptionHandlingType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }

    public UnwantedNodesAssert hasAtNo(SynchronisationType synchronisationType) {
        isNotNull();
        UnwantedNodeManager.getMessageForUnwantedNodesForJavaFileAt(this.actual.getPath(), synchronisationType.getNodeNamePairs())
                .ifPresent(unwantedNodeMessageForAllJavaFiles ->
                    failWithMessage("Unwanted statement found:\n" + unwantedNodeMessageForAllJavaFiles)
                );
        return this;
    }
}
