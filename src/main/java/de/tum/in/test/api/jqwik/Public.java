package de.tum.in.test.api.jqwik;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.TestType;
import de.tum.in.test.api.io.IOTester;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

/**
 * Marks a <b>PUBLIC</b> {@link Property}/{@link Example}, uses the
 * PdgpSecurityManager, can declare {@link IOTester} as parameter using the
 * annotation {@link ForAll}.
 * <p>
 * <b>This annotation must be accompanied by some jqwik test annotation, it will
 * not cause test execution by itself!</b>
 * <p>
 * Can be used on class level in addition to method level. This will cause all
 * members of the class (e.g. test methods) to inherit this test case type,
 * unless they declare another one.
 *
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.1.0
 */
@API(status = Status.MAINTAINED)
@Documented
@Retention(RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE, TYPE })
@JqwikArtemisTest(TestType.PUBLIC)
public @interface Public {
	// marker only
}
