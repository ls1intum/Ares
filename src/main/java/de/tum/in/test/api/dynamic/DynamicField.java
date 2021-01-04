package de.tum.in.test.api.dynamic;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DynamicField<T> implements Checkable {

	final DynamicClass<?> dClass;
	final List<String> fName;
	final DynamicClass<T> fType;
	final DynamicMethod<T> backup;
	final boolean ignoreCase;
	Class<?> c;
	Field f;

	public DynamicField(DynamicClass<?> dClass, Class<T> fType, DynamicMethod<T> backup, boolean ignoreCase,
			String... fNames) {
		this(dClass, DynamicClass.toDynamic(fType), backup, ignoreCase, fNames);
	}

	public DynamicField(DynamicClass<?> dClass, DynamicClass<T> fType, DynamicMethod<T> backup, boolean ignoreCase,
			String... fNames) {
		this.dClass = Objects.requireNonNull(dClass);
		this.ignoreCase = ignoreCase;
		this.fName = List.of(Objects.requireNonNull(fNames));
		this.fType = Objects.requireNonNull(fType);
		this.backup = backup;
	}

	public Field getF() {
		if (f == null) {
			var of = findField(dClass.getC());
			if (of.isPresent()) {
				f = of.get();
				f.setAccessible(true);
			} else
				fail("Feld " + fName + " konnte nicht gefunden werden");
		}
		return f;
	}

	@SuppressWarnings("unchecked")
	public T getOf(Object o) {
		try {
			return (T) getF().get(o);
		} catch (IllegalAccessException e) {
			fail("Feld " + fName + " der Klasse " + dClass
					+ " konnte nicht aufgerufen werden, Zugriff auf das Feld nicht möglich", e);
		} catch (IllegalArgumentException e) {
			fail("Feld " + fName + " von Klasse " + dClass
					+ " wurde nicht auf einem passenden Objekt aufgerufen (-> Testfehler)", e);
		} catch (ClassCastException e) {
			fail("Feld " + fName + " der Klasse " + dClass + " kann nicht nach " + fType.getName() + "gecastet werden",
					e);
		}
		return null; // unreachable
	}

	public T getStatic() {
		try {
			return getOf(null);
		} catch (NullPointerException e) {
			fail("Feld " + fName + " der Klasse " + dClass + " ist nicht statisch", e);
		}
		return null; // unreachable
	}

	public Optional<Field> findField(Class<?> c) {
		return fieldsOf(c).stream().filter(f -> fName.contains(f.getName())).findFirst();
	}

	public List<Field> fieldsOf(Class<?> c) {
		ArrayList<Field> al = new ArrayList<>();
		while (c != Object.class) {
			for (Field ff : c.getDeclaredFields())
				if (fType.getC().isAssignableFrom(ff.getType()))
					al.add(ff);
			c = c.getSuperclass();
		}
		return al;
	}

	@Override
	public String toString() {
		return dClass.toString() + "." + fName;
	}

	@Override
	public void check() {
		getF();
	}
}
