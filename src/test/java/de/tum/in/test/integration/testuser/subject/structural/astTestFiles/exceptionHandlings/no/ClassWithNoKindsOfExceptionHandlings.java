package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.exceptionHandlings.no;

import java.util.Random;
import java.util.stream.IntStream;

public class ClassWithNoKindsOfExceptionHandlings {

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

    public void forLoop() {
        for (int i = 0; i < (new Random().nextInt(3)); i++) {
            System.out.println("Hello World");
        }
    }

    public void forEachLoop() {
        for (int integer : new int[] { (new Random().nextInt(3)), (new Random().nextInt(3)), (new Random().nextInt(3))}) {
            System.out.println("Hello World");
        }
    }

    public void whileLoop() {
        int i = 0;
        while (i < (new Random().nextInt(3))) {
            System.out.println("Hello World");
            i++;
        }
    }

    public void doWhileLoop() {
        int i = 0;
        do {
            System.out.println("Hello World");
            i++;
        } while (i < (new Random().nextInt(3)));
    }

    public void forEachStream() {
        IntStream.range(0, (new Random().nextInt(3))).mapToObj((int i) -> "Hello World").forEach(System.out::println);
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
