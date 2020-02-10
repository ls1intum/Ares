package de.tum.in.test.api.util;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

import de.tum.in.test.api.locked.ArtemisSecurityManager;
import de.tum.in.test.api.util.sanitization.ThrowableSanitizer;

/**
 * For handling and post processing Exceptions and Errors.
 *
 * @author Christian Femers
 *
 */
public class ReportingUtils {

	private ReportingUtils() {

	}

	public static <T> T doProceedAndPostProcess(Invocation<T> invocation) throws Throwable {
		try {
			return invocation.proceed();
		} catch (Throwable t) {
			throw processThrowable(t);
		}
	}

	public static Throwable processThrowable(Throwable t) {
		if (t == null)
			return null;
		String name = "unknown";
		Throwable newT;
		try {
			name = t.getClass().getName();
			newT = ThrowableSanitizer.sanitize(t);
		} catch (@SuppressWarnings("unused") Throwable error) {
			return new SecurityException("Throwable " + name + " threw an error when retrieving information about it.");
		}
		tryPostProcessFieldOrAddSuppressed(t, "detailMessage", ReportingUtils::postProcessMessage);
		if (!(newT instanceof AssertionError)) {
			StackTraceElement[] stackTrace = newT.getStackTrace();
			var first = ArtemisSecurityManager.firstNonWhitelisted(stackTrace);
			if (first.isPresent()) {
				String call = first.get().toString();
				tryPostProcessFieldOrAddSuppressed(newT, "detailMessage", old -> {
					return Objects.toString(old, "") + "  /// AJTS: Mögliche Problemstelle: " + call + " ///";
				});
			}
		}
		return newT;
	}

	private static String tryPostProcessFieldOrAddSuppressed(Throwable t, String fieldName,
			UnaryOperator<String> transform) {
		try {
			Field f = Throwable.class.getDeclaredField(fieldName);
			f.setAccessible(true);
			String value = transform.apply((String) f.get(t));
			f.set(t, value);
			return value;
		} catch (Exception e) {
			t.addSuppressed(e);
			return null;
		}
	}

	/**
	 * This will replace any newlines by spaces, so that multiline messages are
	 * displayed fully in the Artemis user interface.
	 */
	private static String postProcessMessage(String message) {
		return message != null ? message.replaceAll("\r?\n", "  ") : null;
	}
}
