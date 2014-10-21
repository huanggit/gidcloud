package com.cpsdna.gidCloud.web.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	public static void set(HttpServletResponse response, String key,
			String value, int sec) {
		Cookie co = new Cookie(key, value);
		co.setMaxAge(sec);
		response.addCookie(co);
	}

	public static String get(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}

	public static Cookie getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName()))
				return cookie;
		}
		return null;
	}

	public static boolean delete(HttpServletResponse response, String cookieName) {
		if (cookieName != null) {
			Cookie cookie = new Cookie(cookieName, null);
			if (cookie != null) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				return true;
			}
		}
		return false;
	}
}
