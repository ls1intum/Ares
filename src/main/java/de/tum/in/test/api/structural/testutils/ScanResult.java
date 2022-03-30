package de.tum.in.test.api.structural.testutils;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * A ScanResult the result object generated from the ClassNamesScanner. It
 * consists of an enum representing the type of the result and a string
 * representing the feedback tied to that result type.
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (2022-03-30)
 */
@API(status = Status.MAINTAINED)
public class ScanResult {

	private final ScanResultType type;
	private final String message;

	public ScanResult(ScanResultType result, String message) {
		this.type = result;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public ScanResultType getResult() {
		return type;
	}
}
