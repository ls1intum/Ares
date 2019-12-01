package de.tum.in.test.api.util.sanitization;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.SoftAssertionError;

public enum SoftAssertionErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(SoftAssertionError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		SoftAssertionError sae = new SoftAssertionError(
				List.copyOf(((SoftAssertionError) t).getErrors().stream().collect(Collectors.toList())));
		ThrowableSanitizer.copyThrowableInfo(t, sae);
		return sae;
	}
}
