package de.tum.in.test.api.dynamic;

import static de.tum.in.test.api.dynamic.DynamicMethod.*;
import static de.tum.in.test.api.localization.Messages.formatLocalized;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.UnexpectedExceptionError;

@API(status = Status.MAINTAINED)
public class DynamicConstructor<T> implements Checkable {

	private final DynamicClass<T> owner;
	private final DynamicClass<?>[] parameters;
	private Constructor<T> constructor;

	public DynamicConstructor(DynamicClass<T> dClass, Object... dynamicableParams) {
		this.owner = Objects.requireNonNull(dClass);
		this.parameters = DynamicClass.toDynamic(Objects.requireNonNull(dynamicableParams));
	}

	public Constructor<T> toConstructor() {
		if (constructor == null) {
			try {
				constructor = owner.toClass().getDeclaredConstructor(DynamicClass.resolveAll(parameters));
				constructor.trySetAccessible();
			} catch (NoSuchMethodException e) {
				fail(formatLocalized("dynamics.constructor.not_found", owner, descParams(this.parameters)), e); //$NON-NLS-1$
			}
		}
		return constructor;
	}

	@Override
	public boolean exists() {
		if (constructor == null) {
			try {
				constructor = owner.toClass().getDeclaredConstructor(DynamicClass.resolveAll(parameters));
				constructor.trySetAccessible();
			} catch (@SuppressWarnings("unused") Exception e) {
				return false;
			}
		}
		return true;
	}

	public T newInstance(Object... params) {
		try {
			return toConstructor().newInstance(params);
		} catch (InstantiationException e) {
			fail(formatLocalized("dynamics.constructor.abstract", owner), e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			fail(formatLocalized("dynamics.constructor.access", this), e); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			fail(formatLocalized("dynamics.constructor.arguments", this, descArgs(params)), e); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			throw UnexpectedExceptionError.wrap(e.getTargetException());
		}
		return null; // unreachable
	}

	@Override
	public String toString() {
		return owner.toString() + descParams(parameters);
	}

	@Override
	public void check(Check... checks) {
		toConstructor();
		int modifiers = toConstructor().getModifiers();
		for (Check check : checks)
			check.checkModifiers(modifiers, () -> formatLocalized("dynamics.constructor.name", this)); //$NON-NLS-1$
	}
}
