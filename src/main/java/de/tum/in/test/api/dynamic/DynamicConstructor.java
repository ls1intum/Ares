package de.tum.in.test.api.dynamic;

import static de.tum.in.test.api.dynamic.DynamicMethod.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.UnexpectedExceptionError;

@API(status = Status.EXPERIMENTAL)
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
				fail("Kein Konstruktor für " + owner + " mit Parametern " + descParams(this.parameters) + " gefunden.",
						e);
			}
		}
		return constructor;
	}

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

	public Object newInstance(Object... params) {
		try {
			return toConstructor().newInstance(params);
		} catch (InstantiationException e) {
			fail("Objekt der Klasse " + owner + " konnte nicht erzeugt werden, ist die Klasse abstract?", e);
		} catch (IllegalAccessException e) {
			fail("Objekt der Klasse " + owner + " konnte nicht erzeugt werden, Zugriff auf Konstruktor nicht möglich",
					e);
		} catch (IllegalArgumentException e) {
			fail("Konstruktor " + this + " konnte Parametertypen " + descArgs(params) + " nicht entgegennehmen", e);
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
		String desc = "Konstruktor " + this;
		for (Check check : checks)
			check.checkModifiers(modifiers, desc);
	}
}
