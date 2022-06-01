package de.tum.in.test.api;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Allows networking with <code>localhost</code> at the specified ports.
 * <p>
 * Use {@link #value()} or {@link #allowPortsAbove()} to specify the ports
 * students should use. Otherwise this annotation has no effect.
 *
 * @author Christian Femers
 * @since 0.3.6
 * @version 1.1.0
 */
@API(status = Status.STABLE)
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
public @interface AllowLocalPort {

	/**
	 * The maximum possible network port number.
	 * <p>
	 * <i>Allow no other ports.</i>
	 */
	int MAXIMUM = 0xFFFF;

	/**
	 * One below the minimum IANA ephemeral/private port.
	 * <p>
	 * <i>Allow only ephemeral ports according to IANA.</i>
	 */
	int IANA_EPHEMERAL_LOWER_BORDER = 0xC000 - 1;

	/**
	 * One below the minimum IANA registered port.
	 * <p>
	 * <i>Allow registered and ephemeral ports according to IANA.</i>
	 */
	int IANA_REGISTERED_LOWER_BORDER = 0x0400 - 1;

	/**
	 * Zero, the minimum.
	 * <p>
	 * <i>Allow all ports. (set {@link #value()} to <code>0</code>.)</i>
	 */
	int MINIMUM = 0x0000;

	/**
	 * The ports to allow listening / connecting to. Add ports that students should
	 * use here.
	 * <p>
	 * Empty by default. Must not contain ports in {@link #exclude()}.
	 *
	 * @return the port numbers for which all local port connections should be
	 *         allowed.
	 */
	int[] value() default {};

	/**
	 * The number above which listening / connecting to is also allowed. This is
	 * needed if the student code runs the server and needs to connect to clients
	 * which get their ports dynamically assigned.
	 * <p>
	 * By default, this is {@link #MAXIMUM}, meaning that only {@link #value()} is
	 * allowed.
	 * <p>
	 * See other constants in {@link AllowLocalPort} for some possible values, e.g.
	 * {@link #IANA_REGISTERED_LOWER_BORDER}.
	 *
	 * @return the port number above which all local port connections should be
	 *         allowed in addition to {@link #value()}.
	 */
	int allowPortsAbove() default MAXIMUM;

	/**
	 * Exclusions from the rules above, those will never be allowed.
	 * <p>
	 * Empty by default. Must not contain ports in {@link #value()}.
	 *
	 * @return the port numbers in {@link #allowPortsAbove()} which should
	 *         explicitly not be allowed.
	 */
	int[] exclude() default {};
}
