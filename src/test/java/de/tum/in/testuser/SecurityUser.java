package de.tum.in.testuser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.testuser.subject.SecurityPenguin;

@Public
@UseLocale("en")
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@AllowThreads(maxActiveCount = 100)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/{classes,test-classes}/de/tum**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class SecurityUser {

	@Test
	void doSystemExit() {
		System.exit(0);
	}

	@Test
	void longOutputJUnit4() {
		String a = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String b = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aluyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		assertEquals(a, b);
	}

	@Test
	void longOutputJUnit5() {
		String a = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String b = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aluyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		Assertions.assertEquals(a, b);
	}

	@Test
	void testDefinePackage() {
		SecurityPenguin.definePackage();
	}

	@Test
	void testEvilPermission() {
		assertFalse(SecurityPenguin.tryEvilPermission());
	}

	@Test
	void testExecuteGit() {
		SecurityPenguin.tryExecuteGit();
	}

	@PublicTest
	void testMaliciousExceptionA() {
		SecurityPenguin.maliciousExceptionA();
	}

	@PublicTest
	void testMaliciousExceptionB() {
		assertFalse(SecurityPenguin.maliciousExceptionB());
	}

	@PublicTest
	void testNewClassLoader() throws IOException {
		SecurityPenguin.newClassLoader();
	}

	@PublicTest
	void testNewSecurityManager() {
		SecurityPenguin.newSecurityManager();
	}

	@PublicTest
	void tryManageProcess() {
		SecurityPenguin.tryManageProcess();
	}

	@PublicTest
	void trySetSecurityManager() {
		SecurityPenguin.trySetSecurityManagerNull();
	}

	@PublicTest
	void trySetSystemOut() {
		SecurityPenguin.trySetSystemOut();
	}

	@PublicTest
	void useCommonPoolBadNormal() throws Throwable {
		SecurityPenguin.useCommonPoolBadNormal();
	}

	@PublicTest
	void useCommonPoolBadTrick() throws Throwable {
		SecurityPenguin.useCommonPoolBadTrick();
	}

	@PublicTest
	void useCommonPoolGood() throws Throwable {
		SecurityPenguin.useCommonPoolGood();
	}

	@PublicTest
	void useReflectionNormal() {
		SecurityPenguin.useReflection();
	}

	@PublicTest
	void useReflectionPrivileged() {
		SecurityPenguin.useReflectionPrivileged();
	}

	@PublicTest
	void useReflectionTrick() throws Throwable {
		SecurityPenguin.useReflection2();
	}

	@PublicTest
	void weUseReflection() {
		IOTester.class.getDeclaredFields()[0].setAccessible(true);
	}

	@PublicTest
	void weUseShutdownHooks() {
		Thread t = new Thread("xyz");
		Runtime.getRuntime().addShutdownHook(t);
		Runtime.getRuntime().removeShutdownHook(t);
	}
}
