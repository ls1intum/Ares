package de.tum.in.test.integration.testuser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.*;

@Public
@UseLocale("de")
public class LocaleUser {

	private static final String ACTIVE_LOCALIZATION = "active_localization";

	@Public
	@UseLocale("en")
	public static class LocaleEn {
		@Test
		void testLocaleEn() {
			assertThat(Locale.getDefault()).isEqualTo(Locale.ENGLISH);
			assertThat(Messages.localized(ACTIVE_LOCALIZATION)).isEqualTo("en_US");
		}
	}

	@Public
	@UseLocale("fr")
	public static class LocaleUnsupported {
		@Test
		void testLocaleUnsupported() {
			assertThat(Locale.getDefault()).isEqualTo(Locale.FRENCH);
			assertThat(Messages.localized(ACTIVE_LOCALIZATION)).isEqualTo("en_US");
		}
	}

	@Test
	void testLocaleDe() {
		assertThat(Locale.getDefault()).isEqualTo(Locale.GERMAN);
		assertThat(Messages.localized(ACTIVE_LOCALIZATION)).isEqualTo("de_DE");
	}

	@Test
	void testUnknownFormatted() {
		assertThat(Messages.localized("_unknown_%s_", "x")).isEqualTo("!_unknown_%s_!");
	}

	@Test
	void testUnknownNormal() {
		assertThat(Messages.localized("_unknown_")).isEqualTo("!_unknown_!");
	}
}
