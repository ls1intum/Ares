package de.tum.in.test.integration.testuser.subject.structural;

public abstract class SomeAbstractClass {

	public static int someInt = 2;

	protected final String someProtectedAttribute = "hidden";

	@SuppressWarnings("unused")
	private final long somePrivateAttribute = 3L;

	public SomeAbstractClass() {
		// nothing
	}

	@SuppressWarnings("unused")
	protected SomeAbstractClass(String someString, int someInt) {
		// nothing
	}

	abstract void doNothing();

	protected void nonAbstractProtected() {
		// nothing
	}

	@SuppressWarnings("unused")
	private void nonAbstractPrivate() {
		// nothing
	}

	void nonAbstractPackagePrivate() {
		// nothing
	}
}
