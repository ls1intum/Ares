package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.conditionals.yes;

import java.util.Random;

public class ClassWithAnyKindsOfConditionals2 {

    public void ifStatement() {
        int x = new Random().nextInt(3);
        if (x == 1) {
            System.out.println("Hello");
        } else if (x == 0) {
            System.out.println("World");
        } else {
            System.out.println("!");
        }
    }

    public void switchStatement() {
        String output;
        switch (new Random().nextInt(3)) {
            case 1:
                output = "Hello";
                break;
            case 0:
                output = "World";
                break;
            default:
                output = "!";
                break;
        }
        System.out.println(output);
    }
}
