package de.tum.in.test.integration.testuser.subject.structural.astTestFiles.recursions.overloaded.yes;

public class FactorialCalculator {

    // Overloaded method for factorial calculation with integer input
    public int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        } else {
            // Call the double factorial method with the same argument
            return (int) factorial((double) n);
        }
    }

    // Overloaded method for factorial calculation with double input
    public double factorial(double n) {
        if (n == 0 || n == 1) {
            return 1;
        } else {
            // Call the integer factorial method with the truncated value of n
            return n * factorial((int) n - 1);
        }
    }
}

