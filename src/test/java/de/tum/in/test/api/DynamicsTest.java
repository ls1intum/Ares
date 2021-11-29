package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;

import org.junit.platform.testkit.engine.Events;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.DynamicsUser;

@UserBased(DynamicsUser.class)
class DynamicsTest {

	@UserTestResults
	private static Events tests;

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
	private final String field_getStatic = "field_getStatic";
	private final String field_noSuchField = "field_noSuchField";
	private final String field_wrongType = "field_wrongType";
	private final String method_badCast = "method_badCast";
	private final String method_check = "method_check";
	private final String method_illegalArguments = "method_illegalArguments";
	private final String method_invoke = "method_invoke";
	private final String method_notFound = "method_notFound";
	private final String method_notStatic = "method_notStatic";
	private final String method_null = "method_null";
	private final String method_throwing = "method_throwing";

	@TestTest
	void test_class_check() {
		tests.assertThatEvents().haveExactly(1, event(test(class_check), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_class_isClass() {
		tests.assertThatEvents().haveExactly(1, event(test(class_isClass), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_class_notFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_notFound, AssertionFailedError.class,
				"Klasse/Interface DoesNotExist nicht gefunden"));
	}

	@TestTest
	void test_class_searchNonFinalFields() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_searchNonFinalFields, AssertionFailedError.class,
				"Attribut private java.lang.String de.tum.in.testuser.subject.structural.SomeClass.someAttribute muss final sein."));
	}

	@TestTest
	void test_class_searchNonPrivateFields() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_searchNonPrivateFields, AssertionFailedError.class,
				"Attribut public static final int de.tum.in.testuser.subject.structural.SomeClass.SOME_CONSTANT muss private sein."));
	}

	@TestTest
	void test_class_searchPublicOrProtectedMethods() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(class_searchPublicOrProtectedMethods,
				AssertionFailedError.class, "Methode doSomething(java.lang.String) ist public, sollte sie aber nicht"));
	}

	@TestTest
	void test_constructor_abstract() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_abstract, AssertionFailedError.class,
				"Objekt der Klasse de.tum.in.testuser.subject.structural.SomeAbstractClass konnte nicht erzeugt werden, ist die Klasse abstract?"));
	}

	@TestTest
	void test_constructor_check() {
		tests.assertThatEvents().haveExactly(1, event(test(constructor_check), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_constructor_illegalArguments() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_illegalArguments, AssertionFailedError.class,
				"Konstruktor de.tum.in.testuser.subject.structural.SomeClass(java.lang.Integer) konnte Parametertypen (java.lang.String) nicht entgegennehmen"));
	}

	@TestTest
	void test_constructor_newInstance() {
		tests.assertThatEvents().haveExactly(1, event(test(constructor_newInstance), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_constructor_notFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_notFound, AssertionFailedError.class,
				"Kein Konstruktor für de.tum.in.testuser.subject.structural.SomeClass mit Parametern (java.lang.Boolean, java.lang.Boolean) gefunden."));
	}

	@TestTest
	void test_constructor_throwing() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(constructor_throwing, RuntimeException.class,
				"\n/// Mögliche Problemstelle: de.tum.in.testuser.subject.structural.SomeClass.<init>(SomeClass.java:20) ///"));
	}

	@TestTest
	void test_field_check() {
		tests.assertThatEvents().haveExactly(1, event(test(field_check), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_field_getInstance() {
		tests.assertThatEvents().haveExactly(1, event(test(field_getInstance), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_field_getStatic() {
		tests.assertThatEvents().haveExactly(1, event(test(field_getStatic), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_field_noSuchField() {
		tests.assertThatEvents().haveExactly(1,
				testFailedWith(field_noSuchField, AssertionFailedError.class, "Feld [a] konnte nicht gefunden werden"));
	}

	@TestTest
	void test_field_wrongType() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(field_wrongType, AssertionFailedError.class,
				"Feld [some_constant] konnte nicht gefunden werden"));
	}

	@TestTest
	void test_method_badCast() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_badCast, AssertionFailedError.class,
				"Methode de.tum.in.testuser.subject.structural.SomeClass.doSomethingElse(int) gibt nicht double zurück"));
	}

	@TestTest
	void test_method_check() {
		tests.assertThatEvents().haveExactly(1, event(test(method_check), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_method_illegalArguments() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_illegalArguments, AssertionFailedError.class,
				"Methode de.tum.in.testuser.subject.structural.SomeClass.doSomethingElse(int) konnte Parametertypen (java.lang.String) für Objekt der Klasse de.tum.in.testuser.subject.structural.SomeClass nicht entgegennehmen"));
	}

	@TestTest
	void test_method_invoke() {
		tests.assertThatEvents().haveExactly(1, event(test(method_invoke), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_method_notFound() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_notFound, AssertionFailedError.class,
				"Keine Methode void de.tum.in.testuser.subject.structural.SomeClass.abc() gefunden."));
	}

	@TestTest
	void test_method_notStatic() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_notStatic, AssertionFailedError.class,
				"Methode de.tum.in.testuser.subject.structural.SomeClass.throwException() ist nicht statisch"));
	}

	@TestTest
	void test_method_null() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_null, AssertionFailedError.class,
				"Methode de.tum.in.testuser.subject.structural.SomeClass.throwException() konnte nicht aufgerufen werden, das Objekt ist null"));
	}

	@TestTest
	void test_method_throwing() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(method_throwing, RuntimeException.class,
				"\n/// Mögliche Problemstelle: de.tum.in.testuser.subject.structural.SomeClass.throwException(SomeClass.java:60) ///"));
	}
}
