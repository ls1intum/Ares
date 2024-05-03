package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.simple;

public class Recursive {

    public void recursiveMethod() {
        recursiveMethod();
    }

    public static void main(String[] args) {
        Recursive recursive = new Recursive();
        recursive.recursiveMethod();
    }
}
