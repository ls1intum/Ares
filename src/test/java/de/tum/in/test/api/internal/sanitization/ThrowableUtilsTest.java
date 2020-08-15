package de.tum.in.test.api.internal.sanitization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class ThrowableUtilsTest {

	private Throwable target = new Exception("ABC");

	@Test
	void testCause() {
		var cause = ThrowableUtils.getCause(target);
		// not initialized
		assertThat(cause).isSameAs(target);

		// set cause
		var newCause = new NullPointerException();
		ThrowableUtils.setCause(target, newCause);

		cause = ThrowableUtils.getCause(target);
		// set to new cause
		assertThat(cause).isSameAs(newCause);
		assertThat(cause).isSameAs(target.getCause());
	}

	@Test
	void testStackTrace() {
		var stackTrace = ThrowableUtils.getStackTrace(target);
		// not initialized
		assertThat(stackTrace).isEmpty();

		// set suppressed
		var newStackTrace = new StackTraceElement[] { new StackTraceElement("A", "B", "as.df", 42) };
		ThrowableUtils.setStackTrace(target, newStackTrace);

		stackTrace = ThrowableUtils.getStackTrace(target);
		// set to new suppressed
		assertThat(stackTrace).isSameAs(newStackTrace);
		assertThat(stackTrace).containsExactly(target.getStackTrace());
	}

	@Test
	void testSuppressedExceptions() {
		var suppressed = ThrowableUtils.getSuppressedExceptions(target);
		// not initialized
		assertThat(suppressed).isSameAs(Collections.emptyList());

		// set suppressed
		var newSuppressed = List.<Throwable>of(new NullPointerException());
		ThrowableUtils.setSuppressedException(target, newSuppressed);

		suppressed = ThrowableUtils.getSuppressedExceptions(target);
		// set to new suppressed
		assertThat(suppressed).isSameAs(newSuppressed);
		assertThat(suppressed).containsExactly(target.getSuppressed());
	}
}
