package de.tum.in.test.api;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.io.Line;
import de.tum.in.test.api.jqwik.Hidden;
import de.tum.in.test.api.jqwik.Public;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.CharRange;
import net.jqwik.api.constraints.Positive;

@SuppressWarnings("static-method")
@Deadline("2200-01-01 16:00")
public class JqwickUser {

	@Hidden
	void testHiddenIncomplete() {
		// nothing
	}

	@Public
	void testPublicIncomplete() {
		// nothing
	}

	@Hidden
	@Example
	void exampleHiddenNormal() {
		// nothing
	}

	@Public
	@Example
	void examplePublicNormal() {
		// nothing
	}

	@Hidden
	@Example
	@Deadline("2000-01-01 16:00")
	void exampleHiddenCustomDeadlinePast() {
		// nothing
	}

	@Hidden
	@Example
	@Deadline("2200-01-01 16:00")
	void exampleHiddenCustomDeadlineFuture() {
		// nothing
	}

	@Public
	@Example
	@Deadline("2200-01-01 16:00")
	void examplePublicCustomDeadline() {
		// nothing
	}

	@Hidden
	@Property
	boolean propertyHiddenNormal(@ForAll @Positive int x) {
		return x != 0;
	}

	@Public
	@Property
	boolean propertyPublicNormal(@ForAll @Positive int x) {
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
	@Deadline("2200-01-01 16:00")
	boolean propertyHiddenCustomDeadlineFuture(@ForAll @Positive int x) {
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
	void propertyUseIOTesterCorrect(@ForAll IOTester test, @ForAll @CharRange(from = 'a', to = 'z') String s) {
		test.reset();
		System.out.println(s);
		Assertions.assertThat(test.getOutput()).containsExactly(Line.of(s), Line.of(""));
	}

	@Public
	@Property
	void propertyUseIOTesterWrong(@ForAll IOTester test, @ForAll @CharRange(from = 'a', to = 'z') String s) {
		test.reset();
		System.out.println(s.length() < 5 ? s : s.substring(0, 5));
		Assertions.assertThat(test.getOutput()).containsExactly(Line.of(s), Line.of(""));
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
	@Property(tries = 1)
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepTries(@ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@Public
	@Example
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepExample(@ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}

	@Public
	@Property
	@StrictTimeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void provokeTimeoutSleepProperty(@ForAll @Positive int x) {
		try {
			Thread.sleep(300);
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			// do nothing
		}
	}
}