package de.tum.in.testuser;

import static org.junit.Assert.*;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.testuser.subject.ThreadPenguin;

@UseLocale("en")
@AllowThreads(maxActiveCount = 100)
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(value = 300, unit = TimeUnit.MILLISECONDS)
@TestMethodOrder(Alphanumeric.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class ThreadUser {

	@PublicTest
	public void commonPoolInterruptable() throws InterruptedException, ExecutionException {
		// check functionality
		var res = ForkJoinPool.commonPool().submit(() -> "A").get();
		assertEquals("A", res);
		// submit long-running task
		ForkJoinPool.commonPool().submit(() -> {
			try {
				Thread.sleep(5_000);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		// check that the task is still running after 100 ms
		try {
			Thread.sleep(100);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		assertFalse(ForkJoinPool.commonPool().isQuiescent());
		// wait for task end
		ForkJoinPool.commonPool().awaitQuiescence(5, TimeUnit.SECONDS);
	}

	@Disabled("Inconsistent on the CI environment")
	@PublicTest
	@StrictTimeout(2)
	public void testThreadBomb() {
		ThreadPenguin.spawnEndlessThreads();
	}

	@PublicTest
	public void testThreadGroup() {
		ThreadPenguin.tryBreakThreadGroup();
	}

	@PublicTest
	public void threadWhitelistingWithPathCorrect() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		ArtemisSecurityManager.requestThreadWhitelisting(t);
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				failure.set(e);
			}
		});
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}

	@PublicTest
	public void threadWhitelistingWithPathFail() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				failure.set(e);
			}
		});
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}
	
	@AllowThreads(maxActiveCount = 1)
	@PublicTest
	public void threadLimitExceeded() throws Throwable {
		ThreadPenguin.tryStartTwoThreads();
	}

	@PublicTest
	public void threadWhitelistingWithPathPenguin() throws Throwable {
		ThreadPenguin.tryThreadWhitelisting();
	}

	/**
	 * This can be used to check for Threads that are not stoppable. This should
	 * never happen, but it could. Note that this test beaks all further ones,
	 * because the security manager will not be uninstalled and block everything. It
	 * works by catching the {@link ThreadDeath}.
	 */
//	@PublicTest
//	public void zz_unstoppable() {
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