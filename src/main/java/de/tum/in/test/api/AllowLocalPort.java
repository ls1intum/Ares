package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Allows networking with <code>localhost</code> at the specified port.
 *
 * @author Christian Femers
 * @since 0.3.6
 * @version 1.0.0
 */
@API(status = Status.EXPERIMENTAL)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface AllowLocalPort {
	int value();
}
