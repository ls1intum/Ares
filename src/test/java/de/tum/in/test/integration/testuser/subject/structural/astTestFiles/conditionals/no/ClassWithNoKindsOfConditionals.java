package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.conditionals.no;

import java.util.Random;
import java.util.stream.IntStream;

public class ClassWithNoKindsOfConditionals {
    public void tenaryOperator() {
        int x = new Random().nextInt(3);
        System.out.println(x == 1 ? "Hello" : (x == 0 ? "World" : "!"));
    }
}
