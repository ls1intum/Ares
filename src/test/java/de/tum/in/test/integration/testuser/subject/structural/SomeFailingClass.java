package de.tum.in.test.integration.testuser.subject.structural;

public class SomeFailingClass {

	public static final int SOME_CONSTANT = calculateConstant();

	@SuppressWarnings("unused")
	public SomeFailingClass(String someString, int someInt) {
		// Do nothing
	}

	@SuppressWarnings("unused")
	public void someMethodWithWrongParameterOrder(double someDouble, int someInt) {
		// Do nothing
	}

	public static int calculateConstant() {
		throw new RuntimeException();
	}
}
