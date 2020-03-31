package de.tum.in.test.api.jupiter;

import static de.tum.in.test.api.internal.ReportingUtils.doProceedAndPostProcess;
import static de.tum.in.test.api.internal.TestGuardUtils.*;
import static de.tum.in.test.api.localization.Messages.formatLocalized;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.TestType;
import de.tum.in.test.api.internal.JupiterContext;

/**
 * This class' main purpose is to guard the {@link HiddenTest}s execution and
 * evaluate the {@link Deadline}.
 *
 * @author Christian Femers
 */
@API(status = Status.INTERNAL)
public final class JupiterTestGuard implements InvocationInterceptor, DisplayNameGenerator {

	private final DisplayNameGenerator defaultNameGen = new DisplayNameGenerator.ReplaceUnderscores();

	@Override
	public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
		checkForHidden(JupiterContext.of(extensionContext));
		doProceedAndPostProcess(invocation);
	}

	@Override
	public <T> T interceptTestFactoryMethod(Invocation<T> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		checkForHidden(JupiterContext.of(extensionContext));
		return doProceedAndPostProcess(invocation);
	}

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		checkForHidden(JupiterContext.of(extensionContext));
		doProceedAndPostProcess(invocation);
	}

	@Override
	public void interceptTestTemplateMethod(Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		checkForHidden(JupiterContext.of(extensionContext));
		doProceedAndPostProcess(invocation);
	}

	@Override
	public String generateDisplayNameForClass(Class<?> testClass) {
		return defaultNameGen.generateDisplayNameForClass(testClass);
	}

	@Override
	public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
		return defaultNameGen.generateDisplayNameForNestedClass(nestedClass);
	}

	@Override
	public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
		if (findAnnotation(testMethod, JupiterArtemisTest.class).map(JupiterArtemisTest::value)
				.orElse(null) == TestType.HIDDEN) {
			var deadline = extractDeadline(Optional.of(testClass), Optional.of(testMethod));
			String name = testMethod.toString();
			if (deadline.isEmpty())
				return formatLocalized("test_guard.obfuscate_hidden_test_missing_deadline", name); //$NON-NLS-1$
			if (LocalDateTime.now().isBefore(deadline.get()))
				return String.format("Hidden Test %08X", name.hashCode()); //$NON-NLS-1$
		}
		return defaultNameGen.generateDisplayNameForMethod(testClass, testMethod);
	}
}
