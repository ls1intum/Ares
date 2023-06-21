package de.tum.in.test.api.util;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import de.tum.in.test.api.AresConfiguration;

@API(status = Status.INTERNAL)
public class ProjectSourcesFinder {

	private static final Logger LOG = LoggerFactory.getLogger(ProjectSourcesFinder.class);

	/**
	 * Pattern for matching the assignment folder name for the build.gradle file of
	 * a Gradle project
	 */
	private static final Pattern GRADLE_SOURCE_DIR_PATTERN = Pattern
			.compile("def\\s+assignmentSrcDir\\s*=\\s*\"(?<dir>.+)\""); //$NON-NLS-1$

	private static String pomXmlPath = "pom.xml"; //$NON-NLS-1$
	private static String buildGradlePath = "build.gradle"; //$NON-NLS-1$

	/**
	 * Returns the project sources path depending on the Ares and project
	 * configuration.
	 * <p>
	 * This methods extracts the source path from the project build file, depending
	 * on whether a pom.xml or build.gradle file was found this method looks. The
	 * location of the project build file can be configured using
	 * {@link AresConfiguration}.
	 *
	 * @return An empty optional if no project build file was found or if the build
	 *         file did not contain the sources path in the format required by Ares.
	 */
	public static Optional<Path> findProjectSourcesPath() {
		String assignmentFolderName = null;
		if (isMavenProject()) {
			assignmentFolderName = getAssignmentFolderNameForMavenProject();
		} else if (isGradleProject()) {
			assignmentFolderName = getAssignmentFolderNameForGradleProject();
		} else {
			LOG.error("Could not find any build file. Contact your instructor."); //$NON-NLS-1$
		}
		return Optional.ofNullable(assignmentFolderName).map(Path::of);
	}

	public static boolean isMavenProject() {
		if (pomXmlPath == null)
			return false;
		File projectFile = new File(pomXmlPath);
		return projectFile.exists() && !projectFile.isDirectory();
	}

	public static boolean isGradleProject() {
		if (buildGradlePath == null)
			return false;
		File projectFile = new File(buildGradlePath);
		return projectFile.exists() && !projectFile.isDirectory();
	}

	/**
	 * Retrieves the assignment folder name for a maven project from the pom.xml
	 *
	 * @return the folder name of the maven project, relative to project root
	 */
	private static String getAssignmentFolderNameForMavenProject() {
		try {
			var pomFile = new File(pomXmlPath);
			var documentBuilderFactory = DocumentBuilderFactory.newInstance();
			// make sure to avoid loading external files which would not be compliant
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); //$NON-NLS-1$
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); //$NON-NLS-1$
			var documentBuilder = documentBuilderFactory.newDocumentBuilder();
			var pomXmlDocument = documentBuilder.parse(pomFile);

			NodeList buildNodes = pomXmlDocument.getElementsByTagName("build"); //$NON-NLS-1$
			for (var i = 0; i < buildNodes.getLength(); i++) {
				var buildNode = buildNodes.item(i);
				if (buildNode.getNodeType() == Node.ELEMENT_NODE) {
					var buildNodeElement = (Element) buildNode;
					var sourceDirectoryPropertyValue = buildNodeElement.getElementsByTagName("sourceDirectory").item(0) //$NON-NLS-1$
							.getTextContent();
					return sourceDirectoryPropertyValue.substring(sourceDirectoryPropertyValue.indexOf("}") + 2); //$NON-NLS-1$
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
			LOG.error("Could not retrieve the source directory from the pom.xml file. Contact your instructor.", e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Retrieves the assignment folder name for a gradle project from the
	 * build.gradle
	 *
	 * @return the folder name of the gradle project, relative to project root
	 */
	private static String getAssignmentFolderNameForGradleProject() {
		try {
			var path = Path.of(buildGradlePath);
			String fileContent = Files.readString(path);

			var matcher = GRADLE_SOURCE_DIR_PATTERN.matcher(fileContent);
			if (matcher.find()) {
				return matcher.group("dir"); //$NON-NLS-1$
			}
			return null;
		} catch (IOException | NullPointerException e) {
			LOG.error("Could not retrieve the source directory from the build.gradle file. Contact your instructor.", //$NON-NLS-1$
					e);
		}
		return null;
	}

	public static String getPomXmlPath() {
		return pomXmlPath;
	}

	public static void setPomXmlPath(String path) {
		pomXmlPath = path;
	}

	public static String getBuildGradlePath() {
		return buildGradlePath;
	}

	public static void setBuildGradlePath(String path) {
		buildGradlePath = path;
	}
}
