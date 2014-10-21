package com.cpsdna.gidCloud.web.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class EncryptUtil {

	static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f'};

	private static String byteToHexString(byte[] tmp, char hexDigit[]) {
		String s;
		char str[] = new char[16 * 2];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			str[k++] = hexDigit[byte0 >>> 4 & 0xf];
			str[k++] = hexDigit[byte0 & 0xf];
		}
		s = new String(str);
		return s;
	}

	public static byte[] md5Byte(String source) {
		if (source == null) {
			return new byte[32];
		}
		MessageDigest md = null;
		byte[] b = null;
		try {
			md = MessageDigest.getInstance("MD5");
			try {
				b = md.digest(source.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return b;

	}

	public static String md5(String source) {
		return byteToHexString(md5Byte(source), hexDigits);
	}


	public static String generalVerifyCode() {
		String arr[] = new String[]{"0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9"};

		Random rm = new Random(
				new Double(Math.random() * 10000 * 59).longValue());

		String strRand = "";
		for (int i = 0; i < 6; i++) {
			String rand = arr[rm.nextInt(arr.length)];
			strRand += rand;
		}
		return strRand;
	}

}
