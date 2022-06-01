package de.tum.in.test.integration.testuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.api.*;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.io.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.InputOutputPenguin;

@Public
@UseLocale("en")
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class InputOutputUser {

	public static class CustomManager implements IOManager<StringBuilder> {

		private PrintStream previousOut;
		private StringBuilder current;

		public CustomManager() {
		}

		@Override
		public void afterTestExecution(AresIOContext context) {
			System.setOut(previousOut);
			current = null;
		}

		@Override
		@SuppressWarnings("resource")
		public void beforeTestExecution(AresIOContext context) {
			current = new StringBuilder();
			var output = new ByteArrayOutputStream() {
				@Override
				public void flush() throws IOException {
					super.flush();
					current.append(toString(StandardCharsets.UTF_8));
					reset();
				}
			};
			previousOut = System.out;
			System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
		}

		@Override
		public Class<StringBuilder> getControllerClass() {
			return StringBuilder.class;
		}

		@Override
		public StringBuilder getControllerInstance(AresIOContext context) {
			return current;
		}
	}

	public static class WrongCustomManager extends CustomManager {
		@SuppressWarnings("unused")
		public WrongCustomManager(int x) {
			// one parameter instead of none
		}
	}

	@Test
	@WithIOManager(CustomManager.class)
	void customStringBuilderManager(StringBuilder output) {
		InputOutputPenguin.writeTwoLines();
		assertThat(output).contains("Nieder mit den Eisbären!", "Pinguine sind die Besten!");
	}

	@Test
	void makeUTF8Error() throws IOException {
		System.out.write(new byte[] { 'P', 'i', 'n', 'g', 'u', (byte) 0xFF });
	}

	@Test
	@WithIOManager(WithIOManager.None.class)
	void noneManagerInvalidParameter(IOTester tester) {
		tester.provideInputLines("");
		InputOutputPenguin.readTwoTimes();
	}

	@Test
	void testLinesMatch(IOTester tester) {
		System.out.println("ABC ((");
		System.out.println("x");
		System.out.println("y");
		System.out.println(">> HERE >>");
		System.out.println("|| NONE ||");
		System.out.println("123");
		System.out.println("AAAAA");
		System.out.println("BBBBB");
		System.out.println(">> 2");
		System.out.println("|| (( |");
		System.out.println("Something");

		tester.out().assertLinesMatch("This should not pass", //
				"ABC ((", //
				">> some lines >>", //
				"\\>> HERE >>", //
				"\\|| NONE ||", //
				"||\\d+||", //
				">> 2 >>", //
				">> 2", //
				"|| (( |", //
				"Something");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass", //
					"ABC ((", //
					">> some lines >>", //
					"\\>> HERE >>");
		}).isInstanceOf(AssertionFailedError.class).hasMessageContainingAll("more actual lines than expected: 7");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass", //
					"ABC ((", //
					">> some lines >>", //
					"\\>> HERE >>", //
					"\\|| NONE ||", //
					"||\\d+XX||", //
					">> 2 >>", //
					">> 2", //
					"|| (( |", //
					"Something");
		}).isInstanceOf(AssertionFailedError.class)
				.hasMessageContainingAll("expected: matches regular expression: `d+XX`", "actual: `123`");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass", //
					"ABC ((", //
					">> some lines >>", //
					"\\>> ---- >>");
		}).isInstanceOf(AssertionFailedError.class).hasMessageContainingAll("didn't find: `>> ---- >>`");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass", //
					"ABC XXX");
		}).isInstanceOf(AssertionFailedError.class).hasMessageContainingAll("expected: `ABC XXX`", "actual: `ABC ((`");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass",
					new OutputTestOptions[] { OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE }, //
					"ABC ((", //
					">> some lines >>", //
					"\\>> HERE >>", //
					"\\|| NONE ||", //
					"||\\d+||", //
					">> 2 >>", //
					">> 2", //
					"|| (( |", //
					"Something");
		}).isInstanceOf(AssertionFailedError.class).hasMessageContainingAll("more actual lines than expected: 1");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass", //
					">> 11 >>", //
					"ABCDEF");
		}).isInstanceOf(AssertionFailedError.class)
				.hasMessageContainingAll("expected line #2:`ABCDEF` not found - actual lines depleted");

		assertThatThrownBy(() -> {
			tester.out().assertLinesMatch("This should not pass", //
					">> 12 >>", //
					"ABCDEF");
		}).isInstanceOf(AssertionFailedError.class).hasMessageContainingAll(
				"This should not pass ==> fast-forward(12) error: not enough actual lines remaining (11)");
	}

	@Test
	void testPenguin1(IOTester tester) {
		InputOutputPenguin.writeTwoLines();

		List<Line> lines = tester.out().getLines();

		if (lines.isEmpty())
			fail("Es wird nichts ausgegeben!");

		String firstLine = lines.get(0).text();
		assertEquals("Pinguine sind die Besten!", firstLine);
	}

	@Test
	void testPenguin2(IOTester tester) {
		InputOutputPenguin.writeTwoLines();

		List<Line> lines = tester.out().getLines();

		String secondLine = lines.get(0).text();
		assertEquals("Pinguine sind  die Besten!", secondLine);
	}

	@Test
	void testPolarBear(IOTester tester) {
		InputOutputPenguin.writeTwoLines();

		List<Line> lines = tester.out().getLines();

		if (lines.size() < 2)
			fail("Es wird keine zweite Zeile ausgegeben!");

		tester.err().assertThat().isEmpty();
		tester.out().assertThatLines().element(1).isEqualTo("Nieder mit den Eisbären!");
		tester.out().assertThat(OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE).hasLineCount(2)
				.isEqualTo("Pinguine sind die Besten!\nNieder mit den Eisbären!\n");
	}

	@Test
	void testSquareCorrect(IOTester tester) {
		tester.provideInputLines("5");

		InputOutputPenguin.calculateSquare();

		var out = tester.out().getLines();

		assertEquals("Keine Fehlerausgabe erwartet", 3, out.size());
		assertEquals("Zahl eingeben:", out.get(0).text());
		assertEquals("Ausgabe:", out.get(1).text());
		assertEquals("25", out.get(2).text());
	}

	@Test
	void testSquareWrong(IOTester tester) {
		InputOutputPenguin.calculateSquare();

		assertEquals("Keine Fehlerausgabe erwartet", 0, tester.err().getLines().size());
	}

	@Test
	@MirrorOutput(maxCharCount = 10, value = MirrorOutputPolicy.DISABLED)
	void testTooManyChars() {
		InputOutputPenguin.writeTwoLines();
	}

	@Test
	void testTooManyReads(IOTester tester) {
		tester.provideInputLines("12");

		InputOutputPenguin.readTwoTimes();
	}

	@Test
	@WithIOManager(WrongCustomManager.class)
	void wrongCustomManager(StringBuilder output) {
		InputOutputPenguin.writeTwoLines();
		assertThat(output).contains("Nieder mit den Eisbären!", "Pinguine sind die Besten!");
	}
}
