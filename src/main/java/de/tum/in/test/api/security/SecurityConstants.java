package de.tum.in.test.api.security;

import java.io.PrintStream;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.BlacklistedInvoker;

@API(status = Status.INTERNAL)
public final class SecurityConstants {

	static final PrintStream SYSTEM_OUT = System.out;
	static final PrintStream SYSTEM_ERR = System.err;
	static final Thread MAIN_THREAD = Thread.currentThread();
	static final String SECURITY_PACKAGE_NAME = SecurityConstants.class.getPackageName();

	static final Set<String> STACK_WHITELIST = Set.of("java.", "org.junit.", "jdk.", "org.eclipse.", "com.intellij", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"org.assertj", "org.opentest4j.", "com.sun.", "sun.", "org.apache.", "de.tum.in.test.api", "net.jqwik", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"ch.qos.logback", "org.jacoco", "javax.", "org.json", "org.gradle", "com.teamscale", "okhttp3", "retrofit2", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"shadow.org", "com.squareup", "okio.Okio", "shadow.spark");
	static final Set<String> STACK_BLACKLIST = Set.of(BlacklistedInvoker.class.getName(),
			"org.junit.platform.commons.util.ReflectionUtils.getUnderlyingCause"); //$NON-NLS-1$

	static final Set<String> PACKAGE_USE_BLACKLIST = Set.of(SECURITY_PACKAGE_NAME, "de.tum.in.test.api.internal", //$NON-NLS-1$
			"jdk.internal", "sun.", "org.gradle");

	private SecurityConstants() {
	}
}
