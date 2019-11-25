package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

/**
 * Set a Deadline for {@link HiddenTest}, must be present. After this deadline,
 * hidden tests will be executed if there are no relevant
 * {@link ExtendedDeadline} annotations present. The annotation can also be
 * placed on a single method, which will <b>overwrite</b> any annotations on the
 * class level. This annotation is optional if all tests of the test class are
 * {@link PublicTest}s.
 * <p>
 * The deadline date (and time) is the date set by this annotation <b>plus</b>
 * the {@link ExtendedDeadline} annotation of the test class if present and
 * <b>plus</b> the {@link ExtendedDeadline} annotation of the test method, if
 * present. Hidden tests are executed, when {@link LocalDateTime#now()} is
 * <b>past this date</b>.
 * <p>
 * The format has to be {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}. In this
 * case, we allow replacing the <code>T</code> with a space, to improve
 * readability. So this can be e.g.
 * 
 * <ul>
 * <li><code>2019-09-09 06:00</code></li>
 * <li><code>2019-09-09T06:00</code></li>
 * <li><code>2019-09-09T06:00:01</code></li>
 * </ul>
 * 
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface Deadline {
	String value();
}
