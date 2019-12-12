package de.tum.in.test.api.io;

import java.util.Objects;

import de.tum.in.test.api.localization.Messages;

public abstract class AbstractLine implements Line {

	int lineNumber = -1;

	@Override
	public String toString() {
		return lineNumber == -1 ? Messages.formatLocalized("abstract_line.plain_line", text()) //$NON-NLS-1$
				: Messages.formatLocalized("abstract_line.numbered_line", lineNumber, text()); //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Line))
			return false;
		Line other = (Line) obj;
		return Objects.equals(text(), other.text());
	}

	@Override
	public int hashCode() {
		return text().hashCode();
	}

	@Override
	public final int lineNumber() {
		return lineNumber;
	}

	public final void setLineNumber(int lineNumber) {
		if (lineNumber <= 0)
			throw new IllegalArgumentException("Line number cannot be set to " + lineNumber); //$NON-NLS-1$
		if (this.lineNumber != -1)
			throw new IllegalStateException("Line number has already been set"); //$NON-NLS-1$
		this.lineNumber = lineNumber;
	}
}