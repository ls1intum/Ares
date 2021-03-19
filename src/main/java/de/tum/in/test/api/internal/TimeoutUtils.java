package de.tum.in.test.api.internal;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invokeChecked;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.platform.commons.support.AnnotationSupport;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.api.PrivilegedExceptionsOnly;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.security.ArtemisSecurityManager;

public final class TimeoutUtils {

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
		return executeWithTimeout(timeout.get(), () -> rethrowThrowableSafe(execution), context);
	}

	private static <T> T rethrowThrowableSafe(ThrowingSupplier<T> execution) throws Exception {
		try {
			return execution.get();
		} catch (Exception | Error e) {
			throw e;
		} catch (Throwable t) {
			/*
			 * Should never happen, as there are no other direct subclasses of Throwable in
			 * use. But students might still do that, so better be prepared.
			 */
			throw new ExecutionException(t);
		}
	}

	private static <T> T executeWithTimeout(Duration timeout, Callable<T> action, TestContext context)
			throws Throwable {
		ArtemisSecurityManager.revokeThreadWhitelisting();
		ExecutorService executorService = Executors.newSingleThreadExecutor(new WhitelistedThreadFactory());
		try {
			Future<T> future = executorService.submit(action);
			return invokeChecked(() -> future.get(timeout.toMillis(), TimeUnit.MILLISECONDS));
		} catch (ExecutionException ex) {
			// should never happen, but you never know
			if (ex.getCause() instanceof ExecutionException)
				throw ex.getCause().getCause();
			throw ex.getCause();
		} catch (@SuppressWarnings("unused") TimeoutException ex) {
			throw generateTimeoutFailure(timeout, context);
		} finally {
			executorService.shutdownNow();
		}
	}

	private static AssertionFailedError generateTimeoutFailure(Duration timeout, TestContext context) {
		var failure = new AssertionFailedError("execution timed out after " + formatDuration(timeout));
		if (TestContextUtils.findAnnotationIn(context, PrivilegedExceptionsOnly.class).isPresent())
			throw new PrivilegedException(failure);
		return failure;
	}

	private static String formatDuration(Duration duration) {
		List<String> parts = new ArrayList<>();
		long h = duration.toHours();
		long m = duration.toMinutesPart();
		long s = duration.toSecondsPart();
		long ms = duration.toMillisPart();
		if (h != 0)
			parts.add(h + " h");
		if (m != 0)
			parts.add(m + " min");
		if (s != 0)
			parts.add(s + " s");
		if (ms != 0)
			parts.add(ms + " ms");
		return String.join(" ", parts);
	}

	private static class WhitelistedThreadFactory implements ThreadFactory {
		private static final AtomicInteger TIMEOUT_THREAD_ID = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "ajts-to-" + TIMEOUT_THREAD_ID.getAndIncrement());
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			ArtemisSecurityManager.requestThreadWhitelisting(t);
			return t;
		}
	}
}
