package de.tum.in.test.api.util;

import java.lang.reflect.Field;

import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

import de.tum.in.test.api.util.sanitization.ThrowableSanitizer;

/**
 * For handling and post processing Exceptions and Errors, currently not in use.
 *
 * @author Christian Femers
 *
 */
public class ReportingUtils {

	private ReportingUtils() {

	}

	public static <T> T doProceedAndPostProcess(Invocation<T> invocation) throws Throwable {
		/*
		 * Currently not needed anymore, because Artemis #993 is fixed, but might be
		 * useful in future (e.g. convert newlines/tabs to \n, \t or similar)
		 */
		try {
			return invocation.proceed();
		} catch (Throwable t) {
			String name = "unknown";
			Throwable newT;
			try {
				name = t.getClass().getName();
				newT = ThrowableSanitizer.sanitize(t);
			} catch (@SuppressWarnings("unused") Throwable error) {
				throw new SecurityException(
						"Throwable " + name + " threw an error when retrieving information about it.");
			}
			throw newT;
		}
	}

	@SuppressWarnings("unused")
	private static String tryPostProcessFieldOrAddSuppressed(Throwable t, String fieldName) {
		try {
			Field f = Throwable.class.getDeclaredField(fieldName);
			f.setAccessible(true);
			String value = postProcessMessage((String) f.get(t));
			f.set(t, value);
			return value;
		} catch (Exception e) {
			t.addSuppressed(e);
			return null;
		}
	}

	/**
	 * @deprecated This was needed to escape HTML-like messages. It is now fixed in
	 *             Artemis and not needed for now.
	 * @author Christian Femers
	 */
	@Deprecated(since = "0.2.0")
	private static String postProcessMessage(String message) {
		return message.replace('<', '"').replace('>', '"');
	}
}
