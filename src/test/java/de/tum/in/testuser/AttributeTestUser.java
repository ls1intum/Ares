package de.tum.in.testuser;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Timeout;

import de.tum.in.test.api.structural.testutils.ClassNameScanner;

public class AttributeTestUser extends de.tum.in.test.api.structural.AttributeTest {

	@BeforeAll
	static void setupTest() {
		ClassNameScanner.pomXmlPath = "src/test/resources/de/tum/in/testuser/pom.xml";
	}

	@Timeout(10)
	@TestFactory
	public DynamicContainer generateTestsForAllClasses() throws URISyntaxException {
		structureOracleJSON = retrieveStructureOracleJSON(this.getClass().getResource("test.json"));
		return super.generateTestsForAllClasses();
	}

}
