package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.*;

import de.tum.in.test.api.jqwik.JqwikStrictTimeoutExtension;
import de.tum.in.test.api.jupiter.*;

/**
 * Works like
 * {@link Assertions#assertTimeoutPreemptively(java.time.Duration, org.junit.jupiter.api.function.Executable)
 * Assertions.assertTimeoutPreemptively} and {@link Assertions}, section
 * preemptive timeout. Very different in behavior to {@link Timeout}. This
 * annotation does no cause problems with the ArtemisSecurityManager and can
 * terminate tests stuck in endless loops (which {@link Timeout} can not).
 * <p>
 * To use {@link StrictTimeout} <b>without</b> any {@link Public} or
 * {@link Hidden}, you need to declare the {@link JupiterStrictTimeoutExtension}
 * for JUnit 5 Jupiter or {@link JqwikStrictTimeoutExtension} for jqwik
 * explicitly. However, this is not recommended as it is less effective.
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 2.1.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface StrictTimeout {
	/**
	 * The duration of this timeout. <i>(per default in seconds)</i>
	 */
	long value();

	/**
	 * The time unit of this timeout, <b>defaults to seconds</b>.
	 *
	 * @see TimeUnit
	 */
	TimeUnit unit() default TimeUnit.SECONDS;
}
