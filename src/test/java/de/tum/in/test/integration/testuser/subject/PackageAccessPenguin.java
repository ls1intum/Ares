package de.tum.in.test.integration.testuser.subject;

import java.util.regex.Pattern;

public final class PackageAccessPenguin {

	private PackageAccessPenguin() {
	}

	public static void useArrayList() {
		Pattern pattern = Pattern.compile("a+");
		pattern.asMatchPredicate().test("aaa");
	}
}
