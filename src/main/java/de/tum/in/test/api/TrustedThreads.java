package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.security.ArtemisSecurityManager;

/**
 * Allows to specify which threads are trusted and whitelisted.
 * <p>
 * The default trust scope is {@link TrustScope#MINIMAL minimal}, so student
 * threads and threads executing arbitrary code are excluded. Other modes may
 * only provide security against mistakes, but are not fully secure unless the
 * user can ensure that student code is never trusted / whitelisted (which is
 * hard to do).
 * <p>
 * Possible ways how student code can be prevented from being in trusted
 * packages are:
 * <ul>
 * <li>Removing student classes that reside in trusted packages after
 * compilation.</li>
 * <li>Whitelist paths such that only student classes in a specific package can
 * be loaded. This could look like
 * <code>@WhitelistPath("target/classes/de/tum")</code> for student classes that
 * should be in packages <code>de.tum.*</code>. Note that this likely requires
 * test code accessing student code only via reflection due to the way classes
 * are loaded.</li>
 * <li>Possibly additional checks at runtime.</li>
 * </ul>
 * <p>
 * <b>We recommend leaving this at the default value (which is also equivalent
 * to this annotation being absent) unless you know a broader trust scope is
 * needed.</b>
 * <p>
 * Testing with jqwik requires the trust scope to be at least
 * {@link TrustScope#EXTENDED} if the test code requires more permissions than
 * student code to work.
 * <p>
 * Note that the stack frame whitelisting is an additional check to the thread
 * whitelisting. A not-whitelisted thread cannot have whitelisted stack frames,
 * but a whitelisted thread can still have non-whitelisted stack frames, such as
 * student methods being invoked from the test method.
 * <p>
 * If test code requires a single thread to be whitelisted and the test code has
 * access to this thread, consider using
 * {@link ArtemisSecurityManager#requestThreadWhitelisting(Thread)}.
 *
 * @since 1.7.3
 * @version 1.1.0
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, CONSTRUCTOR, ANNOTATION_TYPE })
public @interface TrustedThreads {

	/**
	 * The trust scope based on which threads are trusted and whitelisted.
	 * {@link TrustScope#MINIMAL} by default.
	 *
	 * @author Christian Femers
	 */
	TrustScope value() default TrustScope.MINIMAL;

	/**
	 * Contains all possible thread trust settings.
	 *
	 * @author Christian Femers
	 */
	enum TrustScope {
		/**
		 * In this mode, only the main thread is trusted along with threads created
		 * outside of Ares' test thread group (where the student threads reside).
		 * Finalizer and common pool are excluded from this, as they can be passed any
		 * code to execute.
		 */
		MINIMAL,
		/**
		 * In this mode, all threads of {@link #MINIMAL} are trusted as well as
		 * finalizer and common pool threads.
		 * <p>
		 * <b>This mode is not secure unless you prevent student classes in trusted
		 * packages.</b> For more details and possible solutions, see the documentation
		 * of {@link TrustedThreads}. Might still prevent accidental mistakes better
		 * than {@link #ALL_THREADS}
		 */
		EXTENDED,
		/**
		 * In this mode, all threads are trusted.
		 * <p>
		 * <b>This mode is not secure unless you prevent student classes in trusted
		 * packages.</b> For more details and possible solutions, see the documentation
		 * of {@link TrustedThreads}.
		 */
		ALL_THREADS
	}
}
