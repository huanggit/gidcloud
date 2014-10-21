package com.cpsdna.gidCloud.web.controllers;

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
public class GroupController {

	@Autowired
	ServiceApi service;

	private static final Logger logger = LogManager
			.getLogger(GroupController.class.getName());

	@Level
	@ResponseBody
	@RequestMapping("/getGroups")
	public Map<String, Object> getGroups(@RequestParam("token") String token,
			@RequestParam("groupNameKeyword") String groupNameKeyword,
			@RequestParam("groupIdKeyword") String groupIdKeyword,
			@RequestParam("pageNo") int pageNo) {
		String userId = service.jedis.get(token);
		Page page = new Page(pageNo);
		List<Map<String, Object>> list = service.group.getGroupsByUser(userId, groupNameKeyword, groupIdKeyword, page);
		int records = service.group.getGroupsNoByUser(userId, groupNameKeyword, groupIdKeyword);
		page.setRecords(records);
		logger.debug("get groups :{}", list);
		return ResultMap.successPage(page.getPages(), pageNo, list);
	}
	
	@Level
	@ResponseBody
	@RequestMapping("/getGroupsByDeviceId")
	public Map<String, Object> getGroupsByDeviceId(@RequestParam("token") String token,
			@RequestParam("deviceIdKeyword") String deviceIdKeyword,
			@RequestParam("pageNo") int pageNo) {
		String userId = service.jedis.get(token);
		Page page = new Page(pageNo);
		List<Map<String, Object>> list = service.group.getGroupsByUserAndDevice(userId, deviceIdKeyword, page);
		int records = service.group.getGroupsNoByUserAndDevice(userId, deviceIdKeyword);
		page.setRecords(records);
		logger.debug("get groups :{}", list);
		return ResultMap.successPage(page.getPages(), pageNo, list);
	}
	
	@Level
	@ResponseBody
	@RequestMapping("/getDevicesByGroup")
	public Map<String, Object> getDevicesByGroup(@RequestParam("token") String token,
			@RequestParam("groupId") String groupId) {
		List<String> list = service.group.getDevicesByGroup(groupId);
		logger.debug("get devices :{} from group: {}", list, groupId);
		return ResultMap.successList(list);
	}
}
