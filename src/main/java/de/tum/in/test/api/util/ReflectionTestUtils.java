package de.tum.in.test.api.util;

import static de.tum.in.test.api.localization.Messages.*;

import java.lang.reflect.*;
import java.util.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.opentest4j.AssertionFailedError;

/**
 * This class serves as an API to Java Reflection to facilitate various
 * operations that are performed regularly in the functional tests. Facilitation
 * mainly means automatically handling all the various errors Reflection is able
 * to intercept through exceptions and delivering appropriate feedback to these
 * errors.
 * <p>
 * The operations include:
 * <ul>
 * <li>Retrieving a class given its qualified name,</li>
 * <li>Instantiating an object of a given class and with given constructor
 * arguments,</li>
 * <li>Retrieving the value of an attribute from an object given the attribute's
 * name,</li>
 * <li>Retrieving a method from a class given the method's name and parameter
 * types,</li>
 * <li>Invoking a method with certain parameter instances and retrieving its
 * return type.</li>
 * </ul>
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 6.0 (2022-05-27)
 */
@API(status = Status.STABLE)
public final class ReflectionTestUtils {

	private ReflectionTestUtils() {
	}

	/**
	 * Retrieve the actual class by its qualified name.
	 *
	 * @param qualifiedClassName The qualified name of the class that needs to get
	 *                           retrieved (package.classname)
	 * @return The wanted class object.
	 */
	public static Class<?> getClazz(String qualifiedClassName) {
		// The simple class name is the last part of the qualified class name.
		var qualifiedClassNameSegments = qualifiedClassName.split("\\."); //$NON-NLS-1$
		var className = qualifiedClassNameSegments[qualifiedClassNameSegments.length - 1];
		try {
			return Class.forName(qualifiedClassName);
		} catch (@SuppressWarnings("unused") ClassNotFoundException e) {
			throw localizedFailure("reflection_test_utils.class_not_found", className); //$NON-NLS-1$
		} catch (@SuppressWarnings("unused") ExceptionInInitializerError e) {
			throw localizedFailure("reflection_test_utils.class_initialization", className); //$NON-NLS-1$
		}
	}

	/**
	 * Instantiate an object of a class by its qualified name and the constructor
	 * arguments, if applicable.
	 * <p>
	 * This method does not support passing null, passing subclasses of the
	 * parameter types or invoking constructors with primitive parameters. Use
	 * {@link #newInstance(Constructor, Object...)} for that.
	 *
	 * @param qualifiedClassName The qualified name of the class that needs to get
	 *                           retrieved (package.classname)
	 * @param constructorArgs    Parameter instances of the constructor of the
	 *                           class, that it should use to get instantiated with.
	 * @return The instance of this class.
	 * @see #newInstance(Class, Object...)
	 */
	public static Object newInstance(String qualifiedClassName, Object... constructorArgs) {
		return newInstance(getClazz(qualifiedClassName), constructorArgs);
	}

	/**
	 * Instantiate an object of a class by its qualified name and the constructor
	 * arguments, if applicable.
	 * <p>
	 * This method does not support passing null, passing subclasses of the
	 * parameter types or invoking constructors with primitive parameters. Use
	 * {@link #newInstanceFromNonPublicConstructor(Constructor, Object...)} for
	 * that.
	 * <p>
	 * Forces the access to package-private, {@code protected}, and {@code private}
	 * constructors. Use {@link #newInstance(String, Object...)} if you do not
	 * require this functionality.
	 *
	 * @param qualifiedClassName The qualified name of the class that needs to get
	 *                           retrieved (package.classname)
	 * @param constructorArgs    Parameter instances of the constructor of the class
	 *                           that it should use to get instantiated with.
	 * @return The instance of this class.
	 * @see #newInstance(Class, Object...)
	 */
	public static Object newInstanceFromNonPublicConstructor(String qualifiedClassName, Object... constructorArgs) {
		return newInstanceFromNonPublicConstructor(getClazz(qualifiedClassName), constructorArgs);
	}

	/**
	 * Instantiate an object of a given class using the given constructor arguments,
	 * if applicable.
	 * <p>
	 * This method does not support passing null, passing subclasses of the
	 * parameter types or invoking constructors with primitive parameters. Use
	 * {@link #newInstance(Constructor, Object...)} for that.
	 *
	 * @param clazz           The class for which a new instance should be created
	 * @param constructorArgs Parameter instances of the constructor of the class,
	 *                        that it should use to get instantiated with.
	 * @return The instance of this class.
	 */
	public static Object newInstance(Class<?> clazz, Object... constructorArgs) {
		return newInstanceAccessible(clazz, false, constructorArgs);
	}

