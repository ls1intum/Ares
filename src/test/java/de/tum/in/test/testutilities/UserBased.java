package de.tum.in.test.testutilities;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Sets the user for a specific test class. A user in this context is the user
 * of the test API, so the user is a different test class that is not called
 * "Test" because it is not the main test itself, but executed using the JUnit
 * platform.
 * 
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@ExtendWith(TestUserExtension.class)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface UserBased {
	Class<?> value();
}
