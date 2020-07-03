package de.tum.in.test.api.internal;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public final class BlacklistedInvoker {
	private BlacklistedInvoker() {
		// prevent instantiation
	}

	public static void invoke(Runnable action) {
		action.run();
	}

	public static <T, R> R invoke(T t, Function<T, R> action) {
		return action.apply(t);
	}

	public static <T> void invoke(T t, Consumer<T> action) {
		action.accept(t);
	}

	public static <R> R invoke(Supplier<R> action) {
		return action.get();
	}

	public static <R> R invokeOrElse(Supplier<R> action, Supplier<R> alternative) {
		try {
			return action.get();
		} catch (@SuppressWarnings("unused") SecurityException se) {
			return alternative.get();
		}
	}
}
