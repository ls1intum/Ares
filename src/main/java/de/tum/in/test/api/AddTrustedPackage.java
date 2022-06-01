package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Marks a package structure as trusted for the test scope the annotation is
 * applied to. The classes in the packages have therefore more freedom and are
 * less restricted by the SecurityManager.This annotation is
 * {@linkplain Repeatable}, and can be placed additively on the test class and
 * test method.
 * <p>
 * You can use <code>*</code> and <code>**</code> in the same way as in GLOB
 * patterns just applied to packages where the delimiter is <code>"."</code>.
 * <p>
 * Use e.g. <code>@AddTrustedPackages("org.powermock**")</code> to whitelist all
 * classes in any <code>org.powermock</code> package.
 *
 * @author Christian Femers
 * @since 1.3.1
 * @version 1.1.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@Repeatable(AddTrustedPackages.class)
public @interface AddTrustedPackage {
	String[] value();
}
