package com.cpsdna.gidCloud.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpsdna.gidCloud.web.mapper.Page;
import com.cpsdna.gidCloud.web.service.ServiceApi;

@Controller
public class DeviceController {

	@Autowired
	ServiceApi service;

	private static final Logger logger = LogManager
			.getLogger(DeviceController.class.getName());

	@Level
	@ResponseBody
	@RequestMapping("/getDevices")
	public Map<String, Object> getDevices(@RequestParam("token") String token,
			@RequestParam("deviceIdKeyword") String deviceIdKeyword,
			@RequestParam("pageNo") int pageNo) {
		String userId = service.jedis.get(token);
		Page page = new Page(pageNo);
		List<Map<String, Object>> list = service.device.getDevicesByUserId(
				userId, deviceIdKeyword, page);
		int records = service.device.getDevicesNoByUserId(userId,
				deviceIdKeyword);
		page.setRecords(records);
		logger.debug("get devices: {}", list);
		return ResultMap.successPage(page.getPages(), pageNo, list);
	}

	@Level
	@ResponseBody
	@RequestMapping("/setDisplacementByDeviceId")
	public Map<String, Object> setDisplacementByDeviceId(
			@RequestParam("token") String token,
			@RequestParam("deviceId") String deviceId,
			@RequestParam("displacement") String displacement) {
		String userId = service.jedis.get(token);
		Map<String, Object> map = new HashMap<>();
		map.put("deviceId", deviceId);
		map.put("engineDisplacement", displacement);
		return service.cloudapi.request(userId, "initDevice", map);
	}

	@Level
	@ResponseBody
	@RequestMapping("/getGpsInfo")
	public Map<String, Object> getGpsInfo(@RequestParam("token") String token,
			@RequestParam("deviceId") String deviceId) {
		String userId = service.jedis.get(token);
		Map<String, Object> map = new HashMap<>();
		map.put("deviceId", deviceId);
		return service.cloudapi.request(userId, "deviceLocation", map);
	}
}
