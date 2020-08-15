package de.tum.in.testuser.subject;

public class CustomException extends RuntimeException {
	private static final long serialVersionUID = -7580807176725417304L;

	public CustomException() {
		super("ABC", new ArrayIndexOutOfBoundsException());
	}
}
