package de.tum.in.test.api.io;

import static de.tum.in.test.api.localization.Messages.localized;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;

final class TestOutStream extends OutputStream {

	private static final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
	static {
		decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		decoder.onMalformedInput(CodingErrorAction.REPORT);
	}

	private final LineAcceptor outputAcceptor;
	private final OutputStream mirror;
	private final long maxChars;
	private long charCount;
	private volatile boolean closed;

	private final ByteArrayOutputStream currentInput;

	TestOutStream(LineAcceptor outputAcceptor, OutputStream mirror, long maxChars) {
		this.mirror = mirror;
		this.outputAcceptor = outputAcceptor;
		this.currentInput = new ByteArrayOutputStream();
		this.maxChars = maxChars;
	}

	@Override
	public void write(int b) throws IOException {
		checkCharCount(1);
		currentInput.write(b);
		if (mirror != null)
			mirror.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		checkCharCount(b.length);
		currentInput.write(b);
		if (mirror != null)
			mirror.write(b);
	}

	@Override
	public void write(byte[] b, int offset, int length) throws IOException {
		checkCharCount(length);
		currentInput.write(b, offset, length);
		if (mirror != null)
			mirror.write(b, offset, length);
	}

	@Override
	public void flush() throws IOException {
		super.flush();
		var bytes = ByteBuffer.wrap(currentInput.toByteArray());
		CharBuffer result;
		try {
			result = decoder.decode(bytes);
		} catch (CharacterCodingException e) {
			var problemString = new String(bytes.array(), decoder.charset());
			throw new IllegalArgumentException(localized("output_tester.output_is_invalid_utf8", problemString), //$NON-NLS-1$
					e);
		}
		outputAcceptor.acceptOutput(result);
		currentInput.reset();
	}

	@Override
	public void close() throws IOException {
		closed = true;
		if (mirror != null)
			mirror.close();
	}

	void resetInternalState() {
		charCount = 0;
		currentInput.reset();
	}

	private void checkCharCount(int newChars) throws IOException {
		if (closed)
			throw new IOException(localized("output_tester.output_closed")); //$NON-NLS-1$
		charCount += newChars;
		if (charCount > maxChars)
			throw new SecurityException(localized("output_tester.output_maxExceeded", charCount)); //$NON-NLS-1$
	}
}
