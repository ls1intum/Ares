package de.tum.in.test.api.io;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.context.*;
import de.tum.in.test.api.internal.ConfigurationUtils;

@API(status = Status.EXPERIMENTAL)
public final class AresIOContext extends AresContext {

	private final boolean mirrorOutput;
	private final long maxStdOut;

	private AresIOContext(TestContext testContext, boolean mirrorOutput, long maxStdOut) {
		super(testContext);
		this.mirrorOutput = mirrorOutput;
		this.maxStdOut = maxStdOut;
	}

	/**
	 * Returns true if the user requested to mirror recorded output to the console.
	 *
	 * @return the mirror output value.
	 * @see MirrorOutput#value()
	 */
	public boolean mirrorOutput() {
		return mirrorOutput;
	}

	/**
	 * Returns the maximal number of chars that the test should allow to be printed.
	 *
	 * @return the maximal number of chars.
	 * @see MirrorOutput#maxCharCount()
	 */
	public long maxStdOut() {
		return maxStdOut;
	}

	public static AresIOContext from(TestContext testContext) {
		boolean mirrorOutput = ConfigurationUtils.shouldMirrorOutput(testContext);
		long maxStdOut = ConfigurationUtils.getMaxStandardOutput(testContext);
		return new AresIOContext(testContext, mirrorOutput, maxStdOut);
	}
}