	/**
	 * Instantiate an object of a given class using the given constructor arguments,
	 * if applicable.
	 * <p>
	 * This method does not support passing null, passing subclasses of the
	 * parameter types or invoking constructors with primitive parameters. Use
	 * {@link #newInstance(Constructor, Object[])} for that.
	 * <p>
	 * Forces the access to package-private, {@code protected}, and {@code private}
	 * constructors. Use {@link #newInstance(Class, Object...)} if you do not
	 * require this functionality.
	 *
	 * @param clazz           The class for which a new instance should be created
	 * @param constructorArgs Parameter instances of the constructor of the class,
	 *                        that it should use to get instantiated with.
	 * @return The instance of this class.
	 */
	public static Object newInstanceFromNonPublicConstructor(Class<?> clazz, Object... constructorArgs) {
		return newInstanceAccessible(clazz, true, constructorArgs);
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable.
	 *
	 * @param constructor     The actual constructor that should be used for
	 *                        creating a new instance of the object
	 * @param constructorArgs Parameter instances of the constructor of the class,
	 *                        that it should use to get instantiated with.
	 * @return The instance of this class.
	 */
	public static Object newInstance(Constructor<?> constructor, Object... constructorArgs) {
		return newInstanceAccessible(constructor, false, constructorArgs);
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable.
	 * <p>
	 * Forces the access to package-private, {@code protected}, and {@code private}
	 * constructors. Use {@link #newInstance(Constructor, Object...)} if you do not
	 * require this functionality.
	 *
	 * @param constructor     The actual constructor that should be used for
	 *                        creating a new instance of the object
	 * @param constructorArgs Parameter instances of the constructor of the class,
	 *                        that it should use to get instantiated with.
	 * @return The instance of this class.
	 */
	public static Object newInstanceFromNonPublicConstructor(Constructor<?> constructor, Object... constructorArgs) {
		return newInstanceAccessible(constructor, true, constructorArgs);
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable.
	 *
	 * @param clazz           The type of the class that should be instantiated.
	 * @param forceAccess     True, if access to a (package) private or protected
	 *                        constructor should be forced. Might fail with an
	 *                        {@link IllegalAccessException} otherwise.
	 * @param constructorArgs Parameter instances the constructor should be called
	 *                        with.
	 * @return An instance of the given class type.
	 */
	private static Object newInstanceAccessible(Class<?> clazz, boolean forceAccess, Object[] constructorArgs) {
		var constructorArgTypes = getParameterTypes(constructorArgs, "reflection_test_utils.constructor_null_args", //$NON-NLS-1$
				clazz.getSimpleName());
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(constructorArgTypes);
			return newInstanceAccessible(constructor, forceAccess, constructorArgs);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			throw localizedFailure("reflection_test_utils.constructor_not_found_args", clazz.getSimpleName(), //$NON-NLS-1$
					getParameterTypesAsString(constructorArgTypes));
		}
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable.
	 *
	 * @param constructor     The constructor that should be used to instantiate an
	 *                        object.
	 * @param forceAccess     True, if access to a (package) private or protected
	 *                        constructor should be forced. Might fail with an
	 *                        {@link IllegalAccessException} otherwise.
	 * @param constructorArgs Parameter instances the constructor should be called
	 *                        with.
	 * @return The object created by calling the given constructor.
	 */
	private static Object newInstanceAccessible(Constructor<?> constructor, boolean forceAccess,
			Object[] constructorArgs) {
		try {
			return newInstanceRethrowingAccessible(constructor, forceAccess, constructorArgs);
		} catch (AssertionFailedError e) {
			throw e;
		} catch (@SuppressWarnings("unused") Throwable e) {
			throw localizedFailure("reflection_test_utils.constructor_internal_exception", //$NON-NLS-1$
					constructor.getDeclaringClass().getSimpleName(), constructorArgs.length);
		}
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable, and re-throw an exception if one occurs
	 * during the method execution.
	 *
	 * @param constructor     The actual constructor that should be used for
	 *                        creating a new instance of the object
	 * @param constructorArgs Parameter instances of the constructor of the class,
	 *                        that it should use to get instantiated with.
	 * @return The instance of this class.
	 * @throws Throwable the exception that was caught and which will be re-thrown
	 */
	public static Object newInstanceRethrowing(Constructor<?> constructor, Object... constructorArgs) throws Throwable {
		return newInstanceRethrowingAccessible(constructor, false, constructorArgs);
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable, and re-throw an exception if one occurs
	 * during the method execution.
	 * <p>
	 * Forces the access to package-private, {@code protected}, and {@code private}
	 * constructors. Use {@link #newInstance(Constructor, Object...)} if you do not
	 * require this functionality.
	 *
	 * @param constructor     The actual constructor that should be used for
	 *                        creating a new instance of the object
	 * @param constructorArgs Parameter instances of the constructor of the class,
	 *                        that it should use to get instantiated with.
	 * @return The instance of this class.
	 * @throws Throwable the exception that was caught and which will be re-thrown
	 */
	public static Object newInstanceFromNonPublicConstructorRethrowing(Constructor<?> constructor,
			Object... constructorArgs) throws Throwable {
		return newInstanceRethrowingAccessible(constructor, true, constructorArgs);
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable, and re-throw an exception if one occurs
	 * during the construction.
	 *
	 * @param constructor     The constructor that should be used to instantiate an
	 *                        object.
	 * @param forceAccess     True, if access to a (package) private or protected
	 *                        constructor should be forced. Might fail with an
	 *                        {@link IllegalAccessException} otherwise.
	 * @param constructorArgs Parameter instances the constructor should be called
	 *                        with.
	 * @return The object created by calling the given constructor.
	 * @throws Throwable the exception that was caught and which will be re-thrown
	 */
	private static Object newInstanceRethrowingAccessible(Constructor<?> constructor, boolean forceAccess,
			Object[] constructorArgs) throws Throwable {
		try {
			if (forceAccess) {
				constructor.setAccessible(true);
			}
			return constructor.newInstance(constructorArgs);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			throw localizedFailure("reflection_test_utils.constructor_access", //$NON-NLS-1$
					constructor.getDeclaringClass().getSimpleName(),
					getParameterTypesAsString(constructor.getParameterTypes()), getIllegalAccessSource(constructor));
		} catch (@SuppressWarnings("unused") IllegalArgumentException iae) {
			throw localizedFailure("reflection_test_utils.constructor_arguments", //$NON-NLS-1$
					constructor.getDeclaringClass().getSimpleName(),
					getParameterTypesAsString(constructor.getParameterTypes()));
		} catch (@SuppressWarnings("unused") InstantiationException ie) {
			throw localizedFailure("reflection_test_utils.constructor_abstract_class", //$NON-NLS-1$
					constructor.getDeclaringClass().getSimpleName());
		} catch (@SuppressWarnings("unused") ExceptionInInitializerError eiie) {
			throw localizedFailure("reflection_test_utils.constructor_class_init", //$NON-NLS-1$
					constructor.getDeclaringClass().getSimpleName(), constructorArgs.length);
		} catch (InvocationTargetException ite) {
			throw ite.getCause();
		}
	}

	/**
	 * Retrieve an attribute value of a given instance of a class by the attribute
	 * name.
	 *
	 * @param object        The instance of the class that contains the attribute.
	 *                      Must not be null, even for static fields.
	 * @param attributeName The name of the attribute whose value needs to get
	 *                      retrieved.
	 * @return The instance of the attribute with the wanted value.
	 */
	public static Object valueForAttribute(Object object, String attributeName) {
		return valueForAttribute(object, attributeName, false);
	}

	/**
	 * Retrieve an attribute value of a given instance of a class by the attribute
	 * name.
	 * <p>
	 * Forces access to package-private, {@code protected}, and {@code private}
	 * attributes. Use {@link #valueForAttribute(Object, String)} when reading
	 * accessible attributes.
	 *
	 * @param object        The instance of the class that contains the attribute.
	 *                      Must not be null, even for static fields.
	 * @param attributeName The name of the attribute whose value needs to get
	 *                      retrieved.
	 * @return The instance of the attribute with the wanted value.
	 */
	public static Object valueForNonPublicAttribute(Object object, String attributeName) {
		return valueForAttribute(object, attributeName, true);
	}

	/**
	 * Retrieve an attribute value of a given instance of a class by the attribute
	 * name.
	 *
	 * @param object        The object from which the attribute should be read.
	 * @param attributeName The name of the attribute that should be read.
	 * @param forceAccess   True, if access to a (package) private or protected
	 *                      attribute should be forced. Might fail with an
	 *                      {@link IllegalAccessException} otherwise.
	 * @return The value that is stored in the attribute in the given object.
	 */
	private static Object valueForAttribute(Object object, String attributeName, boolean forceAccess) {
		Field field = getAttribute(object, attributeName, forceAccess);
		try {
			return field.get(object);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			throw attributeAccessFailure(object, attributeName, field);
		}
	}

	/**
	 * Sets the attribute value of a given instance of a class by the attribute
	 * name.
	 *
	 * @param object        The instance of the class that contains the attribute.
	 *                      Must not be null, even for static fields.
	 * @param attributeName The name of the attribute whose value should be set.
	 * @param newValue      The new value that should be assigned to the attribute.
	 */
	public static void setValueOfAttribute(Object object, String attributeName, Object newValue) {
		setValueOfAttribute(object, attributeName, newValue, false);
	}

	/**
	 * Sets the attribute value of a given instance of a class by the attribute
	 * name.
	 * <p>
	 * Forces access to package-private, {@code protected}, and {@code private}
	 * attributes. Use {@link #valueForAttribute(Object, String)} when reading
	 * accessible attributes.
	 *
	 * @param object        The instance of the class that contains the attribute.
	 *                      Must not be null, even for static fields.
	 * @param attributeName The name of the attribute whose value should be set.
	 * @param newValue      The new value that should be assigned to the attribute.
	 */
	public static void setValueOfNonPublicAttribute(Object object, String attributeName, Object newValue) {
		setValueOfAttribute(object, attributeName, newValue, true);
	}

	/**
	 * Sets the attribute value of a given instance of a class by the attribute
	 * name.
	 *
	 * @param object        The object for which the attribute should be set.
	 * @param attributeName The name of the attribute that should be set.
	 * @param newValue      The new value that should be assigned to the attribute.
	 * @param forceAccess   True, if access to a (package) private or protected
	 *                      attribute should be forced. Might fail with an
	 *                      {@link IllegalAccessException} otherwise.
	 */
	private static void setValueOfAttribute(Object object, String attributeName, Object newValue, boolean forceAccess) {
		Field field = getAttribute(object, attributeName, forceAccess);
		if (Modifier.isFinal(field.getModifiers()))
			throw localizedFailure("reflection_test_utils.attribute_set_final", attributeName, //$NON-NLS-1$
					object.getClass().getSimpleName());
		try {
			field.set(object, newValue);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			throw attributeAccessFailure(object, attributeName, field);
		}
	}

	private static Field getAttribute(Object object, String attributeName, boolean forceAccess) {
		requireNonNull(object, "reflection_test_utils.attribute_null", attributeName); //$NON-NLS-1$
		Field field;
		try {
			field = ClassMemberAccessor.getField(object.getClass(), attributeName, forceAccess);
			if (forceAccess) {
				field.setAccessible(true);
			}
		} catch (@SuppressWarnings("unused") NoSuchFieldException nsfe) {
			throw localizedFailure("reflection_test_utils.attribute_not_found", attributeName, //$NON-NLS-1$
					object.getClass().getSimpleName());
		}
		return field;
	}

	private static AssertionFailedError attributeAccessFailure(Object object, String attributeName, Field field) {
		return localizedFailure("reflection_test_utils.attribute_access", attributeName, //$NON-NLS-1$
				object.getClass().getSimpleName(), getIllegalAccessSource(field));
	}

	/**
	 * Helper method that retrieves a public method with arguments of a given object
	 * by its name.
	 *
	 * @param object         instance of the class that defines the method.
	 * @param methodName     the name of the method.
	 * @param parameterTypes The parameter types of this method.
	 * @return The wanted method.
	 */
	public static Method getMethod(Object object, String methodName, Class<?>... parameterTypes) {
		requireNonNull(object, "reflection_test_utils.method_null_target", methodName); //$NON-NLS-1$
		return getMethod(object.getClass(), methodName, parameterTypes);
	}

	/**
	 * Retrieve a public method with arguments of a given class by its name.
	 *
	 * @param declaringClass The class that declares this method.
	 * @param methodName     The name of this method.
	 * @param parameterTypes The parameter types of this method.
	 * @return The wanted method.
	 */
	public static Method getMethod(Class<?> declaringClass, String methodName, Class<?>... parameterTypes) {
		return getMethodAccessible(declaringClass, methodName, false, parameterTypes);
	}

	/**
	 * Helper method that retrieves a non-public method with arguments of a given
	 * object by its name.
	 *
	 * @param object         Instance of the class that defines the method.
	 * @param methodName     The name of the method.
	 * @param parameterTypes The parameter types of this method.
	 * @return The wanted method.
	 */
	public static Method getNonPublicMethod(Object object, String methodName, Class<?>... parameterTypes) {
		requireNonNull(object, "reflection_test_utils.method_null_target", methodName); //$NON-NLS-1$
		return getNonPublicMethod(object.getClass(), methodName, parameterTypes);
	}

	/**
	 * Retrieve a non-public method with arguments of a given class by its name.
	 *
	 * @param declaringClass The class that declares this method.
	 * @param methodName     The name of this method.
	 * @param parameterTypes The parameter types of this method.
	 * @return The wanted method.
	 */
	public static Method getNonPublicMethod(Class<?> declaringClass, String methodName, Class<?>... parameterTypes) {
		return getMethodAccessible(declaringClass, methodName, true, parameterTypes);
	}

	/**
	 * Retrieve a method with arguments of a given class by its name.
	 *
	 * @param declaringClass The class that declares this method.
	 * @param methodName     The name of this method.
	 * @param findNonPublic  True, if this method should search for (package)
	 *                       private or protected methods.
	 * @param parameterTypes The parameter types of this method.
	 * @return The wanted method.
	 */
	private static Method getMethodAccessible(Class<?> declaringClass, String methodName, boolean findNonPublic,
			Class<?>[] parameterTypes) {
		try {
			return ClassMemberAccessor.getMethod(declaringClass, methodName, findNonPublic, parameterTypes);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			throw localizedFailure("reflection_test_utils.method_not_found", methodName, //$NON-NLS-1$
					describeParameters(parameterTypes), declaringClass.getSimpleName());
		} catch (@SuppressWarnings("unused") NullPointerException npe) {
			throw localizedFailure("reflection_test_utils.method_name_null", methodName, //$NON-NLS-1$
					describeParameters(parameterTypes), declaringClass.getSimpleName());
		}
	}

	/**
	 * Invoke a given method name of a given object with instances of the
	 * parameters.
	 * <p>
	 * This method does not support invoking static methods and passing null,
	 * passing subclasses of the parameter types or invoking methods with primitive
	 * parameters. Use {@link #invokeMethod(Object, Method, Object...)} for that.
	 *
	 * @param object     The instance of the class that should invoke the method.
	 *                   Must not be null, even for static methods.
	 * @param methodName The method name that has to get invoked.
	 * @param params     Parameter instances of the method.
	 * @return The return value of the method.
	 */
	public static Object invokeMethod(Object object, String methodName, Object... params) {
		return invokeMethodAccessible(object, methodName, false, params);
	}

	/**
	 * Invoke a given method name of a given object with instances of the
	 * parameters.
	 * <p>
	 * This method does not support invoking static methods and passing null,
	 * passing subclasses of the parameter types or invoking methods with primitive
	 * parameters. Use {@link #invokeNonPublicMethod(Object, Method, Object...)} for
	 * that.
	 * <p>
	 * Forces access to package-private, {@code protected}, and {@code private}
	 * methods. Use {@link #invokeMethod(Object, String, Object...)} when invoking
	 * accessible methods.
	 *
	 * @param object     The instance of the class that should invoke the method.
	 *                   Must not be null, even for static methods.
	 * @param methodName The method name that has to get invoked.
	 * @param params     Parameter instances of the method.
	 * @return The return value of the method.
	 */
	public static Object invokeNonPublicMethod(Object object, String methodName, Object... params) {
		return invokeMethodAccessible(object, methodName, true, params);
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters.
	 *
	 * @param object The instance of the class that should invoke the method. Can be
	 *               null if the method is static.
	 * @param method The method that has to get invoked.
	 * @param params Parameter instances of the method.
	 * @return The return value of the method.
	 */
	public static Object invokeMethod(Object object, Method method, Object... params) {
		return invokeMethodAccessible(object, method, false, params);
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters.
	 * <p>
	 * Forces access to package-private, {@code protected}, and {@code private}
	 * methods. Use {@link #invokeMethod(Object, Method, Object...)} when invoking
	 * accessible methods.
	 *
	 * @param object The instance of the class that should invoke the method. Can be
	 *               null if the method is static.
	 * @param method The method that has to get invoked.
	 * @param params Parameter instances of the method.
	 * @return The return value of the method.
	 */
	public static Object invokeNonPublicMethod(Object object, Method method, Object... params) {
		return invokeMethodAccessible(object, method, true, params);
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters.
	 *
	 * @param object      The instance of the class that should invoke the method.
	 * @param methodName  The name of the method that has to get invoked.
	 * @param forceAccess True, if access to a (package) private or protected method
	 *                    should be forced. Might fail with an
	 *                    {@link IllegalAccessException} otherwise.
	 * @param params      Parameter instances of the method.
	 * @return The return value of the method.
	 */
	private static Object invokeMethodAccessible(Object object, String methodName, boolean forceAccess,
			Object[] params) {
		var parameterTypes = getParameterTypes(params, "reflection_test_utils.method_null_args", methodName); //$NON-NLS-1$
		var method = getMethodAccessible(object.getClass(), methodName, forceAccess, parameterTypes);
		return invokeMethodAccessible(object, method, forceAccess, params);
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters.
	 *
	 * @param object      The instance of the class that should invoke the method.
	 * @param method      The method that has to get invoked.
	 * @param forceAccess True, if access to a (package) private or protected method
	 *                    should be forced. Might fail with an
	 *                    {@link IllegalAccessException} otherwise.
	 * @param params      Parameter instances of the method.
	 * @return The return value of the method.
	 */
	private static Object invokeMethodAccessible(Object object, Method method, boolean forceAccess, Object[] params) {
		// NOTE: object can be null, if method is static
		try {
			return invokeMethodRethrowingAccessible(object, method, forceAccess, params);
		} catch (AssertionFailedError e) {
			throw e;
		} catch (Throwable e) {
			throw localizedFailure("reflection_test_utils.method_internal_exception", method.getName(), //$NON-NLS-1$
					method.getDeclaringClass().getSimpleName(), e);
		}
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters, and
	 * re-throw an exception if one occurs during the method execution.
	 *
	 * @param object The instance of the class that should invoke the method.
	 * @param method The method that has to get invoked.
	 * @param params Parameter instances of the method.
	 * @return The return value of the method.
	 * @throws Throwable the exception that was caught and which will be re-thrown
	 */
	public static Object invokeMethodRethrowing(Object object, Method method, Object... params) throws Throwable {
		return invokeMethodRethrowingAccessible(object, method, false, params);
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters, and
	 * re-throw an exception if one occurs during the method execution.
	 * <p>
	 * Forces access to package-private, {@code protected}, and {@code private}
	 * methods. Use {@link #invokeMethodRethrowing(Object, Method, Object...)} when
	 * invoking accessible methods.
	 *
	 * @param object The instance of the class that should invoke the method.
	 * @param method The method that has to get invoked.
	 * @param params Parameter instances of the method.
	 * @return The return value of the method.
	 * @throws Throwable the exception that was caught and which will be re-thrown
	 */
	public static Object invokeNonPublicMethodRethrowing(Object object, Method method, Object... params)
			throws Throwable {
		return invokeMethodRethrowingAccessible(object, method, true, params);
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters, and
	 * rethrow an exception if one occurs during the method execution.
	 *
	 * @param object      The instance of the class that should invoke the method.
	 * @param method      The method that has to get invoked.
	 * @param forceAccess True, if access to a (package) private or protected method
	 *                    should be forced. Might fail with an
	 *                    {@link IllegalAccessException} otherwise.
	 * @param params      Parameter instances of the method.
	 * @return The return value of the method.
	 * @throws Throwable the exception that was caught and which will be rethrown
	 */
	private static Object invokeMethodRethrowingAccessible(Object object, Method method, boolean forceAccess,
			Object[] params) throws Throwable {
		// NOTE: object can be null, if method is static
		try {
			if (forceAccess) {
				method.setAccessible(true);
			}
			return method.invoke(object, params);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			throw localizedFailure("reflection_test_utils.method_access", method.getName(), //$NON-NLS-1$
					method.getDeclaringClass().getSimpleName(), getIllegalAccessSource(method));
		} catch (@SuppressWarnings("unused") IllegalArgumentException iae) {
			throw localizedFailure("reflection_test_utils.method_parameters", method.getName(), //$NON-NLS-1$
					method.getDeclaringClass().getSimpleName());
		} catch (@SuppressWarnings("unused") NullPointerException e) {
			throw localizedFailure("reflection_test_utils.method_null_target_instance", method.getName(), //$NON-NLS-1$
					method.getDeclaringClass().getSimpleName());
		} catch (@SuppressWarnings("unused") ExceptionInInitializerError e) {
			throw localizedFailure("reflection_test_utils.method_class_init", method.getName(), //$NON-NLS-1$
					method.getDeclaringClass().getSimpleName());
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	/**
	 * Retrieve a constructor with arguments of a given class.
	 *
	 * @param declaringClass The class that declares this constructor.
	 * @param parameterTypes The parameter types of this method.
	 * @param <T>            The type parameter of the constructor and class
	 * @return The wanted constructor.
	 */
	public static <T> Constructor<T> getConstructor(Class<T> declaringClass, Class<?>... parameterTypes) {
		try {
			return declaringClass.getConstructor(parameterTypes);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			throw localizedFailure("reflection_test_utils.constructor_not_found_params", //$NON-NLS-1$
					describeParameters(parameterTypes), declaringClass.getSimpleName());
		}
	}

	/**
	 * Retrieve a non-public constructor with arguments of a given class.
	 *
	 * @param declaringClass The class that declares this constructor.
	 * @param parameterTypes The parameter types of this method.
	 * @param <T>            The type parameter of the constructor and class
	 * @return The wanted method.
	 */
	public static <T> Constructor<T> getNonPublicConstructor(Class<T> declaringClass, Class<?>... parameterTypes) {
		try {
			return declaringClass.getDeclaredConstructor(parameterTypes);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			throw localizedFailure("reflection_test_utils.constructor_not_found_params", //$NON-NLS-1$
					describeParameters(parameterTypes), declaringClass.getSimpleName());
		}
	}

	private static String describeParameters(Class<?>... parameterTypes) {
		if (parameterTypes.length == 0)
			return localized("reflection_test_utils.no_parameters"); //$NON-NLS-1$
		return localized("reflection_test_utils.with_parameters", getParameterTypesAsString(parameterTypes)); //$NON-NLS-1$
	}

	/**
	 * Retrieves the parameters types of a given collection of parameter instances.
	 *
	 * @param key         The key for the localized exception message.
	 * @param messageArgs The arguments for the formatted localized message.
	 * @param params      The instances of the parameters.
	 * @return The parameter types of the instances as an array.
	 */
	private static Class<?>[] getParameterTypes(Object[] params, String key, Object... messageArgs) {
		return Arrays.stream(params).map(it -> requireNonNull(it, key, messageArgs)).map(Object::getClass)
				.toArray(Class<?>[]::new);
	}

	/**
	 * Generates a string representation of a given collection of parameter types.
	 *
	 * @param parameterTypes The parameter types we want a string representation of.
	 * @return The string representation of the parameter types.
	 */
	private static String getParameterTypesAsString(Class<?>... parameterTypes) {
		var joiner = new StringJoiner(", ", "[ ", " ]"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		joiner.setEmptyValue("none"); //$NON-NLS-1$
		Arrays.stream(parameterTypes).map(type -> requireNonNull(type, "One of the supplied types was null.")) //$NON-NLS-1$
				.map(Class::getSimpleName).forEach(joiner::add);
		return joiner.toString();
	}

	/**
	 * Throws an {@link AssertionFailedError} if the given object is null using the
	 * provided localized message.
	 *
	 * @param <T>         The type of the object that should be checked.
	 * @param object      The object to check for null.
	 * @param key         The key for the localized exception message.
	 * @param messageArgs The arguments for the formatted localized message.
	 * @return the same, unchanged object in case it was not null
	 */
	private static <T> T requireNonNull(T object, String key, Object... messageArgs) {
		if (object == null)
			throw localizedFailure(key, messageArgs);
		return object;
	}

	private static String getIllegalAccessSource(Member member) {
		if (!Modifier.isPublic(member.getModifiers()))
			return localized(
					"reflection_test_utils.construct." + member.getClass().getSimpleName().toLowerCase(Locale.ROOT)); //$NON-NLS-1$
		return localized("reflection_test_utils.construct.class"); //$NON-NLS-1$
	}
}
