package de.tum.in.test.api.localization;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Used to specify a specific locale for a test class.
 * <p>
 * <i>Does not work for jqwick.</i>
 *
 * @see Locale#Locale(String)
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@API(status = Status.EXPERIMENTAL)
@ExtendWith(JupiterLocaleExtension.class)
public @interface UseLocale {
	String value();
}
