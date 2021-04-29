package de.tum.in.test.api.security;

import ch.qos.logback.core.OutputStreamAppender;

import de.tum.in.test.api.io.IOTester;

/**
 * Required because ConsoleAppender will always use the current System.out or
 * System.err, which is not what we want because it collides with the
 * {@link IOTester}
 *
 * @param <E> the type of the events that the appender handles
 * @author Christian Femers
 */
public class FixSystemErrAppender<E> extends OutputStreamAppender<E> {
	public FixSystemErrAppender() {
		setOutputStream(SecurityConstants.SYSTEM_ERR);
	}
}
