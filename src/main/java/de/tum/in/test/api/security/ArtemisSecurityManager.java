package de.tum.in.test.api.security;

import static de.tum.in.test.api.localization.Messages.localized;

import java.awt.AWTPermission;
import java.io.*;
import java.lang.StackWalker.StackFrame;
import java.lang.Thread.State;
import java.lang.management.ManagementPermission;
import java.lang.reflect.ReflectPermission;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.*;

import javax.net.ssl.SSLPermission;
import javax.security.auth.AuthPermission;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.localization.Messages;
import de.tum.in.test.api.util.DelayedFilter;

/**
 * Prevents System.exit, reflection to a certain degree, networking use,
 * execution commands and long running threads.
 *
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
public final class ArtemisSecurityManager extends SecurityManager {

	private static final int MAX_PORT = AllowLocalPort.MAXIMUM;
	private static final Logger LOG = LoggerFactory.getLogger(ArtemisSecurityManager.class);
	private static final SecurityManager ORIGINAL = System.getSecurityManager();
	private static final ArtemisSecurityManager INSTANCE = new ArtemisSecurityManager();
	private static final Pattern RECURSIVE_FILE_PERMISSION = Pattern.compile("[/\\\\][-*]$"); //$NON-NLS-1$
	private static final String LOCALHOST = "localhost"; //$NON-NLS-1$
	private static final Predicate<StackFrame> IGNORE_ACCESS_PRIVILEGED = stackframe -> true;
	private static final String COMMON_POOL_THREAD_NAME = "ForkJoinPool.commonPool"; //$NON-NLS-1$
	private static final Set<String> THREAD_NAME_BLACKLIST = Set.of(COMMON_POOL_THREAD_NAME, "Finalizer", //$NON-NLS-1$
			"InnocuousThread", "Common-Cleaner"); //$NON-NLS-1$ //$NON-NLS-2$
	private static final MessageDigest SHA256;
	static {
		try {
			SHA256 = MessageDigest.getInstance("SHA-256"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
		// Allow to load resources
		Messages.init();
		/*
		 * Check for main Thread
		 */
		if (SecurityConstants.MAIN_THREAD_GROUP == null) {
			LOG.error("main thread group could not be found. Exiting..."); //$NON-NLS-1$
			System.exit(1);
		}
		/*
		 * Initialize common ForkJoinPool for parallel streams and alike
		 */
		System.setSecurityManager(INSTANCE);
		ForkJoinPool.commonPool();
		INSTANCE.isPartlyDisabled = true;
		System.setSecurityManager(ORIGINAL);
		INSTANCE.isPartlyDisabled = false;
		// Create all common pool threads
		IntStream.range(0, ForkJoinPool.getCommonPoolParallelism() * 10).parallel().sum();
		// We explain the deprecated security manager warning in case JDK 17 is used
		if (Runtime.version().feature() == 17) {
			SecurityConstants.SYSTEM_ERR
					.format("NOTICE: The warning above is expected and the issue is already known.%n" //$NON-NLS-1$
							+ "        Visit https://github.com/ls1intum/Ares/discussions/113 for more details.%n"); //$NON-NLS-1$
		}
	}

	private final ThreadGroup testThreadGroup = new ThreadGroup("Test-Threadgroup"); //$NON-NLS-1$
	private final ThreadLocal<AtomicInteger> recursionBreak = ThreadLocal.withInitial(AtomicInteger::new);
	private final StackWalker stackWalker = StackWalker.getInstance();

	private AresSecurityConfiguration configuration;
	private String accessToken;
	private Set<Thread> whitelistedThreads = new HashSet<>();
	private volatile boolean isPartlyDisabled;
	private volatile boolean blockThreadCreation;
	private volatile boolean lastUninstallFailed;
	private volatile boolean isActive;

	private ArtemisSecurityManager() {
		if (INSTANCE != null)
			throw new IllegalStateException(localized("security.already_created")); //$NON-NLS-1$
	}

	private synchronized String generateAccessToken() {
		var tokenString = UUID.randomUUID().toString();
		accessToken = hash(tokenString);
		return tokenString;
	}

	private synchronized void checkAccess(String accessToken) {
		if (!this.accessToken.equals(hash(accessToken)))
			throw new SecurityException(localized("security.access_token_invalid")); //$NON-NLS-1$
	}

	private boolean enterPublicInterface() {
		return recursionBreak.get().getAndIncrement() > 0;
	}

	private boolean exitPublicInterface() {
		return recursionBreak.get().getAndDecrement() > 0;
	}

	private <T> T externGet(Supplier<T> supplier) {
		try {
			exitPublicInterface();
			return supplier.get();
		} finally {
			enterPublicInterface();
		}
	}

	@Override
	public void checkExit(int status) {
		try {
			ThreadGroup currentThreadGroup = Thread.currentThread().getThreadGroup();
			if (enterPublicInterface())
				return;
			if (currentThreadGroup == SecurityConstants.MAIN_THREAD_GROUP && getNonWhitelistedStackFrames().isEmpty()) {
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
			if (isConnectionAllowed(host, port))
				return;
			throw new SecurityException(localized("security.error_network_accept", host, port)); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkConnect(String host, int port) {
		try {
			if (enterPublicInterface())
				return;
			if (isConnectionAllowed(host, port))
				return;
			throw new SecurityException(localized("security.error_network_connect", host, port)); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		try {
			if (enterPublicInterface())
				return;
			if (isConnectionAllowed(host, port))
				return;
			throw new SecurityException(localized("security.error_network_connect_with_context", host, port)); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkLink(String lib) {
		try {
			if (enterPublicInterface())
				return;
			checkForNonWhitelistedStackFrames(() -> localized("security.error_link")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkListen(int port) {
		try {
			if (enterPublicInterface())
				return;
			if (isConnectionAllowed(LOCALHOST, port))
				return;
			throw new SecurityException(localized("security.error_network_listen", port)); //$NON-NLS-1$
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
			if (isWorkerThreadAndInactive()) {
				LOG.trace("Allowing main thread to create a ClassLoader inbetween tests"); //$NON-NLS-1$
				return;
			}
			checkForNonWhitelistedStackFrames(() -> localized("security.error_classloader")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkAccess(Thread t) {
		var threadGroup = t.getThreadGroup();
		try {
			if (enterPublicInterface())
				return;
			super.checkAccess(t);
			// Thread terminated
			if (threadGroup == null)
				return;
			if (isWorkerThreadAndInactive())
				return;
			if (!testThreadGroup.parentOf(threadGroup))
				checkForNonWhitelistedStackFrames(() -> localized("security.error_thread_access")); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkAccess(ThreadGroup g) {
		try {
			if (enterPublicInterface())
				return;
			super.checkAccess(g);
			if (isWorkerThreadAndInactive())
				return;
			if (!testThreadGroup.parentOf(g))
				checkForNonWhitelistedStackFrames(() -> localized("security.error_threadgroup_access")); //$NON-NLS-1$
			checkThreadCreation();
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkPackageDefinition(String pkg) {
		try {
			if (enterPublicInterface())
				return;
			LOG.info("PKG-DEF: {}", pkg); //$NON-NLS-1$
			super.checkPackageDefinition(pkg);
			if (SecurityConstants.STACK_WHITELIST.stream().anyMatch(pkg::startsWith))
				throw new SecurityException(localized("security.error_package_definition", pkg)); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	@Override
	public void checkPermission(Permission perm) {
		var permName = perm.getName();
		var permActions = perm.getActions();
		var permString = String.valueOf(perm);
		try {
			if (enterPublicInterface())
				return;
			// for files: read, readlink, write, delete
			// for threads: modifyThread
			// for preferences: preferences
			// for redefinition of IO: setIO
			var whitelist = List.of("getClassLoader", "accessSystemModules"); //$NON-NLS-1$ //$NON-NLS-2$
			if (whitelist.contains(permName))
				return;
			var blacklist = List.of("manageProcess", "shutdownHooks", "createSecurityManager"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (blacklist.contains(permName))
				checkForNonWhitelistedStackFrames(() -> localized("security.error_blacklist") + permString); //$NON-NLS-1$
			if ("setIO".equals(permName) && !isWorkerThreadAndInactive()) //$NON-NLS-1$
				checkForNonWhitelistedStackFrames(() -> localized("security.error_blacklist") + permString); //$NON-NLS-1$
			// this could be removed / reduced, if the specified part is needed (does not
			// work for gradle)
			if ("setSecurityManager".equals(permName) && !isPartlyDisabled) //$NON-NLS-1$
				throw new SecurityException(localized("security.error_security_manager")); //$NON-NLS-1$
			if (perm instanceof SerializablePermission)
				checkForNonWhitelistedStackFrames(() -> localized("security.error_modify_serialization") + permString); //$NON-NLS-1$
			if (perm instanceof AWTPermission)
				throw new SecurityException(localized("security.error_awt") + permString); //$NON-NLS-1$
			if (perm instanceof ManagementPermission)
				checkForNonWhitelistedStackFrames(() -> localized("security.error_management") + permString); //$NON-NLS-1$
			if ((configuration == null || configuration.allowLocalPortsAbove().isEmpty())
					&& (perm instanceof NetPermission || perm instanceof SocketPermission))
				checkForNonWhitelistedStackFrames(() -> localized("security.error_networking") + permString); //$NON-NLS-1$
			if (perm instanceof SecurityPermission) {
				if (permName.startsWith("getPolicy") || permName.startsWith("getProperty")) //$NON-NLS-1$ //$NON-NLS-2$
					return;
				checkForNonWhitelistedStackFrames(() -> localized("security.error_modify_security") + permString); //$NON-NLS-1$
			}
			if (perm instanceof SSLPermission)
				throw new SecurityException(localized("security.error_modify_ssl") + permString); //$NON-NLS-1$
			if (perm instanceof AuthPermission)
				throw new SecurityException(localized("security.error_modify_auth") + permString); //$NON-NLS-1$
			if (perm instanceof FilePermission)
				checkPathAccess(permName, PathActionLevel.getLevelOf(permActions));
			if (perm instanceof ReflectPermission || "accessDeclaredMembers".equals(permName)) //$NON-NLS-1$
				checkForNonWhitelistedStackFrames(() -> localized("security.error_modify_security") + permString); //$NON-NLS-1$
		} finally {
			exitPublicInterface();
		}
	}

	private void checkPathAccess(String path, PathActionLevel pathActionLevel) {
		var whitelisted = false;
		var blacklisted = false;
		try {
			if ("<<ALL FILES>>".equals(path)) { //$NON-NLS-1$
				blacklisted = true;
			} else {
				String commonPath = getFilePermissionsCommonPath(path);
				if (commonPath == null) {
					var absolutePath = Path.of(path).toAbsolutePath();
					blacklisted = isPathBlacklisted(absolutePath, pathActionLevel);
					whitelisted = isPathWhitelisted(absolutePath, pathActionLevel);
				} else {
					var absolutePath = Path.of(commonPath).toAbsolutePath();
					blacklisted = !configuration.blacklistedPaths().isEmpty();
					whitelisted = !blacklisted && configuration.whitelistedPaths().orElse(Set.of()).stream()
							.anyMatch(pm -> pm.matchesRecursivelyWithLevel(absolutePath, pathActionLevel));
				}
			}
			if (!blacklisted && whitelisted)
				return;
		} catch (Exception e) {
			LOG.warn("Error in checkPathAccess", e); //$NON-NLS-1$
		}
		if (isWorkerThreadAndInactive() && pathActionLevel.isBelowOrEqual(PathActionLevel.READLINK)) {
			LOG.trace("Allowing read access for main thread inbetween tests"); // appears very often //$NON-NLS-1$
			return;
		}
		var message = String.format("BAD PATH ACCESS: %s (BL:%s, WL:%s)", path, blacklisted, whitelisted); //$NON-NLS-1$
		if (configuration == null || configuration.threadTrustScope() == TrustScope.MINIMAL) {
			/*
			 * If the configuration is not present or minimal, we keep the old behavior, as
			 * we can protect resources better if the thread whitelisting is restricted.
			 */
			checkForNonWhitelistedStackFrames(() -> {
				LOG.warn(message);
				return localized("security.error_path_access", path); //$NON-NLS-1$
			});
		} else {
			/*
			 * this is stricter with IGNORE_ACCESS_PRIVILEGED because we can now regulate
			 * wich paths are accessed in more detail (necessary to have a somewhat decent
			 * and secure configuration
			 */
			checkForNonWhitelistedStackFrames(() -> {
				LOG.warn(message);
				return localized("security.error_path_access", path); //$NON-NLS-1$
			}, IGNORE_ACCESS_PRIVILEGED);
		}
	}

	private static String getFilePermissionsCommonPath(String path) {
		if (RECURSIVE_FILE_PERMISSION.matcher(path).find())
			return path.substring(0, path.length() - 2);
		if ("*".equals(path) || "-".equals(path)) //$NON-NLS-1$ //$NON-NLS-2$
			return ""; //$NON-NLS-1$
		return null;
	}

	private boolean isPathWhitelisted(Path pa, PathActionLevel pathActionLevel) {
		var pathWhitelist = configuration.whitelistedPaths();
		if (pathWhitelist.isEmpty())
			return pa.startsWith(configuration.executionPath());
		return pathWhitelist.get().stream().anyMatch(pm -> pm.matchesWithLevel(pa, pathActionLevel));
	}

	private boolean isPathBlacklisted(Path pa, PathActionLevel pathActionLevel) {
		var pathBlacklist = configuration.blacklistedPaths();
		return pathBlacklist.stream().anyMatch(pm -> pm.matchesWithLevel(pa, pathActionLevel));
	}

	@Override
	public void checkPackageAccess(String pkg) {
		try {
			if (enterPublicInterface())
				return;
			super.checkPackageAccess(pkg);
			if (!isWorkerThreadAndInactive() && isPackageAccessForbidden(pkg)) {
				/*
				 * this is a very expensive operation, can we do better? (no)
				 */
				checkForNonWhitelistedStackFrames(() -> {
					LOG.warn("BAD PACKAGE ACCESS: {} (BL:{}, WL:{})", pkg, isPackageBlacklisted(pkg), //$NON-NLS-1$
							isPackageWhitelisted(pkg));
					return localized("security.error_disallowed_package", pkg); //$NON-NLS-1$
				}, IGNORE_ACCESS_PRIVILEGED);
			}
		} finally {
			exitPublicInterface();
		}
	}

	private boolean isPackageAccessForbidden(String pkg) {
		return SecurityConstants.PACKAGE_USE_BLACKLIST.stream().anyMatch(pkg::startsWith)
				|| (isPackageBlacklisted(pkg) && !isPackageWhitelisted(pkg));
	}

	private boolean isPackageBlacklisted(String packageName) {
		var packageBlacklist = configuration.blacklistedPackages();
		return packageBlacklist.stream().anyMatch(pm -> pm.matches(packageName));
	}

	private boolean isPackageWhitelisted(String packageName) {
		var packageWhitelist = configuration.whitelistedPackages();
		return packageWhitelist.stream().anyMatch(pm -> pm.matches(packageName));
	}

	private void checkForNonWhitelistedStackFrames(Supplier<String> message) {
		var nonWhitelisted = getNonWhitelistedStackFrames();
		throwSecurityExceptionIfNonWhitelistedFound(message, nonWhitelisted);
	}

	private void checkForNonWhitelistedStackFrames(Supplier<String> message,
			Predicate<StackFrame> takeFromTopWhileFilter) {
		var nonWhitelisted = getNonWhitelistedStackFrames(takeFromTopWhileFilter);
		throwSecurityExceptionIfNonWhitelistedFound(message, nonWhitelisted);
	}

	private static void throwSecurityExceptionIfNonWhitelistedFound(Supplier<String> message,
			List<StackFrame> nonWhitelisted) {
		if (!nonWhitelisted.isEmpty()) {
			LOG.warn("NWSFs ==> {}", nonWhitelisted); //$NON-NLS-1$
			var first = nonWhitelisted.get(0);
			throw new SecurityException(localized("security.stackframe_add_info", message.get(), //$NON-NLS-1$
					first.getLineNumber(), first.getFileName()));
		}
	}

	private List<StackFrame> getNonWhitelistedStackFrames() {
		// one for AccessController itself and one for the caller
		DelayedFilter<StackFrame> delayedIsNotPrivileged = new DelayedFilter<>(2, this::isNotPrivileged, true);
		return getNonWhitelistedStackFrames(delayedIsNotPrivileged);
	}

	private List<StackFrame> getNonWhitelistedStackFrames(Predicate<StackFrame> takeFromTopWhileFilter) {
		List<StackFrame> result;
		if (isCurrentThreadWhitelisted()) {
			result = stackWalker.walk(sfs -> sfs.takeWhile(takeFromTopWhileFilter)
					.filter(this::isStackFrameNotWhitelisted).collect(Collectors.toList()));
		} else {
			result = stackWalker.walk(sfs -> sfs.takeWhile(takeFromTopWhileFilter).collect(Collectors.toList()));
		}
		return result;
	}

	private boolean isNotPrivileged(StackFrame stackFrame) {
		return !AccessController.class.getName().equals(stackFrame.getClassName());
	}

	private boolean isCallNotWhitelisted(String className, String methodName) {
		String call = className + "." + methodName; //$NON-NLS-1$
		return SecurityConstants.STACK_BLACKLIST.stream().anyMatch(call::startsWith)
				|| (SecurityConstants.STACK_WHITELIST.stream().noneMatch(call::startsWith)
						&& (configuration == null || (!configuration.whitelistedClassNames().contains(className)
								&& configuration.trustedPackages().stream().noneMatch(pm -> pm.matches(className)))));
	}

	private boolean isStackFrameNotWhitelisted(StackFrame sf) {
		return isCallNotWhitelisted(sf.getClassName(), sf.getMethodName());
	}

	private boolean isStackFrameNotWhitelisted(StackTraceElement ste) {
		return isCallNotWhitelisted(ste.getClassName(), ste.getMethodName());
	}

	public static Optional<StackTraceElement> firstNonWhitelisted(StackTraceElement... elements) {
		for (StackTraceElement ste : elements) {
			if (INSTANCE.isStackFrameNotWhitelisted(ste))
				return Optional.ofNullable(ste);
		}
		return Optional.empty();
	}

	public static void checkCurrentStack(Supplier<String> exceptionMessage) {
		INSTANCE.checkForNonWhitelistedStackFrames(exceptionMessage);
	}

	private boolean isConnectionAllowed(String host, int port) {
		var nwsfs = getNonWhitelistedStackFrames();
		LOG.info("Connection use request: {}:{} [NWSFs: {}]", host, port, nwsfs.size()); //$NON-NLS-1$
		if (nwsfs.isEmpty())
			return true;
		return configuration != null && isLocalHost(host) && isLocalPortUsageAllowed(port);
	}

	private static boolean isLocalHost(String host) {
		try {
			var address = InetAddress.getByName(host);
			return address.isLoopbackAddress() || address.isAnyLocalAddress();
		} catch (@SuppressWarnings("unused") UnknownHostException e) {
			return false;
		}
	}

	private boolean isLocalPortUsageAllowed(int port) {
		if (port < -1 || port > MAX_PORT)
			return false;
		if (port == -1 || port == 0)
			return true;
		if (configuration == null)
			return false;
		return configuration.allowedLocalPorts().contains(port)
				|| (configuration.allowLocalPortsAbove().orElse(MAX_PORT) < port
						&& !configuration.excludedLocalPorts().contains(port));
	}

	@Override
	public ThreadGroup getThreadGroup() {
		if (!isActive)
			return super.getThreadGroup();
		return testThreadGroup;
	}

	/**
	 * Checks the test thread group for any threads that are still running.
	 * <p>
	 * <ol>
	 * <li>If there are no threads in the test thread group, return an empty
	 * array.</li>
	 * <li>Otherwise, get a list of all threads in the test thread group.</li>
	 * <li>For each thread in the list, try to interrupt it and wait for it to
	 * terminate.</li>
	 * <li>If there are no threads in the test thread group, return an empty
	 * array.</li>
	 * <li>Otherwise, for each thread in the list, try to stop it and wait for it to
	 * terminate.</li>
	 * <li>If there are still threads in the test thread group, throw a
	 * {@link SecurityException}.</li>
	 * <li>Otherwise, return the list of threads.</li>
	 * </ol>
	 *
	 * @return the list of threads in the test thread group
	 * @throws SecurityException if there are still threads in the test thread group
	 */
	@SuppressWarnings("deprecation")
	private Thread[] checkThreadGroup() {
		if (configuration.isThreadGroupCheckDisabled()) {
			LOG.debug("Thread group check is disabled"); //$NON-NLS-1$
			return new Thread[0];
		}

		blockThreadCreation = true;
		int originalCount = testThreadGroup.activeCount();
		if (originalCount == 0)
			return new Thread[0]; // everything ok
		var threads = new Thread[originalCount];
		testThreadGroup.enumerate(threads);
		// try gentle shutdown; without that, runs on CI might fail because of previous.
		for (Thread thread : threads) {
			if (thread == null)
				continue;
			try {
				thread.interrupt();
				thread.join(500 / originalCount + 1L);
				LOG.debug("State {} after interrupt and join of {}", thread.getState(), thread); //$NON-NLS-1$
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		if (testThreadGroup.activeCount() == 0)
			return new Thread[0];
		// try forceful shutdown
		var securityException = new SecurityException(
				localized("security.error_threads_not_stoppable", Arrays.toString(threads))); //$NON-NLS-1$
		int alive = threads.length;
		TRIES: for (var i = 0; i < 50 && alive > 0; i++) {
			alive = 0;
			for (Thread thread : threads) {
				if (thread == null || !thread.isAlive())
					continue;
				alive++;
				LOG.debug("Try {} to stop {}, state: {}", i + 1, thread, thread.getState()); //$NON-NLS-1$
				/*
				 * we definitely want to forcefully terminate all threads (otherwise, next tests
				 * will fail)
				 */
				thread.stop();
				try {
					thread.join(20 / originalCount + 1L);
				} catch (InterruptedException e) {
					LOG.warn("Error in checkThreadGroup 1", e); //$NON-NLS-1$
					securityException.addSuppressed(e);
					Thread.currentThread().interrupt();
					break TRIES;
				}
			}
		}
		for (Thread thread : threads) {
			if (thread == null || !thread.isAlive())
				continue;
			try {
				thread.join(100 / originalCount + 1L);
			} catch (InterruptedException e) {
				LOG.warn("Error in checkThreadGroup 2", e); //$NON-NLS-1$
				securityException.addSuppressed(e);
				Thread.currentThread().interrupt();
				break;
			}
			if (thread.getState() != State.TERMINATED)
				LOG.error("THREAD STOP ERROR: Thread {} is still in state {}", thread, thread.getState()); //$NON-NLS-1$
		}
		if (testThreadGroup.activeCount() > 0)
			throw securityException;
		return threads;
	}

	private void checkCommonThreadPool() {
		var commonPool = ForkJoinPool.commonPool();
		if (commonPool.isQuiescent())
			return;
		LOG.debug("Common pool is active: {} with number {} workers", !commonPool.isQuiescent(), //$NON-NLS-1$
				commonPool.getActiveThreadCount());
		var rootThreadGroup = getRootThreadGroup(); // NOSONAR
		// have some buffer in case the count changes
		// (does not need to be massive due to blockThreadCreation being true)
		var allThreads = new Thread[rootThreadGroup.activeCount() + 32];
		rootThreadGroup.enumerate(allThreads, true);
		var threads = Stream.of(allThreads).filter(Objects::nonNull)
				.filter(t -> t.getName().contains(COMMON_POOL_THREAD_NAME)).toArray(Thread[]::new);

		LOG.info("Try interrupt common pool"); //$NON-NLS-1$
		for (Thread thread : threads) {
			if (thread.isAlive()) { // $NON-NLS-1$
				// We look which workers might be executing student code
				var notIdle = Stream.of(thread.getStackTrace()).anyMatch(this::isStackFrameNotWhitelisted);
				if (notIdle) {
					LOG.info("Interrupting non-whitelsited code executing common pool worker: {}", thread); //$NON-NLS-1$
					thread.interrupt();
				}
			}
		}
		try {
			Thread.sleep(100);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (commonPool.isQuiescent())
			return;
		LOG.warn("There are still {} common pool workers active", commonPool.getActiveThreadCount()); //$NON-NLS-1$
	}

	private void checkThreadCreation() {
		if (blockThreadCreation || configuration == null || configuration.allowedThreadCount().isEmpty()) {
			checkForNonWhitelistedStackFrames(() -> localized("security.error_thread_access")); //$NON-NLS-1$
			return;
		}
		var current = testThreadGroup.activeCount();
		var max = configuration.allowedThreadCount().getAsInt();
		if (max < current)
			checkForNonWhitelistedStackFrames(() -> localized("security.error_thread_maxExceeded", current, max)); //$NON-NLS-1$
	}

	/**
	 * Similar to the way in {@link ForkJoinWorkerThread} in the JDK
	 */
	private ThreadGroup getRootThreadGroup() {
		var group = testThreadGroup; // NOSONAR
		for (ThreadGroup nextParent; (nextParent = group.getParent()) != null;)
			group = nextParent;
		return group;
	}

	private boolean isWorkerThreadAndInactive() {
		return !isActive && Thread.currentThread() == SecurityConstants.WORKER_THREAD;
	}

	private boolean isCurrentThreadWhitelisted() {
		var trustScope = configuration != null ? configuration.threadTrustScope() : TrustScope.MINIMAL;
		if (trustScope == TrustScope.ALL_THREADS)
			return true;
		var currentThread = Thread.currentThread();
		var name = externGet(currentThread::getName);
		/*
		 * NOTE: the order is very important here!
		 */
		if (THREAD_NAME_BLACKLIST.stream().anyMatch(name::startsWith))
			return trustScope != TrustScope.MINIMAL;
		if (!testThreadGroup.parentOf(currentThread.getThreadGroup()))
			return true;
		return whitelistedThreads.stream().anyMatch(t -> t.equals(currentThread));
	}

	private void whitelistThread(Thread t) {
		LOG.info("Request whitelisting: {} {}", t, INSTANCE.whitelistedThreads); //$NON-NLS-1$
		boolean whitelisted = isCurrentThreadWhitelisted();
		if (!whitelisted)
			throw new SecurityException(localized("security.error_thread_whitelisting_failed")); //$NON-NLS-1$
		whitelistedThreads.add(t);
		LOG.info("Thread whitelisted: {}", t); //$NON-NLS-1$
	}

	private void unwhitelistThreads() {
		whitelistedThreads.clear();
	}

	private void removeDeadThreads() {
		whitelistedThreads.removeIf(thread -> !thread.isAlive());
	}

	public static synchronized boolean isInstalled() {
		return System.getSecurityManager() instanceof ArtemisSecurityManager;
	}

	public static synchronized String install(AresSecurityConfiguration configuration) {
		if (INSTANCE.lastUninstallFailed) {
			LOG.info("Try recovery from lastUninstallFailed"); //$NON-NLS-1$
			INSTANCE.checkThreadGroup();
			INSTANCE.isPartlyDisabled = true;
			System.setSecurityManager(ORIGINAL);
			INSTANCE.isPartlyDisabled = false;
			INSTANCE.lastUninstallFailed = false;
		}
		if (LOG.isInfoEnabled())
			LOG.info("Request install with {}", configuration.shortDesc()); //$NON-NLS-1$
		String token = INSTANCE.generateAccessToken();
		INSTANCE.blockThreadCreation = false;
		INSTANCE.configuration = Objects.requireNonNull(configuration);
		INSTANCE.removeDeadThreads();
		if (!isInstalled())
			System.setSecurityManager(INSTANCE);
		INSTANCE.isActive = true;
		return token;
	}

	public static synchronized void uninstall(String accessToken) {
		if (!isInstalled())
			throw new IllegalStateException(localized("security.not_installed")); //$NON-NLS-1$
		var activeThreads = new Thread[0];
		int oldPrio = Thread.currentThread().getPriority();
		try {
			INSTANCE.checkAccess(accessToken);
			if (INSTANCE.isPartlyDisabled)
				throw new IllegalStateException(localized("security.already_disabled")); //$NON-NLS-1$

			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			LOG.info("Request uninstall"); //$NON-NLS-1$
			// try to clean up and try to run finalize() of test objects
			System.gc(); // NOSONAR
			System.runFinalization(); // NOSONAR
			// cannot be used in conjunction with classic JUnit timeout, use @StrictTimeout
			activeThreads = INSTANCE.checkThreadGroup();
			INSTANCE.checkCommonThreadPool();
			INSTANCE.unwhitelistThreads();
			INSTANCE.blockThreadCreation = false;
			INSTANCE.lastUninstallFailed = false;
			INSTANCE.isActive = false;
		} catch (Throwable t) { // NOSONAR
			INSTANCE.lastUninstallFailed = true;
			LOG.error("UNINSTALL FAILED", t); //$NON-NLS-1$
			throw t;
		} finally {
			Thread.currentThread().setPriority(oldPrio);
		}
		if (activeThreads.length > 0)
			throw new IllegalStateException(
					localized("security.error_threads_still_active", Arrays.toString(activeThreads))); //$NON-NLS-1$
	}

	public static synchronized void configure(String accessToken, AresSecurityConfiguration configuration) {
		INSTANCE.checkAccess(accessToken);
		INSTANCE.configuration = configuration;
	}

	public static synchronized void requestThreadWhitelisting(Thread t) {
		INSTANCE.whitelistThread(t);
	}

	public static synchronized void revokeThreadWhitelisting() {
		if (INSTANCE.isCurrentThreadWhitelisted())
			INSTANCE.unwhitelistThreads();
	}

	private static String hash(String s) {
		return Base64.getEncoder().encodeToString(SHA256.digest(s.getBytes(StandardCharsets.UTF_8)));
	}
}
