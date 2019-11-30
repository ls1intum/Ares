package de.tum.in.test.api.util.sanitization;

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
	public Throwable sanitize(Throwable t) throws SanitizationError {
		AssertionFailedError afe = (AssertionFailedError) t;
		AssertionFailedError newAfe = new AssertionFailedError(afe.getMessage(), sanitizeValue(afe.getExpected()),
				sanitizeValue(afe.getActual()));
		ThrowableSanitizer.copyThrowableInfo(afe, newAfe);
		return newAfe;
	}

	private static Object sanitizeValue(ValueWrapper vw) {
		if(vw == null)
			return null;
		return vw.getStringRepresentation();
	}
}