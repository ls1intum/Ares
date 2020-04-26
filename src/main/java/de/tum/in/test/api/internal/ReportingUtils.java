package de.tum.in.test.api.internal;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

import de.tum.in.test.api.internal.sanitization.SanitizationError;
import de.tum.in.test.api.internal.sanitization.ThrowableSanitizer;
import de.tum.in.test.api.localization.Messages;
import de.tum.in.test.api.security.ArtemisSecurityManager;

/**
 * For handling and post processing Exceptions and Errors.
 *
 * @author Christian Femers
 *
 */
public final class ReportingUtils {

	private static final String LINEBREAK_REPLACEMENT = "  ";

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
		String name = "unknown";
		Throwable newT;
		try {
			name = t.getClass().getName();
			newT = ThrowableSanitizer.sanitize(t);
		} catch (Throwable sanitizationError) {
			return handleSanitizationFailure(name, sanitizationError);
		}
		tryPostProcessFieldOrAddSuppressed(newT, "detailMessage", ReportingUtils::postProcessMessage);
		if (!(newT instanceof AssertionError)) {
			addStackframeInfoToMessage(newT);
		}
		return newT;
	}

	private static Throwable handleSanitizationFailure(String name, Throwable error) {
		String info = error.getClass() == SanitizationError.class ? error.toString() : error.getClass().toString();
		return new SecurityException(
				"Throwable " + name + " threw an error when retrieving information about it. (" + info + ")");
	}

	private static void addStackframeInfoToMessage(Throwable newT) {
		StackTraceElement[] stackTrace = newT.getStackTrace();
		var first = ArtemisSecurityManager.firstNonWhitelisted(stackTrace);
		if (first.isPresent()) {
			String call = first.get().toString();
			tryPostProcessFieldOrAddSuppressed(newT, "detailMessage", old -> {
				return Objects.toString(old, "") + "  "
						+ Messages.formatLocalized("reporting.problem_location_hint", call);
			});
		}
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
		return message != null ? message.replaceAll("\r?\n", LINEBREAK_REPLACEMENT) : null;
	}
}
