package de.tum.in.test.api.structural.testutils;

/**
 * A ScanResult the result object generated from the ClassNamesScanner. It
 * consists of an enum representing the type of the result and a string
 * representing the feedback tied to that result type.
 * 
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.0 (11.11.2020)
 */
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
