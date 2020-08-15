package de.tum.in.testuser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.xyz.Circumvention;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPackage;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.ExtendedDeadline;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.PrivilegedExceptionsOnly;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.WhitelistPackage;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.io.Line;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.testuser.subject.ArrayListUserProxy;
import de.tum.in.testuser.subject.Penguin;

@MirrorOutput(MirrorOutputPolicy.DISABLED)
@AllowThreads(maxActiveCount = 100)
@Deadline("2019-10-31 05:00")
@ExtendedDeadline("1h 30m")
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(Alphanumeric.class)
@UseLocale("en")
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class SecurityUser {

	@PublicTest
	@WhitelistPath("")
	public void accessPathAllowed() throws IOException {
		Penguin.accessPath(Path.of("pom.xml"));
	}

	@PublicTest
	public void accessPathNormal() throws IOException {
		Penguin.accessPath(Path.of("pom.xml"));
	}

	@PublicTest
	@WhitelistPath("")
	public void accessPathTest() throws IOException {
		if (!Files.exists(Path.of("target/test-classes/de/tum/in/test/api/SecurityTest.class")))
			fail("File not present");
		Penguin.accessPath(Path.of("target/test-classes/de/tum/in/test/api/SecurityTest.class"));
	}

	@WhitelistPath(value = "../artemis-java-test-sandbox**", type = PathType.GLOB)
	@PublicTest
	public void accessPathRelativeGlobA() throws IOException {
		Penguin.accessPath(Path.of("pom.xml").toAbsolutePath());
	}

	@WhitelistPath(value = "./pom.xml", type = PathType.GLOB)
	@PublicTest
	public void accessPathRelativeGlobB() throws IOException {
		Penguin.accessPath(Path.of("pom.xml").toAbsolutePath());
	}

	@PublicTest
	public void doSystemExit() {
		System.exit(0);
	}

	@PublicTest
	public void exceedTimeLimit() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				// ignore
			}
		}
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

	@PublicTest
	public void testEvilPermission() {
		assertFalse(Penguin.tryEvilPermission());
	}

	@PublicTest
	public void testExecuteGit() {
		Penguin.tryExecuteGit();
	}

	@PublicTest
	public void testMaliciousExceptionA() {
		Penguin.maliciousExceptionA();
	}

	@PublicTest
	public void testMaliciousExceptionB() {
		assertFalse(Penguin.maliciousExceptionB());
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
	public void testThreadGroup() {
		Penguin.tryBreakThreadGroup();
	}

	@PublicTest
	public void testTooManyReads(IOTester tester) {
		tester.provideInputLines("12");

		Penguin.readTwoTimes();
	}

	@PublicTest
	@MirrorOutput(maxCharCount = 10, value = MirrorOutputPolicy.DISABLED)
	public void testTooManyChars() {
		Penguin.main(new String[0]);
	}

	@PublicTest
	public void useReflectionNormal() {
		Penguin.useReflection();
	}

	@PublicTest
	public void useReflectionTrick() {
		Penguin.useReflection2();
		Circumvention.thrown.ifPresent(e -> {
			throw e;
		});
	}

	@PublicTest
	public void weAccessPath() throws IOException {
		Files.readString(Path.of("pom.xml"));
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
	public void threadWhitelistingWithPathFail() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				failure.set(e);
			}
		});
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}

	@PublicTest
	public void threadWhitelistingWithPathCorrect() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		ArtemisSecurityManager.requestThreadWhitelisting(t);
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				failure.set(e);
			}
		});
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}

	@PublicTest
	public void threadWhitelistingWithPathPenguin() throws Throwable {
		Penguin.tryThreadWhitelisting();
	}

	@Disabled
	@PublicTest
	@StrictTimeout(2)
	public void testThreadBomb() {
		Penguin.spawnEndlessThreads();
	}

	@BlacklistPackage("java.util")
	@PublicTest
	public void package_aBlacklistingPenguinUtil() throws Throwable {
		ArrayListUserProxy.useArrayList();
	}

	@BlacklistPackage("java.**")
	@PublicTest
	public void package_bBlacklistingPenguinJava() throws Throwable {
		ArrayListUserProxy.useArrayList();
	}

	@BlacklistPackage("**")
	@PublicTest
	public void package_cBlacklistingPenguinAll() throws Throwable {
		ArrayListUserProxy.useArrayList();
	}

	@BlacklistPackage("**")
	@WhitelistPackage("java.util")
	@PublicTest
	public void package_dBlackAndWhitelistingPenguin() throws Throwable {
		ArrayListUserProxy.useArrayList();
	}

	@BlacklistPackage("java.util")
	@PublicTest
	public void package_eBlackPenguinAgain() throws Throwable {
		ArrayListUserProxy.useArrayList();
	}

	@PrivilegedExceptionsOnly("ABC")
	@PublicTest
	public void nonprivilegedExceptionExtern() {
		Penguin.throwNullPointerException();
	}

	@PrivilegedExceptionsOnly("ABC")
	@PublicTest
	public void nonprivilegedExceptionIntern() {
		throw new NullPointerException("xy");
	}

	@PrivilegedExceptionsOnly("ABC")
	@PublicTest
	public void privilegedExceptionNormal() throws Exception {
		TestUtils.privilegedThrow(() -> {
			throw new NullPointerException("xyz");
		});
	}

	@PrivilegedExceptionsOnly("ABC")
	@PublicTest
	public void privilegedExceptionFail() {
		TestUtils.privilegedThrow(() -> {
			fail("xyz");
		});
	}

	@PrivilegedExceptionsOnly("ABC")
	@PublicTest
	public void nonprivilegedExceptionTry() {
		Penguin.throwPrivilegedNullPointerException();
	}

	@PublicTest
	public void commonPoolInterruptable() throws InterruptedException, ExecutionException {
		// check functionality
		var res = ForkJoinPool.commonPool().submit(() -> "A").get();
		assertEquals("A", res);
		// submit long-running task
		ForkJoinPool.commonPool().submit(() -> {
			try {
				Thread.sleep(5_000);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		// check that the task is still running after 100 ms
		try {
			Thread.sleep(100);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		assertFalse(ForkJoinPool.commonPool().isQuiescent());
		// wait for task end
		ForkJoinPool.commonPool().awaitQuiescence(5, TimeUnit.SECONDS);
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