package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import de.tum.in.test.api.locked.StrictTimeoutExtension;

/**
 * Works like
 * {@link Assertions#assertTimeoutPreemptively(java.time.Duration, org.junit.jupiter.api.function.Executable)
 * Assertions.assertTimeoutPreemptively} and {@link Assertions}, section
 * preemptive timeout. Very different in behavior to {@link Timeout}. This
 * annotation does no cause problems with the ArtemisSecurityManager and can
 * terminate tests stuck in endless loops (which {@link Timeout} can not).
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@ExtendWith(StrictTimeoutExtension.class)
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