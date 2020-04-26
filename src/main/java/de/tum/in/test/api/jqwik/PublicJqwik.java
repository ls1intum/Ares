package de.tum.in.test.api.jqwik;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.TestType;
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
 *
 * @author Christian Femers
 * @since 0.2.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@JqwikArtemisTest(TestType.PUBLIC)
public @interface PublicJqwik {
	// marker only
}
