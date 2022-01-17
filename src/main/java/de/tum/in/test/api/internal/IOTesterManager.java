package de.tum.in.test.api.internal;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.security.ArtemisSecurityManager;

@API(status = Status.INTERNAL)
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
