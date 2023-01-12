package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.synchronisations.yes;

public class ClassWithAnyKindsOfSynchronisations {

    private static int i = 0;

    public void synchronisation() {
        synchronized (this) {
            ClassWithAnyKindsOfSynchronisations.i++;
        }
    }
}
