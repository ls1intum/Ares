package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.loops.no;

import java.util.Random;
import java.util.stream.IntStream;

public class ClassWithNoKindsOfLoops {

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

    public void ifExpression() {
        int x = new Random().nextInt(3);
        System.out.println(x == 1 ? "Hello" : (x == 0 ? "World" : "!"));
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

    /*public void switchExpression() {
        System.out.println(
                switch (new Random().nextInt(3)) {
                    case 1 -> "Hello";
                    case 0 -> "World";
                    default -> "!";
                }
        );
    }*/

    public void assertStatement() {
        assert (new Random().nextInt(3)) == 0;
    }

    public void throwStatement() throws Exception {
        throw new Exception("This is a checked exception.");
    }

    public void catchStatement() {
        try {
            throwStatement();
        } catch (Exception e) {
        }
    }

    void localClassContainingFunction() {
        class localClass {
        }
    }

    /*void localRecordContainingFunction() {
        record localRecord(String id) {

        }
    }*/

}
