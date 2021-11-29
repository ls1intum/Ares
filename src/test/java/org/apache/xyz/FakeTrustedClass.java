package org.apache.xyz;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import de.tum.in.test.api.io.IOTester;

public class FakeTrustedClass {

	public static void useCommonPoolBad() throws InterruptedException, ExecutionException {
		CompletableFuture.runAsync(() -> IOTester.class.getDeclaredFields()[0].setAccessible(true)).get();
	}
}
