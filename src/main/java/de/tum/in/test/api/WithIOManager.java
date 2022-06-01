package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.io.*;

/**
 * Allows to overwrite the default IO test implementation of Ares with is using
 * {@link IOTester}.
 * <p>
 * A custom {@link IOManager} class must have a constructor that takes no
 * arguments and be accessible to Ares. All classes used for that purpose should
 * be trusted/whitelisted.
 *
 * @author Christian Femers
 * @since 1.9.1
 * @version 1.0.0
 * @see IOManager
 */
@API(status = Status.EXPERIMENTAL)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface WithIOManager {

	/**
	 * The {@link IOManager} implementation to use for testing in the annotated
	 * element.
	 */
	Class<? extends IOManager<?>> value();

	/**
	 * Effectively no {@link IOManager}. {@link System#out}, {@link System#err} and
	 * {@link System#in} are unchanged. Not recommended. Consider a custom but
	 * functional {@link IOManager} implementation first.
	 */
	public final class None implements IOManager<Void> {

		@Override
		public void beforeTestExecution(AresIOContext context) {
			// do nothing
		}

		@Override
		public void afterTestExecution(AresIOContext context) {
			// do nothing
		}

		@Override
		public Void getControllerInstance(AresIOContext context) {
			return null;
		}

		@Override
		public Class<Void> getControllerClass() {
			return null;
		}
	}
}
