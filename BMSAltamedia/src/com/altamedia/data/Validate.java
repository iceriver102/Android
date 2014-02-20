package com.altamedia.data;

public class Validate {

	public static boolean isEmail(String s) {
		s = s.toLowerCase().trim();
		// neu chuoi rong
		if (isEmpty(s))
			return false;
		// neu email la chuoi co khoang trang
		if (s.indexOf(" ") > 0)
			return false;
		// neu email la chuoi khong co dau @
		if (s.indexOf("@") == -1)
			return false;
		int i = 1;
		int sLength = s.length();
		// neu email la chuoi khong co dau .
		if (s.indexOf("..") != -1)
			return false;
		// neu email la chuoi co 2 dau @
		if (s.indexOf("@") != s.lastIndexOf("@"))
			return false;
		// neu email la chuoi co dau . cuoi cung
		if (s.lastIndexOf(".") == s.length() - 1)
			return false;
		// neu email la chuoi co ky tu khong thuoc cac ky tu sau
		String str = "abcdefghijklmnopqrstuvwxyz-@._0123456789";
		for (int j = 0; j < sLength; j++) {
			if (str.indexOf(s.charAt(j)) == -1)
				return false;
		}
		// Neu email chuoi du lieu hop le thi tra ve true.
		return true;
	}

	public static boolean isEmpty(String s) {
		return ((s.trim() == null) || (s.trim().length() == 0));
	}

	public static boolean isWhitespace(String s) {
		s=s.trim();
		String whitespace = "\t\n\r";
		int i;
		if (isEmpty(s))
			return true;
		for (i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (whitespace.indexOf(c) == -1)
				return false;
		}
		return true;
	}

	public static boolean isPhone(String s) {
		s=s.trim();
		int sLength = s.length();
		if (isEmpty(s))
			return false;
		// neu email la chuoi co khoang trang
		if (isWhitespace(s))
			return false;
		String str = "+0123456789";
		for (int j = 0; j < sLength; j++) {
			if (str.indexOf(s.charAt(j)) == -1)
				return false;
		}
		if (s.charAt(0) != '+')
			if (sLength < 10 || sLength > 11)
				return false;
			else if (sLength < 10)
				return false;
		return true;
	}

}
