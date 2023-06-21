package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.classes.yes;

public class ClassWithAnyKindsOfUnsupportedClasses {

    void localClassContainingFunction() {
        class localClass {
        }
    }

    void localRecordContainingFunction() {
        record localRecord (String id){
        }
    }
}
