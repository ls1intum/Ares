package de.tum.in.test.api.internal.sanitization;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
final class ThrowableUtils {

	private static final String CAUSE_NAME = "cause";
	private static final String STACK_TRACE_NAME = "stackTrace";
	private static final String SUPPRESSED_EXCEPTIONS_NAME = "suppressedExceptions";

	private static final VarHandle CAUSE;
	private static final VarHandle STACK_TRACE;
	private static final VarHandle SUPPRESSED_EXCEPTIONS;

	static {
		try {
			var lookup = MethodHandles.privateLookupIn(Throwable.class, MethodHandles.lookup());
			CAUSE = lookup.findVarHandle(Throwable.class, CAUSE_NAME, Throwable.class);
			STACK_TRACE = lookup.findVarHandle(Throwable.class, STACK_TRACE_NAME, StackTraceElement[].class);
			SUPPRESSED_EXCEPTIONS = lookup.findVarHandle(Throwable.class, SUPPRESSED_EXCEPTIONS_NAME, List.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	static Throwable getCause(Throwable target) {
		return (Throwable) CAUSE.getVolatile(target);
	}

	static StackTraceElement[] getStackTrace(Throwable target) {
		return (StackTraceElement[]) CAUSE.getVolatile(target);
	}

	static List<Throwable> getSuppressedExceptions(Throwable target) {
		return (List<Throwable>) CAUSE.getVolatile(target);
	}

	static void setCause(Throwable target, Throwable newValue) {
		CAUSE.setVolatile(target, newValue);
	}

	static void setStackTrace(Throwable target, StackTraceElement... newValue) {
		STACK_TRACE.setVolatile(target, newValue);
	}

	static void setSuppressedException(Throwable target, List<Throwable> newValue) {
		SUPPRESSED_EXCEPTIONS.setVolatile(target, newValue);
	}

	private ThrowableUtils() {

	}
}
