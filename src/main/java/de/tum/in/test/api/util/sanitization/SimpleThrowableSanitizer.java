package de.tum.in.test.api.util.sanitization;

import static de.tum.in.test.api.util.BlacklistedInvoker.invoke;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.MalformedInputException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.NotLinkException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.zone.ZoneRulesException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.stream.Collectors;

public enum SimpleThrowableSanitizer implements SpecificThrowableSanitizer {
	INSTANCE;

	private final Set<Class<? extends Throwable>> types = Set.of(Throwable.class, Error.class, Exception.class,
			RuntimeException.class, ArithmeticException.class, NullPointerException.class,
			IllegalArgumentException.class, NoClassDefFoundError.class, IllegalStateException.class,
			UnsupportedOperationException.class, SecurityException.class, StackOverflowError.class, ThreadDeath.class,
			ClassCastException.class, ArrayStoreException.class, IndexOutOfBoundsException.class,
			ArrayIndexOutOfBoundsException.class, AssertionError.class, ConcurrentModificationException.class,
			LinkageError.class, NumberFormatException.class, DateTimeException.class, DateTimeParseException.class,
			ZoneRulesException.class, UnsupportedTemporalTypeException.class, EmptyStackException.class,
			BufferOverflowException.class, BufferUnderflowException.class, IllegalMonitorStateException.class,
			InvalidPathException.class, UncheckedIOException.class, StringIndexOutOfBoundsException.class,
			IOException.class, OutOfMemoryError.class, FileNotFoundException.class, NoSuchFileException.class,
			AccessDeniedException.class, DirectoryNotEmptyException.class, NotDirectoryException.class,
			NotLinkException.class, FileAlreadyExistsException.class, MalformedInputException.class,
			MalformedURLException.class, org.junit.ComparisonFailure.class, junit.framework.ComparisonFailure.class,
			org.junit.internal.ArrayComparisonFailure.class, junit.framework.AssertionFailedError.class);

	@Override
	public boolean canSanitize(Throwable t) {
		return types.contains(t.getClass());
	}

	@Override
	public Throwable sanitize(Throwable t) throws SanitizationError {
		Throwable causeVal = invoke(t::getCause);
		Throwable[] supprVal = invoke(t::getSuppressed);
		if (causeVal != null) {
			Field cause;
			try {
				cause = Throwable.class.getDeclaredField("cause");
				cause.setAccessible(true);
				cause.set(t, ThrowableSanitizer.sanitize(causeVal));
			} catch (ReflectiveOperationException e) {
				throw new SanitizationError(e);
			}
		}
		if (supprVal.length != 0) {
			try {
				Field suppr = Throwable.class.getDeclaredField("suppressedExceptions");
				suppr.setAccessible(true);
				suppr.set(t, Arrays.stream(supprVal).map(ThrowableSanitizer::sanitize)
						.collect(Collectors.toUnmodifiableList())); // this breaks addSuppressed by purpose
			} catch (ReflectiveOperationException e) {
				throw new SanitizationError(e);
			}
		}
		return t;
	}
}