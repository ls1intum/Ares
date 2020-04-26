package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * ...
 *
 * @author Christian Femers
 * @since 0.5.2
 * @version 1.0.0
 */
@API(status = Status.EXPERIMENTAL)
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface PrivilegedExceptionsOnly {
	String value() default "";
}
