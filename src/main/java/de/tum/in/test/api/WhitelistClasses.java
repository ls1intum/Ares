package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Container annotation for {@linkplain Repeatable} {@link WhitelistClass}.
 *
 * @see WhitelistClass
 * @author Christian Femers
 * @since 0.4.4
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface WhitelistClasses {
	WhitelistClass[] value();
}
