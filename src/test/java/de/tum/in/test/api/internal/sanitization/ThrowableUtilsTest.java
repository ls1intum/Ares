package de.tum.in.test.api.internal.sanitization;

import static de.tum.in.test.api.internal.sanitization.ThrowableUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.*;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

class ThrowableUtilsTest {

	private static final Map<Class<?>, List<Object>> TEST_VALUES = Map.ofEntries( //
			typeEntry(char.class, 'a', 'b', 'c'), //
			typeEntry(int.class, 42, 43, 44), //
			typeEntry(boolean.class, true, false), //
			typeEntry(long.class, 123L, 124L, 125L), //
			typeEntry(float.class, 1234.5f, 1235.0f, 1236.0f), //
			typeEntry(double.class, 1234.5, 1235.0, 1236.0), //
			typeEntry(Object.class, new Object(), new Object(), new Object()), //
			typeEntry(Class.class, Class.class, Object.class, String.class), //
			typeEntry(String.class, "penguin1", "penguin2", "penguin3", "penguin4"), //
			typeEntry(CharSequence.class, "penguinA", "penguinB"), //
			typeEntry(Throwable.class, new Exception(), new Exception(), new Exception()), //
			typeEntry(IOException.class, new IOException()), //
			typeEntry(TimeUnit.class, TimeUnit.DAYS), //
			typeEntry(Object[].class, new Object[] { "a" }, new Object[] { "b" }), //
			typeEntry(Method.class, ReflectionSupport.findMethod(String.class, "length").orElseThrow()) //
	);

	private static final Set<String> FALSE_POSITIVES = Set.of( //
			"junit.framework.AssertionFailedError", // deviation for null message is not an issue
			"java.util.IllformedLocaleException", // we don't need the index attribute itself
			"java.awt.HeadlessException" // CI systems don't like this, but the constructor works
	);

	private static final Set<Class<?>> SAFE_PROPERTY_TYPES = Set.of(Throwable.class, Throwable[].class);

	@Test
	void testConstructorInstantiation() {
		var duplicationFailures = ThrowableSets.SAFE_TYPES.stream().filter(type -> {
			String typeName = type.getName();
			if (FALSE_POSITIVES.contains(typeName))
				return false;
			if (SafeTypeThrowableSanitizer.NON_DUPLICATABLE_SAFE_TYPES.contains(typeName))
				return false;
			if (SafeTypeThrowableSanitizer.SPECIFIC_CREATORS.containsKey(typeName))
				return false;

			var preferredConstructor = findPreferredConstructor(type);
			return !validate(type, preferredConstructor);
		}).sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList());
		assertThat(duplicationFailures)
				.as("the default Throwable duplication works for all SAFE_TYPE classes that are not specially handeled")
				.isEmpty();
	}

	@Test
	void testSpecificCreatorInstantiation() {
		var duplicationFailures = ThrowableSets.SAFE_TYPES.stream().filter(type -> {
			String typeName = type.getName();
			if (FALSE_POSITIVES.contains(typeName))
				return false;
			var specificCreator = SafeTypeThrowableSanitizer.SPECIFIC_CREATORS.get(typeName);
			if (specificCreator == null)
				return false;

			return !validate(type, specificCreator);
		}).sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList());
		assertThat(duplicationFailures)
				.as("the Throwable duplication works for all SAFE_TYPE classes that have a designated creator")
				.isEmpty();
	}

	/**
	 * This tests that {@link ThrowableUtils#PROPERTY_SANITIZER} can sanitize
	 * everything. Adjust the linked sanitizer if this fails, as well as the safe
	 * property type set {@link #SAFE_PROPERTY_TYPES}.
	 */
	@Test
	void checkProperties() {
		Set<Class<?>> potentiallyUnsafeProperties = ThrowableSets.SAFE_TYPES.stream()
				.map(ThrowableUtils::getPropertiesWithMethods).flatMap(Set::stream).map(Map.Entry::getValue)
				.map(Method::getReturnType).distinct().filter(type -> {
					Class<?> containedType;
					if (type.isArray())
						containedType = Stream.<Class<?>>iterate(type, Class::getComponentType)
								.takeWhile(Objects::nonNull).reduce(null, (a, b) -> b);
					else
						containedType = type;
					if (containedType.isPrimitive())
						return false;
					if (Modifier.isFinal(containedType.getModifiers()))
						return false;
					if (SAFE_PROPERTY_TYPES.stream().anyMatch(safeType -> safeType.isAssignableFrom(containedType)))
						return false;
					return true;
				}).collect(Collectors.toSet());
		assertThat(potentiallyUnsafeProperties).as("property types are all safe or sanitizable").isEmpty();
	}

	static boolean validate(Class<? extends Throwable> type, Constructor<? extends Throwable> constructor) {
		if (constructor == null)
			return false;
		return validate(type, getThrowableCreatorFor(type, constructor));
	}

	static boolean validate(Class<? extends Throwable> type, ThrowableCreator creator) {
		if (creator == null)
			return false;
		@SuppressWarnings("unchecked")
		var allConstructors = (Constructor<? extends Throwable>[]) type.getConstructors();
		var propertiesWithMethods = getRelevantPropertiesWithMethods(type, IGNORE_PROPERTIES);
		for (var constructorToCheck : allConstructors)
			try {
				var arguments = provideArguments(constructorToCheck, TEST_VALUES);
				var originalInstance = constructorToCheck.newInstance(arguments);
				var originalProperties = retrievePropertyValues(originalInstance, propertiesWithMethods);

				var copyInstance = creator.create(ThrowableInfo.of(type, originalProperties));
				copyThrowableAttributesIfNecessary(originalInstance, copyInstance);
				var propertiesOfCopy = retrievePropertyValues(copyInstance, propertiesWithMethods);

				var propertiesEqual = deepEquals(propertiesOfCopy, originalProperties);
				var toStringEqual = copyInstance.toString().equals(originalInstance.toString());
				var everythingOk = propertiesEqual && toStringEqual;
				if (!everythingOk)
					return false;
			} catch (@SuppressWarnings("unused") Exception e) {
				return false;
			}
		return true;
	}

	private static void copyThrowableAttributesIfNecessary(Throwable from, Throwable to) {
		if (to.getCause() != from.getCause())
			to.initCause(from.getCause());
		to.setStackTrace(from.getStackTrace());
	}

	@SafeVarargs
	static <T> Entry<Class<T>, List<Object>> typeEntry(Class<T> clazz, T... instances) {
		return Map.entry(clazz, List.of(instances));
	}

	static boolean deepEquals(Map<String, Object> m1, Map<String, Object> m2) {
		if (!m1.keySet().equals(m2.keySet()))
			return false;
		for (var entry : m1.entrySet()) {
			var key = entry.getKey();
			var v1 = entry.getValue();
			var v2 = m2.get(key);
			if (v1 == v2)
				continue;
			if (v1 == null || v2 == null)
				return false;
			if (v1.getClass().isArray() && v2.getClass().isArray() && Arrays.equals((Object[]) v1, (Object[]) v2))
				continue;
			if (!v1.equals(v2))
				return false;
		}
		return true;
	}
}
