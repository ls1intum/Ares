package de.tum.in.test.api.internal;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public final class ThrowableUtils {

	private static final String DETAIL_MESSAGE_NAME = "detailMessage";
	private static final String CAUSE_NAME = "cause";
	private static final String STACK_TRACE_NAME = "stackTrace";
	private static final String SUPPRESSED_EXCEPTIONS_NAME = "suppressedExceptions";

	private static final VarHandle DETAIL_MESSAGE;
	private static final VarHandle CAUSE;
	private static final VarHandle STACK_TRACE;
	private static final VarHandle SUPPRESSED_EXCEPTIONS;

	static {
		try {
			var lookup = MethodHandles.privateLookupIn(Throwable.class, MethodHandles.lookup());
			DETAIL_MESSAGE = lookup.findVarHandle(Throwable.class, DETAIL_MESSAGE_NAME, String.class);
			CAUSE = lookup.findVarHandle(Throwable.class, CAUSE_NAME, Throwable.class);
			STACK_TRACE = lookup.findVarHandle(Throwable.class, STACK_TRACE_NAME, StackTraceElement[].class);
			SUPPRESSED_EXCEPTIONS = lookup.findVarHandle(Throwable.class, SUPPRESSED_EXCEPTIONS_NAME, List.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static String getDetailMessage(Throwable target) {
		checkAccess();
		return (String) DETAIL_MESSAGE.getVolatile(target);
	}

	public static Throwable getCause(Throwable target) {
		checkAccess();
		return (Throwable) CAUSE.getVolatile(target);
	}

	public static StackTraceElement[] getStackTrace(Throwable target) {
		checkAccess();
		return (StackTraceElement[]) STACK_TRACE.getVolatile(target);
	}

	public static List<Throwable> getSuppressedExceptions(Throwable target) {
		checkAccess();
		return (List<Throwable>) SUPPRESSED_EXCEPTIONS.getVolatile(target);
	}

	public static void setDetailMessage(Throwable target, String newValue) {
		checkAccess();
		DETAIL_MESSAGE.setVolatile(target, newValue);
	}

	public static void setCause(Throwable target, Throwable newValue) {
		checkAccess();
		CAUSE.setVolatile(target, newValue);
	}

	public static void setStackTrace(Throwable target, StackTraceElement... newValue) {
		checkAccess();
		STACK_TRACE.setVolatile(target, newValue);
	}

	public static void setSuppressedException(Throwable target, List<Throwable> newValue) {
		checkAccess();
		SUPPRESSED_EXCEPTIONS.setVolatile(target, newValue);
	}

	private static void checkAccess() {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkPackageAccess(ThrowableUtils.class.getPackageName());
	}

	private ThrowableUtils() {

	}
}
