package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Marks a class trusted for the test scope the annotation is applied to. The
 * class has therefore more freedom and is less restricted by the
 * SecurityManager.
 *
 * @author Christian Femers
 * @since 0.4.4
 * @version 1.1.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@Repeatable(WhitelistClasses.class)
public @interface WhitelistClass {
	Class<?>[] value();
}
