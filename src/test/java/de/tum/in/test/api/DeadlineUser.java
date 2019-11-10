package de.tum.in.test.api;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;

@Deadline("2200-01-01 16:00")
@TestMethodOrder(Alphanumeric.class)
public class DeadlineUser {

	@HiddenTest
	void testHiddenNormal() {
		// nothing
	}

	@PublicTest
	void testPublicNormal() {
		// nothing
	}

	@HiddenTest
	@Deadline("2000-01-01 16:00")
	void testHiddenCustomDeadlinePast() {
		// nothing
	}

	@HiddenTest
	@Deadline("2200-01-01 16:00")
	void testHiddenCustomDeadlineFuture() {
		// nothing
	}

	@PublicTest
	@Deadline("2000-01-01 16:00")
	void testPublicCustomDeadline() {
		// nothing
	}
}
