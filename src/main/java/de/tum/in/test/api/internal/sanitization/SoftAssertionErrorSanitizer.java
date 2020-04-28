package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.SoftAssertionError;

enum SoftAssertionErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(SoftAssertionError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		SoftAssertionError sae = new SoftAssertionError(
				invoke(() -> List.copyOf(((SoftAssertionError) t).getErrors())));
		ThrowableSanitizer.copyThrowableInfo(t, sae);
		return sae;
	}
}
