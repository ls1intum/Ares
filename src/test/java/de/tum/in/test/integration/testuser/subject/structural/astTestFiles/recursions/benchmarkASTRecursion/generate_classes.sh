#!/bin/bash

# Loop to create 10000 Java classes
for ((i=1; i<=10000; i++))
do
    # Define the class name
    class_name="TestClass$i"

    # Create the Java file with the class definition
    cat > "$class_name.java" <<EOF
public class $class_name {
    public void simpleMethod(int n, double n2) {
        // Simple method implementation
        System.out.println("This is a simple method.");
    }
}
EOF
done
