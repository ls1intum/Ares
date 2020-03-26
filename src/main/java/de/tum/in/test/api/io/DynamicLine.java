package de.tum.in.test.api.io;

import java.io.PrintWriter;

/**
 * Note: not thread-safe, but this in not needed because {@link PrintWriter} is.
 */
final class DynamicLine extends AbstractLine {
	private StringBuilder textUnderConstruction;
	private String text;

	DynamicLine(CharSequence text) {
		textUnderConstruction = new StringBuilder(text);
		if (AbstractLine.containsLineBreaks(text))
			throw new IllegalArgumentException("Line must not contain any new lines"); //$NON-NLS-1$
	}

	DynamicLine() {
		textUnderConstruction = new StringBuilder();
	}

	public void complete() {
		if (isComplete())
			throw new IllegalStateException("Line already complete"); //$NON-NLS-1$
		text = textUnderConstruction.toString();
		textUnderConstruction = null;
	}

	public void append(CharSequence s) {
		if (isComplete())
			throw new IllegalStateException("Line already completed"); //$NON-NLS-1$
		if (AbstractLine.containsLineBreaks(s))
			throw new IllegalArgumentException("Line must not contain any new lines"); //$NON-NLS-1$
		textUnderConstruction.append(s);
	}

	@Override
	public String text() {
		return text != null ? text : textUnderConstruction.toString();
	}

	@Override
	public boolean isComplete() {
		return text != null;
	}
}
