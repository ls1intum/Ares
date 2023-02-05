package de.tum.in.test.integration.testuser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.tum.in.test.api.AddTrustedPackage;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.tool.PathTool;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@AddTrustedPackage("com.github.javaparser.**")
@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
public class ITASTTestUser {

	@Nested
	@DisplayName("For-Loop-Tests")
	class ForLoopTests {
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
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.FOR);
		}
	}

	@Nested
	@DisplayName("For-Each-Loop-Tests")
	class ForEachLoopTests {
		@Test
		void testHasBelowNoForEachLoop_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
					.asDirectory().hasNo(LoopType.FOR_EACH);
		}

		@Test
		void testHasBelowNoForEachLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
					.asDirectory().hasNo(LoopType.FOR_EACH);
		}

		@Test
		void testHasAtNoForEachLoop_Success() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
					.asFile().hasNo(LoopType.FOR_EACH);
		}

		@Test
		void testHasAtNoForEachLoop_Fail() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.FOR_EACH);
		}
	}

	@Nested
	@DisplayName("While-Loop-Tests")
	class WhileLoopTests {
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
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.WHILE);
		}
	}

	@Nested
	@DisplayName("Do-While-Loop-Tests")
	class DoWhileLoopTests {
		@Test
		void testHasBelowNoDoWhileLoop_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
					.asDirectory().hasNo(LoopType.DO_WHILE);
		}

		@Test
		void testHasBelowNoDoWhileLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
					.asDirectory().hasNo(LoopType.DO_WHILE);
		}

		@Test
		void testHasAtNoDoWhileLoop_Success() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
					.asFile().hasNo(LoopType.DO_WHILE);
		}

		@Test
		void testHasAtNoDoWhileLoop_Fail() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.DO_WHILE);
		}
	}

	@Nested
	@DisplayName("Any-For-Loop-Tests")
	class AnyForLoopTests {
		@Test
		void testHasBelowNoAnyForLoop_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
					.asDirectory().hasNo(LoopType.ANY_FOR);
		}

		@Test
		void testHasBelowNoAnyForLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
					.asDirectory().hasNo(LoopType.ANY_FOR);
		}

		@Test
		void testHasAtNoAnyForLoop_Success() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
					.asFile().hasNo(LoopType.ANY_FOR);
		}

		@Test
		void testHasAtNoAnyForLoop_Fail() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.ANY_FOR);
		}
	}

	@Nested
	@DisplayName("Any-While-Loop-Tests")
	class AnyWhileLoopTests {
		@Test
		void testHasBelowNoAnyWhileLoop_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/no"))
					.asDirectory().hasNo(LoopType.ANY_WHILE);
		}

		@Test
		void testHasBelowNoAnyWhileLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool.getPath("integration/testuser/subject/structural/astTestFiles/loops/yes"))
					.asDirectory().hasNo(LoopType.ANY_WHILE);
		}

		@Test
		void testHasAtNoAnyWhileLoop_Success() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/no/ClassWithNoKindsOfConditionals.java"))
					.asFile().hasNo(LoopType.ANY_WHILE);
		}

		@Test
		void testHasAtNoAnyWhileLoop_Fail() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.ANY_WHILE);
		}
	}

	@Nested
	@DisplayName("Any-Loop-Tests")
	class AnyLoopTests {
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
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/loops/yes/ClassWithAnyKindsOfLoops.java"))
					.asFile().hasNo(LoopType.ANY);
		}
	}

	@Nested
	@DisplayName("If-Conditional-Tests")
	class IfConditionalTests {
		@Test
		void testHasBelowNoIfConditional_Success() {
			UnwantedNodesAssert
					.assertThat(
							PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no"))
					.asDirectory().hasNo(ConditionalType.IF);
		}

		@Test
		void testHasBelowNoIfConditional_Fail() {
			UnwantedNodesAssert
					.assertThat(
							PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes"))
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
	}

	@Nested
	@DisplayName("Switch-Conditional-Tests")
	class SwitchConditionalTests {
		@Test
		void testHasBelowNoSwitchConditional_Success() {
			UnwantedNodesAssert
					.assertThat(
							PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no"))
					.asDirectory().hasNo(ConditionalType.SWITCH);
		}

		@Test
		void testHasBelowNoSwitchConditional_Fail() {
			UnwantedNodesAssert
					.assertThat(
							PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes"))
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
	}

	@Nested
	@DisplayName("Any-Conditional-Tests")
	class AnyConditionalTests {
		@Test
		void testHasBelowNoAnyConditional_Success() {
			UnwantedNodesAssert
					.assertThat(
							PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/no"))
					.asDirectory().hasNo(ConditionalType.ANY);
		}

		@Test
		void testHasBelowNoAnyConditional_Fail() {
			UnwantedNodesAssert
					.assertThat(
							PathTool.getPath("integration/testuser/subject/structural/astTestFiles/conditionals/yes"))
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
	}

	@Nested
	@DisplayName("Any-Class-Tests")
	class AnyClassTests {
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
	}

	@Nested
	@DisplayName("Assert-Exception-Handling-Tests")
	class AssertExceptionHandlingTests {
		@Test
		void testHasBelowNoAssertExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
					.asDirectory().hasNo(ExceptionHandlingType.ASSERT);
		}

		@Test
		void testHasBelowNoAssertExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
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
	}

	@Nested
	@DisplayName("Throw-Exception-Handling-Tests")
	class ThrowExceptionHandlingTests {
		@Test
		void testHasBelowNoThrowExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
					.asDirectory().hasNo(ExceptionHandlingType.THROW);
		}

		@Test
		void testHasBelowNoThrowExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
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
	}

	@Nested
	@DisplayName("TryCatch-Exception-Handling-Tests")
	class TryCatchExceptionHandlingTests {
		@Test
		void testHasBelowNoTryCatchExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
					.asDirectory().hasNo(ExceptionHandlingType.CATCH);
		}

		@Test
		void testHasBelowNoTryCatchExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
					.asDirectory().hasNo(ExceptionHandlingType.CATCH);
		}

		@Test
		void testHasAtNoTryCatchExceptionHandling_Success() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no/ClassWithNoKindsOfExceptionHandlings.java"))
					.asFile().hasNo(ExceptionHandlingType.CATCH);
		}

		@Test
		void testHasAtNoTryCatchExceptionHandling_Fail() {
			UnwantedNodesAssert.assertThat(PathTool.getPath(
					"integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes/ClassWithAnyKindsOfExceptionHandlings.java"))
					.asFile().hasNo(ExceptionHandlingType.CATCH);
		}
	}

	@Nested
	@DisplayName("Any-Exception-Handling-Tests")
	class AnyExceptionHandlingTests {
		@Test
		void testHasBelowNoAnyExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/no"))
					.asDirectory().hasNo(ExceptionHandlingType.ANY);
		}

		@Test
		void testHasBelowNoAnyExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(PathTool
							.getPath("integration/testuser/subject/structural/astTestFiles/exceptionHandlings/yes"))
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
	}
}
