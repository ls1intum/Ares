package de.tum.in.test.api.io;

import static de.tum.in.test.api.localization.Messages.localized;

import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.api.*;
import org.opentest4j.*;

/**
 * Captures console output as {@link Line}s, and therefore is OS line separator
 * independent. The lines get numbered.
 *
 * @see IOTester
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.1.1
 */
@API(status = Status.STABLE)
public final class OutputTester implements LineAcceptor {

	private static final int MAX_SPACES = 16;
	private static final int RANDOM_BOUND = MAX_SPACES * MAX_SPACES;
	private static final char[] SPACES = " ".repeat(MAX_SPACES).toCharArray(); //$NON-NLS-1$
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final Pattern EXPECTED_LINE_PATTERN = Pattern.compile("`" // line start //$NON-NLS-1$
			+ "\\(\\?x\\)" // comments enabled //$NON-NLS-1$
			+ "#([0-9A-F]{8})\\R" // the line number //$NON-NLS-1$
			+ ".+`" // actual line content and end of line //$NON-NLS-1$
			+ "(?=[^`]*(?:\\R|$))" // lookahead for not quoted text and then line break or end of the //$NON-NLS-1$
									// string
	);

	private final List<Line> actualOutput = new ArrayList<>();

	private final long randomBits;
	private final String randomString;

	public OutputTester() {
		randomBits = SECURE_RANDOM.nextLong();
		randomString = Long.toUnsignedString(randomBits, 16);
	}

	@Override
	public void acceptOutput(CharBuffer output) {
		if (output.length() == 0)
			return;
		DynamicLine currentLine;
		if (getCurrentLine().map(Line::isComplete).orElse(true)) {
			// start new line
			currentLine = new DynamicLine();
			addNewLine(currentLine);
		} else {
			// extend old line
			currentLine = (DynamicLine) getCurrentLine().get();
		}
		// add lines
		var lastPos = 0;
		var lastWasCarriageReturn = false;
		for (var i = 0; i < output.length(); i++) {
			var character = output.charAt(i);
			if (character == '\r' || character == '\n') {
				if (character == '\n' && lastWasCarriageReturn) {
					lastPos++;
				} else {
					currentLine.append(output.subSequence(lastPos, i));
					currentLine.complete();
					currentLine = new DynamicLine();
					addNewLine(currentLine);
					lastPos = i + 1;
				}
				lastWasCarriageReturn = character == '\r';
			} else if (lastWasCarriageReturn) {
				lastWasCarriageReturn = false;
			}
		}
		if (lastPos != output.length())
			currentLine.append(output.subSequence(lastPos, output.length()));
	}

	private void addNewLine(AbstractLine line) {
		actualOutput.add(line);
		line.setLineNumber(actualOutput.size());
	}

	Optional<Line> getCurrentLine() {
		if (actualOutput.isEmpty())
			return Optional.empty();
		return Optional.of(actualOutput.get(actualOutput.size() - 1));
	}

	public void resetOutput() {
		actualOutput.clear();
	}

	/**
	 * Returns the output as list of lines, always including all lines.
	 *
	 * @return all actual output as {@link Line}s.
	 * @deprecated Use {@link #getLines(OutputTestOptions...)} instead, which allows
	 *             to specify options.
	 */
	@Deprecated(since = "1.3.2")
	public List<Line> getOutput() {
		return Collections.unmodifiableList(actualOutput);
	}

	public List<Line> getLines(OutputTestOptions... outputOptions) {
		return Collections.unmodifiableList(processLines(outputOptions));
	}

	public String getOutputAsString(OutputTestOptions... outputOptions) {
		return processLines(outputOptions).stream().map(Line::text)
				.collect(Collectors.joining(IOTester.LINE_SEPERATOR));
	}

	public List<String> getLinesAsString(OutputTestOptions... outputOptions) {
		return processLines(outputOptions).stream().map(Line::text).collect(Collectors.toUnmodifiableList());
	}

	public AbstractStringAssert<?> assertThat(OutputTestOptions... outputOptions) {
		return Assertions.assertThat(getOutputAsString(outputOptions));
	}

	public ListAssert<String> assertThatLines(OutputTestOptions... outputOptions) {
		return Assertions.assertThat(getLinesAsString(outputOptions));
	}

	/**
	 * Checks the lines using
	 * {@link org.junit.jupiter.api.Assertions#assertLinesMatch(List, List, String)
	 * Assertions.assertLinesMatch}. The main difference is that we make the RegEx
	 * matching predictable by using <code>||</code> at the start and at the end of
	 * a line like
	 *
	 * <pre>
	 * ||some pattern here||
	 * ||\d+||
	 * </pre>
	 *
	 * for regular expressions and otherwise do a literal comparison.
	 * <p>
	 * Fast-forward lines are not being escaped and look like
	 *
	 * <pre>
	 * {@code >>>>}
	 * {@code >>} any number of lines because xyz should appear here {@code >>}
	 * {@code >>} 42 {@code >>}
	 * </pre>
	 *
	 * Both special formats can be escaped by a <code>\</code> at the beginning.
	 *
	 * @param message       the error message for the assertion failure.
	 * @param expectedLines the expected line patterns as described above. The
	 *                      strings are allowed to contain line breaks, which means
	 *                      a Java 15+ text block can be used as well.
	 * @author Christian Femers
	 */
	public void assertLinesMatch(String message, String... expectedLines) {
		assertLinesMatch(message, OutputTestOptions.NONE, expectedLines);
	}

