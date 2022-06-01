package de.tum.in.test.api.io;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.*;

import net.jqwik.api.lifecycle.AroundPropertyHook;

import de.tum.in.test.api.WithIOManager;

/**
 * Manages how IO testing is performed in the Ares test extension for IO.
 * <p>
 * Ares does not make any guarantee whether instances are reused across
 * different tests or if a new manager instance is created for each test.
 * <p>
 * Implementations are highly encouraged to respect/reuse the user settings in
 * the given {@link AresIOContext}, if feasible.
 *
 * @param <T> the type of the controller object, that is an object that can be
 *            used by testers to control IO testing inside the test method. See
 *            e.g. {@link IOTester}
 * @author Christian Femers
 * @implSpec Implementations that are used in {@link WithIOManager} must provide
 *           a public default constructor with no arguments.
 */
@API(status = Status.EXPERIMENTAL)
public interface IOManager<T> {

	/**
	 * Invoked before each test is executed.
	 *
	 * @param context the current Ares IO context
	 * @see BeforeEachCallback
	 * @see AroundPropertyHook
	 */
	void beforeTestExecution(AresIOContext context);

	/**
	 * Invoked each the test is executed.
	 *
	 * @param context the current Ares IO context
	 * @see AfterEachCallback
	 * @see AroundPropertyHook
	 */
	void afterTestExecution(AresIOContext context);

	/**
	 * Provides an instance of an object to control IO testing that is available as
	 * parameter in the test method.
	 *
	 * @param context the current Ares IO context
	 * @return a tester instance. This should only be null if
	 *         {@link #getControllerClass()} returns null as well or no test is
	 *         currently running. May be a subclass of
	 *         {@link #getControllerClass()}.
	 * @implSpec This may return different instances each time, but as they are e.g.
	 *           passed as parameter to the test method, they must remain valid for
	 *           the whole life cycle of that test (in between
	 *           {@link #beforeTestExecution(AresIOContext) beforeTest} and
	 *           {@link #afterTestExecution(AresIOContext) afterTest}).
	 */
	T getControllerInstance(AresIOContext context);

	/**
	 * The class of the type provided by
	 * {@link #getControllerInstance(AresIOContext)} such that Ares can register a
	 * parameter provider.
	 *
	 * @return the tester class, or null if no such controller object exists. This
	 *         must not be {@link Object}.
	 * @implSpec this should be stateless, if different implementations are needed
	 *           in certain situations, introduce an appropriate generalization in
	 *           form of a common super class/interface or use composition
	 */
	Class<T> getControllerClass();
}
