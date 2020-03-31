package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.util.BlacklistedInvoker.invoke;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.error.AssertJMultipleFailuresError;
import org.opentest4j.MultipleFailuresError;

enum MultipleFailuresErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(MultipleFailuresError.class,
			AssertJMultipleFailuresError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		try {
			Field f = MultipleFailuresError.class.getDeclaredField("heading");
			f.setAccessible(true);
			String heading = (String) f.get(t);
			List<Throwable> failures = List.copyOf(invoke(() -> ((MultipleFailuresError) t).getFailures().stream()
					.map(ThrowableSanitizer::sanitize).collect(Collectors.toList())));
			MultipleFailuresError mfe = t.getClass().equals(MultipleFailuresError.class)
					? new MultipleFailuresError(heading, failures)
					: new AssertJMultipleFailuresError(heading, failures);
			ThrowableSanitizer.copyThrowableInfo(t, mfe);
			return mfe;
		} catch (ReflectiveOperationException e) {
			throw new SanitizationError(e);
		}
	}
}
