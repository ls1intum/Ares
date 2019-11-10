package de.tum.in.test.api;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.tum.in.test.api.locked.ArtemisTestExtension;
import de.tum.in.test.api.locked.ArtemisTestGuard;

/**
 * This is only for internal use, to reduce redundancy.
 * 
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@Test
@ExtendWith(ArtemisTestExtension.class)
@ExtendWith(ArtemisTestGuard.class)
@interface GeneralArtemisTest {
	// marker only
}
