# Ares
### *The Artemis Java Test Sandbox*
![Java CI](https://github.com/ls1intum/Ares/workflows/Java%20CI/badge.svg?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/de.tum.in.ase/artemis-java-test-sandbox)](https://maven-badges.herokuapp.com/maven-central/de.tum.in.ase/artemis-java-test-sandbox)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/de.tum.in.ase/artemis-java-test-sandbox?label=latest%20snapshot&server=https%3A%2F%2Foss.sonatype.org)

Ares, also known as Artemis Java Test Sandbox *(abbr. AJTS)*, is a JUnit 5 extension for easy and secure Java testing
on the interactive learning platform [Artemis](https://github.com/ls1intum/Artemis).

Its main features are
- a security manager to prevent students crashing the tests or cheating
- more robust tests and builds due to limits on time, threads and io
- support for public and hidden Artemis tests, where hidden ones obey a custom deadline
- utilities for improved feedback in Artemis like processing multiline error messages
  or pointing to a possible location that caused an Exception
- utilities to test exercises using System.out and System.in comfortably

#### Project Status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=alert_status)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=security_rating)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=ncloc)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=coverage)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=sqale_index)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=bugs)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=artemis-java-test-sandbox&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=artemis-java-test-sandbox)

## Installation

*Note: Ares requires at least Java 11.*

Ares is provided as Maven dependency. To use Ares in the test environment of a Maven project, include

```xml
<dependency>
    <groupId>de.tum.in.ase</groupId>
    <artifactId>artemis-java-test-sandbox</artifactId>
    <version>1.7.3</version>
    <scope>test</scope>
</dependency>
```

in the `dependencies` section.

You can now remove dependencies to JUnit 5, AssertJ and Hamcrest if present because Ares already includes them.
If you want to use jqwik (>= 1.2.4) or JUnit 4 (JUnit 5 vintage), simply include them in the dependencies section.


## Basic Usage

*Ares provides a high level of security which comes at the cost of usability.
Several steps need to be taken in order to make tests work properly, and it might require some time to understand what Ares does.
Please study at least this complete basic usage guide before using Ares in production.*

#### Table of Contents
- [Setup](#setup)
- [Integrating Ares](#integrating-ares)
- [What about Security?](#what-about-security)
- [Further Important Options](#further-important-options)
   - [Path Access and Class Loading](#path-access-and-class-loading)
   - [Timeouts](#timeouts)
   - [Showing Standard Output](#showing-standard-output)
   - [Testing the Exercise before Release](#testing-the-exercise-before-release)
   - [Extending a Deadline and Disability Compensation](#extending-a-deadline-and-disability-compensation)
   - [Threads and Concurrency](#threads-and-concurrency)
   - [Testing Console Interaction](#testing-console-interaction)
   - [Networking](#networking)


### Setup

Assume you have a Java 11 Maven project, and the inside of `pom.xml` looks like this:
```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>

<dependencies>
    <dependency>
        <groupId>de.tum.in.ase</groupId>
        <artifactId>artemis-java-test-sandbox</artifactId>
        <version>1.7.3</version>
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

The public test case does not need to be hidden, as its purpose is to give direct feedback. However, there are still multiple possible problems like crashing the Maven build by `System.exit(0)` or containing an endless loop.
Both can have a negative impact on the interactive learning experience because the students get confronted with an incomprehensible log of a failed build.
Such errors can be explained, but that takes a lot of time, especially if it happens a lot (and it will, if the number of students is sufficiently large).

It is also a security problem again, students could try to read the `.java` files containing the test classes.

### Integrating Ares

Therefore, we will use Ares to secure the tests and avoid unintelligible feedback. The most basic way to do this is by using the `@Public` and `@Hidden` annotations:

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

Ares needs to know what the deadline is. We tell Ares with another annotation:
```Java
// Format must be ISO_LOCAL_DATE(T| )ISO_LOCAL_TIME( ZONE_ID)?
@Deadline("2020-06-09 03:14 Europe/Berlin")
public class PenguinTest {
    // ...
}
```
That annotation (like most of the Ares annotations) can also be placed on the test method (and nested classes), if multiple are present, the one that is closest to the test case is used.

Now, it already works! Try to play around with the deadline in the annotation. If the given `LocalDateTime` lies in the past, the test case is executed and - together with the student code presented earlier - passes.
If the deadline hasn't passed, the test case won't pass either. It fails with
`org.opentest4j.AssertionFailedError: hidden tests will be executed after the deadline.` and the test was not executed, as the deadline is always checked before any hidden test case is executed.

You might have noticed that we specify the time zone as well. Although the annotation parser permits leaving it unspecified, this bears the risk of (not) executing the tests at the correct time if the build agents time zone is different from the one on your machine or what you would expect it to be. If you run tests where the time zone is/was not set, Ares will warn your about that in the logs.

### What about Security?

The hidden test case was not executed and static variables cannot leak its contents.
If you change `getName()` to
```Java
public String getName() {
    System.exit(0);
    return name;
}
```
You will now with Ares get the following error message:

```
java.lang.SecurityException: do not use System.exit(int)
/// potential problem location: Penguin.getName(Penguin.java:12) ///
```

As you might be able to see, Ares threw a SecurityException. But it also added `/// potential problem location: Penguin.getName(Penguin.java:12) ///`.
This is the line from the stack trace which Ares thinks is most relevant for the student, essentially, it searches for the uppermost stack frame that is located in the students code.
Student code is basically everything that is not whitelisted.

But what is whitelisted?
- The test class itself (in case of nested classes, the outermost class is whitelisted) and therefore, *all* its nested classes and methods, too.
- A predefined set of packages, like everything that starts with `java.`, `sun.`, `com.sun.`, `org.junit`, `org.apache.`, ...<br>
  Therefore, **never use such package names for student assignments!**
- Single classes whitelisted using `@WhitelistClass` and all classes matching `@AddTrustedPackage`

Ares also grants permissions that are requested by certain actions (`System.exit`, File IO, Networking, Threads, ...) based on whitelisted stack frames. **Ares granting a permission requires all stack frames to be whitelisted.**

Another test:<br>
Adding one of the following lines to `testPenguinPublic()` itself and it will still pass using the correct student code:
```Java
Files.readString(Path.of("pom.xml"));
// or
Files.readString(Path.of("src/test/java/PenguinTest.java")); // assuming default maven structure
```
If you instead add one of the lines to the `getName()` method again, you will get something like: `java.lang.SecurityException: access to path src\test\java\PenguinTest.java denied in line 16 in Penguin.java`.
Which is exactly what you want, students should not be able to read the code of the test classes.
By default, student code has no access to any path, not even read access.

By the way, adding `@WhitelistClass(Penguin.class)` to the test class or method will make the test run fine again because `Penguin` is now whitelisted and can therefore access all files without problems.
**So never whitelist classes that students can edit.**

### Further Important Options

Are we done now? With the most fundamental parts yes, but there is a bit more you need to know about testing with Ares, as this was just a very basic example with a single class and not much testing.
Without further knowledge, you might not get Ares to work and consequently get rather annoyed or even enraged. To prevent that, please read on.

#### Path Access and Class Loading
You can use `@WhitelistPath` and `@BlacklistPath` to control access to paths. By default, no access is granted, and so you need to use `@WhitelistPath` to give student code the permission to read and write files explicitly.
You can specify exceptions using `@BlacklistPath` which will overpower the whitelisted paths.

*The following examples will make use of `course1920xyz` as placeholder value for the real Artemis exercise name/id. Replace it with the real one when borrowing code snippets or nothing will work as expected.*

Most importantly, this does not only apply to explicit file IO, but also to the `.class` files that the class loader reads, as needed. This already happens if one student class requires another one, that has not been loaded after that.
You can recognize that in the standard error output:
```
[WARN] [main] BAD PATH ACCESS: K:\repo\course1920xyz-solution\bin\some\Thing.class (BL:false, WL:false)
```
This usually means the class loader could not load the class. The parentheses show, that the problem is the missing whitelisting. **Therefore, all test setups should have some whitelisting.**

A number of examples how you can whitelist paths in Ares:
- `@WhitelistPath("")` will grant read access to the paths in the directory of execution, which is usually where the `pom.xml` is.
- `@WhitelistPath("pom.xml")` will allow students to read the `pom.xml`.
- `@WhitelistPath("..")` will allow read access to the level above the maven project. In Eclipse, that is the level of your workspace.
- `@WhitelistPath(value = "../course1920xyz**", type = PathType.GLOB)` grants read access to projects beginning with the exercise "id" used by Artemis.
  Should you use the Eclipse feature "Referenced Projects" (or the analog to that in your IDE) to link the student/solution project to the tests, you will need a setting like this.
- `@WhitelistPath(value = "data", level = PathActionLevel.DELETE)` will allow students to read, write and delete files in the `data` directory and subdirectories.
- `@WhitelistPath("target")` allows reading files in target (Maven output folder)
- `@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)` prevents access to classes in source code or compiled form that contain `Test`. If you leave away the `*` after `Test`, nested classes are not blacklisted.
  Student classes should not be called something with "Test" then.

That was not everything but already quite a lot. Take a look at the Javadoc of the annotations and enums used, if you want to know more. Before you give up, here is my recommendation how to start:
```Java
@WhitelistPath(value = "../course1920xyz**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
```
Add a `@BlacklistPath` for other important classes, like your reference implementations of the solution to test against should you use that approach.

*Note: the Artemis project starts with `course1920xyz`, but the build in Bamboo (by Artemis) will
happen in a directory named after the build plan, which is in upper case and therefore,
begins with `COURSE1920XYZ`. Make sure that you do not build multiple student solutions
in the same directory on the same machine using the git clone (lower case) approach. Otherwise, adjust the whitelisting to your needs.*

#### Timeouts

JUnit already provides means of applying timeouts to tests. However, those are *not strict* in the sense of "enforced in the strongest possible way". What is meant by that?

There are three different ways how the timeouts can work:
- like `org.junit.jupiter.api.Timeout`<br>
  This timeout is not preemptive and the test itself runs in the same thread executing the tests. It will only try to stop the test via an interrupt.
  If that fails like it does for an endless loop, the test will definitively fail. After it is finished. Which might never happen and the main reason not to use this when it comes to testing unknown code.
- like `org.junit.jupiter.api.Assertions.assertTimeoutPreemptively`<br>
  This will fail the test preemptively by executing the `Executable` argument itself in a different thread than the thread executing all tests. It will only try to stop the test via an interrupt, but if that fails it will simply carry on. The test thread might still run though.
- like `de.tum.in.test.api.StrictTimeout`<br>
  This uses a mechanism similar to `assertTimeoutPreemptively`, but will resort to harder means if necessary.
  It will in the following order:
   1. wait the given duration
   2. interrupt the thread executing the test and wait no longer (like assertTimeoutPreemptively)
   3. block the creation of new threads
   4. interrupt all threads created during the test and try to join the threads
   5. if that fails, use `Thread.stop()` on all remaining threads and try to join again
   6. repeat step 5 multiple times, if required
   7. Should that fail, report a special SecurityException that not all threads could be stopped.
      (see the standard error output for a detailed report then) *If that happens, no more tests can be properly executed because the security cannot be guaranteed and the test cases cannot be executed "in isolation". All following tests will fail.*

**Rule 1: When testing with Ares, always use `@StrictTimeout` for timeouts, the others will not work reliably especially in conjunction with the Ares security.**

**Rule 2: When writing tests for Artemis, always use `@StrictTimeout`.** There is no reason to omit the timeout, since you do not know the code students will write. (And they will write code spawning millions of threads in endless loops which in turn will do the same recursively.)

#### Showing Standard Output

By default, Ares will record standard and error output of each test internally and not print it to the console. The recorded output can then be obtained and tested, see [`IOTester`](#testing-console-interaction)
The reason for this is on the one hand to keep the console and logs short and clean and on the other hand prevent students from accidentally messing up the logs with millions of lines.
Ares also has a hard limit on the total number of printed chars around 10 million.

To mirror the output recorded by Ares to the console, use the `@MirrorOutput` annotation on the test class or method.

#### Testing the Exercise before Release

Hidden tests will be executed by Ares only after the deadline. This poses the problem, how the exercise creators should work on the tasks, tests and the sample solution. One possible solution would be to use an alternative deadline annotation or change the deadline temporarily.
The problem is that it is quite likely one might forget to change it back again, and protecting the hidden tests would fail.

Use `@ActivateHiddenBefore` just like `@Deadline` to state the LocalDateTime before which hidden tests should be executed. This Date should of course be before the release of the exercise on Artemis.

#### Extending a Deadline and Disability Compensation

You can use `@ExtendedDeadline` together with a duration like `1d` or `2d 12h 30m` to extend the deadline by the given amount. `@ExtendedDeadline("1d")` for example extends the deadline by one day.
If you use the annotation on different levels (e.g. class and method) without stating a new deadline (e.g. deadline only on class level), the extensions will be added together.

#### Threads and Concurrency

By default, Ares will not allow non-whitelisted code to use Threads at all. That includes thread pools, but excludes the common pool and its users, like parallel streams. To allow the use of Threads, use the annotation `@AllowThreads`.
The number of active threads is also limited, the default value of that is 1000, but can be changed in the annotation. Please keep in mind that this limit should not be larger than 1000 to prevent performance and timeout chaos.

New threads are for security reasons not directly whitelisted by Ares and will not be allowed to do anything security critical.
If you trust a thread (at least its entry point), you can explicitly request the thread to be whitelisted using `ArtemisSecurityManager.requestThreadWhitelisting(Thread)`.
The thread calling the method and its stack must be whitelisted, of course.

#### Testing Console Interaction

In progress, see also `IOTester`.

#### Networking

In progress, see also `@AllowLocalPort`.

## Additional Notes

#### Older Versions

For versions prior to `1.0.0`, a repository block had to be added to `<repositories>` section of the `pom.xml`
that referenced the Maven repository URL `https://gitlab.com/ajts-mvn/repo/raw/master/`.

**Using older Ares versions is highly discouraged, remove these repository declarations and update to the newest Ares version
if they appear in your projects.**

#### GitHub Packages

GitHub Packages does currently not allow unregistered, public access to the
packages. Therefore, you will need to authenticate to GitHub if you use
```xml
<repositories>
    <repository>
        <id>ares</id>
        <name>Ares Maven Packages</name>
        <url>https://maven.pkg.github.com/ls1intum/Ares</url>
    </repository>
</repositories>
```

## License

Ares was created by Christian Femers and is licensed under the [MIT License, see `LICENSE.md`](https://github.com/ls1intum/Ares/blob/master/LICENSE).
