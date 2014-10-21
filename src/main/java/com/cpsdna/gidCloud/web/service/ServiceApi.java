package com.cpsdna.gidCloud.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpsdna.gidCloud.web.mapper.DeviceMapper;
import com.cpsdna.gidCloud.web.mapper.GroupMapper;
import com.cpsdna.gidCloud.web.mapper.UserMapper;
import com.fivestars.interfaces.bbs.client.BbsClient;

@Service
public class ServiceApi
{
	@Autowired
	public GroupMapper group;
	
	@Autowired
	public UserMapper user;
	
	@Autowired
	public DeviceMapper device;
	
	@Autowired
	public JedisService jedis;
	
	@Autowired
	public MailService mail;
	
	@Autowired
	public HttpService cloudapi;
	
	@Autowired
	public BbsClient bbs;
}
