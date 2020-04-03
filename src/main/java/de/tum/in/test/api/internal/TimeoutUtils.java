package de.tum.in.test.api.internal;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.platform.commons.support.AnnotationSupport;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.security.ArtemisSecurityManager;

public class TimeoutUtils {

	static {
		/*
		 * Initialize SecurityManager when we are still in the main thread
		 */
		ArtemisSecurityManager.isInstalled();
	}

	private TimeoutUtils() {

	}

	public static Optional<Duration> findTimeout(TestContext context) {
		var methodLevel = AnnotationSupport.findAnnotation(context.testMethod(), StrictTimeout.class);
		var classLevel = AnnotationSupport.findAnnotation(context.testClass(), StrictTimeout.class);
		return methodLevel.or(() -> classLevel).map(st -> Duration.of(st.value(), st.unit().toChronoUnit()));
	}

	public static <T> T performTimeoutExecution(ThrowingSupplier<T> execution, TestContext context) throws Throwable {
		var timeout = findTimeout(context);
		if (timeout.isEmpty())
			return execution.get();
		return Assertions.assertTimeoutPreemptively(timeout.get(), () -> {
			ArtemisSecurityManager.requestThreadWhitelisting();
			return execution.get();
		});
	}
}
