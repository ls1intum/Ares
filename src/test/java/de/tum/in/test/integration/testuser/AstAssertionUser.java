package de.tum.in.test.integration.testuser;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.*;

import com.github.javaparser.ParserConfiguration;

import de.tum.in.test.api.*;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@Public
@UseLocale("en")
@StrictTimeout(5)
public class AstAssertionUser {

	private static final String BASE_PACKAGE = "de.tum.in.test.integration.testuser.subject.structural.astTestFiles";
	private static final Path UNSUPPORTED_LEVEL_BASE_PATH = Path.of("src", "test", "resources", "de", "tum", "in",
			"test", "integration", "testuser", "javaClassesWithUnsupportedFeatures");

	private String mavenOld;
	private String gradleOld;

	@BeforeEach
	void configureProjectBuildFile() {
		mavenOld = AresConfiguration.getPomXmlPath();
		gradleOld = AresConfiguration.getBuildGradlePath();
		AresConfiguration.setPomXmlPath(null);
		AresConfiguration.setBuildGradlePath("src/test/resources/de/tum/in/test/integration/testuser/build.gradle");
	}

	@AfterEach
	void unconfigureProjectBuildFile() {
		AresConfiguration.setPomXmlPath(mavenOld);
		AresConfiguration.setBuildGradlePath(gradleOld);
	}

	@Nested
	@DisplayName("For-Loop-Tests")
	class ForLoopTests {
		@Test
		void testHasBelowNoForLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
		}

