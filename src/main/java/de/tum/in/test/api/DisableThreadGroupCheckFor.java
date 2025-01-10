package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 * Allows to disable the thread group check for threads which names start with
 * any of the given prefixes.
 *
 * @author Benjamin Schmitz
 * @since 1.14.0
 * @version 1.0.0
 */
@API(status = API.Status.EXPERIMENTAL)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface DisableThreadGroupCheckFor {
	String[] value();
}
