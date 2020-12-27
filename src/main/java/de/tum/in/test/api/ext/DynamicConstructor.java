package de.tum.in.test.api.ext;

import static de.tum.in.test.api.ext.DynamicMethod.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import de.tum.in.test.api.util.UnexpectedExceptionError;

public class DynamicConstructor<T> implements Checkable {

	final DynamicClass<T> dClass;
	final DynamicClass<?>[] params;
	Constructor<T> c;

	public DynamicConstructor(DynamicClass<T> dClass, Object... dynamicableParams) {
		this.dClass = Objects.requireNonNull(dClass);
		this.params = DynamicClass.toDynamic(Objects.requireNonNull(dynamicableParams));
	}

	public Constructor<T> getC() {
		if (c == null) {
			try {
				c = dClass.getC().getConstructor(DynamicClass.resolveAll(params));
			} catch (NoSuchMethodException e) {
				fail("Kein öffentlicher Konstruktor für " + dClass + " mit Parametern " + descParams(this.params)
						+ " gefunden.", e);
			}
		}
		return c;
	}

	public Object newInstance(Object... params) {
		try {
			return getC().newInstance(params);
		} catch (InstantiationException e) {
			fail("Objekt der Klasse " + dClass + " konnte nicht erzeugt werden, ist die Klasse abstract?", e);
		} catch (IllegalAccessException e) {
			fail("Objekt der Klasse " + dClass + " konnte nicht erzeugt werden, Zugriff auf Konstruktor nicht möglich",
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
		return dClass.toString() + "(" + descParams(params) + ")";
	}

	@Override
	public void check() {
		getC();
	}
}
