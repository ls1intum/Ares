package de.tum.in.test.api.security;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.iterable;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

import de.tum.in.test.api.*;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.context.*;
import de.tum.in.test.api.internal.ConfigurationUtils;
import de.tum.in.test.api.util.*;

class AresSecurityConfigurationTest {

	private static final int ALLOWED_PORT_NUMBER = 42;
	private static final int ALLOW_PORT_ABOVE = 1000;
	private static final int EXCLUDED_PORT_NUMBER = 1024;
	private static final int THREAD_COUNT = 314;
	private static final String PATH_BLACKLIST = "target/classes";
	private static final String PATH_WHITELIST = "target";
	private static final String PACKAGE_WHITELIST = "java.util.regex";
	private static final String PACKAGE_BLACKLIST = "java.util**";
	private static final String TRUSTED_PACKAGE = "abc.def.**";

	private static final String TEST_ONE = "testOne";
	private static final String TEST_TWO = "testTwo";
	private static final TestContext MOCK_TEST_CONTEXT_ONE = new MockTestContext(new TestTestClass(), TEST_ONE);
	private static final TestContext MOCK_TEST_CONTEXT_TWO = new MockTestContext(new TestTestClass(), TEST_TWO);

	private final AresSecurityConfiguration configurationOneA = ConfigurationUtils
			.generateConfiguration(MOCK_TEST_CONTEXT_ONE);
	private final AresSecurityConfiguration configurationOneB = ConfigurationUtils
			.generateConfiguration(MOCK_TEST_CONTEXT_ONE);
	private final AresSecurityConfiguration configurationTwo = ConfigurationUtils
			.generateConfiguration(MOCK_TEST_CONTEXT_TWO);

	@Test
	void testGetter() {
		assertThat(configurationOneA.testClass()).hasValue(TestTestClass.class);
		assertThat(configurationOneA.testMethod())
				.isEqualTo(ReflectionSupport.findMethod(TestTestClass.class, TEST_ONE));
		assertThat(configurationOneA.whitelistedClassNames()).contains(TestTestClass.class.getName(),
				String.class.getName());

		assertThat(configurationOneA.whitelistedPaths()).isPresent().get(as(iterable(PathRule.class))).hasSize(1)
				.allMatch(pathRule -> pathRule.getActionLevel() == PathActionLevel.READ //
						&& pathRule.getRuleType() == RuleType.WHITELIST //
						&& PATH_WHITELIST.equals(pathRule.getPathPattern()));
		assertThat(configurationOneA.blacklistedPaths()).hasSize(1)
				.allMatch(pathRule -> pathRule.getActionLevel() == PathActionLevel.READ //
						&& pathRule.getRuleType() == RuleType.BLACKLIST //
						&& PATH_BLACKLIST.equals(pathRule.getPathPattern()));

		assertThat(configurationOneA.blacklistedPackages()).hasSize(1)
				.allMatch(packageRule -> packageRule.getRuleType() == RuleType.BLACKLIST //
						&& PACKAGE_BLACKLIST.equals(packageRule.getPackagePattern()));
		assertThat(configurationOneA.whitelistedPackages()).hasSize(1)
				.allMatch(packageRule -> packageRule.getRuleType() == RuleType.WHITELIST //
						&& PACKAGE_WHITELIST.equals(packageRule.getPackagePattern()));

		assertThat(configurationOneA.allowedThreadCount()).isPresent().hasValue(THREAD_COUNT);
		assertThat(configurationOneA.allowedLocalPorts()).containsExactly(ALLOWED_PORT_NUMBER);
		assertThat(configurationOneA.allowLocalPortsAbove()).isPresent().hasValue(ALLOW_PORT_ABOVE);
		assertThat(configurationOneA.excludedLocalPorts()).containsExactly(EXCLUDED_PORT_NUMBER);

		assertThat(configurationOneA.trustedPackages()).hasSize(1)
				.allMatch(packageRule -> packageRule.getRuleType() == RuleType.WHITELIST //
						&& TRUSTED_PACKAGE.equals(packageRule.getPackagePattern()));

		assertThat(configurationOneA.threadTrustScope()).isEqualTo(TrustedThreads.TrustScope.MINIMAL);
		assertThat(configurationTwo.threadTrustScope()).isEqualTo(TrustedThreads.TrustScope.EXTENDED);
	}

	@Test
	void testHashCode() {
		int hashOneA = configurationOneA.hashCode();
		int hashOneB = configurationOneB.hashCode();
		int hashTwo = configurationTwo.hashCode();

		assertThat(hashOneA).isEqualTo(hashOneB).isNotEqualTo(hashTwo);
	}

	@Test
	void testEqualsObject() {
		assertThat(configurationOneA).isEqualTo(configurationOneB).isNotEqualTo(configurationTwo);
	}

	@Test
	void testToString() {
		int toStringOneA = configurationOneA.hashCode();
		int toStringOneB = configurationOneB.hashCode();
		int toStringTwo = configurationTwo.hashCode();

		assertThat(toStringOneA).isEqualTo(toStringOneB).isNotEqualTo(toStringTwo);
	}

	@WhitelistClass(String.class)
	@WhitelistPath(PATH_WHITELIST)
	@BlacklistPath(PATH_BLACKLIST)
	@BlacklistPackage(PACKAGE_BLACKLIST)
	@WhitelistPackage(PACKAGE_WHITELIST)
	@AddTrustedPackage(TRUSTED_PACKAGE)
	static class TestTestClass {

		@AllowThreads(maxActiveCount = THREAD_COUNT)
		@AllowLocalPort(value = ALLOWED_PORT_NUMBER, allowPortsAbove = ALLOW_PORT_ABOVE, exclude = EXCLUDED_PORT_NUMBER)
		void testOne() {
			// nothing to do
		}

		@TrustedThreads(TrustScope.EXTENDED)
		@AllowLocalPort(value = 271)
		void testTwo() {
			// nothing to do
		}
	}

	static class MockTestContext extends TestContext {

		private final Object testInstance;
		private final String testMethodName;

		public MockTestContext(Object testInstance, String testMethodName) {
			this.testInstance = Objects.requireNonNull(testInstance);
			this.testMethodName = Objects.requireNonNull(testMethodName);
		}

		@Override
		public Optional<Method> testMethod() {
			return ReflectionSupport.findMethod(testInstance.getClass(), testMethodName);
		}

		@Override
		public Optional<Class<?>> testClass() {
			return Optional.of(testInstance.getClass());
		}

		@Override
		public Optional<Object> testInstance() {
			return Optional.of(testInstance);
		}

		@Override
		public Optional<String> displayName() {
			return Optional.of("Some test");
		}

		@Override
		public Optional<AnnotatedElement> annotatedElement() {
			return testMethod().map(Function.identity());
		}

		@Override
		public Optional<TestType> findTestType() {
			return Optional.of(TestType.PUBLIC);
		}
	}
}
