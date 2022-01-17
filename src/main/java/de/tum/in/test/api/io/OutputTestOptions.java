package de.tum.in.test.api.io;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Output test options to be used with {@link OutputTester}
 *
 * @author Christian Femers
 */
@API(status = Status.STABLE)
public enum OutputTestOptions {
	/*
	 * A last empty line is ignored by default. Add this option to include this line
	 * into checks and returned strings.
	 */
	DONT_IGNORE_LAST_EMPTY_LINE;

	static final OutputTestOptions[] NONE = {};

	boolean isIn(OutputTestOptions[] outputOptions) {
		for (OutputTestOptions outputOption : outputOptions)
			if (this == outputOption)
				return true;
		return false;
	}
}
