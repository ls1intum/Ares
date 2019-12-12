package de.tum.in.test.api.localization;

import java.util.Locale;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class LocaleExtension implements BeforeAllCallback, AfterAllCallback {

	private Locale oldLocale;

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		var annot = AnnotationSupport.findAnnotation(context.getElement(), UseLocale.class);
		if (annot.isEmpty())
			return;
		oldLocale = Locale.getDefault();
		var newLocale = new Locale(annot.get().value());
		Locale.setDefault(newLocale);
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		if (oldLocale != null)
			Locale.setDefault(oldLocale);
	}
}
