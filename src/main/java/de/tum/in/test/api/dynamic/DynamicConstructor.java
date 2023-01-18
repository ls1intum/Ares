package de.tum.in.test.api.dynamic;

import static de.tum.in.test.api.dynamic.DynamicMethod.*;
import static de.tum.in.test.api.localization.Messages.*;

import java.lang.reflect.*;
import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

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
				throw localizedFailure(e, "dynamics.constructor.not_found", owner, descParams(this.parameters)); //$NON-NLS-1$
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
			throw localizedFailure(e, "dynamics.constructor.abstract", owner); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			throw localizedFailure(e, "dynamics.constructor.access", this); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			throw localizedFailure(e, "dynamics.constructor.arguments", this, descArgs(params)); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			return DynamicClass.rethrowUnchecked(e.getCause());
		}
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
			check.checkModifiers(modifiers, () -> localized("dynamics.constructor.name", this)); //$NON-NLS-1$
	}
}
