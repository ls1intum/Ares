package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.error.MultipleAssertionsError;

enum MultipleAssertionsErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(MultipleAssertionsError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) {
		// the list is not safe here, it is simply set in the constructor
		MultipleAssertionsError mae = new MultipleAssertionsError(
				invoke(() -> List.copyOf(((MultipleAssertionsError) t).getErrors())).stream()
						.map(ThrowableSanitizer::sanitize).map(ae -> (AssertionError) ae)
						.collect(Collectors.toUnmodifiableList()));
		SanitizationUtils.copyThrowableInfoSafe(t, mae);
		return mae;
	}
}
