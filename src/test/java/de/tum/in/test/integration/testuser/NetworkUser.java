package de.tum.in.test.integration.testuser;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.MirrorOutput.MirrorOutputPolicy;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import de.tum.in.test.api.security.ArtemisSecurityManager;
import de.tum.in.test.integration.testuser.subject.NetworkPenguin;

@Public
@UseLocale("en")
@AllowThreads(maxActiveCount = 100)
@MirrorOutput(MirrorOutputPolicy.DISABLED)
@StrictTimeout(5)
@TestMethodOrder(MethodName.class)
@WhitelistPath(value = "target/**", type = PathType.GLOB)
@BlacklistPath(value = "**Test*.{java,class}", type = PathType.GLOB)
public class NetworkUser {

	private static final Logger LOG = LoggerFactory.getLogger(NetworkUser.class);

	private static final int PORT = 25565;
	private static final String MESSAGE = "hello";

	private static Thread serverThread;

	@BeforeAll
	static void startServer() {
		LOG.info("Starting server...");
		serverThread = new Thread(TestUtils.getRootThreadGroup(), () -> {
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

	@ParameterizedTest
	@AllowLocalPort(PORT)
	@ValueSource(strings = { "localhost", "127.0.0.1", "::1" })
	void connectLocallyAllowed(String host) throws Exception {
		NetworkPenguin.tryConnect(host, PORT, MESSAGE);
	}

	@ParameterizedTest
	@AllowLocalPort(PORT)
	@ValueSource(ints = { 22, 80 })
	void connectLocallyNotAllowed(int port) throws Exception {
		NetworkPenguin.tryConnect("localhost", port, MESSAGE);
	}

	@Test
	@AllowLocalPort(80)
	void connectRemoteNotAllowed() throws Exception {
		NetworkPenguin.tryConnect("example.com", 80, null);
	}

	@Test
	@AllowLocalPort(allowPortsAbove = AllowLocalPort.IANA_REGISTERED_LOWER_BORDER)
	void serverAllowedAndAccept() throws Throwable {
		var error = new AtomicReference<Throwable>();
		var serverThread = new Thread(TestUtils.getRootThreadGroup(), () -> {
			try {
				NetworkPenguin.tryStartServer(45458, "something");
			} catch (Exception e) {
				fail(e);
			}
		}, "server-45458");
		serverThread.setUncaughtExceptionHandler((thread, t) -> error.set(t));
		ArtemisSecurityManager.requestThreadWhitelisting(serverThread);
		serverThread.start();
		try (Socket s = new Socket("localhost", 45458); PrintStream out = new PrintStream(s.getOutputStream())) {
			s.setSoTimeout(2500);
			out.println("something");
		}
		serverThread.join(2500);
		if (error.get() != null) {
			throw error.get();
		}
	}

	@Test
	@AllowLocalPort(8083)
	void serverAllowedAndTimeout() throws Exception {
		NetworkPenguin.tryStartServer(8083, "none");
	}

	@Test
	void serverNotAllowed() throws Exception {
		NetworkPenguin.tryStartServer(80, "none");
	}
}
