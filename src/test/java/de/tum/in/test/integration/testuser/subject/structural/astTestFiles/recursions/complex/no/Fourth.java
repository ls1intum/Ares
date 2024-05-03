package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.complex.no;

public class Fourth {

    public void callFifth() {
        Fifth fifth = new Fifth();
        fifth.callSecond();
    }

    public void callThird() {
        Third third = new Third();
        third.callFifth();
    }

    public void callSecond() {
        Second second = new Second();
        second.callThird();
    }
}
