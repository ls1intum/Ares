package de.tum.in.test.api.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DelayedFilterTest {

	@Test
	void testDelayedFilter() {
		assertThrows(IllegalArgumentException.class, () -> new DelayedFilter<>(-1, x -> true, true));
		assertThrows(IllegalArgumentException.class, () -> new DelayedFilter<>(0, x -> true, true));
		assertThrows(NullPointerException.class, () -> new DelayedFilter<>(1, null, true));
	}

	@Test
	void testTestDelayOfOne() {
		DelayedFilter<Character> filter = new DelayedFilter<>(1, Character::isDigit, false);
		assertThat(filter.test('X')).isFalse();
		assertThat(filter.test('0')).isFalse();
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.test('X')).isFalse();
		assertThat(filter.test('X')).isFalse();
	}

	@Test
	void testTestDelayOfTwo() {
		DelayedFilter<Character> filter = new DelayedFilter<>(2, Character::isDigit, true);
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.test('X')).isFalse();
		assertThat(filter.test('0')).isFalse();
		assertThat(filter.test('X')).isFalse();
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.test('X')).isFalse();
	}

	@Test
	void testLastValue() {
		DelayedFilter<Character> filter = new DelayedFilter<>(3, Character::isDigit, true);
		assertThat(filter.lastValue()).isTrue();
		assertThat(filter.test('0')).isTrue();
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.test('X')).isTrue();
		assertThat(filter.lastValue()).isTrue();
		assertThat(filter.test('X')).isFalse();
		assertThat(filter.lastValue()).isFalse();
	}

	@Test
	void testHasBeen() {
		DelayedFilter<Character> filter = new DelayedFilter<>(2, Character::isDigit, true);

		assertThat(filter.hasBeenTrue()).isFalse();
		assertThat(filter.hasBeenFalse()).isFalse();

		assertThat(filter.test('0')).isTrue();

		assertThat(filter.hasBeenTrue()).isTrue();
		assertThat(filter.hasBeenFalse()).isFalse();

		assertThat(filter.test('X')).isTrue();

		assertThat(filter.hasBeenTrue()).isTrue();
		assertThat(filter.hasBeenFalse()).isTrue();
	}
}
