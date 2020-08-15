package de.tum.in.testuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.SoftAssertionError;
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

	@PublicTest
	public void assertionFailed() throws Exception {
		assertEquals(1, 2);
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
	public void exceptionInInitializer() {
		assertEquals(InitFailure.x, 0);
	}

	@PublicTest
	public void multipleAssertions() {
		assertThat("ABC").satisfiesAnyOf(s -> {
			fail("A");
		}, s -> {
			fail("B");
		});
	}

	@PublicTest
	public void softAssertion() {
		throw new SoftAssertionError(List.of("A", "B"));
	}

	@PublicTest
	public void nullPointer() {
		Objects.requireNonNull(null, "XYZ");
	}

	@PublicTest
	public void customException() {
		throw new CustomException();
	}

	static class InitFailure {
		static final int x;
		static {
			x = 4 / 0;
		}
	}
}
