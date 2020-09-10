package de.tum.in.testuser;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.testuser.subject.NetworkPenguin;

@UseLocale("en")
@AllowThreads(maxActiveCount = 100)
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(5)
@TestMethodOrder(Alphanumeric.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
public class NetworkUser {

	private static final Logger LOG = LoggerFactory.getLogger(NetworkUser.class);

	private static final int PORT = 25565;
	private static final String MESSAGE = "hello";

	private static Thread serverThread;

	@Public
	@BeforeAll
	static void startServer() {
		LOG.info("Starting server...");
		serverThread = new Thread(() -> {
			try (ServerSocket socket = new ServerSocket(PORT)) {
				socket.setSoTimeout(50);
				while (!Thread.interrupted()) {
					LOG.info("Waiting for connection on port {}", PORT);
					try (Socket s = socket.accept(); PrintStream out = new PrintStream(s.getOutputStream())) {
						out.println(MESSAGE);
					} catch (@SuppressWarnings("unused") SocketTimeoutException e) {
						// try again
					}
				}
			} catch (@SuppressWarnings("unused") IOException e) {
				// do nothing
			}
		}, "server");
		ArtemisSecurityManager.requestThreadWhitelisting(serverThread);
		serverThread.start();
	}

	@AfterAll
	static void stopServer() throws InterruptedException {
		LOG.info("Stopping server...");
		serverThread.interrupt();
		serverThread.join();
	}

	@Public
	@ParameterizedTest
	@AllowLocalPort(PORT)
	@ValueSource(strings = { "localhost", "127.0.0.1", "::1" })
	public void connectLocallyAllowed(String host) throws Exception {
		NetworkPenguin.tryConnect(host, PORT, MESSAGE);
	}

	@Public
	@ParameterizedTest
	@AllowLocalPort(PORT)
	@ValueSource(ints = { 22, 80 })
	public void connectLocallyNotAllowed(int port) throws Exception {
		NetworkPenguin.tryConnect("localhost", port, MESSAGE);
	}

	@Public
	@Test
	@AllowLocalPort(80)
	public void connectRemoteNotAllowed() throws Exception {
		NetworkPenguin.tryConnect("example.com", 80, null);
	}
}