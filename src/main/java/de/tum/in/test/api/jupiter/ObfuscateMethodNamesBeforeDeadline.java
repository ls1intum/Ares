package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.DisplayNameGeneration;

import de.tum.in.test.api.Deadline;

/**
 * Obfuscates the name of {@link HiddenTest}s before the {@link Deadline}, using
 * some basic hash code.
 * <p>
 * <b>This has not been tested in conjunction with Artemis!</b>
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@API(status = Status.EXPERIMENTAL)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@DisplayNameGeneration(JupiterTestGuard.class)
public @interface ObfuscateMethodNamesBeforeDeadline {
	// marker only
}
