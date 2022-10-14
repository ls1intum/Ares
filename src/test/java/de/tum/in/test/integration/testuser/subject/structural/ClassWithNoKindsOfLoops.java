package de.tum.in.test.integration.testuser.subject.structural;

import java.util.stream.IntStream;

public class ClassWithNoKindsOfLoops {
    public void forEachStream() {
        IntStream.range(0,10).mapToObj((int i) -> "Hello World").forEach(System.out::println);
    }
}
