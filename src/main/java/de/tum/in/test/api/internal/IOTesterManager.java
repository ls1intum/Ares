package de.tum.in.test.api.internal;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.security.ArtemisSecurityManager;

public final class IOTesterManager {

	static {
		/*
		 * Initialize SecurityManager when we are still in the main thread
		 */
		ArtemisSecurityManager.isInstalled();
	}

	private final TestContext context;
	private IOTester ioTester;

	public IOTesterManager(TestContext context) {
		this.context = context;
	}

	public void beforeTestExecution() {
		boolean mirrorOutput = ConfigurationUtils.shouldMirrorOutput(context);
		long maxStdOut = ConfigurationUtils.getMaxStandardOutput(context);
		ioTester = IOTester.installNew(mirrorOutput, maxStdOut);
	}

	public void afterTestExecution() {
		IOTester.uninstallCurrent();
		ioTester = null;
	}

	public IOTester getIOTester() {
		return ioTester;
	}

}
