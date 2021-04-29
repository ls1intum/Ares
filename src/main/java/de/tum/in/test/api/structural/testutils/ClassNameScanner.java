package de.tum.in.test.api.structural.testutils;

import static de.tum.in.test.api.structural.testutils.ScanResultType.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

/**
 * This class scans the submission project if the current expected class is
 * actually present in it or not. The result is returned as an instance of
 * ScanResult. The ScanResult consists of a ScanResultType and a
 * ScanResultMessage as a string. ScanResultType is an enum and is implemented
 * so that identifying just the type of the error and the binding of several
 * messages to a certain result is possible.
 * <p>
 * There are the following possible results:
 * <ul>
 * <li>The class has the correct name and is placed in the correct package.</li>
 * <li>The class has the correct name but is misplaced.</li>
 * <li>The class name has wrong casing, but is placed in the correct
 * package.</li>
 * <li>The class name has wrong casing and is misplaced.</li>
 * <li>The class name has typos, but is placed in the correct package.</li>
 * <li>The class name has typos and is misplaced.</li>
 * <li>The class name has too many typos, thus is declared as not found.</li>
 * <li>Undefined, which is used to initialize the scan result.</li>
 * </ul>
 * <p>
 * A note on the limit of allowed number of typos: the maximal number depends on
 * the length of the class name and is defined as ceiling(classNameLength / 4).
 *
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.0 (11.11.2020)
 */
public class ClassNameScanner {

	private static final Logger LOG = LoggerFactory.getLogger(ClassNameScanner.class);

	/**
	 * Jaro-Winkler string similarity for typo correction to compute the overall
	 * similarity
	 */
	private static final JaroWinkler JARO_WINKLER = new JaroWinkler();
	/**
	 * Normalized Levenshtein as alternative to spot some typos better (e.g. in
	 * longer strings)
	 */
	private static final NormalizedLevenshtein NORMALIZED_LEVENSHTEIN = new NormalizedLevenshtein();
	/**
	 * Damerau-Levenshtein to compute the edit distance to have an absolute value
	 * for the amount of difference
	 */
	private static final Damerau DAMERAU_LEVENSHTEIN = new Damerau();

	private static final String DEVIATES_FROM_THE_EXPECTATION = ", which deviates from the expectation.";
	private static final String IMPLEMENTED_A_CLASS = "We found that you implemented a class ";
	private static final String THE_CLASS = "The class ";
	private static final String EXPECTS_CLASS_WITH_NAME = "The exercise expects a class with the name ";
	private static final String IN_THE_PACKAGE = " in the package ";
	private static final String CORRECT_NAME = " has the correct name";

	/*
	 * The class name and package name of the expected class that is currently being
	 * searched after.
	 */
	private final String expectedClassName;
	private final String expectedPackageName;

	/**
	 * Mapping between the class name and the observed package names (list) in the
	 * project
	 */
	private final Map<String, List<String>> observedClasses = new HashMap<>();
	private final ScanResult scanResult;

	private static String pomXmlPath = "pom.xml";

	public ClassNameScanner(String expectedClassName, String expectedPackageName) {
		this.expectedClassName = expectedClassName;
		this.expectedPackageName = expectedPackageName;
		findObservedClassesInProject();
		this.scanResult = computeScanResult();
	}

	public ScanResult getScanResult() {
		return this.scanResult;
	}

