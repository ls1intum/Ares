package de.tum.in.testuser.subject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.xyz.Circumvention;
import org.apache.xyz.MaliciousExceptionB;

public final class SecurityPenguin {

	private SecurityPenguin() {

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

	public static void useReflectionPrivileged() {
		AccessController.doPrivileged((PrivilegedAction<String>) () -> {
			try {
				Class.forName("de.tum.in.test.api.io.IOTester").getDeclaredFields()[0].setAccessible(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return "42";
		});
	}

	public static void maliciousExceptionA() {
		throw new MaliciousExceptionA();
	}

	public static boolean maliciousExceptionB() {
		AtomicBoolean ab = new AtomicBoolean();
		try {
			throw new MaliciousExceptionB(ab);
		} catch (@SuppressWarnings("unused") SecurityException e) {
			// nothing
		}
		return ab.get();
	}

	@SuppressWarnings("resource")
	public static String tryExecuteGit() {
		try {
			return new String(Runtime.getRuntime().exec("git --help").getInputStream().readAllBytes())
					.replaceAll("\\W| ", "_");
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static boolean tryEvilPermission() {
		AtomicBoolean ab = new AtomicBoolean();
		try {
			System.getSecurityManager().checkPermission(new Permission("setIO") {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean implies(Permission permission) {
					return false;
				}

				@Override
				public int hashCode() {
					return 0;
				}

				@Override
				public String getActions() {
					return null;
				}

				@Override
				public boolean equals(Object obj) {
					return false;
				}

				@Override
				public String toString() {
					try {
						PathAccessPenguin.accessPath(Path.of("pom.xml"));
						ab.set(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return super.toString();
				}
			});
		} catch (@SuppressWarnings("unused") SecurityException e) {
			// do nothing
		}
		return ab.get();
	}

}
