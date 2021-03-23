package de.tum.in.test.api.internal.sanitization;

import static org.assertj.core.api.Assertions.entry;

import java.net.HttpRetryException;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.IllformedLocaleException;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.function.Supplier;

import junit.framework.ComparisonCompactor;
import junit.framework.ComparisonFailure;

import org.junit.experimental.theories.internal.ParameterizedAssertionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tum.in.test.api.internal.sanitization.ThrowableInfo.PropertyKey;
import de.tum.in.test.api.util.LruCache;

enum SafeTypeThrowableSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private static final Logger LOG = LoggerFactory.getLogger(SafeTypeThrowableSanitizer.class);

	static final Set<String> NON_DUPLICATABLE_SAFE_TYPES = Set.of( //
			"java.io.OptionalDataException", //
			"java.util.IllegalFormatException", //
			"net.jqwik.api.configurators.ArbitraryConfigurationException", //
			"net.jqwik.api.lifecycle.CannotFindStoreException", //
			"net.jqwik.api.lifecycle.CannotResolveParameterException", //
			"net.jqwik.engine.execution.pipeline.DuplicateExecutionTaskException", //
			"net.jqwik.engine.execution.pipeline.PredecessorNotSubmittedException", //
			"net.jqwik.engine.properties.arbitraries.NotAFunctionalTypeException", //
			"org.junit.Test$None", //
			"org.junit.internal.ArrayComparisonFailure", //
			"org.junit.runner.FilterFactory$FilterNotCreatedException" //
	);

	static final Map<String, ThrowableCreator> SPECIFIC_CREATORS = Map.ofEntries( //
			entry("java.net.HttpRetryException", new HttpRetryExceptionCreator()), //
			entry("java.time.format.DateTimeParseException", new DateTimeParseExceptionCreator()), //
			entry("java.util.IllformedLocaleException", new IllformedLocaleExceptionCreator()), //
			entry("java.util.MissingResourceException", new MissingResourceExceptionCreator()), //
			entry("junit.framework.ComparisonFailure", new ThrowableCreatorWrapper(ComparisonFailureCreator::new)), //
			entry("org.junit.ComparisonFailure", new ThrowableCreatorWrapper(ComparisonFailureCreator::new)), //
			entry("org.junit.experimental.theories.internal.ParameterizedAssertionError",
					new ThrowableCreatorWrapper(ParameterizedAssertionErrorCreator::new)) //
	);

	final Map<Class<? extends Throwable>, ThrowableCreator> cachedThrowableCreators = Collections
			.synchronizedMap(new LruCache<>(1000));

	@Override
	public boolean canSanitize(Throwable t) {
		Class<? extends Throwable> type = t.getClass();
		boolean isSafe = ThrowableSets.SAFE_TYPES.contains(type);
		boolean isDuplicatable = !NON_DUPLICATABLE_SAFE_TYPES.contains(type.getName());
		return isSafe && isDuplicatable;
	}

	@Override
	public Throwable sanitize(Throwable t, MessageTransformer messageTransformer) {
		Class<? extends Throwable> type = t.getClass();
		// this is OK because we are only dealing with safe types here
		ThrowableInfo info = ThrowableInfo.of(type, ThrowableUtils.retrievePropertyValues(t));
		info.sanitize(ThrowableUtils.PROPERTY_SANITIZER);
		info.setMessage(messageTransformer.apply(info));
		var throwableCreator = cachedThrowableCreators.computeIfAbsent(type, this::findThrowableCreator);
		try {
			Throwable newInstance = throwableCreator.create(info);
			SanitizationUtils.copyThrowableInfoSafe(info, newInstance);
			return newInstance;
		} catch (RuntimeException e) {
			LOG.warn("Failed to sanitize an exception of type {}.", type, e);
			return ArbitraryThrowableSanitizer.INSTANCE.sanitize(t, messageTransformer);
		}
	}

	private ThrowableCreator findThrowableCreator(Class<? extends Throwable> type) {
		var throwableCreator = SPECIFIC_CREATORS.get(type.getName());
		if (throwableCreator == null)
			throwableCreator = ThrowableUtils.getThrowableCreatorFor(type);
		return throwableCreator;
	}

	private static class ThrowableCreatorWrapper implements ThrowableCreator {
		private final Supplier<ThrowableCreator> throwableCreatorSupplier;
		private ThrowableCreator throwableCreator;

		private ThrowableCreatorWrapper(Supplier<ThrowableCreator> throwableCreatorSupplier) {
			this.throwableCreatorSupplier = throwableCreatorSupplier;
		}

		@Override
		public Throwable create(ThrowableInfo throwableInfo) {
			if (throwableCreator == null)
				throwableCreator = throwableCreatorSupplier.get();
			return throwableCreator.create(throwableInfo);
		}
	}

	private static class HttpRetryExceptionCreator implements ThrowableCreator {

		private static final PropertyKey<Integer> RESPONSE_CODE = new PropertyKey<>(int.class, "responseCode");
		private static final PropertyKey<String> LOCATION = new PropertyKey<>(String.class, "location");

		@Override
		public Throwable create(ThrowableInfo info) {
			var detail = info.getMessage();
			var code = info.getProperty(RESPONSE_CODE);
			var location = info.getProperty(LOCATION);
			return new HttpRetryException(detail, code, location);
		}
	}

	private static class DateTimeParseExceptionCreator implements ThrowableCreator {

		private static final PropertyKey<String> PARSED_STRING = new PropertyKey<>(String.class, "parsedString");
		private static final PropertyKey<Integer> ERROR_INDEX = new PropertyKey<>(int.class, "errorIndex");

		@Override
		public Throwable create(ThrowableInfo info) {
			var message = info.getMessage();
			var parsedData = info.getProperty(PARSED_STRING);
			var errorIndex = info.getProperty(ERROR_INDEX);
			return new DateTimeParseException(message, parsedData, errorIndex);
		}
	}

	private static class IllformedLocaleExceptionCreator implements ThrowableCreator {
		@Override
		public Throwable create(ThrowableInfo info) {
			return new IllformedLocaleException(info.getMessage());
		}
	}

	private static class MissingResourceExceptionCreator implements ThrowableCreator {

		private static final PropertyKey<String> CLASS_NAME = new PropertyKey<>(String.class, "className");
		private static final PropertyKey<String> KEY = new PropertyKey<>(String.class, "key");

		@Override
		public Throwable create(ThrowableInfo info) {
			var message = info.getMessage();
			var className = info.getProperty(CLASS_NAME);
			var key = info.getProperty(KEY);
			return new MissingResourceException(message, className, key);
		}
	}

	private static class ComparisonFailureCreator implements ThrowableCreator {

		private static final int MAX_CONTEXT_LENGTH = 20;

		private static final PropertyKey<String> EXPECTED = new PropertyKey<>(String.class, "expected");
		private static final PropertyKey<String> ACTUAL = new PropertyKey<>(String.class, "actual");

		@Override
		public Throwable create(ThrowableInfo info) {
			var message = info.getMessage();
			var expected = info.getProperty(EXPECTED);
			var actual = info.getProperty(ACTUAL);
			String withoutMessage = new ComparisonCompactor(MAX_CONTEXT_LENGTH, expected, actual).compact("");
			var start = SanitizationUtils.removeSuffixMatching(message, withoutMessage);
			if (start == null)
				message = "";
			else
				message = start.trim();
			if (ComparisonFailure.class.isAssignableFrom(info.getType()))
				return new ComparisonFailure(message, expected, actual);
			return new org.junit.ComparisonFailure(message, expected, actual);
		}
	}

	private static class ParameterizedAssertionErrorCreator implements ThrowableCreator {

		@Override
		public Throwable create(ThrowableInfo info) {
			var targetException = info.getCause();
			var message = info.getMessage();
			var methodName = message;
			var params = new Object[0];
			try {
				var parts = message.substring(0, message.length() - 1).split("\\(", 2);
				methodName = parts[0];
				params = parts[1].split(",");
			} catch (@SuppressWarnings("unused") RuntimeException e) {
				// ignore
			}
			return new ParameterizedAssertionError(targetException, methodName, params);
		}
	}
}
