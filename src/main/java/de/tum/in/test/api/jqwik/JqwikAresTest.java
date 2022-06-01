package de.tum.in.test.api.jqwik;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import net.jqwik.api.lifecycle.*;

import de.tum.in.test.api.context.TestType;

/**
 * This is only for internal use, to reduce redundancy.
 * <p>
 * <i>Adaption for jqwick.</i>
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@Inherited
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@AddLifecycleHook(value = JqwikTestGuard.class, propagateTo = PropagationMode.ALL_DESCENDANTS)
@AddLifecycleHook(value = JqwikIOExtension.class, propagateTo = PropagationMode.ALL_DESCENDANTS)
@AddLifecycleHook(value = JqwikSecurityExtension.class, propagateTo = PropagationMode.ALL_DESCENDANTS)
@AddLifecycleHook(value = JqwikStrictTimeoutExtension.class, propagateTo = PropagationMode.ALL_DESCENDANTS)
public @interface JqwikAresTest {

	TestType value();
}
