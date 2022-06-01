package de.tum.in.test.integration.testuser;

import static de.tum.in.test.api.io.OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.io.*;
import de.tum.in.test.api.jqwik.*;
import de.tum.in.test.api.localization.UseLocale;

@Hidden
@UseLocale("de")
@SuppressWarnings("static-method")
@Deadline("2200-01-01 16:00")
/**
 * This is needed because jqwik makes use of the common pool since version
 * 1.3.5, and the test code run there might need to be whitelisted, which in
 * turn requires the thread (here the common pool) to be whitelisted. This also
 * causes the more complicated situation concerning the configuration below.
 * <p>
 * Also see {@link de.tum.in.test.api.jqwik} on why it is all threads here.
 */
@TrustedThreads(TrustScope.ALL_THREADS)
/*
 * In extended trust scope, we might need to whitelist the JDK, because file
 * access is now more restricted, to have more control over security that got
 * lost by using extended mode in the first place.
 */
@WhitelistPath(value = "**/*jdk*/**/bin/**", type = PathType.GLOB_ABSOLUTE)
/*
 * In extended trust scope, we should restrict classes that are loaded even
 * more, like here to a specific package to avoid loading student classes that
 * would reside in trusted packages. Note that test-classes is only needed here
 * in the Ares repository, you should NOT do this for real exercises.
 */
@WhitelistPath(value = "target/{classes,test-classes}/de/tum**", type = PathType.GLOB)
public class JqwickUser {

	@Example
	@Deadline("2200-01-01 16:00")
	void exampleHiddenCustomDeadlineFuture() {
		// nothing
	}

	@Hidden
	@Example
	@Deadline("2000-01-01 16:00")
	void exampleHiddenCustomDeadlinePast() {
		// nothing
	}

	@Example
	void exampleHiddenNormal() {
		// nothing
	}

	@Public
	@Example
	@Deadline("2200-01-01 16:00")
	void examplePublicCustomDeadline() {
		// nothing
	}

	@Public
	@Example
	void examplePublicNormal() {
		// nothing
	}

	@Hidden
	@Property
	@Deadline("2200-01-01 16:00")
	boolean propertyHiddenCustomDeadlineFuture(@ForAll @Positive int x) {
		return x != 0;
	}

	@Hidden
	@Property
	@Deadline("2000-01-01 16:00")
	boolean propertyHiddenCustomDeadlinePast(@ForAll @Positive int x) {
		return x != 0;
	}

	@Hidden
	@Property
	boolean propertyHiddenNormal(@ForAll @Positive int x) {
		return x != 0;
	}

	@Public
	@Property
	@Deadline("2200-01-01 16:00")
	boolean propertyPublicCustomDeadline(@ForAll @Positive int x) {
		return x != 0;
	}

	@Public
	@Property
	boolean propertyPublicNormal(@ForAll @Positive int x) {
		return x != 0;
	}

	@Public
	@Property
	void propertyUseIOTesterCorrect(@ForAll IOTester test, @ForAll @CharRange(from = 'a', to = 'z') String s) {
		test.reset();
		System.out.println(s);
		Assertions.assertThat(test.out().getLines(DONT_IGNORE_LAST_EMPTY_LINE)).containsExactly(Line.of(s),
				Line.of(""));
	}

	@Public
	@Property
	void propertyUseIOTesterWrong(@ForAll IOTester test, @ForAll @CharRange(from = 'a', to = 'z') String s) {
		test.reset();
		System.out.println(s.length() < 5 ? s : s.substring(0, 5));
		Assertions.assertThat(test.out().getLines(DONT_IGNORE_LAST_EMPTY_LINE)).containsExactly(Line.of(s),
				Line.of(""));
	}

	@Public
	@Example
	@StrictTimeout(value = 500, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutEndlessLoop(@ForAll @Positive int x) {
		int y = 0;
		while (x != 0 || y > 0)
			y += x;
	}

	@Public
	@Example
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepExample(@SuppressWarnings("unused") @ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@Public
	@Property
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepProperty(@SuppressWarnings("unused") @ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@Public
	@Property(tries = 1)
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepTries(@SuppressWarnings("unused") @ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@Hidden
	void testHiddenIncomplete() {
		// nothing
	}

	@Public
	void testPublicIncomplete() {
		// nothing
	}

	@Public
	@Example
	void testLocaleDe() {
		assertThat(Locale.getDefault()).isEqualTo(Locale.GERMAN);
	}
}
