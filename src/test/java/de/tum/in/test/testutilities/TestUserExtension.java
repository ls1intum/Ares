package de.tum.in.test.testutilities;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.HierarchyTraversalMode;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.testkit.engine.EngineTestKit;

class TestUserExtension implements BeforeAllCallback, TestInstancePostProcessor, ParameterResolver {

	private static final String TEST_RESULTS = "test-results";

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
		var testClass = testInstance.getClass();
		var testResults = getTestResults(context);
		var varHandles = findSupportedVarHandles(testClass, context, false);
		for (var varHandle : varHandles) {
			varHandle.set(testInstance, testResults);
		}
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		var optionalAnnotation = AnnotationSupport.findAnnotation(context.getElement(), UserBased.class);
		if (optionalAnnotation.isEmpty())
			fail("No annotated element found for @UserBased");
		var user = optionalAnnotation.get().value();
		var tests = EngineTestKit.engine("junit-jupiter").selectors(selectClass(user)).execute();
		var testResults = tests.testEvents();

		if (testResults.count() == 0) {
			tests.containerEvents().debug();
			throw new IllegalStateException("Test execution of " + user + " failed, no tests run.");
		}

		getStore(context).put(TEST_RESULTS, testResults);

		if (context.getTestClass().isPresent()) {
			var testClass = context.getRequiredTestClass();
			var varHandles = findSupportedVarHandles(testClass, context, true);
			for (var varHandle : varHandles) {
				varHandle.set(testResults);
			}
		}
	}

	private List<VarHandle> findSupportedVarHandles(Class<?> target, ExtensionContext context, boolean staticFields)
			throws Exception {
		var lookup = MethodHandles.privateLookupIn(target, MethodHandles.lookup());
		var fields = ReflectionSupport.findFields(target,
				field -> (staticFields ^ !Modifier.isStatic(field.getModifiers()))
						&& field.isAnnotationPresent(UserTestResults.class),
				HierarchyTraversalMode.BOTTOM_UP);
		var varHandles = new ArrayList<VarHandle>();
		for (var field : fields) {
			if (supports(field, field.getType(), context)) {
				varHandles.add(lookup.unreflectVarHandle(field));
			}
		}
		return varHandles;
	}

	private Store getStore(ExtensionContext context) {
		return context.getStore(Namespace.create(getClass(), context.getTestClass()));
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
		return supports(parameterContext.getParameter(), parameterContext.getParameter().getType(), context);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
		return getTestResults(context);
	}

	@SuppressWarnings("null")
	private boolean supports(AnnotatedElement element, Class<?> type, ExtensionContext context) {
		if (!element.isAnnotationPresent(UserTestResults.class))
			return false;
		var testResults = getTestResults(context);
		if (testResults == null)
			fail("No user test results found for " + element);
		if (!type.isAssignableFrom(testResults.getClass()))
			fail("Cannot assign " + testResults.getClass() + " to " + element);
		return true;
	}

	private Object getTestResults(ExtensionContext context) {
		return getStore(context).get(TEST_RESULTS);
	}
}
