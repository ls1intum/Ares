package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.exceptionHandlings.yes;

public class ClassWithAllKindsOfExceptionHandlings2 {

    public void assertStatement() {
        int x = 0;
        assert x == 0;
    }

    public void throwStatement() throws Exception{
        throw new Exception("This is a checked exception.");
    }

    public void tryCatchStatement() {
        try {
            throwStatement();
        } catch (Exception e) {}
    }
}
