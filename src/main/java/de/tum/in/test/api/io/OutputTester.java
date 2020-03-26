package de.tum.in.test.api.io;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Captures console output as {@link Line}s, and therefore is OS line separator
 * independent. The lines get numbered.
 *
 * @see IOTester
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.0
 */
public final class OutputTester implements LineAcceptor {

	private final List<Line> actualOutput = new ArrayList<>();

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
		int lastPos = 0;
		boolean lastWasCarriageReturn = false;
		for (int i = 0; i < output.length(); i++) {
			char c = output.charAt(i);
			if (c == '\r' || c == '\n') {
				if (c == '\n' && lastWasCarriageReturn) {
					lastPos++;
				} else {
					currentLine.append(output.subSequence(lastPos, i));
					currentLine.complete();
					currentLine = new DynamicLine();
					addNewLine(currentLine);
					lastPos = i + 1;
				}
				lastWasCarriageReturn = c == '\r';
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

	public List<Line> getOutput() {
		return Collections.unmodifiableList(actualOutput);
	}
}
