package de.tum.in.test.api;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@StrictTimeout(value = 100, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(Alphanumeric.class)
@SuppressWarnings({ "static-method", "unused" })
public class StrictTimeoutUser {

	@Test
	@StrictTimeout(1)
	void testOneSecondFail() throws InterruptedException {
		Thread.sleep(10_000);
	}

	@Test
	@StrictTimeout(1)
	void testOneSecondSuccess() throws InterruptedException {
		Thread.sleep(800);
	}

	@Test
	@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
	void testMethodFailNormal() throws InterruptedException {
		Thread.sleep(500);
	}

	@Test
	@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
	void testMethodFailLoop() {
		int x = 0;
		while (true)
			x++;
	}

	@Test
	@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
	void testMethodSuccess() throws InterruptedException {
		Thread.sleep(200);
	}

	@Test
	void testClassFailLoop() {
		int x = 0;
		while (true)
			x++;
	}

	@Test
	void testClassFailNormal() throws InterruptedException {
		Thread.sleep(200);
	}

	@Test
	void testClassSuccess() throws InterruptedException {
		Thread.sleep(20);
	}
}
