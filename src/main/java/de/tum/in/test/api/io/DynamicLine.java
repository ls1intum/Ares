package de.tum.in.test.api.io;

import java.io.PrintWriter;

/**
 * Note: not thread-safe, but this in not needed because {@link PrintWriter} is.
 */
final class DynamicLine extends AbstractLine {
	private StringBuilder textUnderConstruction;
	private String text;

	DynamicLine(CharSequence text) {
		this.textUnderConstruction = new StringBuilder(text);
		if (Line.containsLineBreaks(text))
			throw new IllegalArgumentException("Line must not contain any new lines"); //$NON-NLS-1$
	}

	DynamicLine() {
		this.textUnderConstruction = new StringBuilder();
	}

	public final void complete() {
		if (isComplete())
			throw new IllegalStateException("Line already complete"); //$NON-NLS-1$
		text = textUnderConstruction.toString();
		textUnderConstruction = null;
	}

	public final void append(CharSequence s) {
		if (isComplete())
			throw new IllegalStateException("Line already completed"); //$NON-NLS-1$
		if (Line.containsLineBreaks(s))
			throw new IllegalArgumentException("Line must not contain any new lines"); //$NON-NLS-1$
		textUnderConstruction.append(s);
	}

	@Override
	public final String text() {
		return text != null ? text : textUnderConstruction.toString();
	}

	@Override
	public boolean isComplete() {
		return text != null;
	}
}