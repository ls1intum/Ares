package de.tum.in.test.integration.testuser;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.api.structural.AttributeTestProvider;
import de.tum.in.test.api.structural.ClassTestProvider;
import de.tum.in.test.api.structural.ConstructorTestProvider;
import de.tum.in.test.api.structural.MethodTestProvider;
import de.tum.in.test.api.structural.testutils.ClassNameScanner;

@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
public class StructuralUser {

	@Nested
	class Maven extends StrucuralTestSet {
		@BeforeEach
		void setupTest() {
			ClassNameScanner.setPomXmlPath("src/test/resources/de/tum/in/test/integration/testuser/pom.xml");
			ClassNameScanner.setBuildGradlePath(null);
		}
	}

	@Nested
	class Gradle extends StrucuralTestSet {
		@BeforeEach
		void setupTest() {
			ClassNameScanner.setPomXmlPath(null);
			ClassNameScanner.setBuildGradlePath("src/test/resources/de/tum/in/test/integration/testuser/build.gradle");
		}
	}

	class StrucuralTestSet {

		@Nested
		class AttributeTestUser extends AttributeTestProvider {

			@TestFactory
			DynamicContainer testAttributes() throws URISyntaxException {
				structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
				return generateTestsForAllClasses();
			}
		}

		@Nested
		class ClassTestUser extends ClassTestProvider {

			@TestFactory
			DynamicContainer testClasses() throws URISyntaxException {
				structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
				return generateTestsForAllClasses();
			}
		}

		@Nested
		class MethodTestUser extends MethodTestProvider {

			@TestFactory
			DynamicContainer testMethods() throws URISyntaxException {
				structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
				return generateTestsForAllClasses();
			}
		}

		@Nested
		class ConstructorTestUser extends ConstructorTestProvider {

			@TestFactory
			DynamicContainer testConstructors() throws URISyntaxException {
				structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
				return generateTestsForAllClasses();
			}
		}
	}
}
