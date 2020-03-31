package de.tum.in.test.api.internal;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.security.ArtemisSecurityManager;

public final class GeneralTestExtension {

	private final TestContext context;
	private String token;
	private IOTester ioTester;

	public GeneralTestExtension(TestContext context) {
		this.context = context;
	}

	public void beforeTestExecution() throws Exception {
		boolean mirrorOutput = ConfigurationUtils.shouldMirrorOutput(context);
		long maxStdOut = ConfigurationUtils.getMaxStandardOutput(context);
		ioTester = IOTester.installNew(mirrorOutput, maxStdOut);
		try {
			token = ArtemisSecurityManager.install(ConfigurationUtils.generateConfiguration(context));
		} catch (Throwable t) {
			try {
				IOTester.uninstallCurrent();
			} catch (Exception e) {
				t.addSuppressed(e);
			}
			throw t;
		}
	}

	public void afterTestExecution() throws Exception {
		try {
			ArtemisSecurityManager.uninstall(token);
		} finally {
			IOTester.uninstallCurrent();
			ioTester = null;
		}
	}

	public IOTester getIOTester() {
		return ioTester;
	}

}
