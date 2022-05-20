package de.tum.in.test.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides utility methods to search for declared and inherited public,
 * protected, and (package) private members of classes.
 */
class ClassMemberAccessor {
	private ClassMemberAccessor() {
	}

	/**
	 * Retrieve a method with arguments of a given class by its name.
	 * <p>
	 * Also recursively searches for the method in all superclasses and implemented
	 * interfaces.
	 *
	 * @param declaringClass The class that declares or inherits the method.
	 * @param methodName     The name of the method.
	 * @param findPrivate    True, if this method should search for (package)
	 *                       private or protected (inherited) methods.
	 * @param parameterTypes The parameter types of this method.
	 * @return The wanted method.
	 * @throws NoSuchMethodException Thrown if the specified method cannot be found.
	 */
	static Method getMethod(Class<?> declaringClass, String methodName, boolean findPrivate, Class<?>[] parameterTypes)
			throws NoSuchMethodException {
		return getClassHierarchy(declaringClass).flatMap(c -> {
			try {
				if (c.equals(declaringClass) || findPrivate) {
					// Also search for declared methods in the own class even when not explicitly
					// searching for private methods to be able to provide error messages to the
					// users that the method exists, but with the wrong visibility.
					return getInheritedMethod(declaringClass, c, methodName, parameterTypes);
				} else {
					return Stream.of(c.getMethod(methodName, parameterTypes));
				}
			} catch (NoSuchMethodException nsme) {
				return Stream.empty();
			}
		}).findFirst().orElseThrow(() -> new NoSuchMethodException(methodName));
	}

	/**
	 * Searches for a method in the target class that might be inherited from the
	 * declaring class.
	 *
	 * @param targetClass    The class in which the method should be accessible.
	 * @param declaringClass The class from which the method might be inherited.
	 * @param methodName     The name of the method.
	 * @param parameterTypes The parameter types of the method.
	 * @return A method that is accessible in the target class.
	 * @throws NoSuchMethodException Thrown if no method as specified could be found
	 *                               in either class.
	 */
	private static Stream<Method> getInheritedMethod(Class<?> targetClass, Class<?> declaringClass, String methodName,
			Class<?>[] parameterTypes) throws NoSuchMethodException {
		Method method = declaringClass.getDeclaredMethod(methodName, parameterTypes);
		if (isInheritable(targetClass, declaringClass, method.getModifiers())) {
			return Stream.of(method);
		} else {
			return Stream.empty();
		}
	}

	/**
	 * Retrieve a field of a given class by its name.
	 * <p>
	 * Also recursively searches for the field in all superclasses and implemented
	 * interfaces.
	 *
	 * @param declaringClass The class that declares or inherits the field.
	 * @param fieldName      The name of the attribute.
	 * @param findPrivate    True, if this method should search for (package)
	 *                       private or protected (inherited) attributes.
	 * @return The wanted field.
	 * @throws NoSuchFieldException Thrown if the specified field cannot be found.
	 */
	static Field getAttribute(Class<?> declaringClass, String fieldName, boolean findPrivate)
			throws NoSuchFieldException {
		return getClassHierarchy(declaringClass).flatMap(c -> {
			try {
				if (c.equals(declaringClass) || findPrivate) {
					// Also search for declared fields in the own class even when not explicitly
					// searching for private fields to be able to provide error messages to the
					// users that the field exists, but with the wrong visibility.
					return getInheritedField(declaringClass, c, fieldName).stream();
				} else {
					return Stream.of(c.getField(fieldName));
				}
			} catch (NoSuchFieldException nsfe) {
				return Stream.empty();
			}
		}).findFirst().orElseThrow(() -> new NoSuchFieldException(fieldName));
	}

	/**
	 * Searches for a field in the target class that might be inherited from the
	 * declaring class.
	 *
	 * @param targetClass    The class in which the field should be accessible.
	 * @param declaringClass The class from which the field might be inherited.
	 * @param fieldName      The name of the attribute.
	 * @return A field that is accessible in the target class.
	 * @throws NoSuchFieldException Thrown if no field with the given name could be
	 *                              found in either class.
	 */
	private static Optional<Field> getInheritedField(Class<?> targetClass, Class<?> declaringClass, String fieldName)
			throws NoSuchFieldException {
		Field field = declaringClass.getDeclaredField(fieldName);
		if (isInheritable(targetClass, declaringClass, field.getModifiers())) {
			return Optional.of(field);
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Searches for all classes and interfaces the given class is inheriting from.
	 * <p>
	 * The given class itself is part of the result. Performs a depth-first search
	 * starting with classes, then interfaces.
	 *
	 * @param clazz The class for which the inheritance tree should be traversed.
	 * @return A stream of the given class and all superclasses and interfaces it
	 *         inherits from.
	 */
	private static Stream<Class<?>> getClassHierarchy(Class<?> clazz) {
		Stream<Class<?>> directSuperclasses = Stream
				.concat(Stream.of(clazz.getSuperclass()), Arrays.stream(clazz.getInterfaces()))
				.filter(Objects::nonNull);
		return Stream.concat(Stream.of(clazz), directSuperclasses.flatMap(ClassMemberAccessor::getClassHierarchy));
	}

	/**
	 * Checks if a field or method member of a class is accessible in a subclass.
	 *
	 * @param targetClass     The class in which the class member of
	 *                        {@code declaredInClass} should be accessible. Assumes
	 *                        that {@code declaredInClass} is a superclass of
	 *                        {@code targetClass}.
	 * @param declaredInClass The class in which the member was declared.
	 * @param modifier        The modifiers of the member.
	 * @return True, if the class member of {@code declaredInClass} is accessible in
	 *         its subclass {@code targetClass}.
	 */
	private static boolean isInheritable(Class<?> targetClass, Class<?> declaredInClass, int modifier) {
		boolean isInheritable = Modifier.isProtected(modifier) || Modifier.isPublic(modifier);
		boolean isInheritableInPackage = !Modifier.isPrivate(modifier)
				&& targetClass.getPackage().equals(declaredInClass.getPackage());
		return targetClass.equals(declaredInClass) || isInheritable || isInheritableInPackage;
	}
}
