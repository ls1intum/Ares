package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.exceptionHandlings.yes;

public class ClassWithAnyKindsOfExceptionHandlings {

	public void assertStatement() {
		assert 3 == 0;
	}

	public void throwStatement() throws Exception {
		throw new Exception("This is a checked exception.");
	}

	public void catchStatement() {
		try {
			throwStatement();
		} catch (Exception e) {
		}
	}
}
