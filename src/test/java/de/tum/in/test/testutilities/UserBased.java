package de.tum.in.test.testutilities;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;

/**
 * Sets the user(s) for a specific test class. A user in this context is the
 * user of the test API, so the user is a different test class that is not
 * called "Test" because it is not the main test itself, but executed using the
 * JUnit platform.
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@ExtendWith(TestUserExtension.class)
@TestMethodOrder(MethodName.class)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface UserBased {

	Class<?>[] value();

	String testEngineId() default JupiterEngineDescriptor.ENGINE_ID;
}
