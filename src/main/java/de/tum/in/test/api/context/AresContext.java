package de.tum.in.test.api.context;

import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public abstract class AresContext {

	private final TestContext testContext;

	protected AresContext(TestContext testContext) {
		this.testContext = Objects.requireNonNull(testContext);
	}

	/**
	 * Returns the current test context.
	 *
	 * @return the {@link TestContext}, never null.
	 * @author Christian Femers
	 */
	public final TestContext testContext() {
		return testContext;
	}
}
