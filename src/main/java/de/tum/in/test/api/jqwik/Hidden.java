package de.tum.in.test.api.jqwik;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.context.TestType;
import de.tum.in.test.api.io.IOTester;

/**
 * Marks a HIDDEN test case, can declare {@link IOTester} as parameter. This
 * annotation requires a {@link Deadline} annotation to be set either on the
 * test class or the test method. See {@link Deadline} for more information.
 * <p>
 * <b>This annotation must be accompanied by some jqwik test annotation, it will
 * not cause test execution by itself!</b>
 * <p>
 * Can be used on class level in addition to method level. This will cause all
 * members of the class (e.g. test methods) to inherit this test case type,
 * unless they declare another one.
 * <p>
 * <b>Also see {@link de.tum.in.test.api.jqwik the package Javadoc} for the
 * required Ares configuration to use jqwik properly.</b>
 *
 * @see Deadline
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.2.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE, TYPE })
@JqwikAresTest(TestType.HIDDEN)
public @interface Hidden {
	// marker only
}