	/**
	 * This method computes the scan result of the submission for the expected class
	 * name. It first checks if the class is in the project at all. If that's the
	 * case, it then checks if that class is properly placed or not and generates
	 * feedback accordingly. Otherwise the method loops over the observed classes
	 * and checks if any of the observed classes is actually the expected one but
	 * with the wrong case or types in the name. It again checks in each case if the
	 * class is misplaced or not and delivers the feedback. Finally, in none of
	 * these holds, the class is simply declared as not found.
	 *
	 * @return An instance of ScanResult containing the result type and the feedback
	 *         message.
	 */
	private ScanResult computeScanResult() {
		if (observedClasses.containsKey(expectedClassName)) {
			// the class was found in the correct package
			List<String> observedPackageNames = observedClasses.get(expectedClassName);
			return createScanResult(getScanResultTypeClassFound(observedPackageNames), expectedClassName,
					observedPackageNames.toString());
		}
		/*
		 * if the class was NOT found in the correct package, we try to to find it in a
		 * different package or try to find similar classes (e.g. with typos)
		 */
		for (var observedClass : observedClasses.entrySet()) {
			var foundObservedClassName = observedClass.getKey();
			var observedPackageNames = observedClass.getValue();
			var foundObservedPackageNames = observedPackageNames.toString();

			boolean classPresentMultiple = observedPackageNames.size() > 1;
			boolean classCorrectlyPlaced = !classPresentMultiple
					&& (observedPackageNames.contains(expectedPackageName));
			/*
			 * 1) check whether the class might have the wrong case
			 */
			if (foundObservedClassName.equalsIgnoreCase(expectedClassName)) {
				return createScanResult(foundObservedClassName, foundObservedPackageNames, classPresentMultiple,
						classCorrectlyPlaced, WRONG_CASE_MULTIPLE, WRONG_CASE_CORRECT_PLACE, WRONG_CASE_MISPLACED);
			}
			/*
			 * 2) check whether there are similar classes (e.g. the student has a small typo
			 * in the class name)
			 */
			if (isMisspelledWithHighProbability(this.expectedClassName, foundObservedClassName)) {
				return createScanResult(foundObservedClassName, foundObservedPackageNames, classPresentMultiple,
						classCorrectlyPlaced, TYPOS_MULTIPLE, TYPOS_CORRECT_PLACE, TYPOS_MISPLACED);
			}
		}
		return createScanResult(ScanResultType.NOTFOUND, expectedClassName, null);
	}

	private ScanResult createScanResult(String foundObservedClassName, String foundObservedPackageName,
			boolean classPresentMultiple, boolean classCorrectlyPlaced, ScanResultType multipleTimes,
			ScanResultType correctPlace, ScanResultType misplaced) {
		ScanResultType scanResultType;
		if (classPresentMultiple) {
			scanResultType = multipleTimes;
		} else {
			scanResultType = classCorrectlyPlaced ? correctPlace : misplaced;
		}
		return createScanResult(scanResultType, foundObservedClassName, foundObservedPackageName);
	}

	private ScanResultType getScanResultTypeClassFound(List<String> observedPackageNames) {
		ScanResultType scanResultType;
		boolean classIsPresentMultipleTimes = observedPackageNames.size() > 1;
		boolean classIsCorrectlyPlaced = !classIsPresentMultipleTimes
				&& (observedPackageNames.contains(expectedPackageName));

		if (classIsPresentMultipleTimes) {
			scanResultType = CORRECT_NAME_MULTIPLE;
		} else {
			scanResultType = classIsCorrectlyPlaced ? CORRECT_NAME_CORRECT_PLACE : CORRECT_NAME_MISPLACED;
		}
		return scanResultType;
	}

