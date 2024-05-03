package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.overridden;

public class FactorialCalculator {

    // Method for calculating factorial (recursive)
    public int calculateFactorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        } else {
            return n * calculateFactorial(n - 1);
        }
    }

    public static void main(String[] args) {
        FactorialCalculator calculator = new FactorialCalculator();
        int result = calculator.calculateFactorial(5);
        System.out.println("Factorial of 5: " + result);
    }
}

