package de.tum.in.test.testutilities;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.junit.jupiter.api.*;

@Test
@Tag("test-test")
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface TestTest {
	// just a @Test @Tag combination
}
