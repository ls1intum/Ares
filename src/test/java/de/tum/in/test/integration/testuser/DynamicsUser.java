package de.tum.in.test.integration.testuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.dynamic.Check;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.structural.SomeClass;

@Public
@UseLocale("de")
@StrictTimeout(10)
@WhitelistPath("")
@TestMethodOrder(MethodName.class)
public class DynamicsUser {

	private static final String SUBJECT_PACKAGE = "de.tum.in.test.integration.testuser.subject.structural";

	private static final DynamicClass<?> SomeClass = DynamicClass.toDynamic(SUBJECT_PACKAGE + ".SomeClass");
	private static final DynamicClass<?> SomeAbstractClass = DynamicClass
			.toDynamic(SUBJECT_PACKAGE + ".SomeAbstractClass");

	private static final DynamicConstructor<?> SomeClass_new = SomeClass.constructor();
	private static final DynamicConstructor<?> SomeClass_newBool = SomeClass.constructor(Boolean.class);
	private static final DynamicConstructor<?> SomeClass_newInt = SomeClass.constructor(Integer.class);
	private static final DynamicConstructor<?> SomeClass_newString = SomeClass.constructor(String.class);

	private static final DynamicField<Integer> SomeClass_SOME_CONSTANT = SomeClass.field(int.class, "SOME_CONSTANT");
	private static final DynamicField<String> SomeClass_someAttribute = SomeClass.field(String.class, "someAttribute");

	private static final DynamicMethod<String> SomeClass_getSomeAttribute = SomeClass.method(String.class,
			"getSomeAttribute");
	private static final DynamicMethod<Integer> SomeClass_doSomethingElse = SomeClass.method(int.class,
			"doSomethingElse", int.class);
	private static final DynamicMethod<Void> SomeClass_throwException = SomeClass.method(void.class, "throwException");

	@Test
	void class_check() {
		assertThat(SomeClass.exists()).isTrue();
		SomeClass.check(Check.PUBLIC, Check.NOT_FINAL, Check.NOT_STATIC);
		assertThat(SomeClass.toClass()).isEqualTo(SomeClass.class);
	}

	@Test
	void class_isClass() {
		assertThrows(NullPointerException.class, () -> SomeClass.isClass(null));
		assertThrows(IllegalArgumentException.class, () -> SomeClass.isClass(new Object()));
		assertThat(SomeClass.isClass(SomeClass.class)).isTrue();
		assertThat(SomeClass.isClass(SUBJECT_PACKAGE + ".SomeClass")).isTrue();
		assertThat(SomeClass.isClass(DynamicClass.toDynamic(SomeClass.class))).isTrue();
		assertThat(DynamicClass.toDynamic("java.lang.String").isClass(String.class)).isTrue();
	}

	@Test
	void class_notFound() {
		DynamicClass<?> notFound = DynamicClass.toDynamic("DoesNotExist");
		assertThat(notFound.exists()).isFalse();
		notFound.check();
	}

	@Test
	void class_searchNonFinalFields() {
		int checked = DynamicClass.toDynamic(DynamicsUser.class).checkForNonFinalFields();
		assertThat(checked).isEqualTo(12);

		SomeClass.checkForNonFinalFields();
	}

	@Test
	void class_searchNonPrivateFields() {
		int checked = DynamicClass.toDynamic(DynamicsUser.class).checkForNonPrivateFields();
		assertThat(checked).isEqualTo(12);

		SomeClass.checkForNonPrivateFields();
	}

	@Test
	void class_searchPublicOrProtectedMethods() {
		int checked = DynamicClass.toDynamic(SUBJECT_PACKAGE + ".SomeAbstractClass").checkForPublicOrProtectedMethods();
		assertThat(checked).isOne();

		SomeClass.checkForPublicOrProtectedMethods(SomeClass_getSomeAttribute, SomeClass_doSomethingElse,
				SomeClass_throwException, SomeClass.method(Integer.class, "getAnotherAttribute"),
				SomeClass.method(void.class, "setSomeAttribute", String.class),
				SomeClass.method(Class.class, "initializeFailingClass"));
	}

	@Test
	void constructor_abstract() {
		SomeAbstractClass.constructor().newInstance();
	}

	@Test
	void constructor_check() throws NoSuchMethodException {
		assertThat(SomeClass_newString.exists()).isTrue();
		SomeClass_newString.check(Check.PACKAGE_PRIVATE, Check.NOT_PUBLIC);
		assertThat(SomeClass_new.toConstructor()).isEqualTo(SomeClass.class.getConstructor());
	}

	@Test
	void constructor_illegalArguments() {
		SomeClass_newInt.newInstance("");
	}

	@Test
	void constructor_newInstance() {
		var instance = SomeClass_newInt.newInstance(1);
		assertThat(instance).isInstanceOf(SomeClass.class);
	}

	@Test
	void constructor_notFound() {
		SomeClass.constructor(Boolean.class, Boolean.class).newInstance(true, true);
	}

	@Test
	void constructor_throwing() {
		SomeClass_newBool.newInstance(true);
	}

	@Test
	void field_check() throws NoSuchFieldException {
		assertThat(SomeClass_SOME_CONSTANT.exists()).isTrue();
		SomeClass_SOME_CONSTANT.check(Check.PUBLIC, Check.STATIC, Check.FINAL);
		assertThat(SomeClass_SOME_CONSTANT.toField()).isEqualTo(SomeClass.class.getField("SOME_CONSTANT"));
	}

	@Test
	void field_getInstance() {
		var instance = new SomeClass();
		assertThat(SomeClass_someAttribute.getOf(instance)).isNull();
	}

	@Test
	void field_getStatic() {
		assertThat(SomeClass_SOME_CONSTANT.getStatic()).isEqualTo(42);
	}

	@Test
	void field_noSuchField() {
		SomeClass.field(double.class, "a").getStatic();
	}

	@Test
	void field_wrongType() {
		SomeClass.field(byte.class, "SOME_CONSTANT").getStatic();
	}

	@Test
	void method_badCast() {
		var instance = new SomeClass();
		SomeClass.method(double.class, "doSomethingElse", int.class).invokeOn(instance, 10);
	}

	@Test
	void method_check() throws NoSuchMethodException {
		assertThat(SomeClass_getSomeAttribute.exists()).isTrue();
		SomeClass_getSomeAttribute.check(Check.PUBLIC, Check.NOT_STATIC, Check.FINAL);
		assertThat(SomeClass_getSomeAttribute.toMethod()).isEqualTo(SomeClass.class.getMethod("getSomeAttribute"));
	}

	@Test
	void method_illegalArguments() {
		var instance = new SomeClass();
		SomeClass_doSomethingElse.invokeOn(instance, "abc");
	}

	@Test
	void method_invoke() {
		var instance = new SomeClass();
		var value = SomeClass_doSomethingElse.invokeOn(instance, 10);
		assertThat(value).isEqualTo(42);
	}

	@Test
	void method_notFound() {
		SomeClass.method(void.class, "abc").invokeStatic();
	}

	@Test
	void method_notStatic() {
		SomeClass_throwException.invokeStatic();
	}

	@Test
	void method_null() {
		SomeClass_throwException.invokeOn(null);
	}

	@Test
	void method_throwing() {
		var instance = new SomeClass();
		SomeClass_throwException.invokeOn(instance);
	}
}
