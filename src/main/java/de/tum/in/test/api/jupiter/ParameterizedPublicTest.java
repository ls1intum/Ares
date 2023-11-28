package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.context.TestType;
import de.tum.in.test.api.io.IOTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Marks a <b>PUBLIC</b> test case, uses the PdgpSecurityManager, can declare
 * {@link IOTester} as parameter.
 * Requires a {@link ValueSource} annotation.
 *
 * @author Ivan Kuzmin
 * @version 1.1.0
 * @since 0.13.0
 */
@API(status = Status.MAINTAINED)
@Documented()
@Retention(RUNTIME)
@Target(METHOD)
@ParameterizedTest
@JupiterAresTest(TestType.PUBLIC)
public @interface ParameterizedPublicTest {
    // maker only
}
