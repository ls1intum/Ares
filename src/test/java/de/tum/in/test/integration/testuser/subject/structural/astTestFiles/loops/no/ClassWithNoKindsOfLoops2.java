package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.loops.no;

import java.util.stream.IntStream;

public class ClassWithNoKindsOfLoops2 {
    public void forEachStream() {
        IntStream.range(0,10).mapToObj((int i) -> "Hello World").forEach(System.out::println);
    }
}
