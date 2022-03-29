package org.apache.xyz;

import java.util.Optional;

import de.tum.in.test.api.io.IOTester;

public class Circumvention extends Thread {

	public static Optional<Throwable> thrown = Optional.empty();

	@Override
	public void run() {
		try {
//			Thread[] theads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
//			Thread.currentThread().getThreadGroup().enumerate(theads);
//			System.err.println(" ==> " + Arrays.toString(theads) + " - " + Thread.currentThread());
			throw new NullPointerException();
		} catch (@SuppressWarnings("unused") NullPointerException e) {
//			e.printStackTrace();
		}
		try {
			IOTester.class.getDeclaredFields()[0].setAccessible(true);
		} catch (Throwable t) {
			thrown = Optional.of(t);
		}
	}
}
