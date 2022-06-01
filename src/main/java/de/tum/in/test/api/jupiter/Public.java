package de.tum.in.test.api.jupiter;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.*;

import de.tum.in.test.api.context.TestType;
import de.tum.in.test.api.io.IOTester;

/**
 * Marks a <b>PUBLIC</b> test case, uses the PdgpSecurityManager, can declare
 * {@link IOTester} as parameter.
 * <p>
 * <b>This annotation must be accompanied by some JUnit 5 test annotation, it
 * will not cause test execution by itself!</b> This makes it usable with
 * different JUnit 5 runners.
 * <p>
 * Can be used on class level in addition to method level to affect test class
 * initialization, {@link BeforeAll}, {@link AfterAll}, {@link BeforeEach},
 * {@link AfterEach}.This will also cause all members of the class (e.g. test
 * methods) to inherit this test case type, unless they declare another one.
 *
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.2.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE, TYPE })
@JupiterAresTest(TestType.PUBLIC)
public @interface Public {
	// marker only
}
