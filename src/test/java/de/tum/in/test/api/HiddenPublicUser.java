package de.tum.in.test.api;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;

import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Deadline("2200-01-01 16:00")
@TestMethodOrder(Alphanumeric.class)
public class HiddenPublicUser {

	@Hidden
	void testHiddenIncomplete() {
		// nothing
	}

	@Public
	void testPublicIncomplete() {
		// nothing
	}

	@Hidden
	@Test
	void testHiddenNormal() {
		// nothing
	}

	@Public
	@Test
	void testPublicNormal() {
		// nothing
	}

	@Hidden
	@Test
	@Deadline("2000-01-01 16:00")
	void testHiddenCustomDeadlinePast() {
		// nothing
	}

	@Hidden
	@Test
	@Deadline("2200-01-01 16:00")
	void testHiddenCustomDeadlineFuture() {
		// nothing
	}

	@Public
	@Test
	@Deadline("2200-01-01 16:00")
	void testPublicCustomDeadline() {
		// nothing
	}
}
