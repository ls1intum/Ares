package de.tum.in.test.api.ext;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import de.tum.in.test.api.util.UnexpectedExceptionError;

public class DynamicMethod<T> implements Checkable {
	final DynamicClass<?> dClass;
	final String mName;
	final DynamicClass<?>[] params;
	final DynamicClass<T> returnC;
	Method m;

	public DynamicMethod(DynamicClass<?> dClass, Class<T> dynamicableReturn, String mName,
			Object... dynamicableParams) {
		this(dClass, DynamicClass.toDynamic(dynamicableReturn), mName, dynamicableParams);
	}

	public DynamicMethod(DynamicClass<?> dClass, DynamicClass<T> dynamicableReturn, String mName,
			Object... dynamicableParams) {
		this.dClass = Objects.requireNonNull(dClass);
		this.mName = Objects.requireNonNull(mName);
		this.returnC = Objects.requireNonNull(dynamicableReturn);
		this.params = DynamicClass.toDynamic(Objects.requireNonNull(dynamicableParams));
	}

	public Method getM() {
		if (m == null) {
			try {
				m = dClass.getC().getDeclaredMethod(mName, DynamicClass.resolveAll(params));
				m.trySetAccessible();
				if (!returnC.getC().isAssignableFrom(m.getReturnType()))
					fail("Methode " + mName + " mit Parametern " + descParams(params) + " gibt nicht " + returnC
							+ " zurück");
			} catch (NoSuchMethodException e) {
				fail("Keine öffentliche Methode " + returnC + " " + this + " gefunden.", e);
			}
		}
		return m;
	}

	public T invokeOn(Object o, Object... params) {
		try {
			return returnC.cast(getM().invoke(o, params));
		} catch (IllegalAccessException e) {
			fail("Methode " + this + " konnte nicht aufgerufen werden, Zugriff auf die Methode nicht möglich", e);
		} catch (IllegalArgumentException e) {
			fail("Methode " + this + " konnte Parametertypen " + descArgs(params) + " für Objekt " + o
					+ " nicht entgegennehmen", e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			throw UnexpectedExceptionError.wrap(e.getTargetException());
		} catch (ClassCastException e) {
			fail("Rückgabe von " + mName + " mit Parametern " + descParams(this.params) + " der Klasse " + dClass
					+ " kann nicht nach " + returnC + "gecastet werden", e);
		}
		return null; // unreachable
	}

	public void invokeVoidMethodOn(Object o, Object... params) {
		try {
			getM().invoke(o, params);
		} catch (IllegalAccessException e) {
			fail("Methode " + this + " konnte nicht aufgerufen werden, Zugriff auf die Methode nicht möglich", e);
		} catch (IllegalArgumentException e) {
			fail("Methode " + this + " konnte Parametertypen " + descArgs(params) + " für Objekt " + o
					+ " nicht entgegennehmen", e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			throw UnexpectedExceptionError.wrap(e.getTargetException());
		} catch (ClassCastException e) {
			fail("Rückgabe von " + mName + " mit Parametern " + descParams(this.params) + " der Klasse " + dClass
					+ " kann nicht nach " + returnC + "gecastet werden", e);
		}
	}

	public T invokeStatic(Object... params) {
		if (!Modifier.isStatic(getM().getModifiers()))
			fail("Methode " + this + " ist nicht statisch");
		return invokeOn(null, params);
	}

	@SuppressWarnings("unchecked")
	public <R> R invokeOnCast(Object o, Object... params) {
		try {
			return (R) invokeOn(o, params);
		} catch (ClassCastException e) {
			throw new ClassCastException("Return type cast error in " + toString() + ": " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public <R> R invokeStaticCast(Object... params) {
		try {
			return (R) invokeStatic(params);
		} catch (ClassCastException e) {
			throw new ClassCastException("Return type cast error in " + toString() + ": " + e.getMessage());
		}
	}

	@Override
	public String toString() {
		return dClass.toString() + "." + signature();
	}

	public String signature() {
		return mName + descParams(params);
	}

	public static String signatureOf(Method method) {
		return method.getName() + descParams(DynamicClass.toDynamic(method.getParameterTypes()));
	}

	public static String[] signatureOfAll(Method... methods) {
		return Arrays.stream(methods).map(DynamicMethod::signatureOf).toArray(String[]::new);
	}

	public static String[] signatureOfAll(DynamicMethod<?>... methods) {
		return Arrays.stream(methods).map(DynamicMethod::signature).toArray(String[]::new);
	}

	public static String descArgs(Object... args) {
		return Arrays.stream(args).map(x -> x == null ? null : args.getClass().getCanonicalName())
				.collect(Collectors.joining(", ", "(", ")"));
	}

	public static String descParams(DynamicClass<?>... params) {
		return Arrays.stream(params).map(DynamicClass::getName).collect(Collectors.joining(", ", "(", ")"));
	}

	@Override
	public void check() {
		getM();
	}
}
