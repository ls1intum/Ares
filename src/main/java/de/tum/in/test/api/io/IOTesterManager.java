package de.tum.in.test.api.io;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public final class IOTesterManager implements IOManager<IOTester> {

	private IOTester ioTester;

	@Override
	public void beforeTestExecution(AresIOContext context) {
		ioTester = IOTester.installNew(context.mirrorOutput(), context.maxStdOut());
	}

	@Override
	public void afterTestExecution(AresIOContext context) {
		IOTester.uninstallCurrent();
		ioTester = null;
	}

	@Override
	public IOTester getControllerInstance(AresIOContext context) {
		return ioTester;
	}

	@Override
	public Class<IOTester> getControllerClass() {
		return IOTester.class;
	}
}
