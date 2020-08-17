package de.tum.in.test.api.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.EXPERIMENTAL)
public class DelayedFilter<T> implements Predicate<T> {

	private final Predicate<T> original;
	private final int delay;
	private final boolean[] buffer;
	private int pos;
	private boolean lastValue;

	public DelayedFilter(int delay, Predicate<T> original, boolean startValue) {
		this.original = Objects.requireNonNull(original);
		if (delay <= 0)
			throw new IllegalArgumentException("invalid delay: " + delay);
		this.delay = delay;
		this.buffer = new boolean[delay];
		this.lastValue = startValue;
		Arrays.fill(buffer, startValue);
	}

	@Override
	public boolean test(T t) {
		lastValue = buffer[pos];
		buffer[pos] = original.test(t);
		pos = nextPos();
		return lastValue;
	}

	public boolean lastValue() {
		return lastValue;
	}

	private int nextPos() {
		return (pos + 1) % delay;
	}
}
