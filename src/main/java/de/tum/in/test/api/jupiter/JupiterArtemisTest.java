package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

import de.tum.in.test.api.internal.TestType;

/**
 * This is only for internal use, to reduce redundancy.
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@ExtendWith(JupiterTestExtension.class)
@ExtendWith(JupiterTestGuard.class)
public @interface JupiterArtemisTest {

	TestType value();
}
