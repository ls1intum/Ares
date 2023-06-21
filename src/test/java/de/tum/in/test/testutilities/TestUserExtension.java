package de.tum.in.test.testutilities;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.*;
import org.junit.platform.commons.support.*;
import org.junit.platform.engine.discovery.*;
import org.junit.platform.testkit.engine.EngineTestKit;

class TestUserExtension implements BeforeAllCallback, TestInstancePostProcessor, ParameterResolver {

	private static final String TEST_RESULTS = "test-results";

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
		if (isNotApplicableTo(context))
			return;
		var testClass = testInstance.getClass();
		var testResults = getTestResults(context);
		var varHandles = findSupportedVarHandles(testClass, context, false);
		for (var varHandle : varHandles) {
			varHandle.set(testInstance, testResults);
		}
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		if (isNotApplicableTo(context))
			return;
		var optionalAnnotation = AnnotationSupport.findAnnotation(context.getElement(), UserBased.class);
		if (optionalAnnotation.isEmpty())
			fail("No annotated element found for @UserBased");
		var testEngineId = optionalAnnotation.get().testEngineId();
		var users = List.of(optionalAnnotation.get().value());
		var userSelectors = users.stream().map(DiscoverySelectors::selectClass).toArray(ClassSelector[]::new);
		var tests = EngineTestKit.engine(testEngineId).selectors(userSelectors).execute();
		var testResults = tests.testEvents();

		if (testResults.count() == 0) {
			tests.containerEvents().debug();
			throw new IllegalStateException("Test execution of " + users + " failed, no tests run.");
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
				field -> (staticFields == Modifier.isStatic(field.getModifiers()))
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

	private static boolean isNotApplicableTo(ExtensionContext context) {
		return context.getTestClass().map(Class::isMemberClass).orElse(true);
	}
}
