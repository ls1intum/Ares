package de.tum.in.test.integration.testuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.MultipleAssertionsError;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.integration.testuser.subject.*;

@UseLocale("en")
@AllowThreads(maxActiveCount = 100)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test.{java,class}", type = PathType.GLOB)
@SuppressWarnings("static-method")
public class ExceptionFailureUser {

	static class FaultyGetCauseException extends RuntimeException {

		private static final long serialVersionUID = 487618816288959774L;

		public FaultyGetCauseException() {
			super("DEF");
		}

		@Override
		public synchronized Throwable getCause() {
			throw new NullPointerException("Faulty");
		}
	}

	static class FaultyToStringException extends RuntimeException {

		private static final long serialVersionUID = -3215361616244777479L;

		public FaultyToStringException() {
			super("XYZ");
		}

		@Override
		public String getMessage() {
			throw new IllegalStateException("Faulty");
		}
	}

	static class InitFailure {
		static final int x;
		static {
			x = 4 / 0;
		}
	}

	@PublicTest
	void assertJMultipleFailures() {
		assertThat("ABC").satisfiesAnyOf(s -> {
			fail("A");
		}, s -> {
			fail("B");
		});
	}

	@PublicTest
	void assertionFailOnly() throws Exception {
		org.junit.jupiter.api.Assertions.fail("This test failed. Penguin.");
	}

	@PublicTest
	void assertionFailed() throws Exception {
		assertEquals(1, 2);
	}

	@PublicTest
	void customException() {
		throw new CustomException();
	}

	@PublicTest
	void exceptionInInitializer() {
		assertEquals(InitFailure.x, 0);
	}

	@PublicTest
	void faultyGetCauseException() {
		throw new AssertionError("XYZ", new FaultyGetCauseException());
	}

	@PublicTest
	void faultyToStringException() {
		throw new FaultyToStringException();
	}

	@PublicTest
	void multipleAssertions() {
		throw new MultipleAssertionsError(new TextDescription("Failed with %d", 5),
				List.of(new AssertionError("X", new CustomException())));
	}

	@PublicTest
	void multipleFailures() {
		assertAll(() -> {
			fail("A");
		}, () -> {
			fail("B");
		});
	}

	@PublicTest
	void nullPointer() {
		Objects.requireNonNull(null, "XYZ");
	}

	@PublicTest
	void softAssertion() {
		throw new SoftAssertionError(List.of("A", "B"));
	}

	@PublicTest
	void throwExceptionInInitializerError() {
		ExceptionFailurePenguin.throwExceptionInInitializerError();
	}

	@PublicTest
	void throwNullPointerException() {
		ExceptionFailurePenguin.throwNullPointerException();
	}
}
