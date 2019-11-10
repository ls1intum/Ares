package de.tum.in.test.other;

import org.apache.xyz.Circumvention;

public class Penguin extends MiniJava {

	public static void main(String[] args) {
		write("Pinguine sind die Besten!");
		write("Nieder mit den Eisbären!");
	}

	public static void mäin(String[] args) {
		int a = readInt("Zahl eingeben:");
		int b = a * a;
		write("Ausgabe:");
		write(b);
	}
	
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
}
