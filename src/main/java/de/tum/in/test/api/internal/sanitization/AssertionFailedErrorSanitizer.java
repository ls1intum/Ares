package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.util.Set;

import org.opentest4j.AssertionFailedError;
import org.opentest4j.ValueWrapper;

enum AssertionFailedErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(AssertionFailedError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t, MessageTransformer messageTransformer) {
		AssertionFailedError afe = (AssertionFailedError) t;
		ValueWrapper expected = afe.getExpected();
		ValueWrapper actual = afe.getExpected();
		ThrowableInfo info = ThrowableInfo.getEssentialInfosSafeFrom(t).sanitize();
		String newMessage = messageTransformer.apply(info);
		AssertionFailedError newAfe = new AssertionFailedError(newMessage, sanitizeValue(expected),
				sanitizeValue(actual));
		SanitizationUtils.copyThrowableInfoSafe(info, newAfe);
		return newAfe;
	}

	private static Object sanitizeValue(ValueWrapper vw) {
		if (vw == null)
			return null;
		return SanitizationUtils.sanitizeWithinScopeOf(vw.getType(), () -> invoke(vw::getStringRepresentation));
	}
}
