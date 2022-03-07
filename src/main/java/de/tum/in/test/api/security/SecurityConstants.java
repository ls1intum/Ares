package de.tum.in.test.api.security;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.internal.BlacklistedInvoker;

@API(status = Status.INTERNAL)
public final class SecurityConstants {

	static final PrintStream SYSTEM_OUT = System.out;
	static final PrintStream SYSTEM_ERR = System.err;
	static final Thread WORKER_THREAD = Thread.currentThread();
	static final ThreadGroup MAIN_THREAD_GROUP;
	static {
		ThreadGroup rootThreadGroup = TestUtils.getRootThreadGroup();
		ThreadGroup[] threadGroups = new ThreadGroup[rootThreadGroup.activeGroupCount() + 10];
		rootThreadGroup.enumerate(threadGroups, true);
		MAIN_THREAD_GROUP = Stream.of(threadGroups).filter(Objects::nonNull)
				.filter(threadGroup -> "main".equals(threadGroup.getName())) //$NON-NLS-1$
				.findFirst().orElse(null);
	}
	static final String SECURITY_PACKAGE_NAME = SecurityConstants.class.getPackageName();

	static final Set<String> STACK_WHITELIST = Set.of("java.", "org.junit.", "jdk.", "org.eclipse.", "com.intellij", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"org.assertj", "org.opentest4j.", "com.sun.", "sun.", "org.apache.", "de.tum.in.test.api", "net.jqwik", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"ch.qos.logback", "org.jacoco", "javax.", "org.json", "org.gradle", "worker.org.gradle", "com.teamscale", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"okhttp3", "retrofit2", "shadow", "com.squareup", "okio.Okio"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	static final Set<String> STACK_BLACKLIST = Set.of(BlacklistedInvoker.class.getName(),
			"org.junit.platform.commons.util.ReflectionUtils.getUnderlyingCause"); //$NON-NLS-1$

	static final Set<String> PACKAGE_USE_BLACKLIST = Set.of(SECURITY_PACKAGE_NAME, "de.tum.in.test.api.internal", //$NON-NLS-1$
			"jdk.internal", "sun.", "org.gradle"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private SecurityConstants() {
	}
}
