package de.tum.in.test.api.locked;

import static de.tum.in.test.api.localization.Messages.*;

import java.awt.AWTPermission;
import java.io.PrintStream;
import java.io.SerializablePermission;
import java.lang.StackWalker.StackFrame;
import java.lang.management.ManagementPermission;
import java.net.InetAddress;
import java.net.NetPermission;
import java.net.SocketPermission;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.SecurityPermission;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.net.ssl.SSLPermission;
import javax.security.auth.AuthPermission;

/**
 * Prevents System.exit, reflection to a certain degree, networking use,
 * execution commands and long running threads.
 * 
 * @author Christian Femers
 */
final class ArtemisSecurityManager extends SecurityManager {

	private static final SecurityManager ORIGINAL = System.getSecurityManager();
	private static final String PACKAGE_NAME = ArtemisSecurityManager.class.getPackageName();
	private static final ArtemisSecurityManager INSTANCE = new ArtemisSecurityManager();
	private static final PrintStream LOG_OUTPUT = System.err;
	private static final Thread MAIN_THREAD = Thread.currentThread();
	private static final MessageDigest SHA256;
	static {
		try {
			SHA256 = MessageDigest.getInstance("SHA-256"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private final ThreadGroup testThreadGroup = new ThreadGroup("Test-Threadgroup"); //$NON-NLS-1$
	private final ThreadLocal<Boolean> recursionBreak = ThreadLocal.withInitial(() -> Boolean.FALSE);

	private List<String> staticWhiteList = List.of("java.", "org.junit.", "jdk.", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"org.eclipse.", "com.intellij", "org.assertj", // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"com.sun.", "sun.", "org.apache.", "de.tum.in.test.api", PACKAGE_NAME); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private ArtemisSecurityConfiguration configuration;
	private String accessToken;
	private boolean isPartlyDisabled;

	private ArtemisSecurityManager() {
		if (INSTANCE != null)
			throw new IllegalStateException(localized("security.already_created")); //$NON-NLS-1$
	}

	private final synchronized String generateAccessToken() {
		String tokenString = UUID.randomUUID().toString();
		accessToken = hash(tokenString);
		return tokenString;
	}

	private final synchronized void checkAccess(String accessToken) {
		if (!this.accessToken.equals(hash(accessToken)))
			throw new SecurityException(localized("security.access_token_invalid")); //$NON-NLS-1$
	}

	@Override
	public void checkExit(int status) {
		if (Thread.currentThread() == MAIN_THREAD && getNonWhitelistedStackFrames().isEmpty()) {
			// always allow maven to exit
			return;
		}
		throw new SecurityException(localized("security.error_system_exit")); //$NON-NLS-1$
	}

	@Override
	public void checkAccept(String host, int port) {
		throw new SecurityException(localized("security.error_network_accept")); //$NON-NLS-1$
	}

	@Override
	public void checkConnect(String host, int port) {
		throw new SecurityException(localized("security.error_network_connect")); //$NON-NLS-1$
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		throw new SecurityException(localized("security.error_network_connect_with_context")); //$NON-NLS-1$
	}

	@Override
	public void checkListen(int port) {
		throw new SecurityException(localized("security.error_network_listen")); //$NON-NLS-1$
	}

	@Override
	public void checkMulticast(InetAddress maddr) {
		throw new SecurityException(localized("security.error_network_multicast")); //$NON-NLS-1$
	}

	@Override
	public void checkExec(String cmd) {
		throw new SecurityException(localized("security.error_execute")); //$NON-NLS-1$
	}

	@Override
	public void checkPrintJobAccess() {
		throw new SecurityException(localized("security.error_printer")); //$NON-NLS-1$
	}

	@Override
	public void checkCreateClassLoader() {
		throw new SecurityException(localized("security.error_classloader")); //$NON-NLS-1$
	}

	@Override
	public void checkPermission(Permission perm) {
		// for files: read, readlink, write, delete
		// for threads: modifyThread
		// for preferences: preferences
		// for redefinition of IO: setIO
		var whitelist = List.of("getClassLoader", "accessSystemModules"); //$NON-NLS-1$ //$NON-NLS-2$
		var blacklist = List.of("setIO", "manageProcess"); //$NON-NLS-1$ //$NON-NLS-2$
		if (whitelist.contains(perm.getName()))
			return;
		if (blacklist.contains(perm.getName()))
			throw new SecurityException(localized("security.error_blacklist") + perm); //$NON-NLS-1$
		// this could be removed / reduced, if the specified part is needed
		/**
		 * SOME OF THIS IS COMMENTED OUT, BECAUSE JAVA AND JUNIT WON'T WORK
		 */
		if ("setSecurityManager".equals(perm.getName()) && !isPartlyDisabled) //$NON-NLS-1$
			throw new SecurityException(localized("security.error_security_manager")); //$NON-NLS-1$
		if (perm instanceof SerializablePermission)
			throw new SecurityException(localized("security.error_modify_serialization") + perm); //$NON-NLS-1$
		if (perm instanceof AWTPermission)
			throw new SecurityException(localized("security.error_awt") + perm); //$NON-NLS-1$
		if (perm instanceof ManagementPermission)
			throw new SecurityException(localized("security.error_management") + perm); //$NON-NLS-1$
		if (perm instanceof NetPermission || perm instanceof SocketPermission)
			throw new SecurityException(localized("security.error_networking") + perm); //$NON-NLS-1$
		if (perm instanceof SecurityPermission)
			throw new SecurityException(localized("security.error_modify_security") + perm); //$NON-NLS-1$
		if (perm instanceof SSLPermission)
			throw new SecurityException(localized("security.error_modify_ssl") + perm); //$NON-NLS-1$
		if (perm instanceof AuthPermission)
			throw new SecurityException(localized("security.error_modify_auth") + perm); //$NON-NLS-1$
	}

	@Override
	public void checkPackageAccess(String pkg) {
		var blackList = List.of("java.lang.reflect", PACKAGE_NAME); //$NON-NLS-1$
		if (blackList.stream().anyMatch(pkg::startsWith)) {
			/*
			 * this is a very expensive operation, can we do better? (no)
			 */
			checkForNonWhitelistedStackFrames(() -> formatLocalized("security.error_disallowed_package", pkg)); //$NON-NLS-1$
		}
		super.checkPackageAccess(pkg);
	}

	private void checkForNonWhitelistedStackFrames(Supplier<String> message) {
		if (recursionBreak.get().booleanValue())
			return;
		recursionBreak.set(Boolean.TRUE);
		try {
			var nonWhitelisted = getNonWhitelistedStackFrames();
			if (!nonWhitelisted.isEmpty()) {
				LOG_OUTPUT.println("==> " + nonWhitelisted);
				var first = nonWhitelisted.get(0);
				throw new SecurityException(formatLocalized("security.stackframe_add_info", message.get(), //$NON-NLS-1$
						first.getLineNumber(), first.getFileName()));
			}
		} finally {
			recursionBreak.set(Boolean.FALSE);
		}
	}

	private List<StackFrame> getNonWhitelistedStackFrames() {
		return StackWalker.getInstance().walk(sfs -> sfs.filter(ste -> {
			String name = ste.getClassName(); // we don't map here to retain more information
			return staticWhiteList.stream().noneMatch(name::startsWith)
					&& !configuration.whitelistedClassNames().contains(name);
		}).distinct().collect(Collectors.toList()));
	}

	@Override
	public ThreadGroup getThreadGroup() {
		return testThreadGroup;
	}

	@SuppressWarnings("deprecation")
	private Thread[] checkThreadGroup() {
		int originalCount = testThreadGroup.activeCount();
		if (originalCount == 0)
			return new Thread[0]; // everything ok
		Thread[] theads = new Thread[originalCount];
		testThreadGroup.enumerate(theads);
		// try gentle shutdown; without that, runs on CI might fail because of previous.
		for (Thread thread : theads) {
			if (thread == null)
				continue;
			try {
				thread.join(500 / originalCount);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		if (testThreadGroup.activeCount() == 0)
			return new Thread[0];
		// try forceful shutdown
		SecurityException exception = new SecurityException(
				formatLocalized("security.error_threads_not_stoppable", Arrays.toString(theads)));
		for (Thread thread : theads) {
			if (thread == null)
				continue;
			/*
			 * we definitely want to forcefully terminate all threads (otherwise, next tests
			 * will fail)
			 */
			thread.stop();
			try {
				thread.join(500 / originalCount);
			} catch (InterruptedException e) {
				e.printStackTrace(LOG_OUTPUT);
				exception.addSuppressed(e);
				Thread.currentThread().interrupt();
			}
		}
		if (testThreadGroup.activeCount() > 0)
			throw exception;
		return theads;
	}

	static boolean isStaticWhitelisted(String name) {
		return INSTANCE.staticWhiteList.stream().anyMatch(name::startsWith);
	}

	public static synchronized boolean isInstalled() {
		return System.getSecurityManager() instanceof ArtemisSecurityManager;
	}

	public static synchronized String install(ArtemisSecurityConfiguration configuration) {
		if (isInstalled())
			throw new IllegalStateException(localized("security.already_installed")); //$NON-NLS-1$
		String token = INSTANCE.generateAccessToken();
		System.setSecurityManager(INSTANCE);
		INSTANCE.configuration = configuration;
		return token;
	}

	public static synchronized void uninstall(String accessToken) {
		if (!isInstalled())
			throw new IllegalStateException(localized("security.not_installed")); //$NON-NLS-1$

		INSTANCE.checkAccess(accessToken);
		if (INSTANCE.isPartlyDisabled)
			throw new IllegalStateException(localized("security.already_disabled")); //$NON-NLS-1$

		// cannot be used in conjunction with classic JUnit timeout, use @StrictTimeout
		Thread[] active = INSTANCE.checkThreadGroup();
		INSTANCE.isPartlyDisabled = true;
		System.setSecurityManager(ORIGINAL);
		INSTANCE.isPartlyDisabled = false;

		if (active.length > 0)
			throw new IllegalStateException(
					formatLocalized("security.error_threads_still_active", Arrays.toString(active)));
	}

	public static synchronized void configure(String accessToken, ArtemisSecurityConfiguration configuration) {
		INSTANCE.checkAccess(accessToken);
		INSTANCE.configuration = configuration;
	}

	private static final String hash(String s) {
		if (s == null)
			return ""; //$NON-NLS-1$
		return Base64.getEncoder().encodeToString(SHA256.digest(s.getBytes(StandardCharsets.UTF_8)));
	}
}
