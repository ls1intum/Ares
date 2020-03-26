package de.tum.in.test.api.io;

import java.util.Objects;

final class StaticLine extends AbstractLine {
	private final String text;

	StaticLine(String text) {
		this.text = Objects.requireNonNull(text);
		if (AbstractLine.containsLineBreaks(text))
			throw new IllegalArgumentException("Line must not contain any new lines"); //$NON-NLS-1$
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public boolean isComplete() {
		return true;
	}
}
