package de.tum.in.test.integration.testuser.subject.structural;

public class AbstractClassExtension extends SomeAbstractClass {
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
}