	private ScanResult createScanResult(ScanResultType scanResultType, String foundObservedClassName,
			String foundObservedPackageName) {
		String scanResultMessage;
		switch (scanResultType) {
		case CORRECT_NAME_CORRECT_PLACE:
			scanResultMessage = THE_CLASS + foundObservedClassName + CORRECT_NAME + " and is in the correct package.";
			break;
		case CORRECT_NAME_MISPLACED:
			scanResultMessage = THE_CLASS + foundObservedClassName + CORRECT_NAME + "," + " but the package it's in, "
					+ foundObservedPackageName + ", deviates from the expectation."
					+ "  Make sure it is placed in the correct package.";
			break;
		case CORRECT_NAME_MULTIPLE:
			scanResultMessage = THE_CLASS + foundObservedClassName + CORRECT_NAME + ","
					+ " but it is located multiple times in the project and in the packages: "
					+ foundObservedPackageName + DEVIATES_FROM_THE_EXPECTATION
					+ " Make sure to place the class in the correct package and remove any superfluous ones.";
			break;
		case WRONG_CASE_CORRECT_PLACE:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + ". " + IMPLEMENTED_A_CLASS
					+ foundObservedClassName + DEVIATES_FROM_THE_EXPECTATION
					+ " Check for wrong upper case / lower case lettering.";
			break;
		case WRONG_CASE_MISPLACED:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + IN_THE_PACKAGE + expectedPackageName
					+ ". " + IMPLEMENTED_A_CLASS + foundObservedClassName + "," + IN_THE_PACKAGE
					+ foundObservedPackageName + DEVIATES_FROM_THE_EXPECTATION
					+ " Check for wrong upper case / lower case lettering and make sure you place it in the correct package.";
			break;
		case WRONG_CASE_MULTIPLE:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + IN_THE_PACKAGE + expectedPackageName
					+ ". " + IMPLEMENTED_A_CLASS + foundObservedClassName + "," + IN_THE_PACKAGE
					+ foundObservedPackageName + DEVIATES_FROM_THE_EXPECTATION
					+ " Check for wrong upper case / lower case lettering and make sure you place one class in the correct package and remove any superfluous classes.";
			break;
		case TYPOS_CORRECT_PLACE:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + ". " + IMPLEMENTED_A_CLASS
					+ foundObservedClassName + DEVIATES_FROM_THE_EXPECTATION + " Check for typos in the class name.";
			break;
		case TYPOS_MISPLACED:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + IN_THE_PACKAGE + expectedPackageName
					+ ". " + IMPLEMENTED_A_CLASS + foundObservedClassName + "," + IN_THE_PACKAGE
					+ foundObservedPackageName + DEVIATES_FROM_THE_EXPECTATION
					+ " Check for typos in the class name and make sure you place it in the correct package.";
			break;
		case TYPOS_MULTIPLE:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + IN_THE_PACKAGE + expectedPackageName
					+ ". " + IMPLEMENTED_A_CLASS + foundObservedClassName + "," + IN_THE_PACKAGE
					+ observedClasses.get(foundObservedClassName).toString() + DEVIATES_FROM_THE_EXPECTATION
					+ " Check for typos in the class name and make sure you place one class it in the correct package and remove any superfluous classes.";
			break;
		case NOTFOUND:
			scanResultMessage = EXPECTS_CLASS_WITH_NAME + expectedClassName + IN_THE_PACKAGE + expectedPackageName
					+ ". You did not implement the class in the exercise.";
			break;
		default:
			scanResultMessage = "The class could not be scanned.";
			break;
		}
		return new ScanResult(scanResultType, scanResultMessage);
	}

