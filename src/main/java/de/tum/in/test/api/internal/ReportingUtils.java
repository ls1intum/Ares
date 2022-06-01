package de.tum.in.test.api.internal;

import static de.tum.in.test.api.localization.Messages.localized;

import java.util.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;
import org.slf4j.*;

import de.tum.in.test.api.context.TestContext;
import de.tum.in.test.api.internal.sanitization.*;
import de.tum.in.test.api.security.ArtemisSecurityManager;

/**
 * For handling and post processing Exceptions and Errors.
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
public final class ReportingUtils {

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
		return trySanitizeThrowable(t, ReportingUtils::transformMessage);
	}

	private static Throwable processThrowablePrivilegedOnly(Throwable t, String nonprivilegedFailureMessage) {
		if (t instanceof PrivilegedException)
			return trySanitizeThrowable(t, ReportingUtils::transformMessage);
		return new AssertionError(nonprivilegedFailureMessage);
	}

	private static Throwable trySanitizeThrowable(Throwable t, MessageTransformer messageTransformer) {
		var name = "unknown"; //$NON-NLS-1$
		try {
			name = t.getClass().getName();
			return ThrowableSanitizer.sanitize(t, messageTransformer);
		} catch (Throwable sanitizationError) { // NOSONAR
			return handleSanitizationFailure(name, sanitizationError);
		}
	}

	private static String transformMessage(ThrowableInfo info) {
		if (!AssertionError.class.isAssignableFrom(info.getClass()))
			addStackframeInfoToMessage(info);
		return info.getMessage();
	}

	private static Throwable handleSanitizationFailure(String name, Throwable error) {
		var info = BlacklistedInvoker.invokeOrElse(error::toString, () -> error.getClass().toString());
		LOG.error("Sanitization failed for {} with error {}", name, info); //$NON-NLS-1$
		return new SecurityException(localized("sanitization.sanitization_failure", name, info)); //$NON-NLS-1$
	}

	private static void addStackframeInfoToMessage(ThrowableInfo info) {
		StackTraceElement[] stackTrace = info.getStackTrace();
		var first = ArtemisSecurityManager.firstNonWhitelisted(stackTrace);
		if (first.isPresent()) {
			var call = first.get().toString();
			info.setMessage(Objects.toString(info.getMessage(), "") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
					+ localized("reporting.problem_location_hint", call)); //$NON-NLS-1$
		}
	}
}
