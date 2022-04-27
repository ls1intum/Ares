package de.tum.in.test.api.util;

import java.lang.reflect.Method;

/**
 * Provides utility methods to access public, (package) private and protected
 * (inherited) members of classes.
 */
class ClassMemberAccessor {
	private ClassMemberAccessor() {
	}

	/**
	 * Retrieve a method with arguments of a given class by its name.
	 *
	 * @param declaringClass The class that declares this method.
	 * @param methodName     The name of this method.
	 * @param findPrivate    True, if this method should search for (package)
	 *                       private or protected (inherited) methods.
	 * @param parameterTypes The parameter types of this method. Do not include if
	 *                       the method has no parameters.
	 * @return The wanted method.
	 * @throws NoSuchMethodException Thrown if the specified method cannot be found.
	 */
	static Method getMethod(Class<?> declaringClass, String methodName, boolean findPrivate, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		if (!findPrivate) {
			return declaringClass.getMethod(methodName, parameterTypes);
		} else {
			return getPrivateMethod(declaringClass, methodName, parameterTypes);
		}
	}

	/**
	 * Retrieve a method with arguments of a given class by its name.
	 * <p>
	 * Also searches in private and inherited (protected) methods.
	 *
	 * @param declaringClass The class that declares this method.
	 * @param methodName     The name of this method.
	 * @param parameterTypes The parameter types of this method. Do not include if
	 *                       the method has no parameters.
	 * @return The wanted method.
	 * @throws NoSuchMethodException Thrown if the specified method cannot be found.
	 */
	private static Method getPrivateMethod(Class<?> declaringClass, String methodName, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		try {
			return declaringClass.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException nmse) {
			Class<?> superClass = declaringClass.getSuperclass();
			if (superClass == null) {
				throw nmse;
			} else {
				return getPrivateMethod(superClass, methodName, parameterTypes);
			}
		}
	}
}
