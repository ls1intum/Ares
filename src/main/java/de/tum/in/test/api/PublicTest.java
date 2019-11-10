package de.tum.in.test.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.io.IOTester;

/**
 * Marks a <b>PUBLIC</b> test case, uses the PdgpSecurityManager, can declare
 * {@link IOTester} as parameter.
 * 
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented()
@Retention(RUNTIME)
@Target(METHOD)
@GeneralArtemisTest
public @interface PublicTest {
	// maker only
}
