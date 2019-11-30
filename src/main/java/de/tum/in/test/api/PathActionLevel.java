package de.tum.in.test.api;

public enum PathActionLevel {
	READ,
	READLINK,
	WRITE,
	DELETE,
	EXECUTE;

	public boolean isAboveOrEqual(PathActionLevel other) {
		return ordinal() >= other.ordinal();
	}

	public boolean isBelowOrEqual(PathActionLevel other) {
		return ordinal() <= other.ordinal();
	}


	public static PathActionLevel getLevelOf(String actions) {
		if(actions.contains("execute"))
			return EXECUTE;
		if(actions.contains("delete"))
			return DELETE;
		if(actions.contains("write"))
			return WRITE;
		if(actions.contains("readlink"))
			return READLINK;
		return READ;
	}
}
