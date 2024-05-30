package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.excludeMethods;

public class RandomParameterThatShouldBeResolved {

    private final int value;

    public RandomParameterThatShouldBeResolved(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
