package de.tum.in.test.api.structural;

import static de.tum.in.test.api.localization.Messages.localizedFailure;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.json.JSONArray;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;

/**
 * This test evaluates if the specified constructors in the structure oracle are
 * correctly implemented with the expected parameter types and annotations,
 * based on its definition in the structure oracle (test.json).
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (2022-03-30)
 */
@API(status = Status.STABLE)
public abstract class ConstructorTestProvider extends StructuralTestProvider {

	/**
	 * This method collects the classes in the structure oracle file for which
	 * constructors are specified. These classes are then transformed into JUnit 5
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
			throw failure(
					"The ConstructorTest can only run if the structural oracle (test.json) is present. If you do not provide it, delete ConstructorTest.java!"); //$NON-NLS-1$
		for (var i = 0; i < structureOracleJSON.length(); i++) {
			var expectedClassJSON = structureOracleJSON.getJSONObject(i);

			// Only test the constructors if they are specified in the structure diff
			if (expectedClassJSON.has(JSON_PROPERTY_CLASS) && expectedClassJSON.has(JSON_PROPERTY_CONSTRUCTORS)) {
				var expectedClassPropertiesJSON = expectedClassJSON.getJSONObject(JSON_PROPERTY_CLASS);
				var expectedClassName = expectedClassPropertiesJSON.getString(JSON_PROPERTY_NAME);
				var expectedPackageName = expectedClassPropertiesJSON.getString(JSON_PROPERTY_PACKAGE);
				var expectedClassStructure = new ExpectedClassStructure(expectedClassName, expectedPackageName,
						expectedClassJSON);
				tests.add(dynamicTest("testConstructors[" + expectedClassName + "]", //$NON-NLS-1$ //$NON-NLS-2$
						() -> testConstructors(expectedClassStructure)));
			}
		}
		if (tests.isEmpty())
			throw failure(
					"No tests for constructors available in the structural oracle (test.json). Either provide constructor information or delete ConstructorTest.java!"); //$NON-NLS-1$
		/*
		 * Using a custom URI here to workaround surefire rendering the JUnit XML
		 * without the correct test names.
		 */
		return dynamicContainer(getClass().getName(), new URI(getClass().getName()), tests.stream());
	}

	/**
	 * This method gets passed the expected class structure generated by the method
	 * {@link #generateTestsForAllClasses()}, checks if the class is found at all in
	 * the assignment and then proceeds to check its constructors.
	 *
	 * @param expectedClassStructure The class structure that we expect to find and
	 *                               test against.
	 */
	protected static void testConstructors(ExpectedClassStructure expectedClassStructure) {
		var expectedClassName = expectedClassStructure.getExpectedClassName();
		var observedClass = findClassForTestType(expectedClassStructure, "constructor"); //$NON-NLS-1$
		if (expectedClassStructure.hasProperty(JSON_PROPERTY_CONSTRUCTORS)) {
			var expectedConstructors = expectedClassStructure.getPropertyAsJsonArray(JSON_PROPERTY_CONSTRUCTORS);
			checkConstructors(expectedClassName, observedClass, expectedConstructors);
		}
	}

	/**
	 * This method checks if a observed class' constructors match the expected ones
	 * defined in the structure oracle.
	 *
	 * @param expectedClassName    The simple name of the class, mainly used for
	 *                             error messages.
	 * @param observedClass        The class that needs to be checked as a Class
	 *                             object.
	 * @param expectedConstructors The information on the expected constructors
	 *                             contained in a JSON array. This information
	 *                             consists of the parameter types and the
	 *                             visibility modifiers.
	 */
	protected static void checkConstructors(String expectedClassName, Class<?> observedClass,
			JSONArray expectedConstructors) {
		for (var i = 0; i < expectedConstructors.length(); i++) {
			var expectedConstructor = expectedConstructors.getJSONObject(i);
			var expectedParameters = getExpectedJsonProperty(expectedConstructor, JSON_PROPERTY_PARAMETERS);
			var expectedModifiers = getExpectedJsonProperty(expectedConstructor, JSON_PROPERTY_MODIFIERS);
			var expectedAnnotations = getExpectedJsonProperty(expectedConstructor, JSON_PROPERTY_ANNOTATIONS);
			var strictParameterOrder = getExpectedJsonBooleanProperty(expectedConstructor, JSON_PROPERTY_STRICT_ORDER);

			var parametersAreRight = false;
			var modifiersAreRight = false;
			var annotationsAreRight = false;

			for (Constructor<?> observedConstructor : observedClass.getDeclaredConstructors()) {
				var observedParameters = observedConstructor.getParameterTypes();
				var observedModifiers = Modifier.toString(observedConstructor.getModifiers()).split(" "); //$NON-NLS-1$
				var observedAnnotations = observedConstructor.getAnnotations();

				parametersAreRight = checkParameters(observedParameters, expectedParameters, strictParameterOrder);
				modifiersAreRight = checkModifiers(observedModifiers, expectedModifiers);
				annotationsAreRight = checkAnnotations(observedAnnotations, expectedAnnotations);

				// If both are correct, then we found our constructor and we can break the loop
				if (parametersAreRight && modifiersAreRight && annotationsAreRight)
					break;
			}
			checkConstructorCorrectness(expectedClassName, expectedParameters, parametersAreRight, modifiersAreRight,
					annotationsAreRight);
		}
	}

	private static void checkConstructorCorrectness(String expectedClassName, JSONArray expectedParameters,
			boolean parametersAreCorrect, boolean modifiersAreCorrect, boolean annotationsAreCorrect) {
		String parameters = describeParameters(expectedParameters);
		if (!parametersAreCorrect)
			throw localizedFailure("structural.constructor.parameters", expectedClassName, parameters); //$NON-NLS-1$
		if (!modifiersAreCorrect)
			throw localizedFailure("structural.constructor.modifiers", expectedClassName, parameters); //$NON-NLS-1$
		if (!annotationsAreCorrect)
			throw localizedFailure("structural.constructor.annotations", expectedClassName, parameters); //$NON-NLS-1$
	}
}
