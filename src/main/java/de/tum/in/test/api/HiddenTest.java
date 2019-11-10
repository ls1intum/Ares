package de.tum.in.test.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.io.IOTester;

/**
 * Marks a HIDDEN test case, can declare {@link IOTester} as parameter. This
 * annotation requires a {@link Deadline} annotation to be set either on the
 * test class or the test method. See {@link Deadline} for more information.
 * 
 * @see Deadline
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented()
@Retention(RUNTIME)
@Target(METHOD)
@GeneralArtemisTest
public @interface HiddenTest {
	// maker only
}
