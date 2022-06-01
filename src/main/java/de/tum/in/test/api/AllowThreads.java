package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Allows starting and modifying own Threads.
 *
 * @author Christian Femers
 * @since 0.4.0
 * @version 1.0.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface AllowThreads {
	/**
	 * The maximum number of own Threads that are allowed to be active at the same
	 * time.
	 * <p>
	 * Can be used to prevent crashing the test process
	 * <p>
	 * The default value is <code>1000</code>.
	 */
	int maxActiveCount() default 1000;
}
