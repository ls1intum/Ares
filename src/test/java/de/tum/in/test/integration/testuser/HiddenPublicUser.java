package de.tum.in.test.integration.testuser;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@UseLocale("en")
@Deadline("2200-01-01 16:00")
@TestMethodOrder(MethodName.class)
public class HiddenPublicUser {

	@Hidden
	@Test
	@Deadline("2200-01-01 16:00")
	void testHiddenCustomDeadlineFuture() {
		// nothing
	}

	@Hidden
	@Test
	@Deadline("2000-01-01 16:00")
	void testHiddenCustomDeadlinePast() {
		// nothing
	}

	@Hidden
	void testHiddenIncomplete() {
		// nothing
	}

	@Hidden
	@Test
	void testHiddenNormal() {
		// nothing
	}

	@Public
	@Test
	@Deadline("2200-01-01 16:00")
	void testPublicCustomDeadline() {
		// nothing
	}

	@Public
	void testPublicIncomplete() {
		// nothing
	}

	@Public
	@Test
	void testPublicNormal() {
		// nothing
	}
}
