package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.excludeMain.yes;

public class ClassWithLoopOutsideMainMethod {

	public void forLoop() {
		for (int i = 0; i < 3; i++) {
			System.out.println("Hello World");
		}
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.println("Hello World");
		}
	}


}
