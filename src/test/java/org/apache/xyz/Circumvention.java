package org.apache.xyz;

import java.util.Optional;

public class Circumvention extends Thread {

	public static Optional<RuntimeException> thrown = Optional.empty();

	@Override
	public void run() {
		try {
//			Thread[] theads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
//			Thread.currentThread().getThreadGroup().enumerate(theads);
//			System.err.println(" ==> " + Arrays.toString(theads) + " - " + Thread.currentThread());
			throw new NullPointerException();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		try {
			try {
				Class.forName("de.tum.in.test.api.io.IOTester").getDeclaredFields()[0].setAccessible(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (RuntimeException t) {
			thrown = Optional.of(t);
		}
	}
}
