package de.tum.in.test.api;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.ProjectSourcesFinder;

/**
 * Utility class for global Ares configuration.
 *
 * @author Christian Femers
 * @since 1.12.0
 * @version 1.0.0
 */
@API(status = Status.MAINTAINED)
public class AresConfiguration {

	private AresConfiguration() {
	}

	/**
	 * Returns the global Maven POM-file path used by Ares.
	 * <p>
	 * Defaults to the relative path <code>pom.xml</code>.
	 *
	 * @return the configured pom.xml file path as string
	 */
	public static String getPomXmlPath() {
		return ProjectSourcesFinder.getPomXmlPath();
	}

	/**
	 * Sets the global Maven POM-file path to the given file path string.
	 * <p>
	 * Set by default to the relative path <code>pom.xml</code>.
	 *
	 * @param path the path as string, may be both relative or absolute
	 */
	public static void setPomXmlPath(String path) {
		ProjectSourcesFinder.setPomXmlPath(path);
	}

	/**
	 * Returns the global Gradle build file path used by Ares.
	 * <p>
	 * Defaults to the relative path <code>build.gradle</code>.
	 *
	 * @return the configured gradle.build file path as string
	 */
	public static String getBuildGradlePath() {
		return ProjectSourcesFinder.getBuildGradlePath();
	}

	/**
	 * Sets the global Gradle build file path to the given file path string.
	 * <p>
	 * Set by default to the relative path <code>build.gradle</code>.
	 *
	 * @param path the path as string, may be both relative or absolute
	 */
	public static void setBuildGradlePath(String path) {
		ProjectSourcesFinder.setBuildGradlePath(path);
	}
}
