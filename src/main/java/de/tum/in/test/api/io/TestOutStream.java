package de.tum.in.test.api.io;

import static de.tum.in.test.api.localization.Messages.formatLocalized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

final class TestOutStream extends OutputStream {

	private static final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
	static {
		decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		decoder.onMalformedInput(CodingErrorAction.REPORT);
	}

	private final LineAcceptor outputAcceptor;
	private Optional<OutputStream> mirror;

	private final ByteArrayOutputStream currentInput;

	TestOutStream(LineAcceptor outputAcceptor, OutputStream mirror) {
		this.mirror = Optional.ofNullable(mirror);
		this.outputAcceptor = outputAcceptor;
		this.currentInput = new ByteArrayOutputStream();
	}

	@Override
	public void write(int b) throws IOException {
		currentInput.write(b);
		if (mirror.isPresent())
			mirror.get().write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		currentInput.write(b);
		if (mirror.isPresent())
			mirror.get().write(b);
	}

	@Override
	public void write(byte[] b, int offset, int length) throws IOException {
		currentInput.write(b, offset, length);
		if (mirror.isPresent())
			mirror.get().write(b, offset, length);
	}

	@Override
	public void flush() throws IOException {
		super.flush();
		ByteBuffer bytes = ByteBuffer.wrap(currentInput.toByteArray());
		CharBuffer result;
		try {
			result = decoder.decode(bytes);
		} catch (CharacterCodingException e) {
			String problemString = new String(bytes.array(), decoder.charset());
			throw new IllegalArgumentException(formatLocalized("output_tester.output_is_invalid_utf8", problemString), //$NON-NLS-1$
					e);
		}
		outputAcceptor.acceptOutput(result);
		currentInput.reset();
	}

}