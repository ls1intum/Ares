package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.overridden;

public class BetterFactorialCalculator extends FactorialCalculator {

    // Override the calculateFactorial method for better efficiency
    @Override
    public int calculateFactorial(int n) {
        return calculateFactorialHelper(n, 1);
    }

    // Helper method with tail recursion for better efficiency
    private int calculateFactorialHelper(int n, int resultSoFar) {
        if (n == 0 || n == 1) {
            return resultSoFar;
        } else {
            return calculateFactorialHelper(n - 1, n * resultSoFar);
        }
    }

    public static void main(String[] args) {
        BetterFactorialCalculator calculator = new BetterFactorialCalculator();
        int result = calculator.calculateFactorial(5);
        System.out.println("Factorial of 5 (better implementation): " + result);
    }
}
