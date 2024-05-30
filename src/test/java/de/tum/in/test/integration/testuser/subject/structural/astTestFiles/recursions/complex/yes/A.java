package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.complex.yes;

public class A {

    public static String bRecursive() {
        return B.cRecursive();
    }
}
