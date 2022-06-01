package de.tum.in.test.api.structural;

import static de.tum.in.test.api.localization.Messages.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.json.*;
import org.opentest4j.AssertionFailedError;
import org.slf4j.*;

import de.tum.in.test.api.structural.testutils.*;

/**
 * This test and its subclasses evaluate if the following specified elements of
 * a given class in the structure oracle are correctly implemented (in case they
 * are specified):
 * <ol>
 * <li>{@link ClassTestProvider} checks the class itself, i.e. if a specified
 * superclass is extended and if specified interfaces and annotations are
 * implemented</li>
 * <li>{@link ConstructorTestProvider} checks declared constructors their
 * including access modifiers, annotations and parameters</li>
 * <li>{@link MethodTestProvider} checks the declared methods including their
 * access modifiers, annotations, parameters and return type</li>
 * <li>{@link AttributeTestProvider} checks the declared attributes including
 * their access modifiers, annotations and types and the declared enum values of
 * an enum</li>
 * </ol>
 * All these elements are tests based on the test.json that specifies the
 * structural oracle, i.e. how the solution has to look like in terms of
 * structural elements. Note: the file test.json can be automatically generated
 * in Artemis based on the difference between template and solution repository.
 * However, the file test.json needs to be manually adapted afterwards because
 * not all cases can be identified properly in an automatic manner.
 * <p>
 * To deactivate a check, simply remove the specified elements in the test.json
 * file. If no constructors should be tested for correctness, remove
 * {@link ConstructorTestProvider}, otherwise one test will fail (limitation of
 * JUnit). If no methods should be tested for correctness, remove
 * {@link MethodTestProvider}, otherwise one test will fail (limitation of
 * JUnit) If no attributes and no enums should be tested for correctness, remove
 * {@link AttributeTestProvider}, otherwise one test will fail (limitation of
 * JUnit)
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (2022-03-30)
 */
@API(status = Status.MAINTAINED)
public abstract class StructuralTestProvider {

	private static final Logger LOG = LoggerFactory.getLogger(StructuralTestProvider.class);

	protected static final String JSON_PROPERTY_SUPERCLASS = "superclass"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_INTERFACES = "interfaces"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_ANNOTATIONS = "annotations"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_MODIFIERS = "modifiers"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_PARAMETERS = "parameters"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_CONSTRUCTORS = "constructors"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_CLASS = "class"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_ATTRIBUTES = "attributes"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_METHODS = "methods"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_PACKAGE = "package"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_NAME = "name"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_TYPE = "type"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_RETURN_TYPE = "returnType"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_ENUM_VALUES = "enumValues"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_STRICT_ORDER = "strictOrder"; //$NON-NLS-1$
	protected static final String JSON_PROPERTY_OPTIONAL = "optional"; //$NON-NLS-1$

	private static final String PACKAGE_PATH_SEPARATOR = "."; //$NON-NLS-1$
	private static final Pattern PACKAGE_NAME_IN_GENERIC_TYPE = Pattern.compile("(?:[^\\[\\]<>?,\\s.]++\\.)++"); //$NON-NLS-1$

	protected static JSONArray structureOracleJSON;

	protected StructuralTestProvider() {
		// make constructor only protected
	}

	/**
	 * Scans the project and returns the class, if it's found. If not, return the
	 * message of the NamesScanner.
	 *
	 * @param expectedClassStructure The class structure that we expect to find a
	 *                               class for.
	 * @param typeOfTest             The name of the test type that currently called
	 *                               the NamesScanner. The name is displayed in the
	 *                               feedback, if it's negative.
	 * @return The current class that undergoes the tests, never null.
	 */
	protected static Class<?> findClassForTestType(ExpectedClassStructure expectedClassStructure, String typeOfTest) {
		var classNameScanner = new ClassNameScanner(expectedClassStructure.getExpectedClassName(),
				expectedClassStructure.getExpectedPackageName());
		var scanResultEnum = classNameScanner.getScanResult().getResult();
		var classNameScanMessage = classNameScanner.getScanResult().getMessage();
		// please note: inner classes are not supported
		if (!ScanResultType.CORRECT_NAME_CORRECT_PLACE.equals(scanResultEnum))
			throw failure(classNameScanMessage);
		try {
			return Class.forName(expectedClassStructure.getQualifiedClassName(), false,
					StructuralTestProvider.class.getClassLoader());
		} catch (@SuppressWarnings("unused") ClassNotFoundException e) {
			// Note: this error happens when the ClassNameScanner finds the correct file,
			// e.g. 'Course.java', but the class 'Course' was not yet created correctly in
			// this file.
			throw localizedFailure("structural.common.classLoadFailed", typeOfTest, classNameScanMessage); //$NON-NLS-1$
		}
	}

