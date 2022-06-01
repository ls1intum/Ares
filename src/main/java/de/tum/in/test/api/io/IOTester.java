package de.tum.in.test.api.io;

import static de.tum.in.test.api.localization.Messages.localized;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Class for testing console input and output of programs.
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.2
 */
@API(status = Status.STABLE)
public final class IOTester {

	static final String LINE_SEPERATOR = "\n"; //$NON-NLS-1$

	static {
		checkEncoding();
	}

	private static IOTester instance;

	private final InputStream oldIn;
	private final PrintStream oldOut;
	private final PrintStream oldErr;

	private final TestInStream in;
	private final TestOutStream out;
	private final TestOutStream err;

	private final InputTester inTester;
	private final OutputTester outTester;
	private final OutputTester errTester;

	private boolean isInstalled;

	private IOTester(boolean mirrorOutput, long maxChars) {
		// backup
		oldIn = System.in;
		oldOut = System.out;
		oldErr = System.err;

		// initialize expected input/output management
		inTester = new InputTester();
		outTester = new OutputTester();
		errTester = new OutputTester();

		// initialize test streams
		in = new TestInStream(inTester);
		out = new TestOutStream(outTester, mirrorOutput ? oldOut : null, maxChars);
		err = new TestOutStream(errTester, mirrorOutput ? oldErr : null, maxChars);
	}

	public synchronized void install() {
		// check permission already here, we need to be allowed to set system IO
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkPermission(new RuntimePermission("setIO")); //$NON-NLS-1$
		// if this is a problem, make sure to install the security manager after
		// IOTester

		// set test streams
		System.setIn(in);
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(err, true));

		isInstalled = true;
	}

	public synchronized void uninstall() {
		// set original streams
		System.setIn(oldIn);
		System.setOut(oldOut);
		System.setErr(oldErr);

		isInstalled = false;
	}

	public synchronized void reset() {
		inTester.resetInput();
		outTester.resetOutput();
		errTester.resetOutput();

		in.resetInternalState();
		out.resetInternalState();
		err.resetInternalState();
	}

	public IOTester provideInputLines(String... givenInputLines) {
		inTester.addLinesToInput(givenInputLines);
		return this;
	}

	/**
	 * Returns all standard output as lines
	 *
	 * @return a list of all standard output lines
	 * @deprecated See {@link OutputTester#getOutput()}, use
	 *             <code>out().getLines()</code> instead. (With
	 *             <code>OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE</code> if
	 *             testing for the last empty line is desired.)
	 */
	@Deprecated(since = "1.3.2")
	public List<Line> getOutput() {
		return outTester.getOutput();
	}

	/**
	 * Returns all error output as lines
	 *
	 * @return a list of all error output lines
	 * @deprecated See {@link OutputTester#getOutput()} use
	 *             <code>err().getLines()</code> instead. (With
	 *             <code>OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE</code> if
	 *             testing for the last empty line is desired.)
	 */
	@Deprecated(since = "1.3.2")
	public List<Line> getErrorOutput() {
		return errTester.getOutput();
	}

	/**
	 * Returns all standard output as single string.
	 *
	 * @return all standard output lines as single string.
	 * @deprecated See {@link OutputTester#getOutput()}, use
	 *             <code>out().getOutputAsString()</code> instead. (With
	 *             <code>OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE</code> if
	 *             testing for the last empty line is desired)
	 */
	@Deprecated(since = "1.3.2")
	public String getOutputAsString() {
		return Line.joinLinesToString(getOutput(), LINE_SEPERATOR);
	}

	/**
	 * Returns all error output as single string.
	 *
	 * @return all error output lines as single string.
	 * @deprecated See {@link OutputTester#getOutput()}, use
	 *             <code>err().getOutputAsString()</code> instead. (With
	 *             <code>OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE</code> if
	 *             testing for the last empty line is desired)
	 */
	@Deprecated(since = "1.3.2")
	public String getErrorOutputAsString() {
		return Line.joinLinesToString(getErrorOutput(), LINE_SEPERATOR);
	}

	public InputTester getInTester() {
		return inTester;
	}

	public OutputTester getOutTester() {
		return outTester;
	}

	public OutputTester getErrTester() {
		return errTester;
	}

	/**
	 * Testing counterpart to {@link System#in}.
	 * <p>
	 * Returns the input tester.
	 *
	 * @return the {@link InputTester} for {@link System#in}
	 */
	public InputTester in() {
		return getInTester();
	}

	/**
	 * Testing counterpart to {@link System#out}.
	 * <p>
	 * Returns the standard output tester.
	 *
	 * @return the {@link OutputTester} for {@link System#out}
	 */
	public OutputTester out() {
		return getOutTester();
	}

	/**
	 * Testing counterpart to {@link System#err}.
	 * <p>
	 * Returns the error output tester.
	 *
	 * @return the {@link OutputTester} for {@link System#err}
	 */
	public OutputTester err() {
		return getErrTester();
	}

	public static synchronized boolean isInstalled() {
		return instance != null && instance.isInstalled;
	}

	public static synchronized IOTester installNew(boolean mirrorOutput, long maxChars) {
		if (isInstalled())
			throw new IllegalStateException(localized("io_tester.already_installed")); //$NON-NLS-1$
		instance = new IOTester(mirrorOutput, maxChars);
		instance.install();
		return instance;
	}

	public static synchronized void uninstallCurrent() {
		if (!isInstalled())
			throw new IllegalStateException(localized("io_tester.not_installed")); //$NON-NLS-1$
		instance.uninstall();
		instance = null;
	}

	private static void checkEncoding() {
		Charset cs = Charset.defaultCharset();
		if (!"UTF-8".equals(cs.name())) { //$NON-NLS-1$
			String message = localized("io_tester.default_not_utf8", cs); //$NON-NLS-1$
			System.err.println(message); // this is more noticeable in maven build log
			throw new IllegalStateException(message);
		}
	}
}
