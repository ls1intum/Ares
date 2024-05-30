package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.excludeMethods;

public class ClassWithNoExcludeMethods {

    public void something(RandomParameterThatShouldBeResolved parameter) {
        something(parameter);
    }

    public void somethingElse() {
        something(null);
    }

    public static void main(String[] args) {
        new ClassWithNoExcludeMethods().something(null);
    }
}
