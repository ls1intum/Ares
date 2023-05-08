package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.exceptionHandlings.yes;

import java.util.Random;

public class ClassWithAnyKindsOfExceptionHandlings {

	public void assertStatement() {
		assert (new Random().nextInt(3)) == 0;
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
