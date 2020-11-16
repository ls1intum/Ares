package de.tum.in.testuser;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;

import de.tum.in.test.api.AddTrustedPackage;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.structural.AttributeTest;
import de.tum.in.test.api.structural.ClassTest;
import de.tum.in.test.api.structural.ConstructorTest;
import de.tum.in.test.api.structural.MethodTest;
import de.tum.in.test.api.structural.testutils.ClassNameScanner;

@Public
@StrictTimeout(10)
@WhitelistPath("")
@AddTrustedPackage("org.json**")
public class StructuralUser {

	@BeforeAll
	static void setupTest() {
		ClassNameScanner.setPomXmlPath("src/test/resources/de/tum/in/testuser/pom.xml");
	}

	@Nested
	class AttributeTestUser extends AttributeTest {

		@TestFactory
		DynamicContainer testAttributes() throws URISyntaxException {
			structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
			return generateTestsForAllClasses();
		}
	}

	@Nested
	class ClassTestUser extends ClassTest {

		@TestFactory
		DynamicContainer testClasses() throws URISyntaxException {
			structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
			return generateTestsForAllClasses();
		}
	}

	@Nested
	class MethodTestUser extends MethodTest {

		@TestFactory
		DynamicContainer testMethods() throws URISyntaxException {
			structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
			return generateTestsForAllClasses();
		}
	}

	@Nested
	class ConstructorTestUser extends ConstructorTest {

		@TestFactory
		DynamicContainer testConstructors() throws URISyntaxException {
			structureOracleJSON = retrieveStructureOracleJSON(getClass().getResource("test.json"));
			return generateTestsForAllClasses();
		}
	}
}
