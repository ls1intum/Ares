package de.tum.in.testuser.subject;

import java.util.regex.Pattern;

public class PackageAccessPenguin {

	private PackageAccessPenguin() {

	}

	public static void useArrayList() {
		Pattern pattern = Pattern.compile("a+");
		pattern.asMatchPredicate().test("aaa");
	}
}