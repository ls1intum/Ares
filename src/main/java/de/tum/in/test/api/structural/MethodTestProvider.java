package de.tum.in.test.api.structural;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;

/**
 * This test evaluates if the specified methods in the structure oracle are
 * correctly implemented with the expected name, return type, parameter types,
 * visibility modifiers and annotations, based on its definition in the
 * structure oracle (test.json)
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.0 (11.11.2020)
 */
public abstract class MethodTestProvider extends StructuralTestProvider {

	/**
	 * This method collects the classes in the structure oracle file for which
	 * methods are specified. These classes are then transformed into JUnit 5
	 * dynamic tests.
	 *
	 * @return A dynamic test container containing the test for each class which is
	 *         then executed by JUnit.
	 * @throws URISyntaxException an exception if the URI of the class name cannot
	 *                            be generated (which seems to be unlikely)
	 */
	protected DynamicContainer generateTestsForAllClasses() throws URISyntaxException {
		List<DynamicNode> tests = new ArrayList<>();
		if (structureOracleJSON == null)
			fail("The MethodTest test can only run if the structural oracle (test.json) is present. If you do not provide it, delete MethodTest.java!");
		for (var i = 0; i < structureOracleJSON.length(); i++) {
			var expectedClassJSON = structureOracleJSON.getJSONObject(i);
			// Only test the classes that have methods defined in the structure oracle.
			if (expectedClassJSON.has(JSON_PROPERTY_CLASS) && expectedClassJSON.has(JSON_PROPERTY_METHODS)) {
				var expectedClassPropertiesJSON = expectedClassJSON.getJSONObject(JSON_PROPERTY_CLASS);
				var expectedClassName = expectedClassPropertiesJSON.getString(JSON_PROPERTY_NAME);
				var expectedPackageName = expectedClassPropertiesJSON.getString(JSON_PROPERTY_PACKAGE);
				var expectedClassStructure = new ExpectedClassStructure(expectedClassName, expectedPackageName,
						expectedClassJSON);
				tests.add(dynamicTest("testMethods[" + expectedClassName + "]",
						() -> testMethods(expectedClassStructure)));
			}
		}
		if (tests.isEmpty())
			fail("No tests for methods available in the structural oracle (test.json). Either provide attributes information or delete MethodTest.java!");
		/*
		 * Using a custom URI here to workaround surefire rendering the JUnit XML
		 * without the correct test names.
		 */
		return dynamicContainer(getClass().getName(), new URI(getClass().getName()), tests.stream());
	}

	/**
	 * This method gets passed the expected class structure generated by the method
	 * {@link #generateTestsForAllClasses()}, checks if the class is found at all in
	 * the assignment and then proceeds to check its methods.
	 *
	 * @param expectedClassStructure The class structure that we expect to find and
	 *                               test against.
	 */
	public static void testMethods(ExpectedClassStructure expectedClassStructure) {
		var expectedClassName = expectedClassStructure.getExpectedClassName();
		var observedClass = findClassForTestType(expectedClassStructure, "method");
		if (observedClass == null) {
			fail(THE_CLASS + expectedClassName + " was not found for method test");
			return;
		}
		if (expectedClassStructure.hasProperty(JSON_PROPERTY_METHODS)) {
			var methodsJSON = expectedClassStructure.getPropertyAsJsonArray(JSON_PROPERTY_METHODS);
			checkMethods(expectedClassName, observedClass, methodsJSON);
		}
	}

	/**
	 * This method checks if a observed class' methods match the expected ones
	 * defined in the structure oracle.
	 *
	 * @param expectedClassName The simple name of the class, mainly used for error
	 *                          messages.
	 * @param observedClass     The class that needs to be checked as a Class
	 *                          object.
	 * @param expectedMethods   The information on the expected methods contained in
	 *                          a JSON array. This information consists of the name,
	 *                          parameter types, return type and the visibility
	 *                          modifiers of each method.
	 */
	protected static void checkMethods(String expectedClassName, Class<?> observedClass, JSONArray expectedMethods) {
		for (var i = 0; i < expectedMethods.length(); i++) {
			var expectedMethod = expectedMethods.getJSONObject(i);
			var expectedName = expectedMethod.getString(JSON_PROPERTY_NAME);
			var expectedParameters = getExpectedJsonProperty(expectedMethod, JSON_PROPERTY_PARAMETERS);
			var expectedModifiers = getExpectedJsonProperty(expectedMethod, JSON_PROPERTY_MODIFIERS);
			var expectedAnnotations = getExpectedJsonProperty(expectedMethod, JSON_PROPERTY_ANNOTATIONS);
			var expectedReturnType = expectedMethod.getString(JSON_PROPERTY_RETURN_TYPE);
			boolean strictParameterOrder = getExpectedJsonBooleanProperty(expectedMethod, JSON_PROPERTY_Strict_Order);
			var checks = new MethodChecks();
			for (Method observedMethod : observedClass.getDeclaredMethods()) {
				// TODO: check if overloading is supported properly
				if (expectedName.equals(observedMethod.getName())) {
					checks.name = true;
					checks.parameters = checkParameters(observedMethod.getParameterTypes(), expectedParameters,
							strictParameterOrder);
					checks.modifiers = checkModifiers(Modifier.toString(observedMethod.getModifiers()).split(" "),
							expectedModifiers);
					checks.annotations = checkAnnotations(observedMethod.getAnnotations(), expectedAnnotations);
					checks.returnType = checkExpectedType(observedMethod.getReturnType(),
							observedMethod.getGenericReturnType(), expectedReturnType);

					// If all are correct, then we found the desired method and we can break the
					// loop
					if (checks.hasPassedAll())
						break;
				}
				/*
				 * TODO: we should also take wrong case and typos into account (i.e. the else
				 * case)
				 */
			}
			checkMethodCorrectness(expectedClassName, expectedName, expectedParameters, checks);
		}
	}

	private static void checkMethodCorrectness(String expectedClassName, String expectedName,
			JSONArray expectedParameters, MethodChecks methodChecks) {
		var expectedMethodInformation = "the expected method '" + expectedName + "' of the class '" + expectedClassName
				+ "' with " + ((expectedParameters.length() == 0) ? "no parameters"
						: "the parameters: " + expectedParameters.toString());
		if (!methodChecks.name)
			fail(expectedMethodInformation + " was not found or is named wrongly.");
		if (!methodChecks.parameters)
			fail("The parameters of " + expectedMethodInformation + NOT_IMPLEMENTED_AS_EXPECTED);
		if (!methodChecks.modifiers)
			fail("The modifiers (access type, abstract, etc.) of " + expectedMethodInformation
					+ NOT_IMPLEMENTED_AS_EXPECTED);
		if (!methodChecks.annotations)
			fail("The annotation(s) of " + expectedMethodInformation + NOT_IMPLEMENTED_AS_EXPECTED);
		if (!methodChecks.returnType)
			fail("The return type of " + expectedMethodInformation + " is not implemented as expected.");
	}

	private static class MethodChecks {
		boolean name;
		boolean parameters;
		boolean modifiers;
		boolean annotations;
		boolean returnType;

		boolean hasPassedAll() {
			return name && parameters && modifiers && annotations && returnType;
		}
	}
}
