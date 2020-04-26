package de.tum.in.test.api.internal;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tum.in.test.api.internal.sanitization.SanitizationError;
import de.tum.in.test.api.internal.sanitization.ThrowableSanitizer;
import de.tum.in.test.api.localization.Messages;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.test.api.util.BlacklistedInvoker;

/**
 * For handling and post processing Exceptions and Errors.
 *
 * @author Christian Femers
 *
 */
public final class ReportingUtils {

	private static final String LINEBREAK_REPLACEMENT = "  ";
	private static final Logger LOG = LoggerFactory.getLogger(ReportingUtils.class);

	static {
		// Initialize
		ThrowableSanitizer.sanitize(null);
	}

	private ReportingUtils() {

	}

	public static <T> T doProceedAndPostProcess(Invocation<T> invocation, TestContext context) throws Throwable {
		try {
			return invocation.proceed();
		} catch (Throwable t) {
			throw processThrowable(t, context);
		}
	}

	public static Throwable processThrowable(Throwable t, TestContext context) {
		Optional<String> nonprivilegedFailureMessage = ConfigurationUtils.getNonprivilegedFailureMessage(context);
		if (nonprivilegedFailureMessage.isPresent())
			return processThrowablePrivilegedOnly(t, nonprivilegedFailureMessage.get());
		return processThrowableRegularly(t);
	}

	private static Throwable processThrowableRegularly(Throwable t) {
		Throwable newT = trySanitizeThrowable(t);
		tryPostProcessMessageOrAddSuppressed(newT, ReportingUtils::postProcessMessage);
		if (!(newT instanceof AssertionError)) {
			addStackframeInfoToMessage(newT);
		}
		return newT;
	}

	private static Throwable processThrowablePrivilegedOnly(Throwable t, String nonprivilegedFailureMessage) {
		Throwable newT;
		if (t instanceof PrivilegedException)
			newT = trySanitizeThrowable(t);
		else
			newT = new AssertionError(nonprivilegedFailureMessage);
		tryPostProcessMessageOrAddSuppressed(t, ReportingUtils::postProcessMessage);
		return newT;
	}

	private static Throwable trySanitizeThrowable(Throwable t) {
		String name = "unknown";
		Throwable newT;
		try {
			name = t.getClass().getName();
			newT = ThrowableSanitizer.sanitize(t);
		} catch (Throwable sanitizationError) {
			BlacklistedInvoker.invoke(() -> {
				LOG.error("Sanitization failed for " + t + " with error", sanitizationError);
				return null;
			});
			return handleSanitizationFailure(name, sanitizationError);
		}
		return newT;
	}

	private static Throwable handleSanitizationFailure(String name, Throwable error) {
		String info = error.getClass() == SanitizationError.class ? error.toString() : error.getClass().toString();
		return new SecurityException(
				"Throwable " + name + " threw an error when retrieving information about it. (" + info + ")");
	}

	private static void addStackframeInfoToMessage(Throwable newT) {
		StackTraceElement[] stackTrace = BlacklistedInvoker.invoke(newT::getStackTrace);
		var first = ArtemisSecurityManager.firstNonWhitelisted(stackTrace);
		if (first.isPresent()) {
			String call = first.get().toString();
			tryPostProcessMessageOrAddSuppressed(newT, old -> {
				return Objects.toString(old, "") + "  "
						+ Messages.formatLocalized("reporting.problem_location_hint", call);
			});
		}
	}

	private static String tryPostProcessMessageOrAddSuppressed(Throwable t, UnaryOperator<String> transform) {
		try {
			Field f = Throwable.class.getDeclaredField("detailMessage");
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
