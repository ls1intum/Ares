package de.tum.in.test.api.jqwik;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.TestType;
import de.tum.in.test.api.io.IOTester;

/**
 * Marks a HIDDEN test case, can declare {@link IOTester} as parameter. This
 * annotation requires a {@link Deadline} annotation to be set either on the
 * test class or the test method. See {@link Deadline} for more information.
 * <p>
 * <b>This annotation must be accompanied by some jqwik test annotation, it will
 * not cause test execution by itself!</b>
 *
 * @see Deadline
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.0.0
 * @deprecated To avoid importing and using the wrong <code>@Hidden</code>
 *             accidentally. Use {@link HiddenJqwik} instead.
 */
@Deprecated(since = "0.5.3")
@API(status = Status.DEPRECATED)
@Documented
@Retention(RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE })
@JqwikArtemisTest(TestType.HIDDEN)
public @interface Hidden {
	// marker only
}
