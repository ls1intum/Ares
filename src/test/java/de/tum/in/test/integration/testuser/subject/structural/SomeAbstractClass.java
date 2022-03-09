package de.tum.in.test.integration.testuser.subject.structural;

public abstract class SomeAbstractClass {

	public static int someInt = 2;

	public SomeAbstractClass() {
		// nothing
	}

	@SuppressWarnings("unused")
	protected SomeAbstractClass(String someString, int someInt) {
		// nothing
	}

	abstract void doNothing();
}
