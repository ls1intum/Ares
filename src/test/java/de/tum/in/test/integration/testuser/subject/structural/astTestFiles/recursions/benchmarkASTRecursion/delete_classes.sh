#!/bin/bash

# Loop to delete 10000 Java classes
for ((i=1; i<=10000; i++))
do
    # Define the class name
    class_name="TestClass$i.java"

    # Check if the file exists before attempting to delete
    if [ -f "$class_name" ]; then
        rm "$class_name"
        echo "Deleted: $class_name"
    fi
done
