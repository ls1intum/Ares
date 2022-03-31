package de.tum.in.test.api.dynamic;

import static de.tum.in.test.api.localization.Messages.formatLocalized;
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

@API(status = Status.MAINTAINED)
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
				field.trySetAccessible();
			} else {
				fail(formatLocalized("dynamics.field.not_found", name)); //$NON-NLS-1$
			}
		}
		return field;
	}

	@Override
	public boolean exists() {
		if (field == null) {
			var of = findField(owner.toClass());
			if (!of.isPresent()) {
				return false;
			}
			field = of.get();
			field.trySetAccessible();
		}
		return true;
	}

	public T getOf(Object o) {
		try {
			return type.cast(toField().get(o));
		} catch (IllegalAccessException e) {
			fail(formatLocalized("dynamics.field.access", name, owner), e); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			fail(formatLocalized("dynamics.field.target", name, owner), e); //$NON-NLS-1$
		} catch (ClassCastException e) {
			fail(formatLocalized("dynamics.field.cast", name, owner, type.getName()), e); //$NON-NLS-1$
		}
		return null; // unreachable
	}

	public T getStatic() {
		try {
			return getOf(null);
		} catch (NullPointerException e) {
			fail(formatLocalized("dynamics.field.static", name, owner), e); //$NON-NLS-1$
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
		return owner.toString() + "." + name; //$NON-NLS-1$
	}

	@Override
	public void check(Check... checks) {
		int modifiers = toField().getModifiers();
		for (Check check : checks)
			check.checkModifiers(modifiers, () -> formatLocalized("dynamics.field.name", this)); //$NON-NLS-1$
	}
}
