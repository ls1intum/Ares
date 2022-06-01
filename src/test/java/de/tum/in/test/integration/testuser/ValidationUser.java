package de.tum.in.test.integration.testuser;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@UseLocale("en")
public class ValidationUser {

	@AllowLocalPort(value = { 21, 80 }, allowPortsAbove = 21, exclude = { 21, 22 })
	@Public
	@Test
	void allowAndExcludeLocalPortIntersect() {
		// nothing
	}

	@AllowLocalPort(value = { 21, 80 }, allowPortsAbove = 10)
	@Public
	@Test
	void allowLocalPortInsideAllowAboveRange() {
		// nothing
	}

	@AllowLocalPort(allowPortsAbove = 80, exclude = { 8080, 22 })
	@Public
	@Test
	void excludeLocalPortValueToSmall() {
		// nothing
	}

	@AllowLocalPort(exclude = 22)
	@Public
	@Test
	void excludeLocalPortValueWithoutAllowAbove() {
		// nothing
	}

	@AllowLocalPort(allowPortsAbove = -1)
	@Public
	@Test
	void negativeAllowLocalPortAboveValue() {
		// nothing
	}

	@AllowLocalPort(value = -1)
	@Public
	@Test
	void negativeAllowLocalPortValue() {
		// nothing
	}

	@AllowLocalPort(allowPortsAbove = 10, exclude = -1)
	@Public
	@Test
	void negativeExcludeLocalPortValue() {
		// nothing
	}

	@AllowThreads(maxActiveCount = -1)
	@Public
	@Test
	void negativeMaxActiveCount() {
		// nothing
	}

	@AllowLocalPort(allowPortsAbove = 100_000)
	@Public
	@Test
	void tooLargeAllowLocalPortAboveValue() {
		// nothing
	}

	@AllowLocalPort(value = 100_000)
	@Public
	@Test
	void tooLargeAllowLocalPortValue() {
		// nothing
	}

	@AllowLocalPort(allowPortsAbove = 10, exclude = 100_000)
	@Public
	@Test
	void tooLargeExcludeLocalPortValue() {
		// nothing
	}
}
