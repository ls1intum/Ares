package de.tum.in.testsecurity;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.xyz.Circumvention;

public class Penguin extends MiniJava {

	public static void main(String[] args) {
		write("Pinguine sind die Besten!");
		write("Nieder mit den Eisbären!");
	}

	@SuppressWarnings("unused")
	public static void mäin(String[] args) {
		int a = readInt("Zahl eingeben:");
		int b = a * a;
		write("Ausgabe:");
		write(b);
	}

	@SuppressWarnings("unused")
	public static void readTwoTimes() {
		int a = readInt("Zahl eingeben:");
		int b = readInt("Nächste");
		write("Ausgabe:");
	}

	public static void useReflection() {
		try {
			Class.forName("de.tum.in.test.api.io.IOTester").getDeclaredFields()[0].setAccessible(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void useReflection2() {
		Thread d = new Circumvention();
		d.start();
		try {
			d.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void accessPath(Path p) throws IOException {
		Files.readString(p);
	}

	public static void maliciousException() {
		throw new MaliciousException();
	}

	public static String tryExecuteGit() {
		try {
			return new String(Runtime.getRuntime().exec("git --help").getInputStream().readAllBytes())
					.replaceAll("\\W| ", "_");
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void tryBreakThreadGroup() {
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		for (;;) {
			ThreadGroup parent = threadGroup.getParent();
			if (parent == null)
				break;
			threadGroup = parent;
		}
		new Thread(threadGroup, () -> {
		}).start();
	}
}
