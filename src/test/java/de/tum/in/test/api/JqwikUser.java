package de.tum.in.test.api;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.io.Line;
import de.tum.in.test.api.jqwik.HiddenJqwik;
import de.tum.in.test.api.jqwik.PublicJqwik;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.CharRange;
import net.jqwik.api.constraints.Positive;

@SuppressWarnings("static-method")
@Deadline("2200-01-01 16:00")
public class JqwikUser {

	@HiddenJqwik
	void testHiddenIncomplete() {
		// nothing
	}

	@PublicJqwik
	void testPublicIncomplete() {
		// nothing
	}

	@HiddenJqwik
	@Example
	void exampleHiddenNormal() {
		// nothing
	}

	@PublicJqwik
	@Example
	void examplePublicNormal() {
		// nothing
	}

	@HiddenJqwik
	@Example
	@Deadline("2000-01-01 16:00")
	void exampleHiddenCustomDeadlinePast() {
		// nothing
	}

	@HiddenJqwik
	@Example
	@Deadline("2200-01-01 16:00")
	void exampleHiddenCustomDeadlineFuture() {
		// nothing
	}

	@PublicJqwik
	@Example
	@Deadline("2200-01-01 16:00")
	void examplePublicCustomDeadline() {
		// nothing
	}

	@HiddenJqwik
	@Property
	boolean propertyHiddenNormal(@ForAll @Positive int x) {
		return x != 0;
	}

	@PublicJqwik
	@Property
	boolean propertyPublicNormal(@ForAll @Positive int x) {
		return x != 0;
	}

	@HiddenJqwik
	@Property
	@Deadline("2000-01-01 16:00")
	boolean propertyHiddenCustomDeadlinePast(@ForAll @Positive int x) {
		return x != 0;
	}

	@HiddenJqwik
	@Property
	@Deadline("2200-01-01 16:00")
	boolean propertyHiddenCustomDeadlineFuture(@ForAll @Positive int x) {
		return x != 0;
	}

	@PublicJqwik
	@Property
	@Deadline("2200-01-01 16:00")
	boolean propertyPublicCustomDeadline(@ForAll @Positive int x) {
		return x != 0;
	}

	@PublicJqwik
	@Property
	void propertyUseIOTesterCorrect(@ForAll IOTester test, @ForAll @CharRange(from = 'a', to = 'z') String s) {
		test.reset();
		System.out.println(s);
		Assertions.assertThat(test.getOutput()).containsExactly(Line.of(s), Line.of(""));
	}

	@PublicJqwik
	@Property
	void propertyUseIOTesterWrong(@ForAll IOTester test, @ForAll @CharRange(from = 'a', to = 'z') String s) {
		test.reset();
		System.out.println(s.length() < 5 ? s : s.substring(0, 5));
		Assertions.assertThat(test.getOutput()).containsExactly(Line.of(s), Line.of(""));
	}

	@PublicJqwik
	@Example
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutEndlessLoop(@ForAll @Positive int x) {
		int y = 0;
		while (x != 0 || y > 0)
			y += x;
	}

	@PublicJqwik
	@Property(tries = 1)
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepTries(@SuppressWarnings("unused") @ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@PublicJqwik
	@Example
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepExample(@SuppressWarnings("unused") @ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@PublicJqwik
	@Property
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepProperty(@SuppressWarnings("unused") @ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}
}