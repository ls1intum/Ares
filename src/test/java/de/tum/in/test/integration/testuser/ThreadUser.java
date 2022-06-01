package de.tum.in.test.integration.testuser;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;

import de.tum.in.test.api.*;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.test.integration.testuser.subject.ThreadPenguin;

@UseLocale("en")
@AllowThreads(maxActiveCount = 100)
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "{target,build}/**", type = PathType.GLOB) // build for gradle tests
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class ThreadUser {

	@PublicTest
	void commonPoolInterruptable() throws InterruptedException, ExecutionException {
		// check functionality
		var res = ForkJoinPool.commonPool().submit(() -> "A").get();
		assertEquals("A", res);
		// submit long-running task
		var task = ForkJoinPool.commonPool().submit(() -> {
			ThreadPenguin.sleepInCurrentThread(5_000);
		});
		// check that the task is still running after 100 ms
		try {
			Thread.sleep(100);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			Thread.currentThread().interrupt();
			fail("waiting 100 ms was interrupted");
		}
		if (task.isDone())
			fail("task is done" + task);
		assertFalse(ForkJoinPool.commonPool().isQuiescent(), "common pool is quiescent but it shouldn't be");
		// wait for task end
		ForkJoinPool.commonPool().awaitQuiescence(5, TimeUnit.SECONDS);
	}

	@Disabled("Inconsistent on the CI environment")
	@PublicTest
	@StrictTimeout(2)
	void testThreadBomb() {
		ThreadPenguin.spawnEndlessThreads();
	}

	@PublicTest
	void testThreadExtension() throws Throwable {
		new ThreadPenguin().start();
	}

	@PublicTest
	void testThreadGroup() {
		ThreadPenguin.tryBreakThreadGroup();
	}

	@PublicTest
	void threadWhitelistingWithPathCorrect() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		ArtemisSecurityManager.requestThreadWhitelisting(t);
		t.setUncaughtExceptionHandler((t1, e) -> failure.set(e));
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}

	@PublicTest
	void threadWhitelistingWithPathFail() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		t.setUncaughtExceptionHandler((t1, e) -> failure.set(e));
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}

	@AllowThreads(maxActiveCount = 1)
	@PublicTest
	void threadLimitExceeded() throws Throwable {
		ThreadPenguin.tryStartTwoThreads();
	}

	@PublicTest
	void threadWhitelistingWithPathPenguin() throws Throwable {
		ThreadPenguin.tryThreadWhitelisting();
	}

	/**
	 * This can be used to check for Threads that are not stoppable. This should
	 * never happen, but it could. Note that this test beaks all further ones,
	 * because the security manager will not be uninstalled and block everything. It
	 * works by catching the {@link ThreadDeath}.
	 */
//	@PublicTest
//	void zz_unstoppable() {
//		long t = System.currentTimeMillis();
//		while (System.currentTimeMillis() - t < 1000) {
//			try {
//				Thread.sleep(100);
//			} catch (Throwable e) {
//				// ignore
//			}
//		}
//	}
}
