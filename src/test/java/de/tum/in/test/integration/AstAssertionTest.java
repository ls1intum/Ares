package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.*;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.AstAssertionUser;
import de.tum.in.test.testutilities.*;

@UserBased(AstAssertionUser.class)
public class AstAssertionTest {

	@UserTestResults
	private static Events tests;

	public static void setTests(Events tests) {
		AstAssertionTest.tests = tests;
	}

	@Nested
	@DisplayName("For-Loop-Test-Tests")
	class ForLoopTestTests {
		@TestTest
		void test_testHasBelowNoForLoop_Success() {
			String testHasBelowNoForLoop_Success = "testHasBelowNoForLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoForLoop_Fail() {
			String testHasBelowNoForLoop_Fail = "testHasBelowNoForLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoForLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - For-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 8 (column 3) and line 10 (column 3)"));
		}
	}

	@Nested
	@DisplayName("For-Each-Loop-Test-Tests")
	class ForEachLoopTestTests {
		@TestTest
		void test_testHasBelowNoForEachLoop_Success() {
			String testHasBelowNoForEachLoop_Success = "testHasBelowNoForEachLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoForEachLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoForEachLoop_Fail() {
			String testHasBelowNoForEachLoop_Fail = "testHasBelowNoForEachLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoForEachLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - For-Each-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 14 (column 3) and line 16 (column 3)"));
		}
	}

	@Nested
	@DisplayName("While-Loop-Test-Tests")
	class WhileLoopTestTests {

		@TestTest
		void test_testHasBelowNoWhileLoop_Success() {
			String testHasBelowNoWhileLoop_Success = "testHasBelowNoWhileLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoWhileLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoWhileLoop_Fail() {
			String testHasBelowNoWhileLoop_Fail = "testHasBelowNoWhileLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoWhileLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - While-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 21 (column 3) and line 24 (column 3)"));
		}
	}

	@Nested
	@DisplayName("Do-While-Loop-Test-Tests")
	class DoWhileLoopTestTests {

		@TestTest
		void test_testHasBelowNoDoWhileLoop_Success() {
			String testHasBelowNoDoWhileLoop_Success = "testHasBelowNoDoWhileLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoDoWhileLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoDoWhileLoop_Fail() {
			String testHasBelowNoDoWhileLoop_Fail = "testHasBelowNoDoWhileLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoDoWhileLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - Do-While-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 29 (column 3) and line 32 (column 18)"));
		}
	}

	@Nested
	@DisplayName("Any-For-Loop-Test-Tests")
	class AnyForLoopTestTests {

		@TestTest
		void test_testHasBelowNoAnyForLoop_Success() {
			String testHasBelowNoAnyForLoop_Success = "testHasBelowNoAnyForLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyForLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyForLoop_Fail() {
			String testHasBelowNoAnyForLoop_Fail = "testHasBelowNoAnyForLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoAnyForLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - For-Statement was found:"
									+ System.lineSeparator() + "   - Between line 8 (column 3) and line 10 (column 3)"
									+ System.lineSeparator() + "  - For-Each-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 14 (column 3) and line 16 (column 3)"));
		}
	}

	@Nested
	@DisplayName("Any-While-Loop-Test-Tests")
	class AnyWhileLoopTestTests {

		@TestTest
		void test_testHasBelowNoAnyWhileLoop_Success() {
			String testHasBelowNoAnyWhileLoop_Success = "testHasBelowNoAnyWhileLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyWhileLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyWhileLoop_Fail() {
			String testHasBelowNoAnyWhileLoop_Fail = "testHasBelowNoAnyWhileLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoAnyWhileLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - While-Statement was found:"
									+ System.lineSeparator() + "   - Between line 21 (column 3) and line 24 (column 3)"
									+ System.lineSeparator() + "  - Do-While-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 29 (column 3) and line 32 (column 18)"));
		}
	}

	@Nested
	@DisplayName("Any-Loop-Test-Tests")
	class AnyLoopTestTests {

		@TestTest
		void test_testHasBelowNoAnyLoop_Success() {
			String testHasBelowNoAnyLoop_Success = "testHasBelowNoAnyLoop_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyLoop_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyLoop_Fail() {
			String testHasBelowNoAnyLoop_Fail = "testHasBelowNoAnyLoop_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoAnyLoop_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "loops", "yes",
											"ClassWithAnyKindsOfLoops.java")
									+ ":" + System.lineSeparator() + "  - For-Statement was found:"
									+ System.lineSeparator() + "   - Between line 8 (column 3) and line 10 (column 3)"
									+ System.lineSeparator() + "  - For-Each-Statement was found:"
									+ System.lineSeparator() + "   - Between line 14 (column 3) and line 16 (column 3)"
									+ System.lineSeparator() + "  - While-Statement was found:" + System.lineSeparator()
									+ "   - Between line 21 (column 3) and line 24 (column 3)" + System.lineSeparator()
									+ "  - Do-While-Statement was found:" + System.lineSeparator()
									+ "   - Between line 29 (column 3) and line 32 (column 18)"));
		}
	}

	@Nested
	@DisplayName("If-Statement-Test-Tests")
	class IfStatementTestTests {

		@TestTest
		void test_testHasBelowNoIfStatement_Success() {
			String testHasBelowNoIfConditional_Success = "testHasBelowNoIfStatement_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoIfConditional_Success));
		}

		@TestTest
		void test_testHasBelowNoIfStatement_Fail() {
			String testHasBelowNoIfConditional_Fail = "testHasBelowNoIfStatement_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoIfConditional_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "conditionals", "yes",
											"ClassWithAnyKindsOfConditionals.java")
									+ ":" + System.lineSeparator() + "  - If-Statement was found:"
									+ System.lineSeparator() + "   - Between line 7 (column 3) and line 13 (column 3)"
									+ System.lineSeparator()
									+ "   - Between line 9 (column 10) and line 13 (column 3)"));
		}
	}

	@Nested
	@DisplayName("If-Expression-Test-Tests")
	class IfExpressionTestTests {

		@TestTest
		void test_testHasBelowNoIfExpression_Success() {
			String testHasBelowNoIfConditional_Success = "testHasBelowNoIfExpression_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoIfConditional_Success));
		}

		@TestTest
		void test_testHasBelowNoIfExpression_Fail() {
			String testHasBelowNoIfConditional_Fail = "testHasBelowNoIfExpression_Fail";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoIfConditional_Fail,
					AssertionError.class,
					"Unwanted statement found:" + System.lineSeparator() + " - In "
							+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration", "testuser",
									"subject", "structural", "astTestFiles", "conditionals", "yes",
									"ClassWithAnyKindsOfConditionals.java")
							+ ":" + System.lineSeparator() + "  - If-Expression was found:" + System.lineSeparator()
							+ "   - Between line 18 (column 22) and line 18 (column 64)" + System.lineSeparator()
							+ "   - Between line 18 (column 42) and line 18 (column 63)"));
		}
	}

	@Nested
	@DisplayName("Switch-Statement-Test-Tests")
	class SwitchStatementTestTests {

		@TestTest
		void test_testHasBelowNoSwitchStatement_Success() {
			String testHasBelowNoSwitchConditional_Success = "testHasBelowNoSwitchStatement_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoSwitchConditional_Success));
		}

		@TestTest
		void test_testHasBelowNoSwitchStatement_Fail() {
			String testHasBelowNoSwitchConditional_Fail = "testHasBelowNoSwitchStatement_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoSwitchConditional_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "conditionals", "yes",
											"ClassWithAnyKindsOfConditionals.java")
									+ ":" + System.lineSeparator() + "  - Switch-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 23 (column 3) and line 33 (column 3)"));
		}
	}

	@Nested
	@DisplayName("Switch-Expression-Test-Tests")
	class SwitchExpressionTestTests {

		@TestTest
		void test_testHasBelowNoSwitchExpression_Success() {
			String testHasBelowNoSwitchConditional_Success = "testHasBelowNoSwitchExpression_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoSwitchConditional_Success));
		}

		@TestTest
		void test_testHasBelowNoSwitchExpression_Fail() {
			String testHasBelowNoSwitchConditional_Fail = "testHasBelowNoSwitchExpression_Fail";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoSwitchConditional_Fail,
					AssertionError.class,
					"Unwanted statement found:" + System.lineSeparator() + " - In "
							+ Path.of("src", "test", "resources", "de", "tum", "in", "test", "integration", "testuser",
									"javaClassesWithUnsupportedFeatures", "conditionals", "yes",
									"ClassWithAnyKindsOfUnsupportedConditionals.java")
							+ ":" + System.lineSeparator() + "  - Switch-Expression was found:" + System.lineSeparator()
							+ "   - Between line 38 (column 28) and line 43 (column 9)"));
		}
	}

	@Nested
	@DisplayName("Any-If-Test-Tests")
	class AnyIfTestTests {

		@TestTest
		void test_testHasBelowNoAnyIf_Success() {
			String testHasBelowNoAnyClass_Success = "testHasBelowNoAnyIf_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyClass_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyIf_Fail() {
			String testHasBelowNoAnyClass_Fail = "testHasBelowNoAnyIf_Fail";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAnyClass_Fail, AssertionError.class,
					"Unwanted statement found:" + System.lineSeparator() + " - In "
							+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration", "testuser",
									"subject", "structural", "astTestFiles", "conditionals", "yes",
									"ClassWithAnyKindsOfConditionals.java")
							+ ":" + System.lineSeparator() + "  - If-Statement was found:" + System.lineSeparator()
							+ "   - Between line 7 (column 3) and line 13 (column 3)" + System.lineSeparator()
							+ "   - Between line 9 (column 10) and line 13 (column 3)" + System.lineSeparator()
							+ "  - If-Expression was found:" + System.lineSeparator()
							+ "   - Between line 18 (column 22) and line 18 (column 64)" + System.lineSeparator()
							+ "   - Between line 18 (column 42) and line 18 (column 63)"));
		}
	}

	@Nested
	@DisplayName("Any-Switch-Test-Tests")
	class AnySwitchTestTests {

		@TestTest
		void test_testHasBelowNoAnySwitch_Success() {
			String testHasBelowNoAnyClass_Success = "testHasBelowNoAnySwitch_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyClass_Success));
		}

		@TestTest
		void test_testHasBelowNoAnySwitch_Fail() {
			String testHasBelowNoAnyClass_Fail = "testHasBelowNoAnySwitch_Fail";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAnyClass_Fail, AssertionError.class,
					"Unwanted statement found:" + System.lineSeparator() + " - In "
							+ Path.of("src", "test", "resources", "de", "tum", "in", "test", "integration", "testuser",
									"javaClassesWithUnsupportedFeatures", "conditionals", "yes",
									"ClassWithAnyKindsOfUnsupportedConditionals.java")
							+ ":" + System.lineSeparator() + "  - Switch-Statement was found:" + System.lineSeparator()
							+ "   - Between line 23 (column 9) and line 33 (column 9)" + System.lineSeparator()
							+ "  - Switch-Expression was found:" + System.lineSeparator()
							+ "   - Between line 38 (column 28) and line 43 (column 9)"));
		}
	}

	@Nested
	@DisplayName("Any-Conditional-Test-Tests")
	class AnyConditionalTestTests {

		@TestTest
		void test_testHasBelowNoAnyConditional_Success() {
			String testHasBelowNoAnyConditional_Success = "testHasBelowNoAnyConditional_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyConditional_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyConditional_Fail() {
			String testHasBelowNoAnyConditional_Fail = "testHasBelowNoAnyConditional_Fail";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAnyConditional_Fail,
					AssertionError.class,
					"Unwanted statement found:" + System.lineSeparator() + " - In "
							+ Path.of("src", "test", "resources", "de", "tum", "in", "test", "integration", "testuser",
									"javaClassesWithUnsupportedFeatures", "conditionals", "yes",
									"ClassWithAnyKindsOfUnsupportedConditionals.java")
							+ ":" + System.lineSeparator() + "  - If-Statement was found:" + System.lineSeparator()
							+ "   - Between line 7 (column 9) and line 13 (column 9)" + System.lineSeparator()
							+ "   - Between line 9 (column 16) and line 13 (column 9)" + System.lineSeparator()
							+ "  - If-Expression was found:" + System.lineSeparator()
							+ "   - Between line 18 (column 28) and line 18 (column 70)" + System.lineSeparator()
							+ "   - Between line 18 (column 48) and line 18 (column 69)" + System.lineSeparator()
							+ "  - Switch-Statement was found:" + System.lineSeparator()
							+ "   - Between line 23 (column 9) and line 33 (column 9)" + System.lineSeparator()
							+ "  - Switch-Expression was found:" + System.lineSeparator()
							+ "   - Between line 38 (column 28) and line 43 (column 9)"

			));
		}
	}

	@Nested
	@DisplayName("Class-Test-Tests")
	class ClassTestTests {

		@TestTest
		void test_testHasBelowNoClass_Success() {
			String testHasBelowNoClass_Success = "testHasBelowNoClass_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoClass_Success));
		}

		@TestTest
		void test_testHasBelowNoClass_Fail() {
			String testHasBelowNoClass_Fail = "testHasBelowNoClass_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoClass_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "classes", "yes",
											"ClassWithAnyKindsOfClasses.java")
									+ ":" + System.lineSeparator() + "  - Local-Class-Statement was found:"
									+ System.lineSeparator() + "   - Between line 6 (column 3) and line 7 (column 3)"));
		}
	}

	@Nested
	@DisplayName("Record-Test-Tests")
	class RecordTestTests {

		@TestTest
		void test_testHasBelowNoRecord_Success() {
			String testHasBelowNoRecord_Success = "testHasBelowNoRecord_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoRecord_Success));
		}

		@TestTest
		void test_testHasBelowNoRecord_Fail() {
			String testHasBelowNoRecord_Fail = "testHasBelowNoRecord_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoRecord_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "resources", "de", "tum", "in", "test", "integration",
											"testuser", "javaClassesWithUnsupportedFeatures", "classes", "yes",
											"ClassWithAnyKindsOfUnsupportedClasses.java")
									+ ":" + System.lineSeparator() + "  - Local-Record-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 11 (column 9) and line 12 (column 9)"));
		}
	}

	@Nested
	@DisplayName("Any-Class-Test-Tests")
	class AnyClassTestTests {

		@TestTest
		void test_testHasBelowNoAnyClass_Success() {
			String testHasBelowNoClass_Success = "testHasBelowNoAnyClass_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoClass_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyClass_Fail() {
			String testHasBelowNoClass_Fail = "testHasBelowNoAnyClass_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoClass_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "resources", "de", "tum", "in", "test", "integration",
											"testuser", "javaClassesWithUnsupportedFeatures", "classes", "yes",
											"ClassWithAnyKindsOfUnsupportedClasses.java")
									+ ":" + System.lineSeparator() + "  - Local-Class-Statement was found:"
									+ System.lineSeparator() + "   - Between line 6 (column 9) and line 7 (column 9)"
									+ System.lineSeparator() + "  - Local-Record-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 11 (column 9) and line 12 (column 9)"));
		}
	}

	@Nested
	@DisplayName("Assert-Exception-Handling-Test-Tests")
	class AssertExceptionHandlingTestTests {

		@TestTest
		void test_testHasBelowNoAssertExceptionHandling_Success() {
			String testHasBelowNoAssertExceptionHandling_Success = "testHasBelowNoAssertExceptionHandling_Success";
			tests.assertThatEvents().haveExactly(1,
					finishedSuccessfully(testHasBelowNoAssertExceptionHandling_Success));
		}

		@TestTest
		void test_testHasBelowNoAssertExceptionHandling_Fail() {
			String testHasBelowNoAssertExceptionHandling_Fail = "testHasBelowNoAssertExceptionHandling_Fail";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testHasBelowNoAssertExceptionHandling_Fail,
					AssertionError.class,
					"Unwanted statement found:" + System.lineSeparator() + " - In "
							+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration", "testuser",
									"subject", "structural", "astTestFiles", "exceptionHandlings", "yes",
									"ClassWithAnyKindsOfExceptionHandlings.java")
							+ ":" + System.lineSeparator() + "  - Assert-Statement was found:" + System.lineSeparator()
							+ "   - Between line 6 (column 3) and line 6 (column 16)"));
		}
	}

	@Nested
	@DisplayName("Throw-Exception-Handling-Test-Tests")
	class ThrowExceptionHandlingTestTests {

		@TestTest
		void test_testHasBelowNoThrowExceptionHandling_Success() {
			String testHasBelowNoThrowExceptionHandling_Success = "testHasBelowNoThrowExceptionHandling_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoThrowExceptionHandling_Success));
		}

		@TestTest
		void test_testHasBelowNoThrowExceptionHandling_Fail() {
			String testHasBelowNoThrowExceptionHandling_Fail = "testHasBelowNoThrowExceptionHandling_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoThrowExceptionHandling_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "exceptionHandlings",
											"yes", "ClassWithAnyKindsOfExceptionHandlings.java")
									+ ":" + System.lineSeparator() + "  - Throw-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 10 (column 3) and line 10 (column 54)"));
		}
	}

	@Nested
	@DisplayName("Catch-Exception-Handling-Test-Tests")
	class CatchExceptionHandlingTestTests {

		@TestTest
		void test_testHasBelowNoCatchExceptionHandling_Success() {
			String testHasBelowNoCatchExceptionHandling_Success = "testHasBelowNoCatchExceptionHandling_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoCatchExceptionHandling_Success));
		}

		@TestTest
		void test_testHasBelowNoCatchExceptionHandling_Fail() {
			String testHasBelowNoCatchExceptionHandling_Fail = "testHasBelowNoCatchExceptionHandling_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoCatchExceptionHandling_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "exceptionHandlings",
											"yes", "ClassWithAnyKindsOfExceptionHandlings.java")
									+ ":" + System.lineSeparator() + "  - Catch-Statement was found:"
									+ System.lineSeparator()
									+ "   - Between line 16 (column 5) and line 17 (column 3)"));
		}
	}

	@Nested
	@DisplayName("Any-Exception-Handling-Test-Tests")
	class AnyExceptionHandlingTestTests {

		@TestTest
		void test_testHasBelowNoAnyExceptionHandling_Success() {
			String testHasBelowNoAnyExceptionHandling_Success = "testHasBelowNoAnyExceptionHandling_Success";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testHasBelowNoAnyExceptionHandling_Success));
		}

		@TestTest
		void test_testHasBelowNoAnyExceptionHandling_Fail() {
			String testHasBelowNoAnyExceptionHandling_Fail = "testHasBelowNoAnyExceptionHandling_Fail";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testHasBelowNoAnyExceptionHandling_Fail, AssertionError.class,
							"Unwanted statement found:" + System.lineSeparator() + " - In "
									+ Path.of("src", "test", "java", "de", "tum", "in", "test", "integration",
											"testuser", "subject", "structural", "astTestFiles", "exceptionHandlings",
											"yes", "ClassWithAnyKindsOfExceptionHandlings.java")
									+ ":" + System.lineSeparator() + "  - Assert-Statement was found:"
									+ System.lineSeparator() + "   - Between line 6 (column 3) and line 6 (column 16)"
									+ System.lineSeparator() + "  - Throw-Statement was found:" + System.lineSeparator()
									+ "   - Between line 10 (column 3) and line 10 (column 54)" + System.lineSeparator()
									+ "  - Catch-Statement was found:" + System.lineSeparator()
									+ "   - Between line 16 (column 5) and line 17 (column 3)"));
		}
	}

	@Nested
	@DisplayName("PomXml-Test-Tests")
	class PomXmlTestTests {

		@TestTest
		void test_testPomXmlFileDoesExist() {
			String testPomXmlFileDoesExist = "testPomXmlFileDoesExist";
			tests.assertThatEvents().haveExactly(1, finishedSuccessfully(testPomXmlFileDoesExist));
		}
	}

	@Nested
	@DisplayName("Error-Test-Tests")
	class ErrorTestTests {

		@TestTest
		void test_testPathDoesNotExist() {
			String testPathDoesNotExist = "testPathDoesNotExist";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testPathDoesNotExist, AssertionError.class,
					"The source directory " + Path.of("this/path/does/not/exist") + " does not exist"));
		}

		@TestTest
		void test_testPathIsNull() {
			String testPathIsNull = "testPathIsNull";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testPathIsNull, NullPointerException.class,
					"The given source path must not be null."));
		}

		@TestTest
		void test_testPackageDoesNotExist() {
			String testPackageDoesNotExist = "testPackageDoesNotExist";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testPackageDoesNotExist, AssertionError.class, "The source directory "
							+ Path.of("src/test/java/this/package/name/does/not/exist") + " does not exist"));
		}

		@TestTest
		void test_testPackageIsNull() {
			String testPackageIsNull = "testPackageIsNull";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testPackageIsNull, NullPointerException.class,
					"The package name must not be null."));
		}

		@TestTest
		void test_testBuildGradleFileDoesNotExist() {
			String testBuildGradleFileDoesNotExist = "testBuildGradleFileDoesNotExist";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testBuildGradleFileDoesNotExist, AssertionError.class,
							"Could not find project sources folder. Make sure the build file is configured correctly."
									+ " If it is not located in the execution folder directly,"
									+ " set the location using AresConfiguration methods."));
		}

		@TestTest
		void test_testBuildGradleFileIsNull() {
			String testBuildGradleFileIsNull = "testBuildGradleFileIsNull";
			tests.assertThatEvents().haveExactly(1,
					testFailedWith(testBuildGradleFileIsNull, AssertionError.class,
							"Could not find project sources folder. Make sure the build file is configured correctly."
									+ " If it is not located in the execution folder directly,"
									+ " set the location using AresConfiguration methods."));
		}

		@TestTest
		void test_testLevelIsNull() {
			String testLevelIsNull = "testLevelIsNull";
			tests.assertThatEvents().haveExactly(1, testFailedWith(testLevelIsNull, AssertionError.class,
					"The 'level' is not set. Please use UnwantedNodesAssert.withLanguageLevel(LanguageLevel)."));
		}
	}
}