		@Test
		void testHasBelowNoForLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
		}
	}

	@Nested
	@DisplayName("For-Each-Loop-Tests")
	class ForEachLoopTests {
		@Test
		void testHasBelowNoForEachLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FOR_EACHSTMT);
		}

		@Test
		void testHasBelowNoForEachLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FOR_EACHSTMT);
		}
	}

	@Nested
	@DisplayName("While-Loop-Tests")
	class WhileLoopTests {
		@Test
		void testHasBelowNoWhileLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.WHILESTMT);
		}

		@Test
		void testHasBelowNoWhileLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.WHILESTMT);
		}
	}

	@Nested
	@DisplayName("Do-While-Loop-Tests")
	class DoWhileLoopTests {
		@Test
		void testHasBelowNoDoWhileLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.DO_WHILESTMT);
		}

		@Test
		void testHasBelowNoDoWhileLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.DO_WHILESTMT);
		}
	}

	@Nested
	@DisplayName("Any-For-Loop-Tests")
	class AnyForLoopTests {
		@Test
		void testHasBelowNoAnyForLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_FOR);
		}

		@Test
		void testHasBelowNoAnyForLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_FOR);
		}
	}

	@Nested
	@DisplayName("Any-While-Loop-Tests")
	class AnyWhileLoopTests {
		@Test
		void testHasBelowNoAnyWhileLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_WHILE);
		}

		@Test
		void testHasBelowNoAnyWhileLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_WHILE);
		}
	}

	@Nested
	@DisplayName("Any-Loop-Tests")
	class AnyLoopTests {
		@Test
		void testHasBelowNoAnyLoop_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testHasBelowNoAnyLoop_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}
	}

	@Nested
	@DisplayName("If-Statement-Tests")
	class IfStatementTests {
		@Test
		void testHasBelowNoIfStatement_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.IFSTMT);
		}

		@Test
		void testHasBelowNoIfStatement_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.IFSTMT);
		}
	}

	@Nested
	@DisplayName("If-Expression-Tests")
	class IfExpressionTests {
		@Test
		void testHasBelowNoIfExpression_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(ConditionalType.CONDITIONALEXPR);
		}

		@Test
		void testHasBelowNoIfExpression_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(ConditionalType.CONDITIONALEXPR);
		}
	}

	@Nested
	@DisplayName("Switch-Statement-Tests")
	class SwitchStatementTests {
		@Test
		void testHasBelowNoSwitchStatement_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHSTMT);
		}

		@Test
		void testHasBelowNoSwitchStatement_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHSTMT);
		}
	}

	@Nested
	@DisplayName("Switch-Expression-Tests")
	class SwitchExpressionTests {
		@Test
		void testHasBelowNoSwitchExpression_Success() throws IOException {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("conditionals", "no")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHEXPR);
		}

		@Test
		void testHasBelowNoSwitchExpression_Fail() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("conditionals", "yes")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.SWITCHEXPR);
		}
	}

	@Nested
	@DisplayName("Any-If-Tests")
	class AnyIfTests {
		@Test
		void testHasBelowNoAnyIf_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_IF);
		}

		@Test
		void testHasBelowNoAnyIf_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".conditionals.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_IF);
		}
	}

	@Nested
	@DisplayName("Any-Switch-Tests")
	class AnySwitchTests {
		@Test
		void testHasBelowNoAnySwitch_Success() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("conditionals", "no")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_SWITCH);
		}

		@Test
		void testHasBelowNoAnySwitch_Fail() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("conditionals", "yes")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY_SWITCH);
		}
	}

	@Nested
	@DisplayName("Any-Conditional-Tests")
	class AnyConditionalTests {
		@Test
		void testHasBelowNoAnyConditional_Success() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("conditionals", "no")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY);
		}

		@Test
		void testHasBelowNoAnyConditional_Fail() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("conditionals", "yes")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ConditionalType.ANY);
		}
	}

	@Nested
	@DisplayName("Class-Tests")
	class ClassTests {
		@Test
		void testHasBelowNoClass_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".classes.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.CLASS);
		}

		@Test
		void testHasBelowNoClass_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".classes.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.CLASS);
		}
	}

	@Nested
	@DisplayName("Record-Tests")
	class RecordTests {
		@Test
		void testHasBelowNoRecord_Success() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("classes", "no")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.RECORD);
		}

		@Test
		void testHasBelowNoRecord_Fail() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("classes", "yes")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.RECORD);
		}
	}

	@Nested
	@DisplayName("Any-Class-Tests")
	class AnyClassTests {
		@Test
		void testHasBelowNoAnyClass_Success() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("classes", "no")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.ANY);
		}

		@Test
		void testHasBelowNoAnyClass_Fail() {
			UnwantedNodesAssert.assertThatSourcesIn(UNSUPPORTED_LEVEL_BASE_PATH.resolve(Path.of("classes", "yes")))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ClassType.ANY);
		}
	}

	@Nested
	@DisplayName("Assert-Exception-Handling-Tests")
	class AssertExceptionHandlingTests {
		@Test
		void testHasBelowNoAssertExceptionHandling_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ASSERT);
		}

		@Test
		void testHasBelowNoAssertExceptionHandling_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ASSERT);
		}
	}

	@Nested
	@DisplayName("Throw-Exception-Handling-Tests")
	class ThrowExceptionHandlingTests {
		@Test
		void testHasBelowNoThrowExceptionHandling_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.THROW);
		}

		@Test
		void testHasBelowNoThrowExceptionHandling_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.THROW);
		}
	}

	@Nested
	@DisplayName("Catch-Exception-Handling-Tests")
	class CatchExceptionHandlingTests {
		@Test
		void testHasBelowNoCatchExceptionHandling_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.CATCH);
		}

		@Test
		void testHasBelowNoCatchExceptionHandling_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.CATCH);
		}
	}

	@Nested
	@DisplayName("Any-Exception-Handling-Tests")
	class AnyExceptionHandlingTests {
		@Test
		void testHasBelowNoAnyExceptionHandling_Success() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ANY);
		}

		@Test
		void testHasBelowNoAnyExceptionHandling_Fail() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".exceptionHandlings.yes")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(ExceptionHandlingType.ANY);
		}
	}

	@Nested
	@DisplayName("PomXml-Tests")
	class PomXmlTests {
		@Test
		void testPomXmlFileDoesExist() {
			AresConfiguration.setBuildGradlePath(null);
			AresConfiguration.setPomXmlPath("src/test/resources/de/tum/in/test/integration/testuser/pom.xml");
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(BASE_PACKAGE + ".loops.no")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
		}
	}

	@Nested
	@DisplayName("Error-Tests")
	class ErrorTests {

		@Test
		void testPathDoesNotExist() {
			UnwantedNodesAssert.assertThatSourcesIn(Path.of("this", "path", "does", "not", "exist"))
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testPathIsNull() {
			UnwantedNodesAssert.assertThatSourcesIn(null).withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(LoopType.ANY);
		}

		@Test
		void testPackageDoesNotExist() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage("this.package.name.does.not.exist")
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testPackageIsNull() {
			UnwantedNodesAssert.assertThatProjectSources().withinPackage(null)
					.withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY);
		}

		@Test
		void testBuildGradleFileDoesNotExist() {
			AresConfiguration.setBuildGradlePath("does/not/exist/build.bradle");
			UnwantedNodesAssert.assertThatProjectSources().withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(LoopType.ANY);
		}

		@Test
		void testBuildGradleFileIsNull() {
			AresConfiguration.setBuildGradlePath(null);
			UnwantedNodesAssert.assertThatProjectSources().withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
					.hasNo(LoopType.ANY);
		}

		@Test
		void testLevelIsNull() {
			UnwantedNodesAssert.assertThatProjectSources().hasNo(LoopType.ANY);
		}
	}
}