	/**
	 * get the expected elements or an empty JSON array
	 *
	 * @param element         the class, attribute, method or constructor JSON
	 *                        object element
	 * @param jsonPropertyKey the key used in JSON
	 * @return a JSON array of the elements (can be empty)
	 */
	protected static JSONArray getExpectedJsonProperty(JSONObject element, String jsonPropertyKey) {
		return element.has(jsonPropertyKey) ? element.getJSONArray(jsonPropertyKey) : new JSONArray();
	}

	/**
	 * get the expected boolean value of the element or false
	 *
	 * @param element         the class, attribute, method or constructor JSON
	 *                        object element
	 * @param jsonPropertyKey the key used in JSON
	 * @return a boolean as specified in the JSON or false if not present
	 */
	protected static boolean getExpectedJsonBooleanProperty(JSONObject element, String jsonPropertyKey) {
		return element.has(jsonPropertyKey) && element.getBoolean(jsonPropertyKey);
	}

	/**
	 * This method checks if the visibility modifiers of the observed structural
	 * element match the ones in the expected structural element.
	 *
	 * @param observedModifiers The observed modifiers as a string array.
	 * @param expectedModifiers The expected modifiers as a JSONArray.
	 * @return True if they match, false otherwise.
	 */
	protected static boolean checkModifiers(String[] observedModifiers, JSONArray expectedModifiers) {
		/*
		 * If both the observed and expected elements have no modifiers, then they
		 * match. A note: for technical reasons, we get in case of no observed
		 * modifiers, a string array with an empty string.
		 */
		if (Arrays.equals(observedModifiers, new String[] { "" }) && expectedModifiers.length() == 0) //$NON-NLS-1$
			return true;
		/*
		 * Otherwise check if all expected necessary modifiers are contained in the
		 * array of the observed ones and if any forbidden modifiers were used.
		 */
		Set<ModifierSpecification> modifierSpecifications = new HashSet<>();
		for (var i = 0; i < expectedModifiers.length(); i++)
			modifierSpecifications.add(ModifierSpecification.getModifierForJsonString(expectedModifiers.getString(i)));
		Set<String> observedModifiersSet = Set.of(observedModifiers);
		Set<String> allowedModifiers = modifierSpecifications.stream().map(ModifierSpecification::getModifier)
				.collect(Collectors.toSet());
		boolean hasAllNecessaryModifiers = modifierSpecifications.stream().filter(ModifierSpecification::isRequired)
				.map(ModifierSpecification::getModifier).allMatch(observedModifiersSet::contains);
		boolean hasForbiddenModifier = observedModifiersSet.stream()
				.anyMatch(modifier -> !allowedModifiers.contains(modifier));

		return hasAllNecessaryModifiers && !hasForbiddenModifier;
	}

	private static final class ModifierSpecification {

		private final String modifier;
		private final boolean optional;

		private ModifierSpecification(String modifier, boolean optional) {
			this.modifier = Objects.requireNonNull(modifier);
			this.optional = optional;
		}

		String getModifier() {
			return modifier;
		}

		boolean isRequired() {
			return !optional;
		}

