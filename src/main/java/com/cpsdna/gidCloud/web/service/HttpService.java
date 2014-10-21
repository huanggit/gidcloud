package com.cpsdna.gidCloud.web.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpService {

	private String ip;

	private static final Logger logger = LogManager.getLogger(HttpService.class
			.getName());

	public Map<String, Object> request(String userId, String cmd,
			Map<String, Object> reqMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmd", cmd);
		map.put("userId", userId);
		if (reqMap != null)
			map.putAll(reqMap);
		try {
			String postReq = JsonUtil.writeValueAsString(map);
			String response = post(postReq);

			if (response == null || response.isEmpty()) {
				return null;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> respMap = (Map<String, Object>) JsonUtil
					.readValue(response, Map.class);
			return respMap;
		} catch (Exception e) {
			return null;
		}
	}

	public String post(String json) {
		logger.info("TM API request --> {} to host {}", json, ip);
		StringBuffer sb = new StringBuffer("");
		long start = System.currentTimeMillis();
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		DataOutputStream out = null;
		try {
			URL url = new URL(ip);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(20000);
			connection.setReadTimeout(20000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
			out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(json);
			out.flush();
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
		} catch (Exception e) {
			logger.error("TM device http connect error: {}", e.getMessage());
		} finally {
			try {
				out.close();
				reader.close();
				connection.disconnect();
			} catch (Exception e) {
				logger.error("TM device http connect colse error: {}",
						e.getMessage());
			}
		}
		String result = sb.toString();
		logger.info("TM API response --> {} invoke time {} ms", result,
				(System.currentTimeMillis() - start));
		return result;
	}

	// setters
	public void setIp(String ip) {
		this.ip = ip;
	}

}
