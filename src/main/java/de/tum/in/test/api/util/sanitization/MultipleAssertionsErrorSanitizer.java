package de.tum.in.test.api.util.sanitization;

import static de.tum.in.test.api.util.BlacklistedInvoker.invoke;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.error.MultipleAssertionsError;

public enum MultipleAssertionsErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(MultipleAssertionsError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		MultipleAssertionsError mae = new MultipleAssertionsError(List.copyOf(
				invoke(() -> ((MultipleAssertionsError) t).getErrors().stream().map(ThrowableSanitizer::sanitize)
						.map(ae -> (AssertionError) ae).collect(Collectors.toList()))));
		ThrowableSanitizer.copyThrowableInfo(t, mae);
		return mae;
	}
}
