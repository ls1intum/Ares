package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.error.AssertJMultipleFailuresError;
import org.opentest4j.MultipleFailuresError;

enum MultipleFailuresErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private static final String HEADING_NAME = "heading";

	private final Set<Class<? extends Throwable>> types = Set.of(MultipleFailuresError.class,
			AssertJMultipleFailuresError.class);

	private static final VarHandle HEADING;

	static {
		try {
			var lookup = MethodHandles.privateLookupIn(MultipleFailuresError.class, MethodHandles.lookup());
			HEADING = lookup.findVarHandle(MultipleFailuresError.class, HEADING_NAME, String.class);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		String heading = (String) HEADING.get(t);
		List<Throwable> failures = List.copyOf(invoke(() -> ((MultipleFailuresError) t).getFailures().stream()
				.map(ThrowableSanitizer::sanitize).collect(Collectors.toList())));
		MultipleFailuresError mfe = createNewInstance(t, heading, failures);
		ThrowableSanitizer.copyThrowableInfoSafe(t, mfe);
		return mfe;
	}

	private static MultipleFailuresError createNewInstance(Throwable t, String heading, List<Throwable> failures) {
		if (t.getClass().equals(MultipleFailuresError.class))
			return new MultipleFailuresError(heading, failures);
		return new AssertJMultipleFailuresError(heading, failures);
	}
}
