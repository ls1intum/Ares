package de.tum.in.testuser;

import static de.tum.in.test.api.io.OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;

import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.CharRange;
import net.jqwik.api.constraints.Positive;

import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.io.Line;
import de.tum.in.test.api.jqwik.Hidden;
import de.tum.in.test.api.jqwik.Public;
import de.tum.in.test.api.localization.UseLocale;

@Hidden
@UseLocale("de")
@SuppressWarnings("static-method")
@Deadline("2200-01-01 16:00")
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
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
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
