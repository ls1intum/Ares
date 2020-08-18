package de.tum.in.testuser;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;

@Deadline("2200-01-01 16:00")
@TestMethodOrder(Alphanumeric.class)
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
