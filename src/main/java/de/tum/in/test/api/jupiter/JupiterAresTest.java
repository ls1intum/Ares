package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

import de.tum.in.test.api.context.TestType;

/**
 * This is only for internal use, to reduce redundancy.
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@Inherited
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@ExtendWith(JupiterIOExtension.class)
@ExtendWith(JupiterTestGuard.class)
@ExtendWith(JupiterSecurityExtension.class)
@ExtendWith(JupiterStrictTimeoutExtension.class)
public @interface JupiterAresTest {

	TestType value();
}
