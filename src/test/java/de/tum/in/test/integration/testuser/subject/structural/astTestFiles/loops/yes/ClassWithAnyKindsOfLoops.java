package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.loops.yes;

public class ClassWithAnyKindsOfLoops {

    public void forLoop() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello World");
        }
    }

    public void forEachLoop() {
        for (int integer : new int[]{0,1,2,3,4,5,6,7,8,9}) {
            System.out.println("Hello World");
        }
    }

    public void whileLoop() {
        int i = 0;
        while (i < 10) {
            System.out.println("Hello World");
            i++;
        }

    }

    public void doWhileLoop() {
        int i = 0;
        do {
            System.out.println("Hello World");
            i++;
        } while (i < 10);
    }
}
