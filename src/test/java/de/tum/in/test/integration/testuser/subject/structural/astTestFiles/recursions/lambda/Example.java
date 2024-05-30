package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.lambda;

public class Example {


    public void method() {
        Runnable r = () -> {
            method2();
        };
        r.run();
    }

    public void method2() {
        Runnable r = () -> {
            method();
        };
        r.run();
    }


}
