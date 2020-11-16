package de.tum.in.test.api.behavior;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 *
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
 * @version 5.0 (11.11.2020)
 */
public abstract class BehaviorTest {

	private static final String COULD_NOT_FIND_THE_METHOD = "Could not find the method ";
	private static final String BECAUSE = " because";
	private static final String THE_CONSTRUCTOR_WITH = " the constructor with ";
	private static final String ACCESS_DENIED = " access to the package of the class was denied.";

	/**
	 * Retrieve the actual class by its qualified name.
	 * 
	 * @param qualifiedClassName The qualified name of the class that needs to get
	 *                           retrieved (package.classname)
	 * @return The wanted class object.
	 */
	protected Class<?> getClass(String qualifiedClassName) {
		// The simple class name is the last part of the qualified class name.
		String[] qualifiedClassNameSegments = qualifiedClassName.split("\\.");
		String className = qualifiedClassNameSegments[qualifiedClassNameSegments.length - 1];

		try {
			return Class.forName(qualifiedClassName);
		} catch (@SuppressWarnings("unused") ClassNotFoundException e) {
			fail("The class '" + className
					+ "' was not found within the submission. Make sure to implement it properly.");
		} catch (@SuppressWarnings("unused") ExceptionInInitializerError e) {
			fail("The class '" + className
					+ "' could not be initialized because an exception was thrown in a static initializer block. "
					+ "Make sure to implement the static initialization without errors.");
		}
		// unreachable
		return null;
	}

	/**
	 * Instantiate an object of a class by its qualified name and the
	 * constructor arguments, if applicable.
	 * <p>
	 * This method does not support passing null, passing subclasses of the
	 * parameter types or invoking constructors with primitive parameters. Use
	 * {@link #newInstance(Constructor, Object...)} for that.
	 * 
	 * @param qualifiedClassName The qualified name of the class that needs to get
	 *                           retrieved (package.classname)
	 * @param constructorArgs    Parameter instances of the constructor of the
	 *                           class, that it should use to get instantiated with.
	 *                           Do not include, if the constructor has no arguments.
	 * @return The instance of this class.
	 * @see #newInstance(Class, Object...)
	 */
	protected Object newInstance(String qualifiedClassName, Object... constructorArgs) {
		return newInstance(getClass(qualifiedClassName), constructorArgs);
	}

