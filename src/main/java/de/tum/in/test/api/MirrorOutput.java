package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.io.IOTester;

/**
 * This annotation can be applied to a class or method and tells the
 * {@link IOTester}, whether to pipe the output to the original standard output
 * as well (mirroring everything received). It does not affect the test result
 * (unless the standard output throws an exception).
 * <p>
 * A {@link MirrorOutput} annotation on a method always overrides the one on the
 * class level.
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface MirrorOutput {

	MirrorOutputPolicy value() default MirrorOutputPolicy.ENABLED;

	enum MirrorOutputPolicy {
		DISABLED,
		ENABLED;

		public boolean isEnabled() {
			return this == ENABLED;
		}
	}
}
