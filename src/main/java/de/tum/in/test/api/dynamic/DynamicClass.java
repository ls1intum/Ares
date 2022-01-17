package de.tum.in.test.api.dynamic;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.MAINTAINED)
public class DynamicClass<T> implements Checkable {

	private static final Map<Class<?>, Class<?>> primitiveWrappers = Map.of(Boolean.TYPE, Boolean.class, Byte.TYPE,
			Byte.class, Character.TYPE, Character.class, Short.TYPE, Short.class, Integer.TYPE, Integer.class,
			Long.TYPE, Long.class, Float.TYPE, Float.class, Double.TYPE, Double.class);

	private final String name;
	private Class<T> clazz;

	private DynamicClass(Class<T> clazz) {
		this.name = clazz.getCanonicalName();
		this.clazz = clazz;
	}

	public DynamicClass(String cName) {
		this.name = Objects.requireNonNull(cName);
	}

	public String getName() {
		if (clazz != null)
			return clazz.getCanonicalName();
		return name;
	}

	@SuppressWarnings("unchecked")
	public Class<T> toClass() {
		if (clazz == null) {
			try {
				clazz = (Class<T>) Class.forName(name);
			} catch (ClassNotFoundException e) {
				fail("Klasse/Interface " + name + " nicht gefunden", e);
			}
		}
		return clazz;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean exists() {
		if (clazz == null) {
			try {
				clazz = (Class<T>) Class.forName(name);
			} catch (@SuppressWarnings("unused") Exception e) {
				return false;
			}
		}
		return true;
	}

	public boolean isClass(Object classOrStringOrDynamicClass) {
		if (classOrStringOrDynamicClass == null)
			throw new NullPointerException("Internal Test Error, isClass supplied with null");
		if (classOrStringOrDynamicClass instanceof String)
			return name.equals(classOrStringOrDynamicClass);
		if (classOrStringOrDynamicClass instanceof Class<?>) {
			if (clazz != null)
				return clazz.equals(classOrStringOrDynamicClass);
			return name.equals(((Class<?>) classOrStringOrDynamicClass).getCanonicalName());
		}
		if (classOrStringOrDynamicClass instanceof DynamicClass)
			return name.equals(((DynamicClass<?>) classOrStringOrDynamicClass).name);
		throw new IllegalArgumentException(
				"Internal Test Error, isClass supplied with " + classOrStringOrDynamicClass.getClass());
	}

	@SuppressWarnings("unchecked")
	public T cast(Object obj) {
		Class<T> rClass = toClass();
		if (rClass.equals(Void.TYPE) && obj == null)
			return null;
		if (rClass.isPrimitive()) {
			if (obj == null)
				throw new NullPointerException("null kann nicht nach " + getName() + " gecastet werden.");
			Class<?> objClass = obj.getClass();
			Class<?> wrapper = primitiveWrappers.get(rClass);
			if (objClass.equals(wrapper))
				return (T) obj;
		}
		return rClass.cast(obj);
	}

	public DynamicConstructor<T> constructor(Object... dynamicableParams) {
		return new DynamicConstructor<>(this, dynamicableParams);
	}

	public <R> DynamicMethod<R> method(DynamicClass<R> returnType, String name, Object... dynamicableParams) {
		return new DynamicMethod<>(this, returnType, name, dynamicableParams);
	}

	public <R> DynamicMethod<R> method(Class<R> returnType, String name, Object... dynamicableParams) {
		return new DynamicMethod<>(this, returnType, name, dynamicableParams);
	}

	public <R> DynamicField<R> field(DynamicClass<R> type, String... possibleNames) {
		return new DynamicField<>(this, type, true, possibleNames);
	}

	public <R> DynamicField<R> field(Class<R> type, String... possibleNames) {
		return new DynamicField<>(this, type, true, possibleNames);
	}

	public static <T> DynamicClass<T> toDynamic(Class<T> clazz) {
		return new DynamicClass<>(Objects.requireNonNull(clazz, "class must not be null"));
	}

	public static DynamicClass<?> toDynamic(Object classOrStringOrDynamicClass) { // NOSONAR
		if (classOrStringOrDynamicClass == null)
			throw new NullPointerException("Internal Test Error, toDynamic supplied with null");
		if (classOrStringOrDynamicClass instanceof String)
			return new DynamicClass<>((String) classOrStringOrDynamicClass);
		if (classOrStringOrDynamicClass instanceof Class<?>)
			return toDynamic((Class<?>) classOrStringOrDynamicClass);
		if (classOrStringOrDynamicClass instanceof DynamicClass)
			return (DynamicClass<?>) classOrStringOrDynamicClass;
		throw new IllegalArgumentException(
				"Internal Test Error, toDynamic supplied with " + classOrStringOrDynamicClass.getClass());
	}

	public static DynamicClass<?>[] toDynamic(Object[] classesOrStringsOrDynamicClasses) { // NOSONAR
		var dynamicClasses = new DynamicClass[classesOrStringsOrDynamicClasses.length];
		for (var i = 0; i < dynamicClasses.length; i++)
			dynamicClasses[i] = toDynamic(classesOrStringsOrDynamicClasses[i]);
		return dynamicClasses;
	}

	public static Class<?>[] resolveAll(DynamicClass<?>[] dynamicClasses) {
		var classes = new Class<?>[dynamicClasses.length];
		for (var i = 0; i < classes.length; i++) {
			final int x = i;
			classes[x] = Objects.requireNonNull(dynamicClasses[i].toClass(),
					() -> dynamicClasses[x] + " could not be resolved");
		}
		return classes;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void check(Check... checks) {
		toClass();
		int modifiers = toClass().getModifiers();
		String desc = "Klasse/Interface " + this;
		for (Check check : checks)
			check.checkModifiers(modifiers, desc);
	}

	public int checkForPublicOrProtectedMethods(DynamicMethod<?>... exceptions) {
		return checkForPublicOrProtectedMethods(List.of(exceptions));
	}

	public int checkForPublicOrProtectedMethods(List<DynamicMethod<?>> exceptions) {
		var checked = 0;
		Set<String> publicMethods = Set.of(DynamicMethod.signatureOfAll(exceptions));
		Set<String> objectMethods = Set.of(DynamicMethod.signatureOfAll(Object.class.getMethods()));
		for (Method m : toClass().getDeclaredMethods()) {
			if (m.isSynthetic() || m.isBridge())
				continue;
			if (Modifier.isPublic(m.getModifiers())) {
				String sig = DynamicMethod.signatureOf(m);
				if (!"main(java.lang.String[])".endsWith(sig) && !publicMethods.contains(sig)
						&& !objectMethods.contains(sig))
					fail("Methode " + sig + " ist public, sollte sie aber nicht");
			}
			if (Modifier.isProtected(m.getModifiers())) {
				String sig = DynamicMethod.signatureOf(m);
				if (!objectMethods.contains(sig))
					fail("Methode " + sig + " ist protected, sollte sie aber nicht");
			}
			checked++;
		}
		return checked;
	}

	public int checkForNonPrivateFields() {
		var checked = 0;
		for (Field f : toClass().getDeclaredFields()) {
			if (f.isSynthetic())
				continue;
			if (!Modifier.isPrivate(f.getModifiers()))
				fail("Attribut " + f + " muss private sein.");
			checked++;
		}
		return checked;
	}

	public int checkForNonFinalFields() {
		var checked = 0;
		for (Field f : toClass().getDeclaredFields()) {
			if (f.isSynthetic())
				continue;
			if (!Modifier.isFinal(f.getModifiers()))
				fail("Attribut " + f + " muss final sein.");
			checked++;
		}
		return checked;
	}
}