		static ModifierSpecification getModifierForJsonString(String jsonString) {
			String[] sections = jsonString.split(":", -1); //$NON-NLS-1$
			if (sections.length == 1)
				return new ModifierSpecification(jsonString, false);
			if (JSON_PROPERTY_OPTIONAL.equals(sections[0]))
				return new ModifierSpecification(sections[1].trim(), true);
			throw new IllegalArgumentException("Invalid entry for modifier: '" + jsonString + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	protected static boolean checkAnnotations(Annotation[] observedAnnotations, JSONArray expectedAnnotations) {
		/*
		 * If both the observed and expected elements have no annotations, then they
		 * match. A note: for technical reasons, we get in case of no observed
		 * annotations, a string array with an empty string.
		 */
		if (observedAnnotations.length == 0 && expectedAnnotations.length() == 0)
			return true;
		/*
		 * If the number of the annotations does not match, then the annotations per se
		 * do not match either.
		 */
		if (observedAnnotations.length != expectedAnnotations.length())
			return false;
		/*
		 * Otherwise check if each expected annotation is contained in the array of the
		 * observed ones. If at least one isn't, then the modifiers don't match.
		 */
		for (Object expectedAnnotation : expectedAnnotations) {
			var expectedAnnotationFound = false;
			var expectedAnnotationAsString = (String) expectedAnnotation;
			for (Annotation observedAnnotation : observedAnnotations)
				if (checkExpectedType(observedAnnotation.annotationType(), observedAnnotation.annotationType(),
						expectedAnnotationAsString)) {
					expectedAnnotationFound = true;
					break;
				}
			if (!expectedAnnotationFound)
				return false;
		}
		return true;
	}

	/**
	 * This method checks if the parameters of the actual structural element (in
	 * this case method or constructor) match the ones in the expected structural
	 * element.
	 *
	 * @param observedParameters The actual parameter types as a classes array.
	 * @param expectedParameters The expected parameter type names as a JSONArray.
	 * @return True if they match, false otherwise.
	 */
	protected static boolean checkParameters(Class<?>[] observedParameters, JSONArray expectedParameters,
			boolean strictOrder) {
		/*
		 * If both the observed and expected elements have no parameters, then they
		 * match.
		 */
		if (observedParameters.length == 0 && expectedParameters.length() == 0)
			return true;
		/*
		 * If the number of parameters do not match, then the parameters cannot match
		 * either.
		 */
		if (observedParameters.length != expectedParameters.length())
			return false;
		var expectedParameterTypeNames = new String[expectedParameters.length()];
		for (var i = 0; i < expectedParameters.length(); i++)
			expectedParameterTypeNames[i] = expectedParameters.getString(i);

		var observedParameterTypeNames = new String[observedParameters.length];
		for (var i = 0; i < observedParameters.length; i++)
			// TODO: Canonical names should be supported as well.
			observedParameterTypeNames[i] = observedParameters[i].getSimpleName();

		if (strictOrder) {
			return Arrays.equals(expectedParameterTypeNames, observedParameterTypeNames);
		}

		/*
		 * Create hash tables to store how often a parameter type occurs. Checking the
		 * occurrences of a certain parameter type is enough, since the parameter order
		 * is not relevant to us.
		 */
		Map<String, Integer> expectedParametersHashtable = createParametersHashMap(expectedParameterTypeNames);
		Map<String, Integer> observedParametersHashtable = createParametersHashMap(observedParameterTypeNames);

		return expectedParametersHashtable.equals(observedParametersHashtable);
	}

	/**
	 * Returns a string describing the given parameter array.
	 * <p>
	 * This may be something like {@code "no parameters"} in case of empty parameter
	 * arrays.
	 *
	 * @param expectedParameters the parameters to describe.
	 * @return A localized string describing the parameters.
	 */
	protected static String describeParameters(JSONArray expectedParameters) {
		return expectedParameters.isEmpty() ? localized("structural.common.noParams") //$NON-NLS-1$
				: localized("structural.common.withParams", expectedParameters); //$NON-NLS-1$
	}

	/**
	 * This method checks whether the actual type of any implemented structural
	 * element matches its expected name.
	 *
	 * @param actualClass       The class of the structural element
	 * @param actualGenericType The actual generic type of the structural element
	 * @param expectedTypeName  The expected name, provided as a simple or canonical
	 *                          (generic) name.
	 * @return True if the names match, false if not.
	 */
	protected static boolean checkExpectedType(Class<?> actualClass, Type actualGenericType, String expectedTypeName) {
		var expectedTypeIsGeneric = expectedTypeName.contains("<") && expectedTypeName.contains(">"); //$NON-NLS-1$ //$NON-NLS-2$
		String actualName;
		if (expectedTypeIsGeneric) {
			actualName = actualGenericType.getTypeName();
		} else {
			actualName = actualClass.getCanonicalName();
			if (actualName == null)
				actualName = actualClass.getName();
		}
		var actualSimpleName = PACKAGE_NAME_IN_GENERIC_TYPE.matcher(actualName).replaceAll(""); //$NON-NLS-1$
		/*
		 * If the given expected name contains a '.' it can be assumed that it
		 * represents a full canonical name. If it does not, we can assume it represents
		 * a simple name.
		 */
		if (expectedTypeName.contains(PACKAGE_PATH_SEPARATOR))
			return expectedTypeName.equals(actualName);
		return expectedTypeName.equals(actualSimpleName);
	}

	/**
	 * This method creates a hash table where the name of a parameter type is hashed
	 * to the number of the occurrences in the passed string collection.
	 *
	 * @param parameterTypeNames the parameters for which the hash table should be
	 *                           produced
	 * @return the hash table with the number of occurrences
	 */
	protected static Map<String, Integer> createParametersHashMap(String... parameterTypeNames) {
		Map<String, Integer> parametersHashTable = new HashMap<>();
		for (String parameterTypeName : parameterTypeNames) {
			if (!parametersHashTable.containsKey(parameterTypeName)) {
				parametersHashTable.put(parameterTypeName, 1);
			} else {
				var currentParameterCount = parametersHashTable.get(parameterTypeName);
				parametersHashTable.replace(parameterTypeName, currentParameterCount, currentParameterCount + 1);
			}
		}
		return parametersHashTable;
	}

	/**
	 * This method retrieves the JSON array in the structure oracle.
	 *
	 * @param structureOracleFileUrl The file url of the structure oracle file,
	 *                               which is used for the structural tests.
	 * @return The JSONArray representation of the structure oracle.
	 */
	protected static JSONArray retrieveStructureOracleJSON(URL structureOracleFileUrl) {
		if (structureOracleFileUrl == null)
			return null;
		var result = new StringBuilder();
		try (var bufferedReader = new BufferedReader(new InputStreamReader(structureOracleFileUrl.openStream()))) {
			var buffer = new char[8192];
			int length;
			while ((length = bufferedReader.read(buffer, 0, buffer.length)) != -1)
				result.append(buffer, 0, length);
		} catch (IOException e) {
			LOG.error("Could not open stream from URL: {}", structureOracleFileUrl, e); //$NON-NLS-1$
		}
		return new JSONArray(result.toString());
	}

	/**
	 * Container for a class that is tested including a JSON of the structure that
	 * we expect to find and test against.
	 *
	 * @author Christian Femers
	 */
	protected static class ExpectedClassStructure {

		private final String expectedClassName;
		private final String expectedPackageName;
		private final JSONObject expectedClassJson;

		public ExpectedClassStructure(String expectedClassName, String expectedPackageName,
				JSONObject expectedClassJson) {
			this.expectedClassName = Objects.requireNonNull(expectedClassName);
			this.expectedPackageName = Objects.requireNonNull(expectedPackageName);
			this.expectedClassJson = Objects.requireNonNull(expectedClassJson);
		}

		public String getExpectedClassName() {
			return expectedClassName;
		}

		public String getExpectedPackageName() {
			return expectedPackageName;
		}

		public JSONObject getExpectedClassJson() {
			return expectedClassJson;
		}

		public String getQualifiedClassName() {
			if (!expectedPackageName.isEmpty()) {
				return expectedPackageName + PACKAGE_PATH_SEPARATOR + expectedClassName;
			}
			return expectedClassName;
		}

		public boolean hasProperty(String propertyName) {
			return getExpectedClassJson().has(propertyName);
		}

		public JSONObject getPropertyAsJsonObject(String propertyName) {
			return getExpectedClassJson().getJSONObject(propertyName);
		}

		public JSONArray getPropertyAsJsonArray(String propertyName) {
			return getExpectedClassJson().getJSONArray(propertyName);
		}
	}

	protected static AssertionFailedError failure(String message) {
		return new AssertionFailedError(message);
	}
}
