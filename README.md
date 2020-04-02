# Artemis Java Test Sandbox

Artemis Java Test Sandbox _(abbr. AJTS)_ is a JUnit 5 extension for easy and secure Java testing
on the interactive learning platform [Artemis](https://github.com/ls1intum/Artemis).

Its main features are
- a security manager to prevent students crashing the tests or cheating
- more robust tests and builds due to limits on time, threads and io
- support for public and hidden Artemis tests, where hidden ones obey a custom deadline
- utilities for improved feedback in Artemis like processing multiline error messages
  or pointing to a possible location that caused an Exception
- utilities to test exercises using System.out and System.in comfortably

## Installation

*Note: AJTS requires at least Java 11.*

AJTS is provided as Maven dependency. To use AJTS in the test environment of a Maven project, use

```xml
<dependency>
    <groupId>de.tum.in</groupId>
    <artifactId>artemis-java-test-sandbox</artifactId>
    <version>0.4.6</version>
    <scope>test</scope>
</dependency>
```

in the `dependencies` section and include the following block in your `pom.xml` to tell
Maven where to find the AJTS resources:

```xml
<repositories>
    <repository>
        <id>ajts</id>
        <name>AJTS Maven Packages</name>
        <url>https://maven.pkg.github.com/MaisiKoleni/artemis-java-test-sandbox</url>
    </repository>
</repositories>
```
You can now remove dependencies to JUnit 5, AssertJ and Hamcrest if present because AJTS already includes them.
If you want to use jqwik or JUnit 4 (JUnit 5 vintage), simply include them in the dependencies section.


## Basic Usage

*AJTS provides a high level of security which comes at the cost of usability. Severaly steps need to be taken in order to make tests work properly, and it might require some time to understand what AJTS does. Please study at least this complete basic usage guide before using AJTS in production.*

### Setup

Assume you have a Java 11 Maven project, and the inside of `pom.xml` looks like this:
```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>

<repositories>
    <repository>
        <id>ajts</id>
        <name>AJTS Maven Packages</name>
        <url>https://maven.pkg.github.com/MaisiKoleni/artemis-java-test-sandbox</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>de.tum.in</groupId>
        <artifactId>artemis-java-test-sandbox</artifactId>
        <version>0.4.6</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
Consider the following student class that needs to be tested:
```Java
import java.util.Objects;

public final class Penguin {

    private final String name;

    public Penguin(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public String getName() {
        return name;
    }
}
```
And you have already written the following simple JUnit 5 test class:
```Java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PenguinTest {

    @Test
    void testPenguinPublic() {
        Penguin pingu = new Penguin("Julian");
        assertEquals("Julian", pingu.getName(), "getName() does not return the name supplied to the contructor");
    }

    @Test
    void testPenguinHidden() {
        assertThrows(NullPointerException.class, () -> new Penguin(null));
    }
}
```
In this example,
- `testPenguinPublic()` is supposed to be executed after each push and directly give the students their feedback, while
- `testPenguinHidden()` should be executed only after the exercise deadline, and the results should not be visible before the deadline.

While Artemis has a feature to mark test cases as hidden, this will not prevent the contents of the test case leaking through static variables, files and similar, be it accidentally or on purpose.
To prevent that, **the hidden test case must not be executed before the deadline at all.**

The public test case does not need to be hidden, as it's purpose is to give direct feedback. However, there are still multiple possible problems like crashing the Maven build by `System.exit(0)` or containing an endless loop. Both can have a negative impact on the interactive learning experience because the students get confronted with an incomprehensible log of a failed build. Such errors can be explained, but that takes a lot of time, especially if it happens a lot (and it will, if the number of students is sufficiently large).

It is also a security problem again, students could try to read the `.java` files containing the test classes.

### Integrating AJTS

Therefore, we will use AJTS to secure the tests and avoid unintelligible feedback. The most basic way to do this is by using the `@Public` and `@Hidden` annotations:

```Java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// IMPORTANT: make sure to use the "jupiter" ones (if you are not using jqwik)
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;

// This example won't work just like that, see below why
public class PenguinTest {

    @Public
    @Test
    void testPenguinPublic() {
        Penguin pingu = new Penguin("Julian");
        assertEquals("Julian", pingu.getName(), "getName() does not return the name supplied to the contructor");
    }

    @Hidden
    @Test
    void testPenguinHidden() {
        assertThrows(NullPointerException.class, () -> new Penguin(null));
    }
}
```
The code above won't work just like that, if you try to run it as is, you will get the following reported by JUnit:<br>
`java.lang.annotation.AnnotationFormatError: cannot find a deadline for hidden test testPenguinHidden()`

AJTS needs to know what the deadline is. We tell AJTS with another annotation:
```Java
// Format must be DateTimeFormatter.ISO_LOCAL_DATE_TIME (using a space between date and time is permitted, too)
@Deadline("2020-06-09 03:14")
public class PenguinTest {
    // ...
}
```
That annotation (like most of the AJTS annotations) can also be placed on the test method (and nested classes), if multiple are present, the one that is closest to the test case is used.

Now, it already works! Try to play around with the deadline in the annotation. If the given `LocalDateTime` lies in the past, the test case is executed and - together with the student code presented earlier - passes. If the deadline hasn't passed, the test case won't pass either. It fails with
`org.opentest4j.AssertionFailedError: hidden tests will be executed after the deadline.` and the test was not executed, as the deadline is always checked before any hidden test case is executed.

### What about security?

The hidden test case was not executed and static variables cannot leak its contents.
If you change `getName()` to
```Java
public String getName() {
    System.exit(0);
    return name;
}
```
You will now with AJTS get the following error message:<br>
`java.lang.SecurityException: do not use System.exit(int)  /// potential problem location: Penguin.getName(Penguin.java:12) ///`

As you might be able to see, AJTS threw a SecurityException. But it also added `/// potential problem location: Penguin.getName(Penguin.java:12) ///`. This is the line from the stack trace which AJTS thinks is most relevant for the student, essentially, it searches for the uppermost stack frame that is located in the students code. Student code is basically everything that is not whitelisted.

But what is whitelisted?
- The test class itself (in case of nested classes, the outermost class is whitelisted) and therefore, *all* its nested classes and methods, too.
- A predefined set of packages, like everything that starts with `java.`, `sun.`, `com.sun.`, `org.junit`, `org.apache.`, ...<br>
  Therefore, **never use such package names for student assignments!**
- Classes whitelisted using `@WhitelistClass`

AJTS also grants permissions that are requested by certain actions (`System.exit`, File IO, Networking, Threads, ...) based on whitelisted stack frames. **AJTS granting a permission requires all stack frames to be whitelisted.**

Another test:<br>
Adding one of the following lines to `testPenguinPublic()` itself and it will still pass using the correct student code:
```Java
Files.readString(Path.of("pom.xml"));
// or
Files.readString(Path.of("src/test/java/PenguinTest.java")); // assuming default maven structure
```
If you instead add one of the lines to the `getName()` method again, you will get something like: `java.lang.SecurityException: access to path src\test\java\PenguinTest.java denied in line 16 in Penguin.java`. Which is exactly what you want, students should not be able to read the code of the test classes. By default, student code has no access to any path, not even read access.

By the way, adding `@WhitelistClass(Penguin.class)` to the test class or method will make the test run fine again because `Penguin` is now whitelisted and can therefore access all files without problems. **So never whitelist classes that students can edit.**

### Further Important Options

Are we done now? With the most fundamental parts yes, but there is a bit more you need to know about testing with AJTS, as this was just a very basic example with a single class and not much testing. Without further knowledge, you might not get AJTS to work and consequently get rather annoyed or even enraged. To prevent that, please read on.

// TODO
- `@StrictTimeout`
- `@BlacklistPath`, `@WhitelistPath`
- `@MirrorOutput`
- `@ActivateHiddenBefore`
- `@AllowThreads`
- `@ExtendedDeadline`
- `@AllowLocalPort`
- `IOTester`

## License

AJTS was created by Christian Femers and is licensed under the [MIT License, see `LICENSE.md`](https://github.com/MaisiKoleni/artemis-java-test-sandbox/blob/master/LICENSE).
