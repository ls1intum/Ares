package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.synchronisations.yes;

public class ClassWithAllKindsOfSynchronisations2 {

    private static int i = 0;

    public void synchronisation() {
        synchronized (this) {
            ClassWithAllKindsOfSynchronisations2.i++;
        }
    }
}
