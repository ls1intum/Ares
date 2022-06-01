package de.tum.in.test.api.io;

import java.util.*;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * This is basically a String with additional information and some constraints.
 * <p>
 * Constraints:
 * <ul>
 * <li>Must not contain any line break or new line (<code>'\r'</code> or
 * <code>'\n'</code>)</li>
 * </ul>
 * <p>
 * Additional information:
 * <ul>
 * <li><i>Optional:</i> a line number, starting at 1.</li>
 * </ul>
 *
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.1
 */
@API(status = Status.STABLE)
public interface Line {

	String text();

	boolean isComplete();

	/**
	 * Number of the Line, starting at 1.
	 *
	 * @return the line number or -1, if none is set
	 */
	int lineNumber();

	/**
	 * This does <b>not</b> return the lines content only! Use {@link #text()} for
	 * that purpose.
	 */
	@Override
	String toString();

	default boolean contentEquals(CharSequence cs) {
		return text().contentEquals(cs);
	}

	static AbstractLine of(String text) {
		return new StaticLine(text);
	}

	static List<AbstractLine> linesOf(String multiLineText) {
		return Arrays.stream(multiLineText.split("\r?\n|\r", -1)).map(Line::of) //$NON-NLS-1$
				.collect(Collectors.toUnmodifiableList());
	}

	static String joinLinesToString(Collection<? extends Line> lines, CharSequence delimiter) {
		return lines.stream().map(Line::text).collect(Collectors.joining(delimiter));
	}
}
