package de.tum.in.test.api.security;

import ch.qos.logback.core.OutputStreamAppender;
import de.tum.in.test.api.io.IOTester;

/**
 * Required because ConsoleAppender will always use the current System.out or
 * System.err, which is not what we want because it collides with the
 * {@link IOTester}
 *
 * @author Christian Femers
 */
public class FixSystemErrAppender<E> extends OutputStreamAppender<E> {
	public FixSystemErrAppender() {
		super();
		setOutputStream(SecurityConstants.SYSTEM_ERR);
	}
}