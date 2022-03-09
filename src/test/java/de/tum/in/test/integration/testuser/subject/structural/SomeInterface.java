package de.tum.in.test.integration.testuser.subject.structural;

@FunctionalInterface
public interface SomeInterface {

	SomeEnum ANOTHER_CONSTANT = SomeEnum.ONE;

	int doSomething(String someString);

	default int doSomethingElse(int someInt) {
		return -someInt;
	}

	static SomeEnum getOne() {
		return ANOTHER_CONSTANT;
	}
}
