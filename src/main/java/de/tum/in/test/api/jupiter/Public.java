package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.TestType;
import de.tum.in.test.api.io.IOTester;

/**
 * Marks a <b>PUBLIC</b> test case, uses the PdgpSecurityManager, can declare
 * {@link IOTester} as parameter.
 * <p>
 * <b>This annotation must be accompanied by some JUnit 5 test annotation, it
 * will not cause test execution by itself!</b> This makes it usable with
 * different JUnit 5 runners.
 *
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@JupiterArtemisTest(TestType.PUBLIC)
public @interface Public {
	// marker only
}
