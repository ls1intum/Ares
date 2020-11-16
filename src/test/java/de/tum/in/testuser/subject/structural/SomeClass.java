package de.tum.in.testuser.subject.structural;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class SomeClass implements SomeInterface {

	public static final int SOME_CONSTANT = 42;

	private String someAttribute;
	private List<Function<? super String, Integer>> doSomethingOperations;

	SomeClass(String someAttribute) {
		this.someAttribute = someAttribute;
		doSomethingOperations = List.of(String::length, s -> (int) s.lines().count());
	}

	public final String getSomeAttribute() {
		return someAttribute;
	}

	public final void setSomeAttribute(String someAttribute) {
		this.someAttribute = someAttribute;
	}

	@Override
	public int doSomething(String someString) {
		int chosenOperation = ThreadLocalRandom.current().nextInt(doSomethingOperations.size());
		return doSomethingOperations.get(chosenOperation).apply(someString);
	}

	@Override
	public int doSomethingElse(int someInt) {
		return Math.max(SOME_CONSTANT, someInt);
	}
}
