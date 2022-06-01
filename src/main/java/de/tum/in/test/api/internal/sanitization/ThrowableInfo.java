package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invoke;
import static de.tum.in.test.api.internal.sanitization.ThrowableUtils.*;

import java.util.*;
import java.util.function.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Contains information about a Throwable without the Throwable instance itself.
 * This allows the values to be modified by using the setters. Getters of this
 * class will return a copy or unmodifiable version.
 * <p>
 * A {@link ThrowableInfo} can be sanitized, which will guarantee that all
 * information contained is safe to use. Sanitization can be performed by the
 * methods {@link #sanitize()} and {@link #sanitize(BiFunction)}. The
 * sanitization status can be checked by using {@link #isSanitized()}. Some
 * setters of this class (all that involve collections or arrays) reset the
 * sanitization status.
 * <p>
 * The default sanitization status is "not sanitized". This is also the one in
 * which instances of this class are if they are created by one of the static
 * factory methods.
 */
@API(status = Status.INTERNAL)
public final class ThrowableInfo {

	private final Class<? extends Throwable> type;
	private String message;
	private Throwable cause;
	private StackTraceElement[] stackTrace;
	private Throwable[] suppressed;
	private Map<String, Object> additionalProperties;

	private boolean sanitized;

	private ThrowableInfo(Class<? extends Throwable> type, String message, Throwable cause,
			StackTraceElement[] stackTrace, Throwable[] suppressed, Map<String, Object> additionalProperties) {
		this.type = type;
		this.message = message;
		this.cause = cause;
		this.stackTrace = stackTrace;
		this.suppressed = suppressed;
		this.additionalProperties = additionalProperties;
	}

	public Class<? extends Throwable> getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
		sanitized = false;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	public Throwable[] getSuppressed() {
		return suppressed.clone();
	}

	public void setSuppressed(Throwable[] suppressed) {
		this.suppressed = suppressed;
		sanitized = false;
	}

	public Map<String, Object> getAdditionalProperties() {
		return Collections.unmodifiableMap(additionalProperties);
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
		sanitized = false;
	}

	public boolean isSanitized() {
		return sanitized;
	}

	/**
	 * Does a best effort attempt to sanitize all regular {@link Throwable}
	 * properties. Is this {@link ThrowableInfo} has additional properties, this
	 * method will sanitize only the regular properties, but {@link #isSanitized()}
	 * will stay <code>false</code>. if this is a problem,
	 * {@link #sanitize(BiFunction)} should be used.
	 *
	 * @return this
	 */
	public ThrowableInfo sanitize() {
		cause = ThrowableSanitizer.sanitize(cause);
		// defensive copy
		suppressed = suppressed.clone();
		for (var i = 0; i < suppressed.length; i++)
			suppressed[i] = ThrowableSanitizer.sanitize(suppressed[i]);
		sanitized = additionalProperties.isEmpty();
		return this;
	}

	/**
	 * Sanitizes all information in this {@link ThrowableInfo}.
	 *
	 * @param additionalPropertySanitizer a {@link BiFunction} that needs to be able
	 *                                    to sanitize all additional properties. The
	 *                                    first parameter is the property name, the
	 *                                    second is the value. A sanitized version
	 *                                    of the value should then be returned.
	 * @return this
	 */
	public ThrowableInfo sanitize(BiFunction<String, Object, Object> additionalPropertySanitizer) {
		sanitize();
		// defensive copy
		additionalProperties = new HashMap<>(additionalProperties);
		// we have to assume that additionalPropertySanitizer is properly working
		additionalProperties.replaceAll(additionalPropertySanitizer);
		sanitized = true;
		return this;
	}

	public Map<String, Object> toPropertyMap() {
		HashMap<String, Object> properties = new HashMap<>(additionalProperties);
		properties.put(MESSAGE, message);
		properties.put(CAUSE, cause);
		properties.put(STACK_TRACE, stackTrace);
		properties.put(SUPPRESSED, suppressed);
		return properties;
	}

	public <T> T getProperty(PropertyKey<T> key) {
		return key.cast(additionalProperties.get(key.name()));
	}

	public <T> void setProperty(PropertyKey<T> key, T newValue) {
		additionalProperties.put(key.name(), key.cast(newValue));
	}

	public static ThrowableInfo of(Class<? extends Throwable> type, String message, Throwable cause,
			StackTraceElement[] stackTrace, Throwable[] suppressed, Map<String, Object> additionalProperties) {
		return new ThrowableInfo(type, message, cause, stackTrace.clone(), suppressed.clone(),
				new HashMap<>(additionalProperties));
	}

	public static ThrowableInfo of(Class<? extends Throwable> type, Map<String, Object> properties) {
		var message = (String) properties.get(MESSAGE);
		var cause = (Throwable) properties.get(CAUSE);
		var stackTrace = (StackTraceElement[]) properties.get(STACK_TRACE);
		var suppressed = (Throwable[]) properties.get(SUPPRESSED);
		var additionalProperties = new HashMap<>(properties);
		for (var throwableProperty : THROWABLE_PROPERTIES)
			additionalProperties.remove(throwableProperty);
		return new ThrowableInfo(type, message, cause, stackTrace.clone(), suppressed.clone(), additionalProperties);
	}

	public static ThrowableInfo getEssentialInfosSafeFrom(Throwable source) {
		String message = invoke(source::getMessage);
		Throwable cause = invoke(source::getCause);
		StackTraceElement[] stackTrace = invoke(source::getStackTrace);
		Throwable[] suppressed = source.getSuppressed(); // OK: final method
		return ThrowableInfo.of(source.getClass(), message, cause, stackTrace, suppressed, Map.of());
	}

	public static class PropertyKey<T> {

		private final Class<T> type;
		private final String name;
		private final UnaryOperator<T> sanitizer;

		public PropertyKey(Class<T> type, String name, UnaryOperator<T> sanitizer) {
			this.type = Objects.requireNonNull(type);
			this.name = Objects.requireNonNull(name);
			this.sanitizer = Objects.requireNonNull(sanitizer);
		}

		public PropertyKey(Class<T> type, String name) {
			this(type, name, UnaryOperator.identity());
		}

		public final String name() {
			return name;
		}

		public final Class<T> type() {
			return type;
		}

		public T sanitize(T value) {
			return sanitizer.apply(value);
		}

		@SuppressWarnings("unchecked")
		final T cast(Object value) {
			if (value == null && type.isPrimitive())
				throw new NullPointerException("cannot cast null to primitive: " + type); //$NON-NLS-1$
			if (byte.class.equals(type))
				return (T) Byte.class.cast(value);
			if (short.class.equals(type))
				return (T) Short.class.cast(value);
			if (char.class.equals(type))
				return (T) Character.class.cast(value);
			if (int.class.equals(type))
				return (T) Integer.class.cast(value);
			if (long.class.equals(type))
				return (T) Long.class.cast(value);
			if (float.class.equals(type))
				return (T) Float.class.cast(value);
			if (double.class.equals(type))
				return (T) Double.class.cast(value);
			return type.cast(value);
		}
	}
}
