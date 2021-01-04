package de.tum.in.test.api.dynamic;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Modifier;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.EXPERIMENTAL)
public enum Check {

	STATIC {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (!Modifier.isStatic(modifiers))
				fail(desc + " ist nicht statisch.");
		}
	},
	NOT_STATIC {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (Modifier.isStatic(modifiers))
				fail(desc + " ist statisch.");
		}
	},
	FINAL {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (!Modifier.isFinal(modifiers))
				fail(desc + " ist nicht final.");
		}
	},
	NOT_FINAL {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (Modifier.isFinal(modifiers))
				fail(desc + " ist final.");
		}
	},
	PUBLIC {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (!Modifier.isPublic(modifiers))
				fail(desc + " ist nicht public.");
		}
	},
	NOT_PUBLIC {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (Modifier.isPublic(modifiers))
				fail(desc + " ist public.");
		}
	},
	PACKAGE_PRIVATE {
		@Override
		void checkModifiers(int modifiers, String desc) {
			if (Modifier.isPublic(modifiers) || Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers))
				fail(desc + " ist nicht package-private.");
		}
	};

	abstract void checkModifiers(int modifiers, String desc);
}