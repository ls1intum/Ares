package de.tum.in.test.api.util;

import java.util.*;
import java.util.function.Predicate;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public class DelayedFilter<T> implements Predicate<T> {

	private final Predicate<T> original;
	private final int delay;
	private final boolean[] buffer;
	private int pos;
	private boolean hasNotBeenTrue = true;
	private boolean hasNotBeenFalse = true;
	private boolean lastValue;

	public DelayedFilter(int delay, Predicate<T> original, boolean startValue) {
		this.original = Objects.requireNonNull(original);
		if (delay <= 0)
			throw new IllegalArgumentException("invalid delay: " + delay); //$NON-NLS-1$
		this.delay = delay;
		this.buffer = new boolean[delay];
		this.lastValue = startValue;
		Arrays.fill(buffer, startValue);
	}

	@Override
	public boolean test(T t) {
		lastValue = buffer[pos];
		updateWithNewValue(original.test(t));
		return lastValue;
	}

	private void updateWithNewValue(boolean newValue) {
		buffer[pos] = newValue;
		pos = nextPos();
		if (hasNotBeenTrue && newValue)
			hasNotBeenTrue = false;
		else if (hasNotBeenFalse && !newValue)
			hasNotBeenFalse = false;
	}

	public boolean lastValue() {
		return lastValue;
	}

	public boolean hasBeenTrue() {
		return !hasNotBeenTrue;
	}

	public boolean hasBeenFalse() {
		return !hasNotBeenFalse;
	}

	private int nextPos() {
		return (pos + 1) % delay;
	}
}
