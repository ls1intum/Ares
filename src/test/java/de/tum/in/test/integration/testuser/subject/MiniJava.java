package de.tum.in.test.integration.testuser.subject;

import java.io.*;
import java.util.Random;

/**
 * This class provides convenience methods to make the barrier of entry into
 * programming easier. In addition to that it realizes an educational
 * sub-language to Java called "Mini Java" with simpler grammar.
 * <p>
 * <i>Diese Klasse stellt simple Methoden zur Verfügung um den Einstieg in die
 * Programmierung zu vereinfachen. Zusätzlich dazu wird damit eine für die Lehre
 * zusammengestellte Sub-Programmiersprache von Java umgesetzt, die sich "Mini
 * Java" nennt und eine einfachere Grammatik besitzt</i>
 */
public class MiniJava {
	private static Random rand;
	private static InputStream is = System.in;
	private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

	/**
	 * Reads a {@link String} from the console, and prompts the user by printing the
	 * given <code>text</code> with a line break to the console.
	 * <p>
	 * <i>Liest einen {@link String} von der Konsole ein, und fordert den oder die
	 * Benutzerin durch die Ausgabe des übergebenen <code>text</code>es zur Eingabe
	 * auf (mit Zeilenumbruch).</i>
	 *
	 * @param text the text to display on the console before reading an input.
	 * @return the input string or <code>null</code>, if no input is available
	 *         (should normally not happen)
	 */
	public static String readString(String text) {
		// Exchange the reader in case System.in has changed.
		// This might be necessary for testing.
		if (System.in != is) {
			is = System.in;
			bufferedReader = new BufferedReader(new InputStreamReader(is));
		}
		try {
			System.out.println(text);
			return bufferedReader.readLine();
		} catch (IOException e) {
			// We "hide" the exception in the method signature by rethrowing an unchecked
			// exception
			throw new UncheckedIOException("Konnte Eingabe nicht lesen.", e);
		}
	}

	/**
	 * Reads a {@link String} from the console, and prompts the user by printing the
	 * line
	 *
	 * <pre>
	 * Eingabe:
	 * </pre>
	 *
	 * <i>Liest einen {@link String} von der Konsole ein, und fordert den oder die
	 * Benutzerin dazu auf durch die Ausgabe von
	 *
	 * <pre>
	 * Eingabe:
	 * </pre>
	 *
	 * </i>
	 */
	public static String readString() {
		return readString("Eingabe:");
	}

	/**
	 * Tries to read an <code>int</code> from the console, and retires if the input
	 * was not a valid integer. It prompts the user by printing the given
	 * <code>text</code> with a line break to the console.
	 * <p>
	 * <i>Versucht einen <code>int</code>-Wert von der Konsole einzulesen, und
	 * wiederholt die Anfrage solange es nicht nicht um eine ganze Zahl handelt. Der
	 * oder die Benutzerin wird durch die Ausgabe des übergebenen
	 * <code>text</code>es zur Eingabe aufgefordert (mit Zeilenumbruch).</i>
	 *
	 * @see Integer#parseInt(String)
	 */
	public static int readInt(String text) {
		Integer x = null;
		do {
			String s = readString(text);
			if (s == null)
				throw new IllegalStateException("Es ist keine Eingabe (mehr) verfügbar.");
			try {
				x = Integer.parseInt(s.trim());
			} catch (@SuppressWarnings("unused") NumberFormatException e) {
				// try again
			}
		} while (x == null);
		return x;
	}

	/**
	 * Works like {@link #readInt(String)}, but uses the following preset text to
	 * prompt the user:
	 *
	 * <pre>
	 * Geben Sie eine ganze Zahl ein:
	 * </pre>
	 *
	 * <i>Funktioniert wie {@link #readInt(String)}, nutzt jedoch folgenden
	 * vorgegebenen Text:
	 *
	 * <pre>
	 * Geben Sie eine ganze Zahl ein:
	 * </pre>
	 *
	 * </i>
	 */
	public static int readInt() {
		return readInt("Geben Sie eine ganze Zahl ein:");
	}

	/**
	 * Identical to {@link #readInt(String)}.
	 * <p>
	 * <i>Identisch zu {@link #readInt(String)}</i>
	 */
	public static int read(String text) {
		return readInt(text);
	}

	/**
	 * Identical to {@link #readInt()}.
	 * <p>
	 * <i>Identisch zu {@link #readInt()}</i>
	 */
	public static int read() {
		return readInt();
	}

	/**
	 * Tries to read an <code>double</code> from the console, and retires if the
	 * input was not a valid double value. It prompts the user by printing the given
	 * <code>text</code> with a line break to the console.
	 * <p>
	 * <i>Versucht einen <code>double</code>-Wert von der Konsole einzulesen, und
	 * wiederholt die Anfrage solange es nicht nicht um eine gültige
	 * <code>double</code>-Fließkommazahl handelt. Der oder die Benutzerin wird
	 * durch die Ausgabe des übergebenen <code>text</code>es zur Eingabe
	 * aufgefordert (mit Zeilenumbruch).</i>
	 *
	 * @see Double#parseDouble(String)
	 */
	public static double readDouble(String text) {
		Double x = null;
		do {
			String s = readString(text);
			if (s == null)
				throw new IllegalStateException("Es ist keine Eingabe (mehr) verfügbar.");
			try {
				x = Double.parseDouble(s.trim());
			} catch (@SuppressWarnings("unused") NumberFormatException e) {
				// try again
			}
		} while (x == null);
		return x;
	}

