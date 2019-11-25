package de.tum.in.test.other;

//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.xyz.Circumvention;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.ExtendedDeadline;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.io.Line;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

//@MirrorOutput
@Deadline("2019-10-31 05:00")
@ExtendedDeadline("1h 30m")
@StrictTimeout(value = 100, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(Alphanumeric.class)
//@UseLocale("en")
public class SecurityUser {

	@PublicTest
	public void doSystemExit() {
		System.exit(0);
	}

	@PublicTest
	public void longOutputJUnit4() {
		String a = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String b = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aluyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		assertEquals(a, b);
	}

	@PublicTest
	public void longOutputJUnit5() {
		String a = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String b = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aluyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		Assertions.assertEquals(a, b);
	}

	@HiddenTest
	public void makeUTF8Error() throws IOException {
		System.out.write(new byte[] { 'P', 'i', 'n', 'g', 'u', (byte) 0xFF });
	}

	@HiddenTest
	@ExtendedDeadline("1d 10h")
	public void testPenguin1(IOTester tester) {

		Penguin.main(null);

		List<Line> lines = tester.getOutput();

		if (lines.isEmpty())
			fail("Es wird nichts ausgegeben!");

		String firstLine = lines.get(0).text();
		assertEquals("Pinguine sind die Besten!", firstLine);
	}

	@PublicTest
	public void testPenguin2(IOTester tester) {

		Penguin.main(new String[0]);

		List<Line> lines = tester.getOutput();

		String secondLine = lines.get(0).text();
		assertEquals("Pinguine sind  die Besten!", secondLine);
	}

	@PublicTest
	public void testPolarBear(IOTester tester) {

		Penguin.main(new String[0]);

		List<Line> lines = tester.getOutput();

		if (lines.size() < 2)
			fail("Es wird keine zweite Zeile ausgegeben!");

		String secondLine = lines.get(1).text();
		assertEquals("Nieder mit den Eisbären!", secondLine);
	}

	@PublicTest
	public void testSquareCorrect(IOTester tester) {
		tester.provideInputLines("5");

		Penguin.mäin(new String[0]);

		var out = tester.getOutput();

		assertEquals("Keine Fehlerausgabe erwartet", 4, out.size());
		assertEquals("Zahl eingeben:", out.get(0).text());
		assertEquals("Ausgabe:", out.get(1).text());
		assertEquals("25", out.get(2).text());
	}

	@PublicTest
	public void testSquareWrong(IOTester tester) {
		Penguin.mäin(new String[0]);

		assertEquals("Keine Fehlerausgabe erwartet", 0, tester.getErrorOutput().size());
	}

	@PublicTest
	public void exceedTimeLimit(IOTester tester) {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

	@PublicTest
	public void testTooManyreads(IOTester tester) {
		tester.provideInputLines("12");

		Penguin.readTwoTimes();
	}

	@PublicTest
	public void useReflectionNormal() {
//		Thread[] theads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
//		Thread.currentThread().getThreadGroup().enumerate(theads);
//		System.err.println(" " + Arrays.toString(theads) + " - " + Thread.currentThread());
		Penguin.useReflection();
	}

	@PublicTest
//	@MirrorOutput
	public void useReflectionTrick() {
		Penguin.useReflection2();
		Circumvention.thrown.ifPresent(e -> {
			throw e;
		});
	}

	@PublicTest
	public void weUseReflection() {
		try {
			Class.forName("de.tum.in.test.api.io.IOTester").getDeclaredFields()[0].setAccessible(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@PublicTest
	public void accessPathNormal() throws IOException {
		Penguin.accessPath(Path.of("pom.xml"));
	}

	@PublicTest
	public void weAccessPath() throws IOException {
		Files.readString(Path.of("pom.xml"));
	}

	@PublicTest
	@WhitelistPath("")
	public void accessPathAllowed() throws IOException {
		Penguin.accessPath(Path.of("pom.xml"));
	}

	/**
	 * This can be used to check for Threads that are not stoppable. This should
	 * never happen, but it could. Note that this test beaks all further ones,
	 * because the security manager will not be uninstalled and block everything. It
	 * works by catching the {@link ThreadDeath}.
	 */
//	@PublicTest
//	public void zz_unstoppable() {
//		long t = System.currentTimeMillis();
//		while (System.currentTimeMillis() - t < 1000) {
//			try {
//				Thread.sleep(100);
//			} catch (Throwable e) {
//				// ignore
//			}
//		}
//	}
}