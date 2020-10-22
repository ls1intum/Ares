package de.tum.in.test.api.localization;

import java.util.Collections;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.platform.commons.util.LruCache;

@API(status = Status.INTERNAL)
public final class Messages {

	private static final String BUNDLE_NAME = "de.tum.in.test.api.localization.messages"; //$NON-NLS-1$

	/**
	 * Must match the localizations in the resources.
	 */
	private static final Set<Locale> SUPPORTED_LOCALES = Set.of(Locale.ROOT, Locale.GERMAN);

	private static Map<Locale, ResourceBundle> resourceBundleCache = Collections.synchronizedMap(new LruCache<>(100));

	static {
		for (Locale locale : SUPPORTED_LOCALES) {
			resourceBundleCache.put(locale, loadBundleForLocale(locale));
		}
	}

	private Messages() {

	}

	public static String localized(String key) {
		try {
			return getBundleForCurrentLocale().getString(key);
		} catch (@SuppressWarnings("unused") MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String formatLocalized(String key, Object... args) {
		try {
			return String.format(getBundleForCurrentLocale().getString(key), args);
		} catch (@SuppressWarnings("unused") MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	private static ResourceBundle getBundleForCurrentLocale() {
		return resourceBundleCache.computeIfAbsent(Locale.getDefault(Category.DISPLAY), Messages::loadBundleForLocale);
	}

	private static ResourceBundle loadBundleForLocale(Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	public static void init() {
		// just for initialization
	}
}
