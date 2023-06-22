package de.tum.in.test.api.structural.testutils;

import static de.tum.in.test.api.localization.Messages.localized;
import static de.tum.in.test.api.structural.testutils.ScanResultType.*;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.*;

import info.debatty.java.stringsimilarity.*;

import de.tum.in.test.api.AresConfiguration;
import de.tum.in.test.api.util.ProjectSourcesFinder;

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
 * @version 5.1 (2022-03-30)
 */
@API(status = Status.STABLE)
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

	public ClassNameScanner(String expectedClassName, String expectedPackageName) {
		this.expectedClassName = expectedClassName;
		this.expectedPackageName = expectedPackageName;
		findObservedClassesInProject();
		this.scanResult = computeScanResult();
	}

	public ScanResult getScanResult() {
		return scanResult;
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
			if (foundObservedClassName.equalsIgnoreCase(expectedClassName))
				return createScanResult(foundObservedClassName, foundObservedPackageNames, classPresentMultiple,
						classCorrectlyPlaced, WRONG_CASE_MULTIPLE, WRONG_CASE_CORRECT_PLACE, WRONG_CASE_MISPLACED);
			/*
			 * 2) check whether there are similar classes (e.g. the student has a small typo
			 * in the class name)
			 */
			if (isMisspelledWithHighProbability(expectedClassName, foundObservedClassName))
				return createScanResult(foundObservedClassName, foundObservedPackageNames, classPresentMultiple,
						classCorrectlyPlaced, TYPOS_MULTIPLE, TYPOS_CORRECT_PLACE, TYPOS_MISPLACED);
		}
		return createScanResult(ScanResultType.NOTFOUND, expectedClassName, null);
	}

	private ScanResult createScanResult(String foundObservedClassName, String foundObservedPackageName,
			boolean classPresentMultiple, boolean classCorrectlyPlaced, ScanResultType multipleTimes,
			ScanResultType correctPlace, ScanResultType misplaced) {
		ScanResultType scanResultType;
		if (classPresentMultiple)
			scanResultType = multipleTimes;
		else
			scanResultType = classCorrectlyPlaced ? correctPlace : misplaced;
		return createScanResult(scanResultType, foundObservedClassName, foundObservedPackageName);
	}

	private ScanResultType getScanResultTypeClassFound(List<String> observedPackageNames) {
		boolean classIsPresentMultipleTimes = observedPackageNames.size() > 1;
		if (classIsPresentMultipleTimes)
			return CORRECT_NAME_MULTIPLE;
		boolean classIsCorrectlyPlaced = observedPackageNames.contains(expectedPackageName);
		return classIsCorrectlyPlaced ? CORRECT_NAME_CORRECT_PLACE : CORRECT_NAME_MISPLACED;
	}

	private ScanResult createScanResult(ScanResultType scanResultType, String foundClassName, String foundPackageName) {
		String scanResultMessage = createScanResultMessage(scanResultType, foundClassName, foundPackageName);
		return new ScanResult(scanResultType, scanResultMessage);
	}

	private String createScanResultMessage(ScanResultType scanResultType, String foundClassName,
			String foundPackageName) {
		var expectedPackageDescription = describePackageNameLocalized(expectedPackageName);
		var foundPackageDescription = describePackageNameLocalized(foundPackageName);
		switch (scanResultType) {
		case CORRECT_NAME_CORRECT_PLACE:
			return localized("structural.scan.correctNameCorrectPlace", foundClassName); //$NON-NLS-1$
		case CORRECT_NAME_MISPLACED:
			return localized("structural.scan.correctNameMisplaced", foundClassName, foundPackageDescription); //$NON-NLS-1$
		case CORRECT_NAME_MULTIPLE:
			return localized("structural.scan.correctNameMultiple", foundClassName, foundPackageDescription); //$NON-NLS-1$
		case WRONG_CASE_CORRECT_PLACE:
			return localized("structural.scan.wrongCaseCorrectPlace", expectedClassName, foundClassName); //$NON-NLS-1$
		case WRONG_CASE_MISPLACED:
			return localized("structural.scan.wrongCaseMisplaced", expectedClassName, expectedPackageDescription, //$NON-NLS-1$
					foundClassName, foundPackageDescription);
		case WRONG_CASE_MULTIPLE:
			return localized("structural.scan.wrongCaseMultiple", expectedClassName, expectedPackageDescription, //$NON-NLS-1$
					foundClassName, foundPackageDescription);
		case TYPOS_CORRECT_PLACE:
			return localized("structural.scan.typosCorrectPlace", expectedClassName, foundClassName); //$NON-NLS-1$
		case TYPOS_MISPLACED:
			return localized("structural.scan.typosMisplaced", expectedClassName, expectedPackageDescription, //$NON-NLS-1$
					foundClassName, foundPackageDescription);
		case TYPOS_MULTIPLE:
			return localized("structural.scan.typosMultiple", expectedClassName, expectedPackageDescription, //$NON-NLS-1$
					foundClassName, observedClasses.get(foundClassName).toString());
		case NOTFOUND:
			return localized("structural.scan.notFound", expectedClassName, expectedPackageDescription); //$NON-NLS-1$
		default:
			return localized("structural.scan.default"); //$NON-NLS-1$
		}
	}

	/**
	 * This method retrieves the actual type names and their packages by walking the
	 * project file structure. The root node (which is the assignment folder) is
	 * defined in the project build file (pom.xml or build.gradle) of the project.
	 */
	private void findObservedClassesInProject() {
		var assignmentFolderName = ProjectSourcesFinder.findProjectSourcesPath();
		if (assignmentFolderName.isPresent()) {
			walkProjectFileStructure(assignmentFolderName.get(), assignmentFolderName.get().toFile(), observedClasses);
		} else {
			LOG.error("Could not retrieve source directory from project file. Contact your instructor."); //$NON-NLS-1$ ´
		}
	}

	/**
	 * This method recursively walks the actual folder file structure starting from
	 * the assignment folder and adds each type it finds e.g. filenames ending with
	 * <code>.java</code> and <code>.kt</code> to the passed JSON object.
	 *
	 * @param assignmentFolder The root folder where the method starts walking the
	 *                         project structure.
	 * @param node             The current node the method is visiting.
	 * @param foundClasses     The JSON object where the type names and packages get
	 *                         appended.
	 */
	private void walkProjectFileStructure(Path assignmentFolder, File node, Map<String, List<String>> foundClasses) {
		// Example:
		// * assignmentFolderName: assignment/src
		// * fileName: assignment/src/de/tum/in/ase/eist/BubbleSort.java
		// Required Package Name: de.tum.in.ase.eist
		var fileName = node.getName();
		// support Java and Kotlin files
		if (fileName.endsWith(".java") || fileName.endsWith(".kt")) { //$NON-NLS-1$ //$NON-NLS-2$
			var fileNameComponents = fileName.split("\\."); //$NON-NLS-1$
			var className = fileNameComponents[fileNameComponents.length - 2];

			Path packagePath = assignmentFolder.relativize(node.toPath().getParent());
			var packageName = StreamSupport.stream(packagePath.spliterator(), false).map(Object::toString)
					.collect(Collectors.joining(".")); //$NON-NLS-1$

			if (foundClasses.containsKey(className))
				foundClasses.get(className).add(packageName);
			else
				foundClasses.put(className, new ArrayList<>(List.of(packageName)));
		}
		// TODO: we should also support inner classes here
		if (node.isDirectory()) {
			String[] subNodes = node.list();
			if (subNodes != null && subNodes.length > 0)
				for (String currentSubNode : subNodes)
					walkProjectFileStructure(assignmentFolder, new File(node, currentSubNode), foundClasses);
		}
	}

	/**
	 * Returns the global Maven POM-file path used by Ares.
	 * <p>
	 * Defaults to the relative path <code>pom.xml</code>.
	 *
	 * @return the configured pom.xml file path as string
	 * @deprecated Moved to a more general package. Please use
	 *             {@link AresConfiguration#getPomXmlPath()} instead.
	 */
	@Deprecated(since = "1.12.0")
	public static String getPomXmlPath() {
		return AresConfiguration.getPomXmlPath();
	}

	/**
	 * Sets the global Maven POM-file path to the given file path string.
	 * <p>
	 * Set by default to the relative path <code>pom.xml</code>.
	 *
	 * @param path the path as string, may be both relative or absolute
	 * @deprecated Moved to a more general package. Please use
	 *             {@link AresConfiguration#setPomXmlPath(String)} instead.
	 */
	@Deprecated(since = "1.12.0")
	public static void setPomXmlPath(String path) {
		AresConfiguration.setPomXmlPath(path);
	}

	/**
	 * Returns the global Gradle build file path used by Ares.
	 * <p>
	 * Defaults to the relative path <code>build.gradle</code>.
	 *
	 * @return the configured gradle.build file path as string
	 * @deprecated Moved to a more general package. Please use
	 *             {@link AresConfiguration#getBuildGradlePath()} instead.
	 */
	@Deprecated(since = "1.12.0")
	public static String getBuildGradlePath() {
		return AresConfiguration.getBuildGradlePath();
	}

	/**
	 * Sets the global Gradle build file path to the given file path string.
	 * <p>
	 * Set by default to the relative path <code>build.gradle</code>.
	 *
	 * @param path the path as string, may be both relative or absolute
	 * @deprecated Moved to a more general package. Please use
	 *             {@link AresConfiguration#setBuildGradlePath(String)} instead.
	 */
	@Deprecated(since = "1.12.0")
	public static void setBuildGradlePath(String path) {
		AresConfiguration.setBuildGradlePath(path);
	}

	static boolean isMisspelledWithHighProbability(String a, String b) {
		/*
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
		/*
		 * This is the case for most typos, simply one missing or added character or two
		 * next to each other are swapped. (We only use this rule for strings with
		 * length of at least two)
		 */
		double distance = DAMERAU_LEVENSHTEIN.distance(a, b);
		if (distance <= 1.0 && Math.max(a.length(), b.length()) > 2)
			return true;
		/*
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

	static String describePackageNameLocalized(String packageName) {
		if (packageName == null || packageName.isBlank())
			return localized("structural.scan.defaultPackage"); //$NON-NLS-1$
		return packageName;
	}
}
