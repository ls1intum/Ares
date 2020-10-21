package de.tum.in.test.api;

import static de.tum.in.test.testutilities.CustomConditions.*;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.*;

import java.net.SocketTimeoutException;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.testutilities.TestTest;
import de.tum.in.test.testutilities.UserBased;
import de.tum.in.test.testutilities.UserTestResults;
import de.tum.in.testuser.NetworkUser;

@UserBased(NetworkUser.class)
class NetworkTest {

	@UserTestResults
	private static Events tests;

	private final String connectRemoteNotAllowed = "connectRemoteNotAllowed";
	private final String connectLocallyNotAllowed = "connectLocallyNotAllowed(int)";
	private final String connectLocallyAllowed = "connectLocallyAllowed(java.lang.String)";
	private final String serverAllowedAndAccept = "serverAllowedAndAccept";
	private final String serverAllowedAndTimeout = "serverAllowedAndTimeout";
	private final String serverNotAllowed = "serverNotAllowed";

	@TestTest
	void test_connectRemoteNotAllowed() {
		tests.assertThatEvents().haveExactly(1, event(test(connectRemoteNotAllowed),
				finishedWithFailure(instanceOf(SecurityException.class), message(m -> m.contains("example.com")))));
	}

	@TestTest
	void test_connectLocallyNotAllowed() {
		tests.assertThatEvents().haveExactly(2, testFailedWith(connectLocallyNotAllowed, SecurityException.class));
	}

	@TestTest
	void test_connectLocallyAllowed() {
		tests.assertThatEvents().haveExactly(3, event(test(connectLocallyAllowed), finishedSuccessfullyRep()));
	}

	@Disabled("Does currently not work on the CI system for some reason")
	@TestTest
	void test_serverAllowedAndAccept() {
		tests.assertThatEvents().haveExactly(1, event(test(serverAllowedAndAccept), finishedSuccessfullyRep()));
	}

	@TestTest
	void test_serverAllowedAndTimeout() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(serverAllowedAndTimeout, SocketTimeoutException.class));
	}

	@TestTest
	void test_serverNotAllowed() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(serverNotAllowed, SecurityException.class));
	}
}
