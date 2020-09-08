package de.tum.in.testuser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.io.Line;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.testuser.subject.InputOutputPenguin;

@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class InputOutputUser {

	@PublicTest
	void makeUTF8Error() throws IOException {
		System.out.write(new byte[] { 'P', 'i', 'n', 'g', 'u', (byte) 0xFF });
	}

	@PublicTest
	void testPenguin1(IOTester tester) {

		InputOutputPenguin.writeTwoLines();

		List<Line> lines = tester.getOutput();

		if (lines.isEmpty())
			fail("Es wird nichts ausgegeben!");

		String firstLine = lines.get(0).text();
		assertEquals("Pinguine sind die Besten!", firstLine);
	}

	@PublicTest
	void testPenguin2(IOTester tester) {

		InputOutputPenguin.writeTwoLines();

		List<Line> lines = tester.getOutput();

		String secondLine = lines.get(0).text();
		assertEquals("Pinguine sind  die Besten!", secondLine);
	}

	@PublicTest
	void testPolarBear(IOTester tester) {

		InputOutputPenguin.writeTwoLines();

		List<Line> lines = tester.getOutput();

		if (lines.size() < 2)
			fail("Es wird keine zweite Zeile ausgegeben!");

		String secondLine = lines.get(1).text();
		assertEquals("Nieder mit den Eisbären!", secondLine);
	}

	@PublicTest
	void testSquareCorrect(IOTester tester) {
		tester.provideInputLines("5");

		InputOutputPenguin.calculateSquare();

		var out = tester.getOutput();

		assertEquals("Keine Fehlerausgabe erwartet", 4, out.size());
		assertEquals("Zahl eingeben:", out.get(0).text());
		assertEquals("Ausgabe:", out.get(1).text());
		assertEquals("25", out.get(2).text());
	}

	@PublicTest
	void testSquareWrong(IOTester tester) {
		InputOutputPenguin.calculateSquare();

		assertEquals("Keine Fehlerausgabe erwartet", 0, tester.getErrorOutput().size());
	}

	@PublicTest
	@MirrorOutput(maxCharCount = 10, value = MirrorOutputPolicy.DISABLED)
	void testTooManyChars() {
		InputOutputPenguin.writeTwoLines();
	}

	@PublicTest
	void testTooManyReads(IOTester tester) {
		tester.provideInputLines("12");

		InputOutputPenguin.readTwoTimes();
	}
}