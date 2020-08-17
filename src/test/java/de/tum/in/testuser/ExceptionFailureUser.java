package de.tum.in.testuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.MultipleAssertionsError;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.testuser.subject.CustomException;

@AllowThreads(maxActiveCount = 100)
@TestMethodOrder(Alphanumeric.class)
//@UseLocale("en")
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
		public String toString() {
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
	public void assertionFailed() throws Exception {
		assertEquals(1, 2);
	}

	@PublicTest
	public void assertJMultipleFailures() {
		assertThat("ABC").satisfiesAnyOf(s -> {
			fail("A");
		}, s -> {
			fail("B");
		});
	}

	@PublicTest
	public void customException() {
		throw new CustomException();
	}

	@PublicTest
	public void exceptionInInitializer() {
		assertEquals(InitFailure.x, 0);
	}

	@PublicTest
	public void faultyGetCauseException() {
		throw new AssertionError("XYZ", new FaultyGetCauseException());
	}

	@PublicTest
	public void faultyToStringException() {
		throw new FaultyToStringException();
	}

	@PublicTest
	public void multipleAssertions() {
		throw new MultipleAssertionsError(new TextDescription("Failed with %d", 5),
				List.of(new AssertionError("X", new CustomException())));
	}

	@PublicTest
	public void multipleFailures() {
		assertAll(() -> {
			fail("A");
		}, () -> {
			fail("B");
		});
	}

	@PublicTest
	public void nullPointer() {
		Objects.requireNonNull(null, "XYZ");
	}

	@PublicTest
	public void softAssertion() {
		throw new SoftAssertionError(List.of("A", "B"));
	}
}
