package de.tum.in.test.integration.testuser;

import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.javaparser.ParserConfiguration;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@Public
@UseLocale("en")
@StrictTimeout(10000)
@WhitelistPath("")
public class AstAssertionUser {

	@Nested
	@DisplayName("For-Loop-Tests")
	class ForLoopTests {
		@Test
		void testHasBelowNoForLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
		}

		@Test
		void testHasBelowNoForLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
		}
	}

	@Nested
	@DisplayName("For-Each-Loop-Tests")
	class ForEachLoopTests {
		@Test
		void testHasBelowNoForEachLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FOR_EACHSTMT);
		}

		@Test
		void testHasBelowNoForEachLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FOR_EACHSTMT);
		}
	}

	@Nested
	@DisplayName("While-Loop-Tests")
	class WhileLoopTests {
		@Test
		void testHasBelowNoWhileLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.WHILESTMT);
		}

		@Test
		void testHasBelowNoWhileLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.WHILESTMT);
		}
	}

	@Nested
	@DisplayName("Do-While-Loop-Tests")
	class DoWhileLoopTests {
		@Test
		void testHasBelowNoDoWhileLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.DO_WHILESTMT);
		}

		@Test
		void testHasBelowNoDoWhileLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.DO_WHILESTMT);
		}
	}

	@Nested
	@DisplayName("Any-For-Loop-Tests")
	class AnyForLoopTests {
		@Test
		void testHasBelowNoAnyForLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_FOR);
		}

		@Test
		void testHasBelowNoAnyForLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_FOR);
		}
	}

	@Nested
	@DisplayName("Any-While-Loop-Tests")
	class AnyWhileLoopTests {
		@Test
		void testHasBelowNoAnyWhileLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_WHILE);
		}

		@Test
		void testHasBelowNoAnyWhileLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_WHILE);
		}
	}

	@Nested
	@DisplayName("Any-Loop-Tests")
	class AnyLoopTests {
		@Test
		void testHasBelowNoAnyLoop_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testHasBelowNoAnyLoop_Fail() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "yes")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}
	}

	@Nested
	@DisplayName("If-Statement-Tests")
	class IfStatementTests {
		@Test
		void testHasBelowNoIfStatement_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.IFSTMT);
		}

		@Test
		void testHasBelowNoIfStatement_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.IFSTMT);
		}
	}

	@Nested
	@DisplayName("If-Expression-Tests")
	class IfExpressionTests {
		@Test
		void testHasBelowNoIfExpression_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(ConditionalType.CONDITIONALEXPR);
		}

		@Test
		void testHasBelowNoIfExpression_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(ConditionalType.CONDITIONALEXPR);
		}
	}

	@Nested
	@DisplayName("Switch-Statement-Tests")
	class SwitchStatementTests {
		@Test
		void testHasBelowNoSwitchStatement_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHSTMT);
		}

		@Test
		void testHasBelowNoSwitchStatement_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHSTMT);
		}
	}

	@Nested
	@DisplayName("Switch-Expression-Tests")
	class SwitchExpressionTests {
		@Test
		void testHasBelowNoSwitchExpression_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHEXPR);
		}

		@Test
		void testHasBelowNoSwitchExpression_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHEXPR);
		}
	}

	@Nested
	@DisplayName("Any-If-Tests")
	class AnyIfTests {
		@Test
		void testHasBelowNoAnyIf_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_IF);
		}

		@Test
		void testHasBelowNoAnyIf_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_IF);
		}
	}

	@Nested
	@DisplayName("Any-Switch-Tests")
	class AnySwitchTests {
		@Test
		void testHasBelowNoAnySwitch_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_SWITCH);
		}

		@Test
		void testHasBelowNoAnySwitch_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_SWITCH);
		}
	}

	@Nested
	@DisplayName("Any-Conditional-Tests")
	class AnyConditionalTests {
		@Test
		void testHasBelowNoAnyConditional_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY);
		}

		@Test
		void testHasBelowNoAnyConditional_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"conditionals", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY);
		}
	}

	@Nested
	@DisplayName("Class-Tests")
	class ClassTests {
		@Test
		void testHasBelowNoClass_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "classes", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.CLASS);
		}

		@Test
		void testHasBelowNoClass_Fail() {
			UnwantedNodesAssert
					.assertThat(Path
							.of("integration", "testuser", "subject", "structural", "astTestFiles", "classes", "yes")
							.toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.CLASS);
		}
	}

	@Nested
	@DisplayName("Record-Tests")
	class RecordTests {
		@Test
		void testHasBelowNoRecord_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "classes", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.RECORD);
		}

		@Test
		void testHasBelowNoRecord_Fail() {
			UnwantedNodesAssert
					.assertThat(Path
							.of("integration", "testuser", "subject", "structural", "astTestFiles", "classes", "yes")
							.toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.RECORD);
		}
	}

	@Nested
	@DisplayName("Any-Class-Tests")
	class AnyClassTests {
		@Test
		void testHasBelowNoAnyClass_Success() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "classes", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.ANY);
		}

		@Test
		void testHasBelowNoAnyClass_Fail() {
			UnwantedNodesAssert
					.assertThat(Path
							.of("integration", "testuser", "subject", "structural", "astTestFiles", "classes", "yes")
							.toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.ANY);
		}
	}

	@Nested
	@DisplayName("Assert-Exception-Handling-Tests")
	class AssertExceptionHandlingTests {
		@Test
		void testHasBelowNoAssertExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ASSERT);
		}

		@Test
		void testHasBelowNoAssertExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ASSERT);
		}
	}

	@Nested
	@DisplayName("Throw-Exception-Handling-Tests")
	class ThrowExceptionHandlingTests {
		@Test
		void testHasBelowNoThrowExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.THROW);
		}

		@Test
		void testHasBelowNoThrowExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.THROW);
		}
	}

	@Nested
	@DisplayName("Catch-Exception-Handling-Tests")
	class CatchExceptionHandlingTests {
		@Test
		void testHasBelowNoCatchExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.CATCH);
		}

		@Test
		void testHasBelowNoCatchExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.CATCH);
		}
	}

	@Nested
	@DisplayName("Any-Exception-Handling-Tests")
	class AnyExceptionHandlingTests {
		@Test
		void testHasBelowNoAnyExceptionHandling_Success() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "no").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ANY);
		}

		@Test
		void testHasBelowNoAnyExceptionHandling_Fail() {
			UnwantedNodesAssert
					.assertThat(Path.of("integration", "testuser", "subject", "structural", "astTestFiles",
							"exceptionHandlings", "yes").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ANY);
		}
	}

	@Nested
	@DisplayName("Error-Tests")
	class ErrorTests {
		@Test
		void testExpectedClassNameDoesNotExist() {
			UnwantedNodesAssert.assertThat(Path.of("this", "path", "does", "not", "exist").toString(), "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testExpectedClassNameIsNull() {
			UnwantedNodesAssert.assertThat(null, "de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testExpectedPackageNameDoesNotExist() {
			UnwantedNodesAssert.assertThat("integration", "this.package.name.does.not.exist")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testExpectedPackageNameIsNull() {
			UnwantedNodesAssert.assertThat("integration", null)
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testDelocatedBuildGradleFileDoesNotExist() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("this", "path", "does", "not", "exist").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testDelocatedBuildGradleFileIsNull() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(null).withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(LoopType.ANY);
		}

		@Test
		void testLevelIsNull() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedBuildGradleFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "build.gradle").toString())
					.withLanguageLevel(null).hasNo(LoopType.ANY);
		}
	}

	@Nested
	@DisplayName("Pom-Xml-Tests")
	class PomXmlTests {

		@Test
		void testDelocatedPomXmlFileDoesExist() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedPomXmlFile(Path.of("src", "test", "resources", "de", "tum", "in", "test",
							"integration", "testuser", "pom.xml").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
		}

		@Test
		void testDelocatedPomXmlFileDoesNotExist() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedPomXmlFile(Path.of("this", "path", "does", "not", "exist").toString())
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testDelocatedPomXmlFileIsNull() {
			UnwantedNodesAssert
					.assertThat(
							Path.of("integration", "testuser", "subject", "structural", "astTestFiles", "loops", "no")
									.toString(),
							"de.tum.in.test")
					.withDelocatedPomXmlFile(null).withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(LoopType.ANY);
		}
	}
}
