package de.tum.in.test.api.internal;

import static de.tum.in.test.api.internal.BlacklistedInvoker.invokeChecked;
import static de.tum.in.test.api.localization.Messages.localizedFailure;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.api.*;
import de.tum.in.test.api.context.*;
import de.tum.in.test.api.security.ArtemisSecurityManager;

@API(status = Status.INTERNAL)
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
		var strictTimeout = TestContextUtils.findAnnotationIn(context, StrictTimeout.class);
		return strictTimeout.map(st -> Duration.of(st.value(), st.unit().toChronoUnit()));
	}

	public static <T> T performTimeoutExecution(ThrowingSupplier<T> execution, TestContext context) throws Throwable {
		var timeout = findTimeout(context);
		if (timeout.isEmpty())
			return execution.get();
		return executeWithTimeout(timeout.get(), () -> rethrowThrowableSafe(execution), context);
	}

	private static <T> T rethrowThrowableSafe(ThrowingSupplier<T> execution) throws Exception { // NOSONAR
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
			throws Throwable { // NOSONAR
		ArtemisSecurityManager.revokeThreadWhitelisting();
		var executorService = Executors.newSingleThreadExecutor(new WhitelistedThreadFactory());
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
		var failure = localizedFailure("timeout.failure_message", formatDuration(timeout)); //$NON-NLS-1$
		if (TestContextUtils.findAnnotationIn(context, PrivilegedExceptionsOnly.class).isPresent())
			throw new PrivilegedException(failure);
		return failure;
	}

	private static String formatDuration(Duration duration) {
		List<String> parts = new ArrayList<>();
		long h = duration.toHours();
		int m = duration.toMinutesPart();
		int s = duration.toSecondsPart();
		int ms = duration.toMillisPart();
		if (h != 0)
			parts.add(h + " h"); //$NON-NLS-1$
		if (m != 0)
			parts.add(m + " min"); //$NON-NLS-1$
		if (s != 0)
			parts.add(s + " s"); //$NON-NLS-1$
		if (ms != 0)
			parts.add(ms + " ms"); //$NON-NLS-1$
		return String.join(" ", parts); //$NON-NLS-1$
	}

	private static class WhitelistedThreadFactory implements ThreadFactory {
		private static final AtomicInteger TIMEOUT_THREAD_ID = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			var thread = new Thread(r, "ajts-to-" + TIMEOUT_THREAD_ID.getAndIncrement()); //$NON-NLS-1$
			if (thread.getPriority() != Thread.NORM_PRIORITY)
				thread.setPriority(Thread.NORM_PRIORITY);
			ArtemisSecurityManager.requestThreadWhitelisting(thread);
			return thread;
		}
	}
}
