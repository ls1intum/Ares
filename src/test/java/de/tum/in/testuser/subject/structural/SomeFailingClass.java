package de.tum.in.testuser.subject.structural;

public class SomeFailingClass {

	public static final int SOME_CONSTANT = calculateConstant();

	public SomeFailingClass(String someString, int someInt) {

	}

	public void someMethodWithWrongParameterOrder(double someDouble, int someInt) {
		// Do nothing
	}
	
	public static int calculateConstant() {
		throw new RuntimeException();
	}
}
