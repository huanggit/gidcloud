package com.cpsdna.gidCloud.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMap {

	public static int SUCCESS_CODE = 0;
	public static int FAILED_CODE = 1;

	public static Map<String, Object> custom(int resultCode, String note,
			Map<String, Object> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", resultCode);
		resultMap.put("note", note);
		if (data != null)
			resultMap.putAll(data);
		return resultMap;
	}

	public static Map<String, Object> copy(Map<String, Object> map,
			String... copyKeys) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (String key : copyKeys) {
			Object value = map.get(key);
			resultMap.put(key, value);
		}
		return resultMap;
	}

	public static Map<String, Object> successCopy(Map<String, Object> map,
			String... copyKeys) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", SUCCESS_CODE);
		resultMap.put("note", "SUCCESS");
		for (String key : copyKeys) {
			Object value = map.get(key);
			resultMap.put(key, value);
		}
		return resultMap;
	}

	public static Map<String, Object> success(String note,
			Map<String, Object> data) {
		return custom(SUCCESS_CODE, note, data);
	}

	public static Map<String, Object> successData(Map<String, Object> data) {
		return success("SUCCESS", data);
	}

	public static Map<String, Object> successList(List<?> dataList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", SUCCESS_CODE);
		resultMap.put("note", "SUCCESS");
		resultMap.put("dataList", dataList);
		return resultMap;
	}

	public static Map<String, Object> successPage(int pages, int pageNo,
			List<?> dataList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", SUCCESS_CODE);
		resultMap.put("note", "SUCCESS");
		resultMap.put("dataList", dataList);
		resultMap.put("pages", pages);
		resultMap.put("pageNo", pageNo);
		return resultMap;
	}

	public static Map<String, Object> successNote(String note) {
		return success(note, null);
	}

	public static Map<String, Object> failed(String note) {
		return custom(FAILED_CODE, note, null);
	}

}
