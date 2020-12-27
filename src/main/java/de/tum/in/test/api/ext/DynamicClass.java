package de.tum.in.test.api.ext;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DynamicClass<T> implements Checkable {

	private static final Map<Class<?>, Class<?>> primitiveWrappers = Map.of(Boolean.TYPE, Boolean.class, Byte.TYPE,
			Byte.class, Character.TYPE, Character.class, Short.TYPE, Short.class, Integer.TYPE, Integer.class,
			Long.TYPE, Long.class, Float.TYPE, Float.class, Double.TYPE, Double.class);

	final String cName;
	Class<T> c;

	private DynamicClass(Class<T> clazz) {
		this.cName = clazz.getCanonicalName();
		this.c = clazz;
	}

	public DynamicClass(String cName) {
		this.cName = Objects.requireNonNull(cName);
	}

	public String getName() {
		if (c != null)
			return c.getCanonicalName();
		return cName;
	}

	@SuppressWarnings("unchecked")
	public Class<T> getC() {
		if (c == null) {
			try {
				c = (Class<T>) Class.forName(cName);
			} catch (ClassNotFoundException e) {
				fail("Klasse/Interface " + cName + " nicht gefunden", e);
			}
		}
		return c;
	}

	@SuppressWarnings("unchecked")
	public boolean exists() {
		if (c == null) {
			try {
				c = (Class<T>) Class.forName(cName);
			} catch (@SuppressWarnings("unused") Exception e) {
				return false;
			}
		}
		return true;
	}

	public boolean isClass(Object classOrStringOrDynamicClass) {
		if (classOrStringOrDynamicClass == null)
			throw new IllegalArgumentException("Internal Test Error, isClass supplied with null");
		if (classOrStringOrDynamicClass instanceof String) {
			return cName.equals(classOrStringOrDynamicClass);
		} else if (classOrStringOrDynamicClass instanceof Class<?>) {
			if (c != null)
				return c.equals(classOrStringOrDynamicClass);
			return cName.equals(((Class<?>) classOrStringOrDynamicClass).getCanonicalName());
		} else if (classOrStringOrDynamicClass instanceof DynamicClass) {
			return cName.equals(((DynamicClass<?>) classOrStringOrDynamicClass).cName);
		}
		throw new IllegalArgumentException(
				"Internal Test Error, isClass supplied with " + classOrStringOrDynamicClass.getClass());
	}

	@SuppressWarnings("unchecked")
	public T cast(Object obj) {
		Class<T> rClass = getC();
		if (rClass.isPrimitive()) {
			if (obj == null)
				throw new NullPointerException("null kann nicht nach " + getName() + " konvertiert werden.");
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
		return new DynamicField<>(this, type, null, true, possibleNames);
	}

	public <R> DynamicField<R> field(Class<R> type, String... possibleNames) {
		return new DynamicField<>(this, type, null, true, possibleNames);
	}

	public static <T> DynamicClass<T> toDynamic(Class<T> clazz) {
		return new DynamicClass<>(Objects.requireNonNull(clazz, "class most not be null"));
	}

	public static DynamicClass<?> toDynamic(Object classOrStringOrDynamicClass) {
		if (classOrStringOrDynamicClass == null)
			throw new IllegalArgumentException("Internal Test Error, toDynamic supplied with null");
		if (classOrStringOrDynamicClass instanceof String) {
			return new DynamicClass<>((String) classOrStringOrDynamicClass);
		} else if (classOrStringOrDynamicClass instanceof Class<?>) {
			return toDynamic((Class<?>) classOrStringOrDynamicClass);
		} else if (classOrStringOrDynamicClass instanceof DynamicClass) {
			return (DynamicClass<?>) classOrStringOrDynamicClass;
		}
		throw new IllegalArgumentException(
				"Internal Test Error, toDynamic supplied with " + classOrStringOrDynamicClass.getClass());
	}

	public static DynamicClass<?>[] toDynamic(Object[] classesOrStringsOrDynamicClasses) {
		DynamicClass<?>[] dynamicClasses = new DynamicClass[classesOrStringsOrDynamicClasses.length];
		for (int i = 0; i < dynamicClasses.length; i++) {
			dynamicClasses[i] = toDynamic(classesOrStringsOrDynamicClasses[i]);
		}
		return dynamicClasses;
	}

	public static Class<?>[] resolveAll(DynamicClass<?>[] dynamicClasses) {
		Class<?>[] classes = new Class<?>[dynamicClasses.length];
		for (int i = 0; i < classes.length; i++) {
			final int x = i;
			classes[x] = Objects.requireNonNull(dynamicClasses[i].getC(),
					() -> dynamicClasses[x] + " could not be resolved");
		}
		return classes;
	}

	@Override
	public String toString() {
		return cName;
	}

	@Override
	public void check() {
		getC();
	}

	public void checkForPublicMethods(DynamicMethod<?>... allowed) {
		Set<String> publicMethods = Set.of(DynamicMethod.signatureOfAll(allowed));
		Set<String> objectMethods = Set.of(DynamicMethod.signatureOfAll(Object.class.getMethods()));
		for (Method m : getC().getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {
				String sig = DynamicMethod.signatureOf(m);
				if ("main([Ljava.lang.String;)".endsWith(sig))
					continue;
				if (publicMethods.contains(sig))
					continue;
				if (objectMethods.contains(sig))
					continue;
				fail("Methode " + sig + " ist public, sollte sie aber nicht");
			}
		}
	}

	public void checkForNonPrivateFields() {
		for (Field f : getC().getDeclaredFields()) {
			assertTrue(Modifier.isPrivate(f.getModifiers()), "Attribut " + f + " muss private sein.");
		}
	}

	public void checkForNonFinalFields() {
		for (Field f : getC().getDeclaredFields()) {
			assertTrue(Modifier.isFinal(f.getModifiers()), "Attribut " + f + " muss final sein.");
		}
	}
}
