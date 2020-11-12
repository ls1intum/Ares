package de.tum.in.test.api.jqwik;

import java.lang.annotation.AnnotationFormatError;
import java.util.Locale;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.platform.commons.support.AnnotationSupport;

import net.jqwik.api.lifecycle.AroundContainerHook;
import net.jqwik.api.lifecycle.ContainerLifecycleContext;

import de.tum.in.test.api.localization.UseLocale;

@API(status = Status.INTERNAL)
public class JqwikLocaleExtension implements AroundContainerHook {

	private Locale oldLocale;

	@Override
	public int proximity() {
		return -20;
	}

	@Override
	public void beforeContainer(ContainerLifecycleContext context) {
		var annot = AnnotationSupport.findAnnotation(context.optionalElement(), UseLocale.class);
		if (annot.isEmpty())
			return;
		if (oldLocale != null)
			throw new AnnotationFormatError("Locale extension already active,");
		oldLocale = Locale.getDefault();
		var newLocale = new Locale(annot.get().value());
		Locale.setDefault(newLocale);
	}

	@Override
	public void afterContainer(ContainerLifecycleContext context) {
		if (oldLocale != null) {
			Locale.setDefault(oldLocale);
		}
	}
}
