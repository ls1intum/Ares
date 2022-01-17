package de.tum.in.test.api;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.STABLE)
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
		if (actions.contains("execute")) //$NON-NLS-1$
			return EXECUTE;
		if (actions.contains("delete")) //$NON-NLS-1$
			return DELETE;
		if (actions.contains("write")) //$NON-NLS-1$
			return WRITE;
		if (actions.contains("readlink")) //$NON-NLS-1$
			return READLINK;
		return READ;
	}
}
