package de.tum.in.testuser;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.structural.AttributeTestProvider;
import de.tum.in.test.api.structural.ClassTestProvider;
import de.tum.in.test.api.structural.ConstructorTestProvider;
import de.tum.in.test.api.structural.MethodTestProvider;
import de.tum.in.test.api.structural.testutils.ClassNameScanner;

@Public
@StrictTimeout(10)
@WhitelistPath("")
public class StructuralUser {

	@BeforeAll
	static void setupTest() {
		ClassNameScanner.setPomXmlPath("src/test/resources/de/tum/in/testuser/pom.xml");
	}

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
