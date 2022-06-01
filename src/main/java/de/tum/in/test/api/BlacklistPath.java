package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.security.ArtemisSecurityManager;

/**
 * Allows to blacklist a {@link Path}, including all subpaths. The
 * {@link ArtemisSecurityManager} will disallow any fileaccess for
 * <b>non-whitelisted callers</b> outside the set of whitelisted paths. This
 * annotation is {@linkplain Repeatable}, and can be placed additively on the
 * test class and test method.Different types can be set to gain more control
 * over the mtaching paths.<br>
 * Use e.g. <code>@BlacklistPath("foo/")</code> to disallow access to all files
 * in the sub-directory <code>foo</code> of the execution directory.
 * <p>
 * <b>This annotation overpowers any {@link WhitelistPath} annotations.</b>
 *
 * @see Path
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.2.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ METHOD, TYPE, ANNOTATION_TYPE })
@Repeatable(BlacklistPaths.class)
public @interface BlacklistPath {

	String[] value();

	PathType type() default PathType.STARTS_WITH;

	PathActionLevel level() default PathActionLevel.READ;
}
