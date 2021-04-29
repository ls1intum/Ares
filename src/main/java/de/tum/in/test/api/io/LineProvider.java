package de.tum.in.test.api.io;

interface LineProvider {
	Line getNextLine();

	boolean hasNextLine();
}
