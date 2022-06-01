package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.Assertions;

/**
 * This annotation can be placed on a test method or test class to cause all
 * failing tests to hide the failure details. It is useful if you want to show
 * students that something is not working, but they should figure out the reason
 * for themselves.
 * <p>
 * This means that all assertion failures and exceptions will be transformed
 * into a generic assertion failed error that has the
 * {@link PrivilegedExceptionsOnly#value() value} as message.
 * <p>
 * Only exceptions and assertions that are thrown in an privileged context are
 * not filtered. This can be archived by using
 * {@link TestUtils#privilegedFail(String)} instead of
 * {@link Assertions#fail(String)} or by using
 * {@link TestUtils#privilegedThrow(Runnable)} like <blockquote>
 *
 * <pre>
 * int x = ...
 * TestUtils.privilegedThrow(() {@literal ->} {
 *     assertEquals(42, x, "Wrong value for x");
 * });
 * </pre>
 *
 * </blockquote>
 * <p>
 * <b>Important:</b> If you use the variant shown above, make sure to put as
 * little code as possible in the privileged scope. Ideally, only the
 * assert-Statements without any other method calls are in there.
 * <p>
 * Keep in mind that the privileged throw can only be used by whitelisted
 * callers, of course.
 *
 * @see TestUtils#privilegedFail(String)
 * @see TestUtils#privilegedThrow(Runnable)
 * @see TestUtils#privilegedThrow(java.util.concurrent.Callable)
 * @author Christian Femers
 * @since 0.5.2
 * @version 1.1.0
 */
@API(status = Status.STABLE)
@Inherited
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface PrivilegedExceptionsOnly {
	/**
	 * The error message for failures that are not privileged. This message is
	 * displayed when an exception has not been thrown in an explicit privileged
	 * context.
	 * <p>
	 * The default message is <code>Test failed.</code>
	 *
	 * @return the string used as message for all non-privileged failures
	 * @author Christian Femers
	 */
	String value() default "Test failed.";
}
