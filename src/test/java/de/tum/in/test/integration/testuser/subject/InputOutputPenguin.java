package de.tum.in.test.integration.testuser.subject;

public final class InputOutputPenguin extends MiniJava {

	private InputOutputPenguin() {
	}

	public static void writeTwoLines() {
		write("Pinguine sind die Besten!");
		write("Nieder mit den Eisbären!");
	}

	public static void calculateSquare() {
		int a = readInt("Zahl eingeben:");
		write("Ausgabe:");
		write(a * a);
	}

	public static void readTwoTimes() {
		int a = readInt("Zahl eingeben:");
		int b = readInt("Nächste");
		write("Ausgabe:");
		write(a * b);
	}
}
