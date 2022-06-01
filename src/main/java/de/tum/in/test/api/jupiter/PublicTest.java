package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.Test;

import de.tum.in.test.api.context.TestType;
import de.tum.in.test.api.io.IOTester;

/**
 * Marks a <b>PUBLIC</b> test case, uses the PdgpSecurityManager, can declare
 * {@link IOTester} as parameter.
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.1.0
 */
@API(status = Status.MAINTAINED)
@Documented()
@Retention(RUNTIME)
@Target(METHOD)
@Test
@JupiterAresTest(TestType.PUBLIC)
public @interface PublicTest {
	// maker only
}
