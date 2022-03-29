package de.tum.in.test.integration.testuser.subject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import de.tum.in.test.api.security.ArtemisSecurityManager;

public final class ThreadPenguin extends Thread {

	public ThreadPenguin() {
		super("ThreadPenguin");
	}

	public static void sleepInCurrentThread(long millies) {
		try {
			Thread.sleep(millies);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static void tryStartTwoThreads() {
		Thread t1 = new Thread(() -> {
			try {
				Thread.sleep(100);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				// ignore
			}
		});
		assertDoesNotThrow(() -> {
			t1.start();
		});

		new Thread().start();
	}

	public static void tryBreakThreadGroup() {
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		for (;;) {
			ThreadGroup parent = threadGroup.getParent();
			if (parent == null)
				break;
			threadGroup = parent;
		}
		new Thread(threadGroup, () -> {
			// nothing
		}).start();
	}

	public static void spawnEndlessThreads() {
		try {
			Thread.sleep(2);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// nothing
		}
		for (int i = 0; i < 2000; i++) {
			Thread t = new Thread(ThreadPenguin::spawnEndlessThreads);
			t.start();
		}
	}

	public static void tryThreadWhitelisting() throws Throwable {
		AtomicReference<Throwable> failure = new AtomicReference<>();
		Thread t = new Thread(() -> Path.of("pom.xml").toFile().canWrite());
		ArtemisSecurityManager.requestThreadWhitelisting(t);
		t.setUncaughtExceptionHandler((t1, e) -> failure.set(e));
		t.start();
		t.join();
		if (failure.get() != null)
			throw failure.get();
	}
}
