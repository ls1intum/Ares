package de.tum.in.test.api.security;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

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
@API(status = Status.INTERNAL)
public class FixSystemErrAppender<E> extends OutputStreamAppender<E> {
	public FixSystemErrAppender() {
		setName("Ares Console Appender"); //$NON-NLS-1$
	}

	@Override
	public void start() {
		setOutputStream(SecurityConstants.SYSTEM_ERR);
		super.start();
	}
}
