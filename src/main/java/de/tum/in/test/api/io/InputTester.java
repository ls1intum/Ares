package de.tum.in.test.api.io;

import static de.tum.in.test.api.localization.Messages.localized;

import java.util.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Can provide console input, and throws an exception if more is requested than
 * is available. The provided lines get numbered.
 *
 * @see IOTester
 * @author Christian Femers
 * @since 0.1.0
 * @version 1.0.2
 */
@API(status = Status.STABLE)
public final class InputTester implements LineProvider {

	private final List<Line> expectedInput = new ArrayList<>();
	private int position;

	@Override
	public Line getNextLine() {
		if (hasNextLine())
			return expectedInput.get(position++);
		if (expectedInput.isEmpty())
			throw new IllegalStateException(localized("input_tester.no_input_expected")); //$NON-NLS-1$
		throw new IllegalStateException(localized("input_tester.no_more_input_expected", getCurrentLine())); //$NON-NLS-1$
	}

	Line getCurrentLine() {
		return expectedInput.get(position - 1);
	}

	private void addExpectedLine(AbstractLine line) {
		expectedInput.add(line);
		line.setLineNumber(expectedInput.size());
	}

	@Override
	public boolean hasNextLine() {
		return position < expectedInput.size();
	}

	public void addLinesToInput(String... lines) {
		Arrays.stream(lines).map(Line::of).forEach(this::addExpectedLine);
	}

	public void resetInput() {
		expectedInput.clear();
		position = 0;
	}
}
