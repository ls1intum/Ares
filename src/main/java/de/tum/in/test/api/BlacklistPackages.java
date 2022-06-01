package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Container annotation for {@linkplain Repeatable} {@link BlacklistPackage}.
 *
 * @see BlacklistPackage
 * @author Christian Femers
 * @since 0.5.1
 * @version 1.1.0
 */
@API(status = Status.MAINTAINED)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ METHOD, TYPE, ANNOTATION_TYPE })
public @interface BlacklistPackages {
	BlacklistPackage[] value();
}
