package de.tum.in.test.api.internal.sanitization;

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
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) {
		String heading = (String) HEADING.get(t);
		// list is safe here because of defensive copying in MultipleFailuresError
		List<Throwable> failures = ((MultipleFailuresError) t).getFailures().stream().map(ThrowableSanitizer::sanitize)
				.collect(Collectors.toList());
		MultipleFailuresError mfe = createNewInstance(t, heading, failures);
		SanitizationUtils.copyThrowableInfoSafe(t, mfe);
		return mfe;
	}

	private static MultipleFailuresError createNewInstance(Throwable t, String heading, List<Throwable> failures) {
		if (t.getClass().equals(MultipleFailuresError.class))
			return new MultipleFailuresError(heading, failures);
		return new AssertJMultipleFailuresError(heading, failures);
	}
}
