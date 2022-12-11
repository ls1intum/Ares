package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.*;

import de.tum.in.test.integration.testuser.DynamicsUser;
import de.tum.in.test.testutilities.*;
import de.tum.in.test.testutilities.CustomConditions.Option;

@UserBased(DynamicsUser.class)
class DynamicsTest {

	@UserTestResults
	private static Events tests;

	private final String checks_fail = "checks_fail";
	private final String class_check = "class_check";
	private final String class_isClass = "class_isClass";
	private final String class_notFound = "class_notFound";
	private final String class_searchNonFinalFields = "class_searchNonFinalFields";
	private final String class_searchNonPrivateFields = "class_searchNonPrivateFields";
	private final String class_searchPublicOrProtectedMethods = "class_searchPublicOrProtectedMethods";
	private final String constructor_abstract = "constructor_abstract";
	private final String constructor_check = "constructor_check";
	private final String constructor_illegalArguments = "constructor_illegalArguments";
	private final String constructor_newInstance = "constructor_newInstance";
	private final String constructor_notFound = "constructor_notFound";
	private final String constructor_throwing = "constructor_throwing";
	private final String field_check = "field_check";
	private final String field_getInstance = "field_getInstance";
	private final String field_getStaticNull = "field_getStaticNull";
	private final String field_getStaticSuccess = "field_getStaticSuccess";
	private final String field_getWrongObject = "field_getWrongObject";
	private final String field_getWrongType = "field_getWrongType";
	private final String field_noSuchField = "field_noSuchField";
	private final String field_setOf = "field_setOf";
	private final String field_setStaticFinal = "field_setStaticFinal";
	private final String field_setStaticNull = "field_setStaticNull";
	private final String field_setStaticSuccess = "field_setStaticSuccess";
	private final String field_setWrongObject = "field_setWrongObject";
	private final String field_setWrongType = "field_setWrongType";
	private final String method_badCast = "method_badCast";
	private final String method_check = "method_check";
	private final String method_illegalArguments = "method_illegalArguments";
	private final String method_invoke = "method_invoke";
	private final String method_notFound = "method_notFound";
	private final String method_notStatic = "method_notStatic";
	private final String method_null = "method_null";
	private final String method_throwing = "method_throwing";