	/**
	 * Instantiate an object of a given class using the given constructor
	 * arguments, if applicable.
	 * <p>
	 * This method does not support passing null, passing subclasses of the
	 * parameter types or invoking constructors with primitive parameters. Use
	 * {@link #newInstance(Constructor, Object...)} for that.
	 * 
	 * @param clazz           The class for which a new instance should be created
	 * @param constructorArgs Parameter instances of the constructor of the
	 *                        class, that it should use to get instantiated with.
	 *                        Do not include, if the constructor has no arguments.
	 * @return The instance of this class.
	 */
	protected Object newInstance(Class<?> clazz, Object... constructorArgs) {
		String failMessage = "Could not instantiate the class " + clazz.getSimpleName() + BECAUSE;
		Class<?>[] constructorArgTypes = getParameterTypes(
				failMessage + " a fitting constructor could not be found because", constructorArgs);

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(constructorArgTypes);
			return newInstance(constructor, constructorArgs);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			fail(failMessage + " the class does not have a constructor with the arguments: "
					+ getParameterTypesAsString(constructorArgTypes)
					+ ". Make sure to implement this constructor properly.");
		} catch (@SuppressWarnings("unused") SecurityException se) {
			fail(failMessage + ACCESS_DENIED);
		}
		// unreachable
		return null;
	}

	/**
	 * Instantiate an object of a class by using a specific constructor and
	 * constructor arguments, if applicable.
	 * 
	 * @param constructor     The actual constructor that should be used for
	 *                        creating a new instance of the object
	 * @param constructorArgs Parameter instances of the constructor of the
	 *                        class, that it should use to get instantiated with.
	 *                        Do not include, if the constructor has no arguments.
	 * @return The instance of this class.
	 */
	protected Object newInstance(Constructor<?> constructor, Object... constructorArgs) {
		String failMessage = "Could not instantiate the class " + constructor.getDeclaringClass().getSimpleName()
				+ BECAUSE;

		try {
			return constructor.newInstance(constructorArgs);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			fail(failMessage + " access to its constructor with the parameters: "
					+ getParameterTypesAsString(constructor.getParameterTypes()) + " was denied."
					+ " Make sure to check the modifiers of the constructor.");
		} catch (@SuppressWarnings("unused") IllegalArgumentException iae) {
			fail(failMessage
					+ " the actual constructor or none of the actual constructors of this class match the expected one."
					+ " We expect, amongst others, one with "
					+ getParameterTypesAsString(constructor.getParameterTypes()) + " parameters, which does not exist."
					+ " Make sure to implement this constructor correctly.");
		} catch (@SuppressWarnings("unused") InstantiationException ie) {
			fail(failMessage + " the class is abstract and should not have a constructor."
					+ " Make sure to remove the constructor of the class.");
		} catch (@SuppressWarnings("unused") InvocationTargetException ite) {
			fail(failMessage + THE_CONSTRUCTOR_WITH + constructorArgs.length
					+ " parameters threw an exception and could not be initialized."
					+ " Make sure to check the constructor implementation.");
		} catch (@SuppressWarnings("unused") ExceptionInInitializerError eiie) {
			fail(failMessage + THE_CONSTRUCTOR_WITH + constructorArgs.length + " parameters could not be initialized.");
		}
		// unreachable
		return null;
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
	protected Object valueForAttribute(Object object, String attributeName) {
		requireNonNull(object,
				"Could not retrieve the value of attribute '" + attributeName + "' because the object was null.");
		String failMessage = "Could not retrieve the attribute '" + attributeName + "' from the class "
				+ object.getClass().getSimpleName() + BECAUSE;

		try {
			return object.getClass().getDeclaredField(attributeName).get(object);
		} catch (@SuppressWarnings("unused") NoSuchFieldException nsfe) {
			fail(failMessage + " the attribute does not exist. Make sure to implement the attribute correctly.");
		} catch (@SuppressWarnings("unused") SecurityException se) {
			fail(failMessage + ACCESS_DENIED);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			fail(failMessage
					+ " access to the attribute was denied. Make sure to check the modifiers of the attribute.");
		}
		// unreachable
		return null;
	}

	/**
	 * Helper method that retrieves a method with arguments of a given object by its
	 * name.
	 *
	 * @param object         instance of the class that defines the method.
	 * @param methodName     the name of the method.
	 * @param parameterTypes The parameter types of this method. Do not include if
	 *                       the method has no parameters.
	 * @return The wanted method.
	 */
	protected Method getMethod(Object object, String methodName, Class<?>... parameterTypes) {
		requireNonNull(object, COULD_NOT_FIND_THE_METHOD + "'" + methodName + "' because the object was null.");
		return getMethod(object.getClass(), methodName, parameterTypes);
	}

	/**
	 * Retrieve a method with arguments of a given class by its name.
	 * 
	 * @param declaringClass The class that declares this method.
	 * @param methodName     The name of this method.
	 * @param parameterTypes The parameter types of this method. Do not include if
	 *                       the method has no parameters.
	 * @return The wanted method.
	 */
	protected Method getMethod(Class<?> declaringClass, String methodName, Class<?>... parameterTypes) {
		String failMessage = COULD_NOT_FIND_THE_METHOD + "'" + methodName + "' with the parameters: "
				+ getParameterTypesAsString(parameterTypes) + " in the class " + declaringClass.getSimpleName()
				+ BECAUSE;

		if (parameterTypes == null || parameterTypes.length == 0) {
			failMessage = COULD_NOT_FIND_THE_METHOD + "'" + methodName + "' from the class "
					+ declaringClass.getSimpleName() + BECAUSE;
		}

		try {
			return declaringClass.getMethod(methodName, parameterTypes);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			fail(failMessage + " the method does not exist. Make sure to implement this method properly.");
		} catch (@SuppressWarnings("unused") NullPointerException npe) {
			fail(failMessage + " the name of the method is null. Make sure to check the name of the method.");
		} catch (@SuppressWarnings("unused") SecurityException se) {
			fail(failMessage + " access to the package class was denied.");
		}
		// unreachable
		return null;
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters.
	 * 
	 * @param object The instance of the class that should invoke the method. Can be
	 *               null if the method is static.
	 * @param method The method that has to get invoked.
	 * @param params Parameter instances of the method. Do not include if the method
	 *               has no parameters.
	 * @return The return value of the method.
	 */
	protected Object invokeMethod(Object object, Method method, Object... params) {
		// NOTE: object can be null, if method is static
		String failMessage = "Could not invoke the method '" + method.getName() + "' in the class "
				+ method.getDeclaringClass().getSimpleName() + BECAUSE;
		try {
			invokeMethodRethrowing(object, method, params);
		} catch (Throwable e) {
			fail(failMessage + " of an exception within the method: " + e);
		}
		// unreachable
		return null;
	}

	/**
	 * Invoke a given method of a given object with instances of the parameters, and
	 * rethrow an exception if one occurs during the method execution.
	 * 
	 * @param object The instance of the class that should invoke the method.
	 * @param method The method that has to get invoked.
	 * @param params Parameter instances of the method. Do not include if the method
	 *               has no parameters.
	 * @throws Throwable the exception that was caught and which will be rethrown
	 * @return The return value of the method.
	 */
	protected Object invokeMethodRethrowing(Object object, Method method, Object... params) throws Throwable {
		// NOTE: object can be null, if method is static
		String failMessage = "Could not invoke the method '" + method.getName() + "' in the class "
				+ method.getDeclaringClass().getSimpleName() + BECAUSE;
		try {
			return method.invoke(object, params);
		} catch (@SuppressWarnings("unused") IllegalAccessException iae) {
			fail(failMessage + " access to the method was denied. Make sure to check the modifiers of the method.");
		} catch (@SuppressWarnings("unused") IllegalArgumentException iae) {
			fail(failMessage
					+ " the parameters are not implemented right. Make sure to check the parameters of the method");
		} catch (InvocationTargetException e) {
			throw e.getCause();
		} catch (NullPointerException e) {
			fail(failMessage + " the object was null and the method is an instance method. "
					+ "Make sure to check the static modifier of the method");
		} catch (ExceptionInInitializerError e) {
			fail(failMessage + " the static initialization provoked by this method failed. "
					+ "Make sure to check the initialization triggered by this method.");
		}
		// unreachable
		return null;
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
	 * @param params     Parameter instances of the method. Do not include if the
	 *                   method has no parameters.
	 * @return The return value of the method.
	 */
	protected Object invokeMethod(Object object, String methodName, Object... params) {
		String failMessage = COULD_NOT_FIND_THE_METHOD + "'" + methodName + "'" + BECAUSE;
		Class<?>[] parameterTypes = getParameterTypes(failMessage, params);
		Method method = getMethod(object, methodName, parameterTypes);
		return invokeMethod(object, method, params);
	}

	/**
	 * Retrieve a constructor with arguments of a given class.
	 * 
	 * @param declaringClass The class that declares this constructor.
	 * @param parameterTypes The parameter types of this method. Do not include if
	 *                       the method has no parameters.
	 * @param <T>            The type parameter of the constructor and class
	 * @return The wanted method.
	 */
	protected <T> Constructor<T> getConstructor(Class<T> declaringClass, Class<?>... parameterTypes) {
		String failMessage = "Could not find the constructor with the parameters: "
				+ getParameterTypesAsString(parameterTypes) + " in the class " + declaringClass.getSimpleName()
				+ BECAUSE;

		if (parameterTypes == null || parameterTypes.length == 0) {
			failMessage = "Could not find the constructor from the " + declaringClass.getSimpleName() + BECAUSE;
		}

		try {
			return declaringClass.getConstructor(parameterTypes);
		} catch (@SuppressWarnings("unused") NoSuchMethodException nsme) {
			fail(failMessage + " the method does not exist. Make sure to implement this method properly.");
		} catch (@SuppressWarnings("unused") NullPointerException npe) {
			fail(failMessage + " the name of the method is null. Make sure to check the name of the method.");
		} catch (@SuppressWarnings("unused") SecurityException se) {
			fail(failMessage + " access to the package class was denied.");
		}
		// unreachable
		return null;
	}

	/**
	 * Retrieves the parameters types of a given collection of parameter instances.
	 * 
	 * @param failMessage The beginning of message of the failure message if one of
	 *                    params is null
	 * @param params      The instances of the parameters.
	 * @return The parameter types of the instances as an array.
	 */
	private static Class<?>[] getParameterTypes(String failMessage, Object... params) {
		return Arrays.stream(params)
				.map(it -> requireNonNull(it, failMessage + " one of the supplied arguments was null."))
				.map(Object::getClass).toArray(Class<?>[]::new);
	}

	/**
	 * Generates a string representation of a given collection of parameter types.
	 * 
	 * @param parameterTypes The parameter types we want a string representation of.
	 * @return The string representation of the parameter types.
	 */
	private static String getParameterTypesAsString(Class<?>... parameterTypes) {
		StringJoiner joiner = new StringJoiner(", ", "[ ", " ]");
		joiner.setEmptyValue("none");
		Arrays.stream(parameterTypes).map(Class::getSimpleName).forEach(joiner::add);
		return joiner.toString();
	}
}
