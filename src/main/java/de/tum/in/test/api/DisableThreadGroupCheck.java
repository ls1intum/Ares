package de.tum.in.test.api;

import org.apiguardian.api.API;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Disables the Thread Group Check.
 *
 * @author Benjamin Schmitz
 * @since 1.14.0
 * @version 1.0.0
 */
@API(status = API.Status.EXPERIMENTAL)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface DisableThreadGroupCheck {
}
