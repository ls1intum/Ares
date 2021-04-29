package de.tum.in.test.api.dynamic;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.EXPERIMENTAL)
public class DynamicField<T> implements Checkable {

	private final DynamicClass<?> owner;
	private final List<String> name;
	private final DynamicClass<T> type;
	private final boolean ignoreCase;
	private Field field;

	public DynamicField(DynamicClass<?> dClass, Class<T> fType, boolean ignoreCase, String... possibleNames) {
		this(dClass, DynamicClass.toDynamic(fType), ignoreCase, possibleNames);
	}

	public DynamicField(DynamicClass<?> dClass, DynamicClass<T> fType, boolean ignoreCase, String... possibleNames) {
		this.owner = Objects.requireNonNull(dClass);
		if (ignoreCase)
			this.name = Stream.of(possibleNames).map(String::toLowerCase).collect(Collectors.toUnmodifiableList());
		else
			this.name = List.of(possibleNames);
		this.type = Objects.requireNonNull(fType);
		this.ignoreCase = ignoreCase;
	}

	public Field toField() {
		if (field == null) {
			var of = findField(owner.toClass());
			if (of.isPresent()) {
				field = of.get();
				field.setAccessible(true); //NOSONAR
			} else
				fail("Feld " + name + " konnte nicht gefunden werden");
		}
		return field;
	}

	@SuppressWarnings("unchecked")
	public T getOf(Object o) {
		try {
			return (T) toField().get(o);
		} catch (IllegalAccessException e) {
			fail("Feld " + name + " der Klasse " + owner
					+ " konnte nicht aufgerufen werden, Zugriff auf das Feld nicht möglich", e);
		} catch (IllegalArgumentException e) {
			fail("Feld " + name + " von Klasse " + owner
					+ " wurde nicht auf einem passenden Objekt aufgerufen (-> Testfehler)", e);
		} catch (ClassCastException e) {
			fail("Feld " + name + " der Klasse " + owner + " kann nicht nach " + type.getName() + "gecastet werden", e);
		}
		return null; // unreachable
	}

	public T getStatic() {
		try {
			return getOf(null);
		} catch (NullPointerException e) {
			fail("Feld " + name + " der Klasse " + owner + " ist nicht statisch", e);
		}
		return null; // unreachable
	}

	private Optional<Field> findField(Class<?> c) {
		return fieldsOf(c).stream().filter(f -> name.contains(ignoreCase ? f.getName().toLowerCase() : f.getName()))
				.findFirst();
	}

	private List<Field> fieldsOf(Class<?> c) {
		ArrayList<Field> al = new ArrayList<>();
		Class<?> current = c;
		while (current != Object.class) {
			for (Field ff : current.getDeclaredFields())
				if (type.toClass().isAssignableFrom(ff.getType()))
					al.add(ff);
			current = current.getSuperclass();
		}
		return al;
	}

	@Override
	public String toString() {
		return owner.toString() + "." + name;
	}

	@Override
	public void check(Check... checks) {
		int modifiers = toField().getModifiers();
		String desc = "Feld " + this;
		for (Check check : checks) {
			check.checkModifiers(modifiers, desc);
		}
	}
}
