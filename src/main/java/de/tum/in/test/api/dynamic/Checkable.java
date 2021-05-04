package de.tum.in.test.api.dynamic;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.EXPERIMENTAL)
public interface Checkable {

	boolean exists();

	void check(Check... checks);
}
