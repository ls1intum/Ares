package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.exceptionHandlings.no;

import java.util.stream.IntStream;

public class ClassWithNoKindsOfExceptionHandlings {

	public void ifStatement() {
		int x = 3;
		if (x == 1) {
			System.out.println("Hello");
		} else if (x == 0) {
			System.out.println("World");
		} else {
			System.out.println("!");
		}
	}

	public void ifExpression() {
		int x = 3;
		System.out.println(x == 1 ? "Hello" : (x == 0 ? "World" : "!"));
	}

	public void switchStatement() {
		String output;
		switch (3) {
		case 1:
			output = "Hello";
			break;
		case 0:
			output = "World";
			break;
		default:
			output = "!";
			break;
		}
		System.out.println(output);
	}

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

	void localClassContainingFunction() {
		class localClass {
		}
	}
}
