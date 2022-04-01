package de.tum.in.test.api.dynamic;

import static de.tum.in.test.api.localization.Messages.localizedFailure;

import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.MAINTAINED)
public enum Check {

	STATIC {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (!Modifier.isStatic(modifiers))
				throw localizedFailure("dynamics.check.not_static", desc.get()); //$NON-NLS-1$
		}
	},
	NOT_STATIC {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (Modifier.isStatic(modifiers))
				throw localizedFailure("dynamics.check.static", desc.get()); //$NON-NLS-1$
		}
	},
	FINAL {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (!Modifier.isFinal(modifiers))
				throw localizedFailure("dynamics.check.not_final", desc.get()); //$NON-NLS-1$
		}
	},
	NOT_FINAL {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (Modifier.isFinal(modifiers))
				throw localizedFailure("dynamics.check.final", desc.get()); //$NON-NLS-1$
		}
	},
	PUBLIC {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (!Modifier.isPublic(modifiers))
				throw localizedFailure("dynamics.check.not_public", desc.get()); //$NON-NLS-1$
		}
	},
	NOT_PUBLIC {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (Modifier.isPublic(modifiers))
				throw localizedFailure("dynamics.check.public", desc.get()); //$NON-NLS-1$
		}
	},
	PACKAGE_PRIVATE {
		@Override
		void checkModifiers(int modifiers, Supplier<String> desc) {
			if (Modifier.isPublic(modifiers) || Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers))
				throw localizedFailure("dynamics.check.not_package", desc.get()); //$NON-NLS-1$
		}
	};

	abstract void checkModifiers(int modifiers, Supplier<String> descSupplier);
}
