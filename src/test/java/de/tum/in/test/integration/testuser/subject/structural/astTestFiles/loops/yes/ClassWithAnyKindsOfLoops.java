package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.loops.yes;

import java.util.stream.IntStream;

public class ClassWithAnyKindsOfLoops {

	public void forLoop() {
		for (int i = 0; i < 3; i++) {
			System.out.println("Hello World");
		}
	}

	public void forEachLoop() {
		for (int integer : new int[] { 3, 3, 3 }) {
			System.out.println("Hello World");
		}
	}

	public void whileLoop() {
		int i = 0;
		while (i < 3) {
			System.out.println("Hello World");
			i++;
		}
	}

	public void doWhileLoop() {
		int i = 0;
		do {
			System.out.println("Hello World");
			i++;
		} while (i < 3);
	}

	public void forEachStream() {
		IntStream.range(0, 3).mapToObj((int i) -> "Hello World").forEach(System.out::println);
	}
}
