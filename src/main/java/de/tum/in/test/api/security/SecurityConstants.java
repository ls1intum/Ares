package de.tum.in.test.api.security;

import java.io.PrintStream;
import java.util.Set;

import de.tum.in.test.api.internal.BlacklistedInvoker;

public final class SecurityConstants {

	static final PrintStream SYSTEM_OUT = System.out;
	static final PrintStream SYSTEM_ERR = System.err;
	static final Thread MAIN_THREAD = Thread.currentThread();
	static final String SECURITY_PACKAGE_NAME = SecurityConstants.class.getPackageName();

	static final Set<String> STACK_WHITELIST = Set.of("java.", "org.junit.", "jdk.", "org.eclipse.", "com.intellij", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"org.assertj", "org.opentest4j.", "com.sun.", "sun.", "org.apache.", "de.tum.in.test.", "net.jqwik", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"ch.qos.logback", "org.jacoco", "javax.", "org.json", SECURITY_PACKAGE_NAME); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	static final Set<String> STACK_BLACKLIST = Set.of(BlacklistedInvoker.class.getName());

	static final Set<String> PACKAGE_USE_BLACKLIST = Set.of(SECURITY_PACKAGE_NAME, "de.tum.in.test.api.internal", //$NON-NLS-1$
			"jdk.internal", "sun."); //$NON-NLS-1$ //$NON-NLS-2$

	private SecurityConstants() {
	}
}
