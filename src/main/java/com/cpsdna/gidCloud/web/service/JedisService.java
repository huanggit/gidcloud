package com.cpsdna.gidCloud.web.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JedisService {

	@Autowired
	private RedisTemplate<String, String> template;

	public void set(String key, String value) {
		template.opsForValue().set(key, value, 1, TimeUnit.HOURS);
	}

	public String get(String key) {
		return template.opsForValue().get(key);
	}
}
