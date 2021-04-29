package de.tum.in.test.api.io;

import java.nio.CharBuffer;

interface LineAcceptor {
	void acceptOutput(CharBuffer output);
}
