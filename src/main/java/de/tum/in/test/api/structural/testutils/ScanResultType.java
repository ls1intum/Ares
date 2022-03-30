package de.tum.in.test.api.structural.testutils;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Specifies the type of a {@link ScanResult}. This allows to differentiate
 * between correct, mistyped or not found classes.
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (2022-03-30)
 */
@API(status = Status.MAINTAINED)
public enum ScanResultType {
	CORRECT_NAME_CORRECT_PLACE,
	CORRECT_NAME_MISPLACED,
	CORRECT_NAME_MULTIPLE,
	WRONG_CASE_CORRECT_PLACE,
	WRONG_CASE_MISPLACED,
	WRONG_CASE_MULTIPLE,
	TYPOS_CORRECT_PLACE,
	TYPOS_MISPLACED,
	TYPOS_MULTIPLE,
	NOTFOUND
}
