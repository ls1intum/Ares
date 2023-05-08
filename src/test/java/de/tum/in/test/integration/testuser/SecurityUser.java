package de.tum.in.test.integration.testuser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;

import de.tum.in.test.api.*;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.SecurityPenguin;

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

	@Test
	void testMaliciousExceptionA() {
		SecurityPenguin.maliciousExceptionA();
	}

	@Test
	void testMaliciousExceptionB() {
		assertFalse(SecurityPenguin.maliciousExceptionB());
	}

	@Test
	void testMaliciousInvocationTargetException() throws Exception {
		SecurityPenguin.maliciousInvocationTargetException();
	}

	@Test
	void testNewClassLoader() throws IOException {
		SecurityPenguin.newClassLoader();
	}

	@Test
	void testNewSecurityManager() {
		SecurityPenguin.newSecurityManager();
	}

	@Test
	@AddTrustedPackage("xyz.**")
	void trustedPackageWithoutEnforcerRule() {
		// nothing
	}

	@Test
	void tryLoadNativeLibrary() {
		SecurityPenguin.tryLoadNativeLibrary();
	}

	@Test
	void tryManageProcess() {
		SecurityPenguin.tryManageProcess();
	}

	@Test
	void trySetSecurityManager() {
		SecurityPenguin.trySetSecurityManagerNull();
	}

	@Test
	void trySetSystemOut() {
		SecurityPenguin.trySetSystemOut();
	}

	@Test
	void useCommonPoolBadNormal() throws Throwable {
		SecurityPenguin.useCommonPoolBadNormal();
	}

	@Test
	void useCommonPoolBadTrick() throws Throwable {
		SecurityPenguin.useCommonPoolBadTrick();
	}

	@Test
	void useCommonPoolGood() throws Throwable {
		SecurityPenguin.useCommonPoolGood();
	}

	@Test
	void useReflectionPackagePrivateExecute() throws Throwable {
		SecurityPenguin.useReflectionPackagePrivateExecute();
	}

	@Test
	void useReflectionPackagePrivateSetAccessible() throws Throwable {
		SecurityPenguin.useReflectionPackagePrivateSetAccessible();
	}

	@Test
	void useReflectionNormal() {
		SecurityPenguin.useReflection();
	}

	@Test
	void useReflectionPrivileged() {
		SecurityPenguin.useReflectionPrivileged();
	}

	@Test
	void useReflectionTrick() throws Throwable {
		SecurityPenguin.useReflection2();
	}

	@Test
	void weUseReflection() {
		IOTester.class.getDeclaredFields()[0].setAccessible(true);
	}

	@Test
	void weUseShutdownHooks() {
		Thread t = new Thread("xyz");
		Runtime.getRuntime().addShutdownHook(t);
		Runtime.getRuntime().removeShutdownHook(t);
	}
}
