package com.cpsdna.gidCloud.web.service;

public class StrUtil {
	public static boolean checkPassword(String password) {
		if (password == null)
			return false;
		if (password.length() < 6)
			return false;
		return true;
	}

	public static boolean checkEmail(String email) {
		if (email == null)
			return false;
		String format = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$";
		return email.matches(format);
	}

	public static boolean notBlank(String str) {
		return (str != null && str.trim().length() > 0);
	}
}
