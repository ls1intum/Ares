package de.tum.in.test.api.internal;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;

/**
 * Type of an Artemis test case
 *
 * @see Hidden
 * @see Public
 * @see HiddenTest
 * @see PublicTest
 *
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.0.0
 */
@API(status = Status.INTERNAL)
public enum TestType {
	PUBLIC,
	HIDDEN
}
