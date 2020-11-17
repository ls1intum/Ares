package de.tum.in.testuser.subject.structural;

public class SomeFailingClass {

	public static final int SOME_CONSTANT = calculateConstant();

	public static int calculateConstant() {
		throw new RuntimeException();
	}
}
