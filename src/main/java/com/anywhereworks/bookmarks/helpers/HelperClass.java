package com.anywhereworks.bookmarks.helpers;

public class HelperClass {

	public static boolean validateAttribute(String attribute) {
		if (attribute == null || attribute.isBlank())
			return false;
		return true;
	}
}
