package de.tum.in.test.api.jqwik;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.internal.TestType;
import net.jqwik.api.lifecycle.AddLifecycleHook;

/**
 * This is only for internal use, to reduce redundancy.
 * <p>
 * <i>Adaption for jqwick.</i>
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@AddLifecycleHook(JqwikTestGuard.class)
@AddLifecycleHook(JqwikIOExtension.class)
@AddLifecycleHook(JqwikSecurityExtension.class)
@AddLifecycleHook(JqwikStrictTimeoutExtension.class)
public @interface JqwikArtemisTest {

	TestType value();
}
