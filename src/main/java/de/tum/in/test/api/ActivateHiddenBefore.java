package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.jupiter.HiddenTest;

/**
 * This is the counterpart to {@link Deadline}. It specifies a date before which
 * {@link HiddenTest}s will always be executed. The format of the String has to
 * be the same as the format for <code>Deadline</code>.
 * <p>
 * <b>This annotation overrides the {@link Deadline} annotation and deactivates
 * it up to the given date.</b>
 *
 * @author Christian Femers
 * @see Deadline
 * @since 0.1.2
 * @version 1.2.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface ActivateHiddenBefore {
	String value();
}
