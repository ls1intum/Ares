package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

final class SanitizationUtils {

	private SanitizationUtils() {
	}

	static void copyThrowableInfoSafe(Throwable from, Throwable to) {
		copyThrowableInfoSafe(ThrowableInfo.getEssentialInfosSafeFrom(from), to);
	}

	static void copyThrowableInfoSafe(ThrowableInfo from, Throwable to) {
		// make sure everything is sanitized
		if (!from.isSanitized())
			from.sanitize();
		// this returns either null or another Throwable instance
		Throwable fromCause = from.getCause();
		StackTraceElement[] fromStackTrace = from.getStackTrace();
		Throwable[] fromSuppr = from.getSuppressed();

		Throwable toCause = invoke(to::getCause);
		Throwable[] toSuppr = to.getSuppressed(); // OK: final method

		// if toCause is not null, we have to assume it is set and sanitized
		if (toCause == null) {
			try {
				// because causeVal is never from, this will lock the cause
				invoke(() -> to.initCause(fromCause));
			} catch (@SuppressWarnings("unused") IllegalStateException ignored) {
				/*
				 * initCause can fail if a cause is already present, so we add it as suppressed
				 * to not loose information
				 */
				if (fromCause != null)
					to.addSuppressed(fromCause); // OK: final method
			}
		}
		// note that this has the possibility of silently failing
		invoke(() -> to.setStackTrace(fromStackTrace));
		// add suppressed Throwables only if there are none in to but some in from
		if (toSuppr.length == 0 && fromSuppr.length > 0)
			for (Throwable suppressed : toSuppr)
				to.addSuppressed(suppressed); // OK: final method
	}

	static <T> T sanitizeWithinScopeOf(Object scope, SanitizationAction<T> sanitizationAction) {
		return SanitizationUtils.sanitizeWithinScopeOf(scope.getClass(), sanitizationAction);
	}

	static <T> T sanitizeWithinScopeOf(Class<?> scope, SanitizationAction<T> sanitizationAction) {
		try {
			return sanitizationAction.executeSanitization();
		} catch (SanitizationException sanitizationException) {
			throw sanitizationException;
		} catch (Throwable t) { // NOSONAR
			throw new SanitizationException(scope, t);
		}
	}

	static String removeSuffixMatching(String s, String suffix) {
		int end = s.lastIndexOf(suffix);
		if (end == -1 || end + suffix.length() < s.length())
			return null;
		return s.substring(0, end);
	}
}
