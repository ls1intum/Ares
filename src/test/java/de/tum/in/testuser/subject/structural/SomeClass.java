package de.tum.in.testuser.subject.structural;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class SomeClass implements SomeInterface {

	public static final int SOME_CONSTANT = 42;

	private String someAttribute;
	private Integer anotherAttribute;
	private List<Function<? super String, Integer>> doSomethingOperations;
	private final int someFinalAttribute = SOME_CONSTANT;

	public SomeClass() {
	}

	public SomeClass(@SuppressWarnings("unused") Boolean bool) {
		throw new RuntimeException();
	}

	public SomeClass(Integer anotherAttribute) {
		this.anotherAttribute = anotherAttribute;
	}

	SomeClass(String someAttribute) {
		this.someAttribute = someAttribute;
		doSomethingOperations = List.of(String::length, s -> (int) s.lines().count());
	}

	public final String getSomeAttribute() {
		return someAttribute;
	}

	public final Integer getAnotherAttribute() {
		return anotherAttribute;
	}

	public final void setSomeAttribute(String someAttribute) {
		this.someAttribute = someAttribute;
	}

	@Override
	public int doSomething(String someString) {
		int chosenOperation = superSecretMethod();
		return doSomethingOperations.get(chosenOperation).apply(someString);
	}

	@Override
	public int doSomethingElse(int someInt) {
		return Math.max(someFinalAttribute, someInt);
	}

	public int doSomethingWithMoreParameters(int someInt, double someDouble, String someString) {
		return (int) (someString.length() * someInt / someDouble);
	}

	public void throwException() {
		throw new RuntimeException();
	}

	public Class<?> initializeFailingClass() throws ClassNotFoundException {
		return Class.forName("de.tum.in.testuser.subject.structural.SomeFailingClass");
	}

	private int superSecretMethod() {
		return ThreadLocalRandom.current().nextInt(doSomethingOperations.size());
	}
}
