package de.tum.in.test.integration.testuser;

import de.tum.in.test.api.AddTrustedPackage;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.tool.PathTool;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import org.junit.jupiter.api.Test;

//<editor-fold desc="at">
@AddTrustedPackage("com.github.javaparser.**")
@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
//</editor-fold>
public class ITASTTestUser {

    //<editor-fold desc="For-Loop">
    @Test
    void testHasBelowNoForLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.FOR);
    }

    @Test
    void testHasBelowNoForLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.FOR);
    }

    @Test
    void testHasAtNoForLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.FOR);
    }

    @Test
    void testHasAtNoForLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.FOR);
    }
    //</editor-fold>

    //<editor-fold desc="For-Each-Loop">
    @Test
    void testHasBelowNoForEachLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.FOREACH);

    }

    @Test
    void testHasBelowNoForEachLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.FOREACH);
    }

    @Test
    void testHasAtNoForEachLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.FOREACH);
    }

    @Test
    void testHasAtNoForEachLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.FOREACH);
    }
    //</editor-fold>

    //<editor-fold desc="While-Loop">
    @Test
    void testHasBelowNoWhileLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.WHILE);

    }

    @Test
    void testHasBelowNoWhileLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.WHILE);
    }

    @Test
    void testHasAtNoWhileLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.WHILE);
    }

    @Test
    void testHasAtNoWhileLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.WHILE);
    }
    //</editor-fold>

    //<editor-fold desc="Do-While-Loop">
    @Test
    void testHasBelowNoDoWhileLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.DOWHILE);

    }

    @Test
    void testHasBelowNoDoWhileLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.DOWHILE);
    }

    @Test
    void testHasAtNoDoWhileLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.DOWHILE);
    }

    @Test
    void testHasAtNoDoWhileLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.DOWHILE);
    }
    //</editor-fold>

    //<editor-fold desc="All-For-Loop">
    @Test
    void testHasBelowNoAllForLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.ALLFOR);

    }

    @Test
    void testHasBelowNoAllForLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.ALLFOR);
    }

    @Test
    void testHasAtNoAllForLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.ALLFOR);
    }

    @Test
    void testHasAtNoAllForLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.ALLFOR);
    }
    //</editor-fold>

    //<editor-fold desc="All-While-Loop">
    @Test
    void testHasBelowNoAllWhileLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.ALLWHILE);

    }

    @Test
    void testHasBelowNoAllWhileLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.ALLWHILE);
    }

    @Test
    void testHasAtNoAllWhileLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.ALLWHILE);
    }

    @Test
    void testHasAtNoAllWhileLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.ALLWHILE);
    }
    //</editor-fold>

    //<editor-fold desc="All-Loop">
    @Test
    void testHasBelowNoAllLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no")).hasBelowNo(LoopType.ALL);

    }

    @Test
    void testHasBelowNoAllLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes")).hasBelowNo(LoopType.ALL);
    }

    @Test
    void testHasAtNoAllLoop_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(LoopType.ALL);
    }

    @Test
    void testHasAtNoAllLoop_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAllKindsOfLoops.java")).hasAtNo(LoopType.ALL);
    }
    //</editor-fold>

    //<editor-fold desc="If-Conditional">
    @Test
    void testHasBelowNoIfConditional_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no")).hasBelowNo(ConditionalType.IF);

    }

    @Test
    void testHasBelowNoIfConditional_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes")).hasBelowNo(ConditionalType.IF);
    }

    @Test
    void testHasAtNoIfConditional_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(ConditionalType.IF);
    }

    @Test
    void testHasAtNoIfConditional_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java")).hasAtNo(ConditionalType.IF);
    }
    //</editor-fold>

    //<editor-fold desc="Switch-Conditional">
    @Test
    void testHasBelowNoSwitchConditional_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no")).hasBelowNo(ConditionalType.SWITCH);
    }

    @Test
    void testHasBelowNoSwitchConditional_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes")).hasBelowNo(ConditionalType.SWITCH);
    }

    @Test
    void testHasAtNoSwitchConditional_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(ConditionalType.SWITCH);
    }

    @Test
    void testHasAtNoSwitchConditional_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java")).hasAtNo(ConditionalType.SWITCH);
    }
    //</editor-fold>

    //<editor-fold desc="All-Conditional">
    @Test
    void testHasBelowNoAllConditional_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no")).hasBelowNo(ConditionalType.ALL);

    }

    @Test
    void testHasBelowNoAllConditional_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes")).hasBelowNo(ConditionalType.ALL);
    }

    @Test
    void testHasAtNoAllConditional_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no/ClassWithNoKindsOfConditionals.java")).hasAtNo(ConditionalType.ALL);
    }

    @Test
    void testHasAtNoAllConditional_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAllKindsOfConditionals.java")).hasAtNo(ConditionalType.ALL);
    }
    //</editor-fold>

    //<editor-fold desc="ExplicitConstructorInvocation-Function">
    @Test
    void testHasBelowNoExplicitConstructorInvocationFunction_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/no")).hasBelowNo(FunctionType.EXPLICITCONSTRUCTORINVOCATION);
    }

    @Test
    void testHasBelowNoExplicitConstructorInvocationFunction_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/yes")).hasBelowNo(FunctionType.EXPLICITCONSTRUCTORINVOCATION);
    }

    @Test
    void testHasAtNoExplicitConstructorInvocationFunction_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/no/ClassWithNoKindsOfFunctions.java")).hasAtNo(FunctionType.EXPLICITCONSTRUCTORINVOCATION);
    }

    @Test
    void testHasAtNoExplicitConstructorInvocationFunction_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java")).hasAtNo(FunctionType.EXPLICITCONSTRUCTORINVOCATION);
    }
    //</editor-fold>

    //<editor-fold desc="Return-Function">
    @Test
    void testHasBelowNoReturnFunction_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/no")).hasBelowNo(FunctionType.RETURN);

    }

    @Test
    void testHasBelowNoReturnFunction_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/yes")).hasBelowNo(FunctionType.RETURN);
    }

    @Test
    void testHasAtNoReturnFunction_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/no/ClassWithNoKindsOfFunctions.java")).hasAtNo(FunctionType.RETURN);
    }

    @Test
    void testHasAtNoReturnFunction_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java")).hasAtNo(FunctionType.RETURN);
    }
    //</editor-fold>

    //<editor-fold desc="All-Function">
    @Test
    void testHasBelowNoAllFunction_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/no")).hasBelowNo(FunctionType.ALL);

    }

    @Test
    void testHasBelowNoAllFunction_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/yes")).hasBelowNo(FunctionType.ALL);
    }

    @Test
    void testHasAtNoAllFunction_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/no/ClassWithNoKindsOfFunctions.java")).hasAtNo(FunctionType.ALL);
    }

    @Test
    void testHasAtNoAllFunction_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/functions/yes/ClassWithAllKindsOfFunctions.java")).hasAtNo(FunctionType.ALL);
    }
    //</editor-fold>

    //<editor-fold desc="LocalClass-Class">
    @Test
    void testHasBelowNoLocalClassClass_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no")).hasBelowNo(ClassType.LOCALCLASS);
    }

    @Test
    void testHasBelowNoLocalClassClass_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes")).hasBelowNo(ClassType.LOCALCLASS);
    }

    @Test
    void testHasAtNoLocalClassClass_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no/ClassWithNoKindsOfClasses.java")).hasAtNo(ClassType.LOCALCLASS);
    }

    @Test
    void testHasAtNoLocalClassClass_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java")).hasAtNo(ClassType.LOCALCLASS);
    }
    //</editor-fold>

    //<editor-fold desc="LocalRecord-Class">
    @Test
    void testHasBelowNoLocalRecordClass_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no")).hasBelowNo(ClassType.LOCALRECORD);
    }

    @Test
    void testHasBelowNoLocalRecordClass_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes")).hasBelowNo(ClassType.LOCALRECORD);
    }

    @Test
    void testHasAtNoLocalRecordClass_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no/ClassWithNoKindsOfClasses.java")).hasAtNo(ClassType.LOCALRECORD);
    }

    @Test
    void testHasAtNoLocalRecordClass_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java")).hasAtNo(ClassType.LOCALRECORD);
    }
    //</editor-fold>

    //<editor-fold desc="All-Class">
    @Test
    void testHasBelowNoAllClass_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no")).hasBelowNo(ClassType.ALL);
    }

    @Test
    void testHasBelowNoAllClass_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes")).hasBelowNo(ClassType.ALL);
    }

    @Test
    void testHasAtNoAllClass_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no/ClassWithNoKindsOfClasses.java")).hasAtNo(ClassType.ALL);
    }

    @Test
    void testHasAtNoAllClass_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAllKindsOfClasses.java")).hasAtNo(ClassType.ALL);
    }
    //</editor-fold>

    //<editor-fold desc="Assert-ExceptionHandling">
    @Test
    void testHasBelowNoAssertExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no")).hasBelowNo(ExceptionHandlingType.ASSERT);
    }

    @Test
    void testHasBelowNoAssertExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes")).hasBelowNo(ExceptionHandlingType.ASSERT);
    }

    @Test
    void testHasAtNoAssertExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.ASSERT);
    }

    @Test
    void testHasAtNoAssertExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.ASSERT);
    }
    //</editor-fold>

    //<editor-fold desc="Throw-ExceptionHandling">
    @Test
    void testHasBelowNoThrowExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no")).hasBelowNo(ExceptionHandlingType.THROW);
    }

    @Test
    void testHasBelowNoThrowExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes")).hasBelowNo(ExceptionHandlingType.THROW);
    }

    @Test
    void testHasAtNoThrowExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.THROW);
    }

    @Test
    void testHasAtNoThrowExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.THROW);
    }
    //</editor-fold>

    //<editor-fold desc="TryCatch-ExceptionHandling">
    @Test
    void testHasBelowNoTryCatchExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no")).hasBelowNo(ExceptionHandlingType.TRYCATCH);
    }

    @Test
    void testHasBelowNoTryCatchExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes")).hasBelowNo(ExceptionHandlingType.TRYCATCH);
    }

    @Test
    void testHasAtNoTryCatchExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.TRYCATCH);
    }

    @Test
    void testHasAtNoTryCatchExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.TRYCATCH);
    }
    //</editor-fold>

    //<editor-fold desc="All-ExceptionHandling">
    @Test
    void testHasBelowNoAllExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no")).hasBelowNo(ExceptionHandlingType.ALL);
    }

    @Test
    void testHasBelowNoAllExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes")).hasBelowNo(ExceptionHandlingType.ALL);
    }

    @Test
    void testHasAtNoAllExceptionHandling_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.ALL);
    }

    @Test
    void testHasAtNoAllExceptionHandling_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAllKindsOfExceptionHandlings.java")).hasAtNo(ExceptionHandlingType.ALL);
    }
    //</editor-fold>

    //<editor-fold desc="All-Synchronisation">
    @Test
    void testHasBelowNoAllSynchronisation_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/synchronisations/no")).hasBelowNo(SynchronisationType.ALL);
    }

    @Test
    void testHasBelowNoAllSynchronisation_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/synchronisations/yes")).hasBelowNo(SynchronisationType.ALL);
    }

    @Test
    void testHasAtNoAllSynchronisation_Success() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/synchronisations/no/ClassWithNoKindsOfSynchronisations.java")).hasAtNo(SynchronisationType.ALL);
    }

    @Test
    void testHasAtNoAllSynchronisation_Fail() {
        UnwantedNodesAssert.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/synchronisations/yes/ClassWithAllKindsOfSynchronisations.java")).hasAtNo(SynchronisationType.ALL);
    }
    //</editor-fold>
}
