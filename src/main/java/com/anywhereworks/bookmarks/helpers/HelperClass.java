package com.anywhereworks.bookmarks.helpers;

public class HelperClass {

	public static boolean validateAttribute(String attribute) {
		if (attribute == null || attribute.isEmpty())
			return false;
		return true;
	}
}
