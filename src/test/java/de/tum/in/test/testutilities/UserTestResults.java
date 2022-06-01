package de.tum.in.test.testutilities;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

/**
 * The test extension will try to inject the test results of a {@link UserBased}
 * test here. Can be applied to (static) fields and parameters.
 *
 * @author Christian Femers
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, ANNOTATION_TYPE })
public @interface UserTestResults {
	// marker only
}
