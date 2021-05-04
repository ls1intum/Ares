package de.tum.in.test.api.dynamic;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.UnexpectedExceptionError;

@API(status = Status.EXPERIMENTAL)
public class DynamicMethod<T> implements Checkable {
	private final DynamicClass<?> owner;
	private final String name;
	private final DynamicClass<?>[] parameters;
	private final DynamicClass<T> returnType;
	private Method m;

	public DynamicMethod(DynamicClass<?> dClass, Class<T> dynamicableReturn, String mName,
			Object... dynamicableParams) {
		this(dClass, DynamicClass.toDynamic(dynamicableReturn), mName, dynamicableParams);
	}

	public DynamicMethod(DynamicClass<?> dClass, DynamicClass<T> dynamicableReturn, String mName,
			Object... dynamicableParams) {
		this.owner = Objects.requireNonNull(dClass);
		this.name = Objects.requireNonNull(mName);
		this.returnType = Objects.requireNonNull(dynamicableReturn);
		this.parameters = DynamicClass.toDynamic(Objects.requireNonNull(dynamicableParams));
	}

	public Method toMethod() {
		if (m == null) {
			try {
				m = owner.toClass().getDeclaredMethod(name, DynamicClass.resolveAll(parameters));
				m.trySetAccessible();
				if (!returnType.toClass().isAssignableFrom(m.getReturnType()))
					fail("Methode " + this + " gibt nicht " + returnType + " zurück");
			} catch (NoSuchMethodException e) {
				fail("Keine Methode " + returnType + " " + this + " gefunden.", e);
			}
		}
		return m;
	}

	public boolean exists() {
		if (m == null) {
			try {
				m = owner.toClass().getDeclaredMethod(name, DynamicClass.resolveAll(parameters));
				m.trySetAccessible();
				if (!returnType.toClass().isAssignableFrom(m.getReturnType()))
					return false;
			} catch (@SuppressWarnings("unused") Exception e) {
				return false;
			}
		}
		return true;
	}

	public T invokeOn(Object o, Object... params) {
		try {
			return returnType.cast(toMethod().invoke(o, params));
		} catch (NullPointerException e) {
			fail("Methode " + this + " konnte nicht aufgerufen werden, das Objekt ist null", e);
		} catch (IllegalAccessException e) {
			fail("Methode " + this + " konnte nicht aufgerufen werden, Zugriff auf die Methode nicht möglich", e);
		} catch (IllegalArgumentException e) {
			fail("Methode " + this + " konnte Parametertypen " + descArgs(params) + " für Objekt der Klasse "
					+ o.getClass().getCanonicalName() + " nicht entgegennehmen", e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			throw UnexpectedExceptionError.wrap(e.getTargetException());
		} catch (ClassCastException e) {
			fail("Rückgabe von " + this + " kann nicht nach " + returnType + "gecastet werden", e);
		}
		return null; // unreachable
	}

	public T invokeStatic(Object... params) {
		if (!Modifier.isStatic(toMethod().getModifiers()))
			fail("Methode " + this + " ist nicht statisch");
		return invokeOn(null, params);
	}

	@Override
	public String toString() {
		return owner.toString() + "." + signature();
	}

	public String signature() {
		return name + descParams(parameters);
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

	public static String[] signatureOfAll(Collection<Method> methods) {
		return methods.stream().map(DynamicMethod::signatureOf).toArray(String[]::new);
	}

	public static <T extends Object & Collection<DynamicMethod<?>>> String[] signatureOfAll(T methods) {
		return methods.stream().map(DynamicMethod::signature).toArray(String[]::new);
	}

	public static String descArgs(Object... args) {
		return Arrays.stream(args).map(x -> x == null ? null : x.getClass().getCanonicalName())
				.collect(Collectors.joining(", ", "(", ")"));
	}

	public static String descParams(DynamicClass<?>... params) {
		return Arrays.stream(params).map(DynamicClass::getName).collect(Collectors.joining(", ", "(", ")"));
	}

	@Override
	public void check(Check... checks) {
		int modifiers = toMethod().getModifiers();
		String desc = "Methode " + this;
		for (Check check : checks)
			check.checkModifiers(modifiers, desc);
	}
}
