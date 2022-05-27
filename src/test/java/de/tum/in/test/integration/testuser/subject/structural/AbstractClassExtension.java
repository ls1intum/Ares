package de.tum.in.test.integration.testuser.subject.structural;

public class AbstractClassExtension extends SomeAbstractClass implements SomeInterface {
	@Override
	void doNothing() {
		// nothing
	}

	public void declaredMethod() {
		// nothing
	}

	@SuppressWarnings("unused")
	private void declaredMethod(int x) {
		// nothing
	}

	@SuppressWarnings("unused")
	private void declaredMethod(String s) {
		// nothing
	}

	@Override
	public int doSomething(String someString) {
		return 10;
	}
}