	/**
	 * Works like {@link #readDouble(String)}, but uses the following preset text to
	 * prompt the user:
	 *
	 * <pre>
	 * Geben Sie eine Zahl ein:
	 * </pre>
	 *
	 * <i>Funktioniert wie {@link #readDouble(String)}, nutzt jedoch folgenden
	 * vorgegebenen Text:
	 *
	 * <pre>
	 * Geben Sie eine Zahl ein:
	 * </pre>
	 *
	 * </i>
	 */
	public static double readDouble() {
		return readDouble("Geben Sie eine Zahl ein:");
	}

	/**
	 * Prints the {@link String} to the console and breaks the line.
	 * <p>
	 * <i>Gibt den {@link String} auf der Konsole aus und beginnt eine neue
	 * Zeile.</i>
	 */
	public static void write(String output) {
		System.out.println(output);
	}

	/**
	 * Prints the <code>int</code> to the console and breaks the line.
	 * <p>
	 * <i>Gibt den <code>int</code>-Wert auf der Konsole aus und beginnt eine neue
	 * Zeile.</i>
	 */
	public static void write(int output) {
		write(String.valueOf(output));
	}

	/**
	 * Prints the <code>double</code> to the console and breaks the line.
	 * <p>
	 * <i>Gibt den <code>double</code>-Wert auf der Konsole aus und beginnt eine
	 * neue Zeile.</i>
	 */
	public static void write(double output) {
		write(String.valueOf(output));
	}

	/**
	 * Identical to {@link #write(String)}.
	 * <p>
	 * <i>Identisch zu {@link #write(String)}</i>
	 */
	public static void writeLineConsole(String output) {
		System.out.println(output);
	}

	/**
	 * Identical to {@link #write(int)}.
	 * <p>
	 * <i>Identisch zu {@link #write(int)}</i>
	 */
	public static void writeLineConsole(int output) {
		writeLineConsole(String.valueOf(output));
	}

	/**
	 * Identical to {@link #write(double)}.
	 * <p>
	 * <i>Identisch zu {@link #write(double)}</i>
	 */
	public static void writeLineConsole(double output) {
		writeLineConsole(String.valueOf(output));
	}

	/**
	 * Prints a line separator in the console.
	 * <p>
	 * <i>Gibt einen Zeilenumbruch auf der Konsole aus.</i>
	 */
	public static void writeLineConsole() {
		writeLineConsole("");
	}

	/**
	 * Prints the {@link String} to the console. Does not end with a line break.
	 * <p>
	 * <i>Gibt den {@link String} auf der Konsole aus. Endet nicht mit einem
	 * Zeilenumbruch.</i>
	 */
	public static void writeConsole(String output) {
		System.out.print(output);
	}

	/**
	 * Prints the <code>int</code> to the console. Does not end with a line break.
	 * <p>
	 * <i>Gibt den <code>int</code>-Wert auf der Konsole aus. Endet nicht mit einem
	 * Zeilenumbruch.</i>
	 */
	public static void writeConsole(int output) {
		writeConsole(String.valueOf(output));
	}

	/**
	 * Prints the <code>double</code> to the console. Does not end with a line
	 * break.
	 * <p>
	 * <i>Gibt den <code>double</code>-Wert auf der Konsole aus. Endet nicht mit
	 * einem Zeilenumbruch.</i>
	 */
	public static void writeConsole(double output) {
		writeConsole(String.valueOf(output));
	}

	public static void resetRandom() {
		rand = null;
	}

	public static void setRandomWithSeed(int seed) {
		if (rand != null) {
			throw new SecurityException("Don't touch this");
		}
		rand = new Random(seed);
	}

	public static void setRandom() {
		setRandomWithSeed(144);
	}

	public static int randomInt(int minval, int maxval) {
		return minval + rand.nextInt(maxval - minval + 1);
	}

	/**
	 * Draws a random card, represented by its value. The value ranges from 1
	 * (inclusive) to 105 (inclusive).
	 * <p>
	 * <i>Zieht eine zufällige Karte, repräsentiert durch ihren Wert. Der Wert liegt
	 * im Bereich 1 (inklusive) bis 105 (inklusive).</i>
	 */
	public static int drawCard() {
		if (rand == null) {
			setRandom();
		}
		return randomInt(1, 105);
	}

	/**
	 * Returns a random integer between 1 (inclusive) to 6 (inclusive).
	 * <p>
	 * <i>Gibt eine zufällige Zahl zwischen 1 (inklusive) bis 6 (inklusive)
	 * zurück.</i>
	 */
	public static int dice() {
		if (rand == null) {
			setRandom();
		}
		return randomInt(1, 6);
	}
}
