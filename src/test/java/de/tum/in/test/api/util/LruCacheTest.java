package de.tum.in.test.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.LongStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LruCacheTest {

	private static final int NUMBER_OF_PUT_OPERATIONS = 100;

	@ValueSource(ints = { 1, 2, 15, 16, 42 })
	@ParameterizedTest
	void testLruCache(int size) {
		var cache = new LruCache<Long, Integer>(size);

		for (int i = 0; i < NUMBER_OF_PUT_OPERATIONS; i++) {
			long key = i;
			cache.put(key, 0);

			assertThat(cache.size()).isLessThanOrEqualTo(size);

			var expectedKeys = LongStream.iterate(key, k -> k >= 0, k -> k - 1).limit(size).boxed()
					.toArray(Long[]::new);

			assertThat(cache).containsOnlyKeys(expectedKeys);
		}
	}
}
