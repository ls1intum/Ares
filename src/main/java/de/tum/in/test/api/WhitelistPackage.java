package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.security.ArtemisSecurityManager;

/**
 * Allows to whitelist a package, possibly including all subpackages. The
 * {@link ArtemisSecurityManager} will allow access for <b>non-whitelisted
 * callers</b> outside the set of whitelisted packages. This annotation is
 * {@linkplain Repeatable}, and can be placed additively on the test class and
 * test method.
 * <p>
 * You can use <code>*</code> and <code>**</code> in the same way as in GLOB
 * patterns just applied to packages where the delimiter is <code>"."</code>.
 * <p>
 * Use e.g. <code>@BlacklistPackage("java.util")</code> to allow access to all
 * classes in the package <code>java.util</code> and
 * <code>@BlacklistPackage("java.util**")</code> to allow access to classes in
 * <code>java.util</code> and all its subpackages.
 * <p>
 * The access to <code>java.lang</code> is always allowed and by default, all
 * packages can be used, apart from java.lang.reflect, and internal packages of
 * AJTS.
 * <p>
 * <b>This annotation overpowers any {@link BlacklistPackage} annotations.</b>
 *
 * @author Christian Femers
 * @since 0.5.1
 * @version 1.1.0
 */
@API(status = Status.EXPERIMENTAL)
@Documented
@Retention(RUNTIME)
@Target({ METHOD, TYPE, ANNOTATION_TYPE })
@Repeatable(WhitelistPackages.class)
public @interface WhitelistPackage {

	String value();
}
