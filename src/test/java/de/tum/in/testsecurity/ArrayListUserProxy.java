package de.tum.in.testsecurity;

import java.util.ArrayList;

public class ArrayListUserProxy {
	public static void useArrayList() {
		ArrayList<String> x = new ArrayList<>();
		x.add("abc");
	}
}