	/**
	 * This method retrieves the actual type names and their packages by walking the
	 * project file structure. The root node (which is the assignment folder) is
	 * defined in the pom.xml file of the project.
	 */
	private void findObservedClassesInProject() {
		try {
			var pomFile = new File(pomXmlPath);
			var documentBuilderFactory = DocumentBuilderFactory.newInstance();
			// make sure to avoid loading external files which would not be compliant
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			var documentBuilder = documentBuilderFactory.newDocumentBuilder();
			var pomXmlDocument = documentBuilder.parse(pomFile);

			NodeList buildNodes = pomXmlDocument.getElementsByTagName("build");
			for (var i = 0; i < buildNodes.getLength(); i++) {
				var buildNode = buildNodes.item(i);
				if (buildNode.getNodeType() == Node.ELEMENT_NODE) {
					var buildNodeElement = (Element) buildNode;
					var sourceDirectoryPropertyValue = buildNodeElement.getElementsByTagName("sourceDirectory").item(0)
							.getTextContent();
					var assignmentFolderName = sourceDirectoryPropertyValue
							.substring(sourceDirectoryPropertyValue.indexOf("}") + 2);
					walkProjectFileStructure(assignmentFolderName, new File(assignmentFolderName), observedClasses);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOG.error("Could not retrieve the source directory from the pom.xml file. Contact your instructor.", e);
		}
	}

	/**
	 * This method recursively walks the actual folder file structure starting from
	 * the assignment folder and adds each type it finds e.g. filenames ending with
	 * <code>.java</code> and <code>.kt</code> to the passed JSON object.
	 *
	 * @param assignmentFolderName The root folder where the method starts walking
	 *                             the project structure.
	 * @param node                 The current node the method is visiting.
	 * @param foundClasses         The JSON object where the type names and packages
	 *                             get appended.
	 */
	private void walkProjectFileStructure(String assignmentFolderName, File node,
			Map<String, List<String>> foundClasses) {
		// Example:
		// * assignmentFolderName: assignment/src
		// * fileName: assignment/src/de/tum/in/ase/eist/BubbleSort.java
		// Required Package Name: de.tum.in.ase.eist
		var fileName = node.getName();
		// support Java and Kotlin files
		if (fileName.endsWith(".java") || fileName.endsWith(".kt")) {
			var fileNameComponents = fileName.split("\\.");
			var fileExtension = fileNameComponents[fileNameComponents.length - 1];

			var className = fileNameComponents[fileNameComponents.length - 2];
			var packageName = node.getPath().substring(0, node.getPath().indexOf("." + fileExtension));
			packageName = packageName.substring(
					packageName.indexOf(assignmentFolderName) + assignmentFolderName.length() + 1,
					packageName.lastIndexOf(File.separator + className));
			packageName = packageName.replace(File.separatorChar, '.');

			if (packageName.charAt(0) == '.') {
				packageName = packageName.substring(1);
			}
			if (foundClasses.containsKey(className)) {
				foundClasses.get(className).add(packageName);
			} else {
				foundClasses.put(className, Collections.singletonList(packageName));
			}
		}
		// TODO: we should also support inner classes here
		if (node.isDirectory()) {
			String[] subNodes = node.list();
			if (subNodes != null && subNodes.length > 0) {
				for (String currentSubNode : subNodes) {
					walkProjectFileStructure(assignmentFolderName, new File(node, currentSubNode), foundClasses);
				}
			}
		}
	}

	public static String getPomXmlPath() {
		return pomXmlPath;
	}

	public static void setPomXmlPath(String path) {
		pomXmlPath = path;
	}

	static boolean isMisspelledWithHighProbability(String a, String b) {
		/**
		 * This based on observations and experiments with
		 * https://github.com/src-d/datasets/tree/master/Typos and collections of real
		 * (not misspelled) classes.
		 */
		/*
		 * This is a fast check which should work often and not be a problem for the
		 * user to spot (as this requires a significant length difference). Such "long"
		 * typos seem to occur almost never by accident.
		 */
		int lengthDifferenceAbs = Math.abs(a.length() - b.length());
		if (lengthDifferenceAbs > 2)
			return false;
		/**
		 * This is the case for most typos, simply one missing or added character or two
		 * next to each other are swapped. (We only use this rule for strings with
		 * length of at least two)
		 */
		double distance = DAMERAU_LEVENSHTEIN.distance(a, b);
		if (distance <= 1.0 && Math.max(a.length(), b.length()) > 2)
			return true;
		/**
		 * We accept everything with a distance below two as typo. At three and above,
		 * misspelled identifiers can be easily recognized by a human or might not be
		 * spelling errors.
		 */
		if (distance > 2)
			return false;
		/*
		 * Otherwise, if the JW-similarity is below 0.9, it is unlikely that the two
		 * names should be the same. Often, they are opposites or different concepts
		 * that share some letters. It is similar for NL-similarity (which has some
		 * benefits for long strings).
		 */
		return JARO_WINKLER.similarity(a, b) > 0.9 || NORMALIZED_LEVENSHTEIN.similarity(a, b) > 0.9;
	}
}
