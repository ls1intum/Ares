package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Container annotation for {@linkplain Repeatable} {@link BlacklistPath}.
 *
 * @see BlacklistPath
 * @see Path
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.0.0
 */
@API(status = Status.EXPERIMENTAL)
@Documented
@Retention(RUNTIME)
@Target({ METHOD, TYPE, ANNOTATION_TYPE })
public @interface BlacklistPaths {
	BlacklistPath[] value();
}
