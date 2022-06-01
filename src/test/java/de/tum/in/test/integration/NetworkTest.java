package de.tum.in.test.integration;

import static de.tum.in.test.testutilities.CustomConditions.*;

import java.net.SocketTimeoutException;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.testkit.engine.Events;

import de.tum.in.test.integration.testuser.NetworkUser;
import de.tum.in.test.testutilities.*;
import de.tum.in.test.testutilities.CustomConditions.Option;

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
		tests.assertThatEvents().haveExactly(1, testFailedWith(connectRemoteNotAllowed, SecurityException.class,
				"network use not allowed (example.com:-1)", Option.MESSAGE_CONTAINS));
	}

	@TestTest
	void test_connectLocallyNotAllowed() {
		tests.assertThatEvents().haveExactly(2, testFailedWith(connectLocallyNotAllowed, SecurityException.class));
	}

	@TestTest
	void test_connectLocallyAllowed() {
		tests.assertThatEvents().haveExactly(3, finishedSuccessfully(connectLocallyAllowed));
	}

	@Disabled("Does currently not work on the CI system for some reason")
	@TestTest
	void test_serverAllowedAndAccept() {
		tests.assertThatEvents().haveExactly(1, finishedSuccessfully(serverAllowedAndAccept));
	}

	@TestTest
	void test_serverAllowedAndTimeout() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(serverAllowedAndTimeout, SocketTimeoutException.class));
	}

	@TestTest
	void test_serverNotAllowed() {
		tests.assertThatEvents().haveExactly(1, testFailedWith(serverNotAllowed, SecurityException.class,
				"network use not allowed (listen port 80)", Option.MESSAGE_CONTAINS));
	}
}
