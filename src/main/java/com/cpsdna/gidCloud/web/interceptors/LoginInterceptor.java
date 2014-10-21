package com.cpsdna.gidCloud.web.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cpsdna.gidCloud.web.controllers.Level;
import com.cpsdna.gidCloud.web.service.CookieUtil;
import com.cpsdna.gidCloud.web.service.ServiceApi;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	ServiceApi service;
	
	private static final Logger logger = LogManager
			.getLogger(LoginInterceptor.class.getName());

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		Level passport = ((HandlerMethod) handler)
				.getMethodAnnotation(Level.class);
		if (passport == null)
			return true;
		String token = CookieUtil.get(request, "token");
		if (auth(token, passport.value()))
			return true;
		try {
			response.sendRedirect("login");
		} catch (IOException e) {
			logger.error("redirect to login error:{}", e.getMessage());
		}
		return false;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String name = CookieUtil.get(request, "accountname");
		if (modelAndView != null){
			modelAndView.addObject("accountname", name);
			modelAndView.addObject("bbsHome", service.bbs.getHome());
		}
			
	}

	private boolean auth(String token, int authLvl) {
		if (token == null)
			return false;
		String userId = service.jedis.get(token);
		if (userId != null)
			return true;
		return false;
	}
}
