package de.tum.in.test.api.util.sanitization;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.SyncFailedException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.AnnotationFormatError;
import java.lang.module.FindException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.net.http.WebSocketHandshakeException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileLockInterruptionException;
import java.nio.channels.IllegalChannelGroupException;
import java.nio.channels.IllegalSelectorException;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.AccessDeniedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.NotLinkException;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.zone.ZoneRulesException;
import java.util.ConcurrentModificationException;
import java.util.DuplicateFormatFlagsException;
import java.util.EmptyStackException;
import java.util.FormatFlagsConversionMismatchException;
import java.util.IllegalFormatCodePointException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatPrecisionException;
import java.util.IllegalFormatWidthException;
import java.util.InvalidPropertiesFormatException;
import java.util.MissingFormatArgumentException;
import java.util.MissingFormatWidthException;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.UnknownFormatFlagsException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutionException;
import java.util.jar.JarException;
import java.util.regex.PatternSyntaxException;
import java.util.zip.DataFormatException;
import java.util.zip.ZipError;
import java.util.zip.ZipException;

public final class ThrowableSets {

	private ThrowableSets() {
	}

	/**
	 * Classes in this set must satisfy that ...
	 * <ul>
	 * <li>they are known and trusted at compile time of AJTS <b>and</b></li>
	 * <li>they have no more fields than Throwable itself <b>or</b></li>
	 * <li>all their fields are (recursively) final and immutable classes, all of
	 * which are known and trusted at compile time <b>or</b></li>
	 * <li>access to the fields is restricted and controlled enough that the class
	 * and its fields can safely be categorized as immutable</li>
	 * </ul>
	 * Classes whose instances can be insecurely modified after creation or that
	 * allow references to unknown (sub-) classes <b>must not be contained</b> in
	 * this set.
	 */
	static final Set<Class<? extends Throwable>> SAFE_TYPES = Set.of(AbstractMethodError.class,
			AccessDeniedException.class, AnnotationFormatError.class, ArithmeticException.class,
			ArrayIndexOutOfBoundsException.class, ArrayStoreException.class, AssertionError.class,
			AsynchronousCloseException.class, AtomicMoveNotSupportedException.class, BindException.class,
			BootstrapMethodError.class, BrokenBarrierException.class, BufferOverflowException.class,
			BufferUnderflowException.class, ClassCastException.class, ClassCircularityError.class,
			ClassFormatError.class, CloneNotSupportedException.class, ClosedByInterruptException.class,
			ClosedChannelException.class, CoderMalfunctionError.class, ConcurrentModificationException.class,
			ConnectException.class, DataFormatException.class, DateTimeException.class, DateTimeParseException.class,
			DigestException.class, DirectoryIteratorException.class, DirectoryNotEmptyException.class,
			DuplicateFormatFlagsException.class, EOFException.class, EmptyStackException.class,
			EnumConstantNotPresentException.class, Error.class, Exception.class, ExecutionException.class,
			FileAlreadyExistsException.class, FileLockInterruptionException.class, FileNotFoundException.class,
			FileSystemAlreadyExistsException.class, FileSystemNotFoundException.class, FindException.class,
			FormatFlagsConversionMismatchException.class, GeneralSecurityException.class,
			HttpConnectTimeoutException.class, HttpRetryException.class, HttpTimeoutException.class, IOError.class,
			IOException.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class,
			IllegalCallerException.class, IllegalChannelGroupException.class, IllegalCharsetNameException.class,
			IllegalFormatCodePointException.class, IllegalFormatConversionException.class, IllegalFormatException.class,
			IllegalFormatFlagsException.class, IllegalFormatPrecisionException.class, IllegalFormatWidthException.class,
			IllegalMonitorStateException.class, IllegalSelectorException.class, IllegalStateException.class,
			IllegalThreadStateException.class, IncompatibleClassChangeError.class, IndexOutOfBoundsException.class,
			InstantiationError.class, InstantiationException.class, InternalError.class,
			InterruptedByTimeoutException.class, InterruptedException.class, InterruptedIOException.class,
			InvalidKeyException.class, InvalidParameterException.class, InvalidPathException.class,
			InvalidPropertiesFormatException.class, JarException.class, KeyException.class,
			KeyManagementException.class, KeyStoreException.class, LinkageError.class, MalformedInputException.class,
			MalformedURLException.class, MissingFormatArgumentException.class, MissingFormatWidthException.class,
			NoClassDefFoundError.class, NoRouteToHostException.class, NoSuchAlgorithmException.class,
			NoSuchElementException.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchFileException.class,
			NoSuchMethodError.class, NoSuchMethodException.class, NoSuchProviderException.class,
			NotDirectoryException.class, NotLinkException.class, NullPointerException.class,
			NumberFormatException.class, OutOfMemoryError.class, ParseException.class, PatternSyntaxException.class,
			PortUnreachableException.class, ProtocolException.class, ProviderMismatchException.class,
			ReflectiveOperationException.class, RuntimeException.class, SanitizationError.class,
			SecurityException.class, ServiceConfigurationError.class, SocketException.class,
			SocketTimeoutException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class,
			SyncFailedException.class, ThreadDeath.class, Throwable.class, UncheckedIOException.class,
			UnknownFormatConversionException.class, UnknownFormatFlagsException.class, UnknownHostException.class,
			UnknownServiceException.class, UnresolvedAddressException.class, UnsatisfiedLinkError.class,
			UnsupportedAddressTypeException.class, UnsupportedCharsetException.class,
			UnsupportedClassVersionError.class, UnsupportedEncodingException.class, UnsupportedOperationException.class,
			UnsupportedTemporalTypeException.class, UserPrincipalNotFoundException.class, VerifyError.class,
			WebSocketHandshakeException.class, ZipError.class, ZipException.class, ZoneRulesException.class,
			junit.framework.AssertionFailedError.class, junit.framework.ComparisonFailure.class,
			org.junit.ComparisonFailure.class, org.junit.internal.ArrayComparisonFailure.class);
	// up to IllegalState

	/**
	 * This set contains classes that satisfy the criteria of {@link #SAFE_TYPES},
	 * apart from the following:
	 * <ul>
	 * <li>The classes are allowed to contain <b>non-final</b> fields of the type
	 * Throwable or List of Throwable.</li>
	 * <ul>
	 */
	static final Set<Class<? extends Throwable>> THROWABLE_FIELD_TYPES = Set.of();
}
