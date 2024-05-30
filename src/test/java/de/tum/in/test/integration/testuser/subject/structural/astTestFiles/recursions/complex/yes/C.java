package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.complex.yes;

public class C {

    public static String aRecursive() {
        return A.bRecursive();
    }
}