	/**
	 * Checks the lines using
	 * {@link org.junit.jupiter.api.Assertions#assertLinesMatch(List, List, String)
	 * Assertions.assertLinesMatch}. The main difference is that we make the RegEx
	 * matching predictable by using <code>||</code> at the start and at the end of
	 * a line like
	 *
	 * <pre>
	 * ||some pattern here||
	 * ||\d+||
	 * </pre>
	 *
	 * for regular expressions and otherwise do a literal comparison.
	 * <p>
	 * Fast-forward lines are not being escaped and look like
	 *
	 * <pre>
	 * {@code >>>>}
	 * {@code >>} any number of lines because xyz should appear here {@code >>}
	 * {@code >>} 42 {@code >>}
	 * </pre>
	 *
	 * Both special formats can be escaped by a <code>\</code> at the beginning.
	 *
	 * @param message       the error message for the assertion failure.
	 * @param outputOptions the {@link OutputTestOptions} for this test.
	 * @param expectedLines the expected line patterns as described above. The
	 *                      strings are allowed to contain line breaks, which means
	 *                      a Java 15+ text block can be used as well.
	 * @author Christian Femers
	 */
	public void assertLinesMatch(String message, OutputTestOptions[] outputOptions, String... expectedLines) {
		var lines = Stream.of(expectedLines).flatMap(String::lines).collect(Collectors.toList());
		var lineCount = lines.size();
		var expectedLinePatterns = new ArrayList<String>();
		for (var i = 0; i < lineCount; i++) {
			String line = lines.get(i);
			// use StringBuilder as we have potentially many operations on the same string
			var newLine = new StringBuilder(line);
			// remember what the user wants this line to be
			boolean isRegEx = isRegExLine(line);
			boolean isFastForward = !isRegEx && line.startsWith(">>") && line.endsWith(">>"); //$NON-NLS-1$ //$NON-NLS-2$
			// remove escapes and our own special notation
			if (startsWithEscape(line)) {
				// remove char used for escape
				newLine.deleteCharAt(0);
			} else if (isRegEx) {
				// remove pattern notation
				newLine.delete(0, 2).delete(newLine.length() - 2, newLine.length());
			}
			// fast-forward needs to stay the same or JUnit LineMatcher won't recognize it
			if (!isFastForward) {
				// don't quote lines that were marked as regular expressions by the user
				if (!isRegEx)
					newLine.replace(0, newLine.length(), Pattern.quote(newLine.toString()));
				// insert random comment to avoid equality comparison working
				newLine.insert(0, String.format("(?x)#%08X%n", i)); //$NON-NLS-1$
				newLine.append('#').append(randomString);
			} else {
				// add a random number of spaces both to the front and back
				var spacesRandom = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);
				int front = spacesRandom % MAX_SPACES;
				int back = spacesRandom / MAX_SPACES;
				newLine.insert(2, SPACES, 0, front);
				newLine.insert(newLine.length() - 2, SPACES, 0, back);
			}
			expectedLinePatterns.add(newLine.toString());
		}
		var linesAsString = getLinesAsString(outputOptions);
		// use JUnit 5 API to to the line comparison
		try {
			org.junit.jupiter.api.Assertions.assertLinesMatch(expectedLinePatterns, linesAsString, message);
		} catch (AssertionFailedError afe) {
			throw tryCleanUpAssertionFailedError(lines, afe);
		}
	}

	private static AssertionFailedError tryCleanUpAssertionFailedError(List<String> lines, AssertionFailedError afe) {
		var failureMessage = afe.getMessage();
		var matcher = EXPECTED_LINE_PATTERN.matcher(failureMessage);
		if (matcher.find()) {
			var lineNumber = Integer.parseInt(matcher.group(1), 16);
			var expectedLine = lines.get(lineNumber);
			var cleanExpectedLine = new StringBuilder(expectedLine);
			if (startsWithEscape(expectedLine))
				cleanExpectedLine.append('`').setCharAt(0, '`');
			else if (isRegExLine(expectedLine))
				cleanExpectedLine.delete(0, 2).delete(cleanExpectedLine.length() - 2, cleanExpectedLine.length())
						.insert(0, " `").insert(0, localized("output_tester.line_matching_regex_mismatch")).append('`'); //$NON-NLS-1$ //$NON-NLS-2$
			else
				cleanExpectedLine.insert(0, '`').append('`');
			String newFailureMessage = matcher.replaceFirst(cleanExpectedLine.toString());
			return new AssertionFailedError(newFailureMessage, tryReplaceExpected(lines, afe.getExpected()),
					afe.getActual().getEphemeralValue());
		}
		return afe;
	}

	private static Object tryReplaceExpected(List<String> expectedLines, ValueWrapper expectedValueWrapper) {
		Object value = expectedValueWrapper.getEphemeralValue();
		if (!(value instanceof String))
			return value;
		return expectedLines.stream().map(line -> {
			if (startsWithEscape(line))
				return line.substring(1);
			if (isRegExLine(line))
				return line.substring(2, line.length() - 2);
			return line;
		}).collect(Collectors.joining(IOTester.LINE_SEPERATOR));
	}

	private static boolean startsWithEscape(String line) {
		return line.startsWith("\\||") || line.startsWith("\\>>"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static boolean isRegExLine(String expectedLine) {
		return expectedLine.startsWith("||") && expectedLine.endsWith("||"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private List<Line> processLines(OutputTestOptions... outputOptions) {
		boolean ignoreLastEmpty = !OutputTestOptions.DONT_IGNORE_LAST_EMPTY_LINE.isIn(outputOptions);
		if (ignoreLastEmpty && !actualOutput.isEmpty() && actualOutput.get(actualOutput.size() - 1).text().isEmpty())
			return actualOutput.subList(0, actualOutput.size() - 1);
		return actualOutput;
	}
}
