package de.tum.in.test.integration.testuser;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.AddTrustedPackage;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.tool.PathTool;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

//<editor-fold desc="at">
@AddTrustedPackage("com.github.javaparser.**")
@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
//</editor-fold>
public class ITASTTestUser {

	// <editor-fold desc="For-Loop">
	@Test
	void testHasBelowNoForLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.FOR);
	}

	@Test
	void testHasBelowNoForLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.FOR);
	}

	@Test
	void testHasAtNoForLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.FOR);
	}

	@Test
	void testHasAtNoForLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.FOR);
	}
	// </editor-fold>

	// <editor-fold desc="For-Each-Loop">
	@Test
	void testHasBelowNoForEachLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.FOREACH);
	}

	@Test
	void testHasBelowNoForEachLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.FOREACH);
	}

	@Test
	void testHasAtNoForEachLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.FOREACH);
	}

	@Test
	void testHasAtNoForEachLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.FOREACH);
	}
	// </editor-fold>

	// <editor-fold desc="While-Loop">
	@Test
	void testHasBelowNoWhileLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.WHILE);
	}

	@Test
	void testHasBelowNoWhileLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.WHILE);
	}

	@Test
	void testHasAtNoWhileLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.WHILE);
	}

	@Test
	void testHasAtNoWhileLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.WHILE);
	}
	// </editor-fold>

	// <editor-fold desc="Do-While-Loop">
	@Test
	void testHasBelowNoDoWhileLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.DOWHILE);
	}

	@Test
	void testHasBelowNoDoWhileLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.DOWHILE);
	}

	@Test
	void testHasAtNoDoWhileLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.DOWHILE);
	}

	@Test
	void testHasAtNoDoWhileLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.DOWHILE);
	}
	// </editor-fold>

	// <editor-fold desc="Any-For-Loop">
	@Test
	void testHasBelowNoAnyForLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.ANYFOR);
	}

	@Test
	void testHasBelowNoAnyForLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.ANYFOR);
	}

	@Test
	void testHasAtNoAnyForLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.ANYFOR);
	}

	@Test
	void testHasAtNoAnyForLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.ANYFOR);
	}
	// </editor-fold>

	// <editor-fold desc="Any-While-Loop">
	@Test
	void testHasBelowNoAnyWhileLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.ANYWHILE);
	}

	@Test
	void testHasBelowNoAnyWhileLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.ANYWHILE);
	}

	@Test
	void testHasAtNoAnyWhileLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.ANYWHILE);
	}

	@Test
	void testHasAtNoAnyWhileLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.ANYWHILE);
	}
	// </editor-fold>

	// <editor-fold desc="Any-Loop">
	@Test
	void testHasBelowNoAnyLoop_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
				.asDirectory().hasNo(LoopType.ANY);
	}

	@Test
	void testHasBelowNoAnyLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
				.asDirectory().hasNo(LoopType.ANY);
	}

	@Test
	void testHasAtNoAnyLoop_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(LoopType.ANY);
	}

	@Test
	void testHasAtNoAnyLoop_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath(
						"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
				.asFile().hasNo(LoopType.ANY);
	}
	// </editor-fold>

	// <editor-fold desc="If-Conditional">
	@Test
	void testHasBelowNoIfConditional_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no"))
				.asDirectory().hasNo(ConditionalType.IF);
	}

	@Test
	void testHasBelowNoIfConditional_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes"))
				.asDirectory().hasNo(ConditionalType.IF);
	}

	@Test
	void testHasAtNoIfConditional_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/conditionals/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(ConditionalType.IF);
	}

	@Test
	void testHasAtNoIfConditional_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java"))
				.asFile().hasNo(ConditionalType.IF);
	}
	// </editor-fold>

	// <editor-fold desc="Switch-Conditional">
	@Test
	void testHasBelowNoSwitchConditional_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no"))
				.asDirectory().hasNo(ConditionalType.SWITCH);
	}

	@Test
	void testHasBelowNoSwitchConditional_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes"))
				.asDirectory().hasNo(ConditionalType.SWITCH);
	}

	@Test
	void testHasAtNoSwitchConditional_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/conditionals/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(ConditionalType.SWITCH);
	}

	@Test
	void testHasAtNoSwitchConditional_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java"))
				.asFile().hasNo(ConditionalType.SWITCH);
	}
	// </editor-fold>

	// <editor-fold desc="Any-Conditional">
	@Test
	void testHasBelowNoAnyConditional_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no"))
				.asDirectory().hasNo(ConditionalType.ANY);
	}

	@Test
	void testHasBelowNoAnyConditional_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes"))
				.asDirectory().hasNo(ConditionalType.ANY);
	}

	@Test
	void testHasAtNoAnyConditional_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/conditionals/no/ClassWithNoKindsOfConditionals.java"))
				.asFile().hasNo(ConditionalType.ANY);
	}

	@Test
	void testHasAtNoAnyConditional_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/conditionals/yes/ClassWithAnyKindsOfConditionals.java"))
				.asFile().hasNo(ConditionalType.ANY);
	}
	// </editor-fold>

	// <editor-fold desc="Any-Class">
	@Test
	void testHasBelowNoAnyClass_Success() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/no"))
				.asDirectory().hasNo(ClassType.ANY);
	}

	@Test
	void testHasBelowNoAnyClass_Fail() {
		UnwantedNodesAssert
				.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/classes/yes"))
				.asDirectory().hasNo(ClassType.ANY);
	}

	@Test
	void testHasAtNoAnyClass_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/classes/no/ClassWithNoKindsOfClasses.java"))
				.asFile().hasNo(ClassType.ANY);
	}

	@Test
	void testHasAtNoAnyClass_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/classes/yes/ClassWithAnyKindsOfClasses.java"))
				.asFile().hasNo(ClassType.ANY);
	}
	// </editor-fold>

	// <editor-fold desc="Assert-ExceptionHandling">
	@Test
	void testHasBelowNoAssertExceptionHandling_Success() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
				.asDirectory().hasNo(ExceptionHandlingType.ASSERT);
	}

	@Test
	void testHasBelowNoAssertExceptionHandling_Fail() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
				.asDirectory().hasNo(ExceptionHandlingType.ASSERT);
	}

	@Test
	void testHasAtNoAssertExceptionHandling_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.ASSERT);
	}

	@Test
	void testHasAtNoAssertExceptionHandling_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.ASSERT);
	}
	// </editor-fold>

	// <editor-fold desc="Throw-ExceptionHandling">
	@Test
	void testHasBelowNoThrowExceptionHandling_Success() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
				.asDirectory().hasNo(ExceptionHandlingType.THROW);
	}

	@Test
	void testHasBelowNoThrowExceptionHandling_Fail() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
				.asDirectory().hasNo(ExceptionHandlingType.THROW);
	}

	@Test
	void testHasAtNoThrowExceptionHandling_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.THROW);
	}

	@Test
	void testHasAtNoThrowExceptionHandling_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.THROW);
	}
	// </editor-fold>

	// <editor-fold desc="TryCatch-ExceptionHandling">
	@Test
	void testHasBelowNoTryCatchExceptionHandling_Success() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
				.asDirectory().hasNo(ExceptionHandlingType.TRYCATCH);
	}

	@Test
	void testHasBelowNoTryCatchExceptionHandling_Fail() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
				.asDirectory().hasNo(ExceptionHandlingType.TRYCATCH);
	}

	@Test
	void testHasAtNoTryCatchExceptionHandling_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.TRYCATCH);
	}

	@Test
	void testHasAtNoTryCatchExceptionHandling_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.TRYCATCH);
	}
	// </editor-fold>

	// <editor-fold desc="Any-ExceptionHandling">
	@Test
	void testHasBelowNoAnyExceptionHandling_Success() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
				.asDirectory().hasNo(ExceptionHandlingType.ANY);
	}

	@Test
	void testHasBelowNoAnyExceptionHandling_Fail() {
		UnwantedNodesAssert
				.assertThat(
						PathTool.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
				.asDirectory().hasNo(ExceptionHandlingType.ANY);
	}

	@Test
	void testHasAtNoAnyExceptionHandling_Success() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.ANY);
	}

	@Test
	void testHasAtNoAnyExceptionHandling_Fail() {
		UnwantedNodesAssert.assertThat(PathTool.getPath(
				"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java"))
				.asFile().hasNo(ExceptionHandlingType.ANY);
	}
	// </editor-fold>
}
