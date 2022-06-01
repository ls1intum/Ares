package de.tum.in.test.integration.testuser;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.jupiter.*;
import de.tum.in.test.api.localization.UseLocale;

@UseLocale("en")
@Deadline("2200-01-01 16:00")
@TestMethodOrder(MethodName.class)
public class DeadlineUser {

	@HiddenTest
	@Deadline("2200-01-01 16:00")
	void testHiddenCustomDeadlineFuture() {
		// nothing
	}

	@HiddenTest
	@Deadline("2000-01-01 16:00")
	void testHiddenCustomDeadlinePast() {
		// nothing
	}

	@HiddenTest
	void testHiddenNormal() {
		// nothing
	}

	@HiddenTest
	@Deadline("2000-01-01 16:00 Europe/Berlin")
	void testHiddenTimeZoneBerlin() {
		// nothing
	}

	@HiddenTest
	@Deadline("2000-01-01 16:00 UTC")
	void testHiddenTimeZoneUtc() {
		// nothing
	}

	@PublicTest
	@Deadline("2000-01-01 16:00")
	void testPublicCustomDeadline() {
		// nothing
	}

	@PublicTest
	void testPublicNormal() {
		// nothing
	}
}
