package de.tum.in.test.api.dynamic;

import static de.tum.in.test.api.localization.Messages.formatLocalized;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.UnexpectedExceptionError;

@API(status = Status.MAINTAINED)
public class DynamicMethod<T> implements Checkable {

	static final Collector<CharSequence, ?, String> JOIN_PARAMS = Collectors.joining(", ", "(", ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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
					fail(formatLocalized("dynamics.method.return", this, returnType)); //$NON-NLS-1$
			} catch (NoSuchMethodException e) {
				fail(formatLocalized("dynamics.method.not_found", returnType, this), e); //$NON-NLS-1$
			}
		}
		return m;
	}

	@Override
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
			fail(formatLocalized("dynamics.method.null", this), e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			fail(formatLocalized("dynamics.method.access", this), e); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			fail(formatLocalized("dynamics.method.arguments", this, descArgs(params), o.getClass().getCanonicalName()), //$NON-NLS-1$
					e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			throw UnexpectedExceptionError.wrap(e.getTargetException());
		} catch (ClassCastException e) {
			fail(formatLocalized("dynamics.method.cast", this, returnType), e); //$NON-NLS-1$
		}
		return null; // unreachable
	}

	public T invokeStatic(Object... params) {
		if (!Modifier.isStatic(toMethod().getModifiers()))
			fail(formatLocalized("dynamics.method.static", this)); //$NON-NLS-1$
		return invokeOn(null, params);
	}

	@Override
	public String toString() {
		return owner.toString() + "." + signature(); //$NON-NLS-1$
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
		return Arrays.stream(args).map(x -> x == null ? null : x.getClass().getCanonicalName()).collect(JOIN_PARAMS);
	}

	public static String descParams(DynamicClass<?>... params) {
		return Arrays.stream(params).map(DynamicClass::getName).collect(JOIN_PARAMS);
	}

	@Override
	public void check(Check... checks) {
		int modifiers = toMethod().getModifiers();
		for (Check check : checks)
			check.checkModifiers(modifiers, () -> formatLocalized("dynamics.method.name", this)); //$NON-NLS-1$
	}
}
