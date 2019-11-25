package de.tum.in.test.api.locked;

import static de.tum.in.test.api.localization.Messages.*;

import java.awt.AWTPermission;
import java.io.FilePermission;
import java.io.PrintStream;
import java.io.SerializablePermission;
import java.lang.StackWalker.StackFrame;
import java.lang.management.ManagementPermission;
import java.net.InetAddress;
import java.net.NetPermission;
import java.net.SocketPermission;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.SecurityPermission;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
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
public final class ArtemisSecurityManager extends SecurityManager {

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
	private final ThreadLocal<AtomicInteger> recursionBreak = ThreadLocal.withInitial(() -> new AtomicInteger());
	private final StackWalker stackWalker = StackWalker.getInstance();

	private List<String> staticWhiteList = List.of("java.", "org.junit.", "jdk.", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"org.eclipse.", "com.intellij", "org.assertj", "org.opentest4j.", // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"com.sun.", "sun.", "org.apache.", "de.tum.in.test.api.", "net.jqwik", PACKAGE_NAME); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	private ArtemisSecurityConfiguration configuration;
	private String accessToken;
	private Optional<Thread> whitelistedThread = Optional.empty();
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

	private boolean enterPublicInterface() {
		return recursionBreak.get().getAndIncrement() > 0;
	}

	private boolean exitPublicInterface() {
		return recursionBreak.get().getAndDecrement() > 0;
	}

	@Override
	public void checkExit(int status) {
		try {
			if (enterPublicInterface())
				return;
			if (Thread.currentThread() == MAIN_THREAD && getNonWhitelistedStackFrames().isEmpty()) {
				// always allow maven to exit
				return;
			}
			throw new SecurityException(localized("security.error_system_exit")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkAccept(String host, int port) {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_network_accept")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkConnect(String host, int port) {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_network_connect")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_network_connect_with_context")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkListen(int port) {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_network_listen")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkMulticast(InetAddress maddr) {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_network_multicast")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkExec(String cmd) {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_execute")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkPrintJobAccess() {
		try {
			if (enterPublicInterface())
				return;
			throw new SecurityException(localized("security.error_printer")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkCreateClassLoader() {
		try {
			if (enterPublicInterface())
				return;
			checkForNonWhitelistedStackFrames(() -> localized("security.error_classloader"));
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkPermission(Permission perm) {
		try {
			if (enterPublicInterface())
				return;
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
			if (perm instanceof FilePermission)
				checkPathAccess(Path.of(perm.getName()));
		} finally

		{
			exitPublicInterface();
		}
	}

	private void checkPathAccess(Path p) {
		boolean whitelisted = false;
		boolean blacklisted = false;
		try {
			Path pa = p.toAbsolutePath();
			whitelisted = isPathWhitelisted(pa);
			blacklisted = isPathBlacklisted(pa);
			if (!blacklisted && whitelisted)
				return;
		} catch (Exception e) {
			e.printStackTrace(LOG_OUTPUT);
		}
		String message = String.format("BAD PATH ACCESS: %s (BL:%s, WL:%s)", p, blacklisted, whitelisted);
		checkForNonWhitelistedStackFrames(() -> {
			LOG_OUTPUT.println(message);
			return formatLocalized("security.error_path_access", p); //$NON-NLS-1$
		});
	}

	private boolean isPathWhitelisted(Path pa) {
		var pathWhitelist = configuration.whitelistedPaths();
		if (pathWhitelist.isEmpty())
			return pa.startsWith(configuration.executionPath());
		return pathWhitelist.get().stream().anyMatch(pm -> pm.matches(pa));
	}

	private boolean isPathBlacklisted(Path pa) {
		var pathBlacklist = configuration.blacklistedPaths();
		return pathBlacklist.stream().anyMatch(pm -> pm.matches(pa));
	}

	@Override
	public void checkPackageAccess(String pkg) {
		try {
			if (enterPublicInterface())
				return;
			var blackList = List.of("java.lang.reflect", "de.tum.in.test.api.util", PACKAGE_NAME); //$NON-NLS-1$ //$NON-NLS-2$
			if (blackList.stream().anyMatch(pkg::startsWith)) {
				/*
				 * this is a very expensive operation, can we do better? (no)
				 */
				checkForNonWhitelistedStackFrames(() -> formatLocalized("security.error_disallowed_package", pkg)); //$NON-NLS-1$
			}
			super.checkPackageAccess(pkg);
		} finally {
			exitPublicInterface();
		}
	}

	private void checkForNonWhitelistedStackFrames(Supplier<String> message) {
		var nonWhitelisted = getNonWhitelistedStackFrames();
		if (!nonWhitelisted.isEmpty()) {
			LOG_OUTPUT.println("NWSFs ==> " + nonWhitelisted);
			var first = nonWhitelisted.get(0);
			throw new SecurityException(formatLocalized("security.stackframe_add_info", message.get(), //$NON-NLS-1$
					first.getLineNumber(), first.getFileName()));
		}
	}

	private List<StackFrame> getNonWhitelistedStackFrames() {
		if (isCurrentThreadWhitelisted()) {
			return stackWalker.walk(sfs -> sfs.filter(ste -> {
				String name = ste.getClassName(); // we don't map here to retain more information
				return staticWhiteList.stream().noneMatch(name::startsWith)
						&& !configuration.whitelistedClassNames().contains(name);
			}).distinct().collect(Collectors.toList()));
		}
		return stackWalker.walk(sfs -> sfs.collect(Collectors.toList()));
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

	private boolean isCurrentThreadWhitelisted() {
		Thread current = Thread.currentThread();
		if (!testThreadGroup.parentOf(current.getThreadGroup()))
			return true;
		if (!configuration.whitelistFirstThread())
			return false;
		return whitelistedThread.filter(current::equals).isPresent();
	}

	private void requestThreadWhitelisting(Thread t) {
		if (isCurrentThreadWhitelisted())
			return;
		if (whitelistedThread.isPresent())
			throw new SecurityException(localized("security.error_thread_already_whitelisted")); //$NON-NLS-1$
		whitelistedThread = Optional.of(t);
	}

	private void unwhitelistThreads() {
		if (!configuration.whitelistFirstThread() && whitelistedThread.isPresent())
			throw new SecurityException(localized("security.error_no_thread_whitelisting")); //$NON-NLS-1$
		whitelistedThread = Optional.empty();
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
		LOG_OUTPUT.println("REQUEST INSTALL " + Thread.currentThread() + " " + configuration.shortDesc());
		String token = INSTANCE.generateAccessToken();
		System.setSecurityManager(INSTANCE);
		INSTANCE.configuration = Objects.requireNonNull(configuration);
		INSTANCE.unwhitelistThreads();
		return token;
	}

	public static synchronized void uninstall(String accessToken) {
		if (!isInstalled())
			throw new IllegalStateException(localized("security.not_installed")); //$NON-NLS-1$

		INSTANCE.checkAccess(accessToken);
		if (INSTANCE.isPartlyDisabled)
			throw new IllegalStateException(localized("security.already_disabled")); //$NON-NLS-1$

		LOG_OUTPUT.println("REQUEST UNINSTALL " + Thread.currentThread());
		// cannot be used in conjunction with classic JUnit timeout, use @StrictTimeout
		Thread[] active = INSTANCE.checkThreadGroup();
		INSTANCE.unwhitelistThreads();
		INSTANCE.isPartlyDisabled = true;
		System.setSecurityManager(ORIGINAL);
		INSTANCE.isPartlyDisabled = false;
		INSTANCE.configuration = null;

		if (active.length > 0)
			throw new IllegalStateException(
					formatLocalized("security.error_threads_still_active", Arrays.toString(active)));
	}

	public static synchronized void configure(String accessToken, ArtemisSecurityConfiguration configuration) {
		INSTANCE.checkAccess(accessToken);
		INSTANCE.configuration = configuration;
	}

	public static synchronized void requestThreadWhitelisting() {
		LOG_OUTPUT.println("REQUEST WHITELISTING " + Thread.currentThread() + " " + INSTANCE.whitelistedThread);
		INSTANCE.requestThreadWhitelisting(Thread.currentThread());
	}

	private static final String hash(String s) {
		if (s == null)
			return ""; //$NON-NLS-1$
		return Base64.getEncoder().encodeToString(SHA256.digest(s.getBytes(StandardCharsets.UTF_8)));
	}
}
