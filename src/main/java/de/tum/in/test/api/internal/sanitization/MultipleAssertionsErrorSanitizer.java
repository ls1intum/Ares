package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.MultipleAssertionsError;

enum MultipleAssertionsErrorSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(MultipleAssertionsError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t, MessageTransformer messageTransformer) {
		MultipleAssertionsError mae = (MultipleAssertionsError) t;
		ThrowableInfo info = ThrowableInfo.getEssentialInfosSafeFrom(mae).sanitize();
		// the list is not safe here, it is simply set in the constructor
		List<AssertionError> errors = invoke(() -> List.copyOf(mae.getErrors())).stream()
				.map(ThrowableSanitizer::sanitize).map(AssertionError.class::cast)
				.collect(Collectors.toUnmodifiableList());
		String description = "";
		if (info.getMessage().startsWith("[")) {
			// has a description, that we now have to get somehow
			String messageWithoutDecscription = invoke(() -> new MultipleAssertionsError(mae.getErrors()).getMessage());
			String start = SanitizationUtils.removeSuffixMatching(info.getMessage(), messageWithoutDecscription);
			if (start != null)
				description = start.substring(1, start.length() - 2);
		}
		/*
		 * Note that this will only affect the description, not the whole message (this
		 * is not possible).
		 */
		info.setMessage(description);
		description = messageTransformer.apply(info);
		MultipleAssertionsError newMae = new MultipleAssertionsError(new TextDescription(description), errors);
		SanitizationUtils.copyThrowableInfoSafe(info, newMae);
		return newMae;
	}
}
