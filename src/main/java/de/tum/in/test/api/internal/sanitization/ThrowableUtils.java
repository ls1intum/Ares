package de.tum.in.test.api.internal.sanitization;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.stream.*;

import org.junit.platform.commons.support.ReflectionSupport;

final class ThrowableUtils {

	static final String MESSAGE = "message"; //$NON-NLS-1$
	static final String CAUSE = "cause"; //$NON-NLS-1$
	static final String STACK_TRACE = "stackTrace"; //$NON-NLS-1$
	static final String SUPPRESSED = "suppressed"; //$NON-NLS-1$

	static final Set<String> IGNORE_PROPERTIES = Set.of("class", "toString", "hashCode", "fillInStackTrace", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"localizedMessage"); //$NON-NLS-1$

	static final Set<String> THROWABLE_PROPERTIES = Stream
			.concat(IGNORE_PROPERTIES.stream(), Stream.of(MESSAGE, STACK_TRACE, CAUSE, SUPPRESSED))
			.collect(Collectors.toUnmodifiableSet());

	static final BiFunction<String, Object, Object> PROPERTY_SANITIZER = (name, value) -> {
		if (value == null)
			return value;
		if (value instanceof Throwable)
			return ThrowableSanitizer.sanitize((Throwable) value);
		if (value instanceof Throwable[])
			return Arrays.stream((Throwable[]) value).map(ThrowableSanitizer::sanitize).toArray(Throwable[]::new);
		return value;
	};

	private static final Comparator<Map.Entry<String, Method>> ORDER_PROPERTIES_BY_RELEVANCE = Map.Entry
			.comparingByKey((firstProperty, secondProperty) -> {
				boolean firstIsLessImportant = THROWABLE_PROPERTIES.contains(firstProperty);
				boolean secondIsLessImportant = THROWABLE_PROPERTIES.contains(secondProperty);
				if (firstIsLessImportant && !secondIsLessImportant)
					return 1;
				if (secondIsLessImportant && !firstIsLessImportant)
					return -1;
				return firstProperty.compareTo(secondProperty);
			});

	private ThrowableUtils() {
	}

	static ThrowableCreator getThrowableCreatorFor(Class<? extends Throwable> type) {
		var preferredConstructor = findPreferredConstructor(type);
		return getThrowableCreatorFor(type, preferredConstructor);
	}

	static ThrowableCreator getThrowableCreatorFor(Class<? extends Throwable> type,
			Constructor<? extends Throwable> constructor) {
		var propertiesWithMethods = getRelevantPropertiesWithMethods(type, IGNORE_PROPERTIES);
		return info -> {
			var originalProperties = info.toPropertyMap();
			var originalPropertyValuesByType = getValuesByPropertyType(
					propertiesWithMethods.stream().sorted(ORDER_PROPERTIES_BY_RELEVANCE), originalProperties);
			var argumentsForCopy = provideArguments(constructor, originalPropertyValuesByType);
			try {
				return constructor.newInstance(argumentsForCopy);
			} catch (InvocationTargetException e) {
				throw new SanitizationException(type, e.getCause());
			} catch (Exception e) {
				throw new SanitizationException(type, e);
			}
		};
	}

	static <T extends Throwable> Constructor<T> findPreferredConstructor(Class<T> type) {
		@SuppressWarnings("unchecked")
		var allConstructors = (Constructor<T>[]) type.getConstructors();
		if (allConstructors.length == 0)
			return null;
		var classPropertyTypeCount = getRelevantPropertiesWithMethods(type, IGNORE_PROPERTIES).stream()
				.collect(Collectors.groupingBy(property -> property.getValue().getReturnType(), Collectors.counting()));
		return Stream.of(allConstructors)
				.filter(constructor -> isSatisfiableByProperties(constructor, classPropertyTypeCount))
				.sorted(getConstructorPreferenceOrder()).findFirst().orElse(allConstructors[0]);
	}

	static boolean isSatisfiableByProperties(Constructor<?> constructor,
			Map<? extends Class<?>, Long> propertyTypeCount) {
		var parameterTypeCount = Stream.of(constructor.getParameterTypes())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		return parameterTypeCount.entrySet().stream()
				.mapToLong(e -> propertyTypeCount.getOrDefault(e.getKey(), 0L) - e.getValue()).min().orElse(0) >= 0;
	}

	static Comparator<Constructor<?>> getConstructorPreferenceOrder() { // NOSONAR
		return Comparator.<Constructor<?>>comparingInt(Constructor::getParameterCount)
				.thenComparingLong(x -> Stream.of(x.getParameterTypes()).filter(String.class::equals).count())
				.reversed();
	}

	static Set<Entry<String, Method>> getPropertiesWithMethods(Class<?> type) {
		return getRelevantPropertiesWithMethods(type, Set.of());
	}

	static Set<Entry<String, Method>> getRelevantPropertiesWithMethods(Class<?> type, Set<String> namesToIgnore) {
		return Stream.of(type.getMethods()).map(method -> {
			var propertyName = extractProperty(method);
			if (propertyName == null || namesToIgnore.contains(propertyName))
				return null;
			return Map.entry(propertyName, method);
		}).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
	}

	static Map<String, Object> retrievePropertyValues(Object instance, Set<Entry<String, Method>> properties) {
		return properties.stream()
				.collect(Collectors.groupingBy(Map.Entry::getKey,
						Collectors.mapping(property -> ReflectionSupport.invokeMethod(property.getValue(), instance),
								Collectors.reducing(null, (a, b) -> a != null ? a : b))));
	}

	static Map<Class<?>, List<Object>> getValuesByPropertyType(Stream<Entry<String, Method>> propertiesWithMethods,
			Map<String, Object> concretePropertyValues) {
		return propertiesWithMethods.collect(Collectors.groupingBy(property -> property.getValue().getReturnType(),
				Collectors.mapping(property -> concretePropertyValues.get(property.getKey()), Collectors.toList())));
	}

	static String extractProperty(Method method) {
		if (method.getParameterCount() > 0)
			return null;
		if (method.isBridge() || method.isSynthetic())
			return null;
		if (void.class.equals(method.getReturnType()))
			return null;
		String name = method.getName();
		if (name.startsWith("get") && name.length() > 3 && Character.isUpperCase(name.charAt(3))) { //$NON-NLS-1$
			var propertyName = new StringBuilder(name);
			propertyName.delete(0, 3);
			propertyName.setCharAt(0, Character.toLowerCase(propertyName.charAt(0)));
			name = propertyName.toString();
		}
		return name;
	}

	static Object[] provideArguments(Constructor<?> constructor, Map<Class<?>, List<Object>> valueSource) {
		var used = new HashSet<>();
		return Stream.of(constructor.getParameterTypes()).map(argType -> {
			var valueList = valueSource.get(argType);
			if (valueList != null)
				for (var value : valueList)
					if (used.add(value))
						return value;
			return null;
		}).toArray();
	}
}
