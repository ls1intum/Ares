package de.tum.in.test.api.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

final class TestInStream extends InputStream {

	private final LineProvider lineProvider;

	private ByteArrayInputStream input;

	TestInStream(LineProvider lineProvider) {
		this.lineProvider = Objects.requireNonNull(lineProvider);
	}

	@Override
	public int read() throws IOException {
		if (input == null)
			tryLoadNextLine();
		int res = input.read();
		if (input.available() == 0)
			input = null;
		return res;
	}

	@Override
	public int available() throws IOException {
		return input == null ? 0 : input.available();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (input == null)
			tryLoadNextLine();
		int res = input.read(b, off, len);
		if (input.available() == 0)
			input = null;
		return res;
	}

	void resetInternalState() {
		input = null;
	}

	private void tryLoadNextLine() {
		Line currentLine = lineProvider.getNextLine();
		byte[] bytes = currentLine.text().concat(IOTester.LINE_SEPERATOR).getBytes(StandardCharsets.UTF_8);
		input = new ByteArrayInputStream(bytes);
	}
}
