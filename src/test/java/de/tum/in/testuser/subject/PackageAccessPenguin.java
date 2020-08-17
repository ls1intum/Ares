package de.tum.in.testuser.subject;

import java.util.ArrayList;

public class PackageAccessPenguin {
	
	private PackageAccessPenguin() {

	}
	
	public static void useArrayList() {
		ArrayList<String> x = new ArrayList<>();
		x.add("abc");
	}
}