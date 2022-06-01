package de.tum.in.test.api.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Scanner;

import org.junit.jupiter.api.*;

@SuppressWarnings("static-method")
class IOTesterTest {

	private IOTester tester;

	@BeforeEach
	void testInstall() {
		tester = IOTester.installNew(false, 1_000_000);
	}

	@AfterEach
	void testUninstall() {
		IOTester.uninstallCurrent();
	}

	@Test
	void testReset() {
		try (Scanner scanner = new Scanner(System.in)) {
			tester.in().addLinesToInput("X", "Y");

			System.out.println("A");
			System.err.println("B");

			assertThat(tester.out().getLines()).isNotEmpty();
			assertThat(tester.err().getLines()).isNotEmpty();
			assertThat(scanner.nextLine()).isEqualTo("X");

			tester.in().addLinesToInput("Z");

			tester.reset();

			assertThat(tester.out().getLines()).isEmpty();
			assertThat(tester.err().getLines()).isEmpty();
			assertThrows(IllegalStateException.class, scanner::nextLine);
		}
	}

	@Test
	void testIn() {
		assertThat(tester.in()).isNotNull().isSameAs(tester.getInTester());
	}

	@Test
	void testOut() {
		assertThat(tester.out()).isNotNull().isSameAs(tester.getOutTester());
	}

	@Test
	void testErr() {
		assertThat(tester.err()).isNotNull().isSameAs(tester.getErrTester());
	}
}