	@TestTest
	void test_checks_fail() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(checks_fail, MultipleFailuresError.class, //
				"Multiple Failures (6 failures)\n" //
						+ "	org.opentest4j.AssertionFailedError: Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass ist public.\n" //
						+ "	org.opentest4j.AssertionFailedError: Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass ist nicht final.\n" //
						+ "	org.opentest4j.AssertionFailedError: Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass ist nicht statisch.\n" //
						+ "	org.opentest4j.AssertionFailedError: Attribut de.tum.in.test.integration.testuser.subject.structural.SomeClass.[someAttribute] ist nicht public.\n" //
						+ "	org.opentest4j.AssertionFailedError: Attribut de.tum.in.test.integration.testuser.subject.structural.SomeClass.[SOME_CONSTANT] ist final.\n" //
						+ "	org.opentest4j.AssertionFailedError: Attribut de.tum.in.test.integration.testuser.subject.structural.SomeClass.[SOME_CONSTANT] ist statisch." //
				, Option.MESSAGE_NORMALIZE_NEWLINE));
	}

	@TestTest
	void test_class_check() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(class_check));
	}

	@TestTest
	void test_class_isClass() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(class_isClass));
	}

	@TestTest
	void test_class_notFound() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(class_notFound, AssertionFailedError.class, "Klasse DoesNotExist nicht gefunden."));
	}

	@TestTest
	void test_class_searchNonFinalFields() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_searchNonFinalFields, AssertionFailedError.class,
				"Attribut private java.lang.String de.tum.in.test.integration.testuser.subject.structural.SomeClass.someAttribute muss final sein."));
	}

	@TestTest
	void test_class_searchNonPrivateFields() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_searchNonPrivateFields, AssertionFailedError.class,
				"Attribut public static final int de.tum.in.test.integration.testuser.subject.structural.SomeClass.SOME_CONSTANT muss private sein."));
	}

	@TestTest
	void test_class_searchPublicOrProtectedMethods() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_searchPublicOrProtectedMethods,
				AssertionFailedError.class, "Methode doSomething(java.lang.String) darf nicht public sein."));
	}

	@TestTest
	void test_constructor_abstract() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_abstract, AssertionFailedError.class,
				"Objekt der Klasse de.tum.in.test.integration.testuser.subject.structural.SomeAbstractClass konnte nicht erzeugt werden, ist die Klasse abstrakt?"));
	}

	@TestTest
	void test_constructor_check() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(constructor_check));
	}

	@TestTest
	void test_constructor_illegalArguments() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_illegalArguments, AssertionFailedError.class,
				"Konstruktor de.tum.in.test.integration.testuser.subject.structural.SomeClass(java.lang.Integer) konnte Argumente mit den Typen (java.lang.String) nicht entgegennehmen."));
	}

	@TestTest
	void test_constructor_newInstance() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(constructor_newInstance));
	}

	@TestTest
	void test_constructor_notFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_notFound, AssertionFailedError.class,
				"Kein Konstruktor für de.tum.in.test.integration.testuser.subject.structural.SomeClass mit Parametern (java.lang.Boolean, java.lang.Boolean) gefunden."));
	}

	@TestTest
	void test_constructor_throwing() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_throwing, RuntimeException.class,
				"\n/// Mögliche Problemstelle: de.tum.in.test.integration.testuser.subject.structural.SomeClass.<init>(SomeClass.java:20) ///"));
	}

	@TestTest
	void test_field_check() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(field_check));
	}

	@TestTest
	void test_field_getInstance() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(field_getInstance));
	}

	@TestTest
	void test_field_getStaticNull() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_getStaticNull, AssertionFailedError.class,
				"Attribut [someAttribute] der Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass ist nicht statisch."));
	}

	@TestTest
	void test_field_getStaticSuccess() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(field_getStaticSuccess));
	}

	@TestTest
	void test_field_getWrongObject() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_getWrongObject, AssertionFailedError.class,
				"Attribut [someAttribute] von Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass wurde nicht auf einem passenden Objekt aufgerufen."));
	}

	@TestTest
	void test_field_getWrongType() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_getWrongType, AssertionFailedError.class,
				"Attribut [SOME_CONSTANT] konnte nicht gefunden werden."));
	}

	@TestTest
	void test_field_noSuchField() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_noSuchField, AssertionFailedError.class,
				"Attribut [a] konnte nicht gefunden werden."));
	}

	@TestTest
	void test_field_setOf() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(field_setOf));
	}

	@TestTest
	void test_field_setStaticFinal() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_setStaticFinal, AssertionFailedError.class,
				"Attribut [SOME_CONSTANT] der Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass kann nicht gesetzt werden, da es final ist."));
	}

	@TestTest
	void test_field_setStaticNull() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_setStaticNull, AssertionFailedError.class,
				"Attribut [someAttribute] der Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass ist nicht statisch."));
	}

	@TestTest
	void test_field_setStaticSuccess() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(field_setStaticSuccess));
	}

	@TestTest
	void test_field_setWrongObject() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_setWrongObject, AssertionFailedError.class,
				"Attribut [someAttribute] von Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass wurde nicht auf einem passenden Objekt aufgerufen."));
	}

	@TestTest
	void test_field_setWrongType() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_setWrongType, AssertionFailedError.class,
				"Attribut [someAttribute] der Klasse de.tum.in.test.integration.testuser.subject.structural.SomeClass vom Typ java.lang.String kann keine Instanz der Klasse class java.lang.Integer zugewiesen werden."));
	}

	@TestTest
	void test_method_badCast() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_badCast, AssertionFailedError.class,
				"Methode de.tum.in.test.integration.testuser.subject.structural.SomeClass.doSomethingElse(int) gibt nicht double zurück."));
	}

	@TestTest
	void test_method_check() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(method_check));
	}

	@TestTest
	void test_method_illegalArguments() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_illegalArguments, AssertionFailedError.class,
				"Methode de.tum.in.test.integration.testuser.subject.structural.SomeClass.doSomethingElse(int) konnte Argumente mit den Typen (java.lang.String) für Objekt vom Typ de.tum.in.test.integration.testuser.subject.structural.SomeClass nicht entgegennehmen."));
	}

	@TestTest
	void test_method_invoke() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(method_invoke));
	}

	@TestTest
	void test_method_notFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_notFound, AssertionFailedError.class,
				"Keine Methode void de.tum.in.test.integration.testuser.subject.structural.SomeClass.abc() gefunden."));
	}

	@TestTest
	void test_method_notStatic() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_notStatic, AssertionFailedError.class,
				"Methode de.tum.in.test.integration.testuser.subject.structural.SomeClass.throwException() ist nicht statisch."));
	}

	@TestTest
	void test_method_null() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_null, AssertionFailedError.class,
				"Methode de.tum.in.test.integration.testuser.subject.structural.SomeClass.throwException() konnte nicht aufgerufen werden, das Objekt ist null."));
	}

	@TestTest
	void test_method_throwing() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_throwing, RuntimeException.class,
				"\n/// Mögliche Problemstelle: de.tum.in.test.integration.testuser.subject.structural.SomeClass.throwException(SomeClass.java:61) ///"));
	}
}
