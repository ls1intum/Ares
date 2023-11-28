package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.context.TestType;
import de.tum.in.test.api.io.IOTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Marks a HIDDEN test case, can declare {@link IOTester} as parameter. This
 * annotation requires a {@link Deadline} annotation to be set either on the
 * test class or the test method. See {@link Deadline} for more information.
 * Requires a {@link ValueSource} annotation.
 *
 * @author Ivan Kuzmin
 * @version 1.1.0
 * @see Deadline
 * @since 0.13.0
 */
@API(status = Status.MAINTAINED)
@Documented()
@Retention(RUNTIME)
@Target(METHOD)
@ParameterizedTest
@JupiterAresTest(TestType.HIDDEN)
public @interface ParameterizedHiddenTest {
    // maker only
}
