package de.tum.in.test.api.security;

import static java.util.function.Predicate.not;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.*;

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
	private static final Set<String> USER_DEFINED_STACK_WHITELIST;
	static {
		// Find the main thread group by traversing up and filter for name "main"
		ThreadGroup rootThreadGroup = TestUtils.getRootThreadGroup();
		ThreadGroup[] threadGroups = new ThreadGroup[rootThreadGroup.activeGroupCount() + 10];
		rootThreadGroup.enumerate(threadGroups, true);
		MAIN_THREAD_GROUP = Stream.of(threadGroups).filter(Objects::nonNull)
				.filter(threadGroup -> "main".equals(threadGroup.getName())) //$NON-NLS-1$
				.findFirst().orElse(null);
		// Search for additional trusted packages specified by system properties
		String additionalTurstedPackages = System.getProperty(AresSystemProperties.ARES_SECURITY_TRUSTED_PACKAGES);
		if (additionalTurstedPackages != null)
			USER_DEFINED_STACK_WHITELIST = Stream.of(additionalTurstedPackages.split(",")).filter(not(String::isBlank)) //$NON-NLS-1$
					.collect(Collectors.toUnmodifiableSet());
		else
			USER_DEFINED_STACK_WHITELIST = Set.of();
	}
	static final String SECURITY_PACKAGE_NAME = SecurityConstants.class.getPackageName();

	private static final Set<String> STATIC_STACK_WHITELIST = Set.of("java.", "org.junit.", "jdk.", "org.eclipse.", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"com.intellij", "org.assertj", "org.opentest4j.", "com.sun.", "sun.", "org.apache.", "de.tum.in.test.api", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"net.jqwik.", "ch.qos.logback", "org.jacoco", "javax.", "org.json", "org.gradle", "worker.org.gradle", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"com.github.javaparser");
	static final Set<String> STACK_WHITELIST = Stream
			.concat(STATIC_STACK_WHITELIST.stream(), USER_DEFINED_STACK_WHITELIST.stream())
			.collect(Collectors.toUnmodifiableSet());
	static final Set<String> STACK_BLACKLIST = Set.of(BlacklistedInvoker.class.getName(),
			"org.junit.platform.commons.util.ReflectionUtils.getUnderlyingCause"); //$NON-NLS-1$

	static final Set<String> PACKAGE_USE_BLACKLIST = Set.of(SECURITY_PACKAGE_NAME, "de.tum.in.test.api.internal", //$NON-NLS-1$
			"jdk.internal", "sun."); //$NON-NLS-1$ //$NON-NLS-2$

	private SecurityConstants() {
	}
}
