package com.cpsdna.gidCloud.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpsdna.gidCloud.web.service.CookieUtil;
import com.cpsdna.gidCloud.web.service.EncryptUtil;
import com.cpsdna.gidCloud.web.service.IdUtil;
import com.cpsdna.gidCloud.web.service.ServiceApi;
import com.cpsdna.gidCloud.web.service.StrUtil;

@Controller
public class UserController {

	@Autowired
	ServiceApi service;


	private static final Logger logger = LogManager
			.getLogger(UserController.class.getName());

	@RequestMapping("/doLogin")
	public String doLogin(HttpServletResponse response, Model model,
			@RequestParam String username, @RequestParam String password) {
		Map<String, Object> user = service.user.getUserByName(username);
		if (user == null) {
			model.addAttribute("errorMessage", "账号/密码错误");
			return "login";
		}
		String correctPasswd = (String) user.get("password");
		if (!EncryptUtil.md5(password).equals(correctPasswd)) {
			model.addAttribute("errorMessage", "账号/密码错误");
			return "login";
		}
		String token = IdUtil.generate();
		String userId = (String) user.get("userId");
		service.jedis.set(token, userId);
		CookieUtil.set(response, "token", token, 3600);
		CookieUtil.set(response, "accountname", username, 3600);
		try {
			int bbsUid = Integer.parseInt((String)user.get("bbsUid"));
			if (service.bbs.isNeedSync()){
				String bbsSync = service.bbs.uc_user_synlogin(bbsUid);
				model.addAttribute("bbsSync", bbsSync);
			}
		} catch (Exception e) {
			logger.error("user:{} bbs sync login error:{}", userId,
					e.getStackTrace());
		}
		model.addAttribute("redirect", "device");
		return "common/redirect";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletResponse response, Model model) {
		CookieUtil.delete(response, "token");
		CookieUtil.delete(response, "accountname");
		try {
			if (service.bbs.isNeedSync()){
				String bbsSync = service.bbs.uc_user_synlogout();
				model.addAttribute("bbsSync", bbsSync);
			}
		} catch (Exception e) {
			logger.error("user{} bbs sync logout error:{}", "",
					e.getStackTrace());
		}
		model.addAttribute("redirect", "index");
		return "common/redirect";
	}

	@RequestMapping("/doRegisterEmail")
	public String doRegisterEmail(@RequestParam String u, @RequestParam String v) {
		Map<String, Object> user = service.user.getUserById(u);
		if (user == null)
			return "register/registerFail";
		String email = (String) user.get("email");
		if (!v.equals(EncryptUtil.md5(email)))
			return "register/registerFail";
		service.user.updateStatusById(u, "1");
		return "register/registerSuc";
	}

	@ResponseBody
	@RequestMapping("/doRegister")
	public Map<String, Object> doRegister(HttpServletRequest request,
			@RequestParam String userName, @RequestParam String email,
			@RequestParam String password, @RequestParam String confirmPassword ) {
		if (service.user.getUserCountByName(userName) > 0)
			return ResultMap.failed("用户名已注册");
		if (!StrUtil.notBlank(userName))
			return ResultMap.failed("用户名不能为空");
		if (service.user.getUserCountByEmail(email) > 0)
			return ResultMap.failed("邮箱已注册");
		if (!StrUtil.checkEmail(email))
			return ResultMap.failed("邮箱不正确");
		if (!StrUtil.checkPassword(password))
			return ResultMap.failed("密码过于简单");
		if (!confirmPassword.equals(password))
			return ResultMap.failed("两次密码输入不一致");

		String userId = IdUtil.generate();
		Map<String, Object> map = new HashMap<>();
		map.put("userName", userName);
		StringBuffer emailUrl = request.getRequestURL();
		emailUrl.append("Email?v=");
		emailUrl.append(EncryptUtil.md5(email));
		emailUrl.append("&u=");
		emailUrl.append(userId);
		logger.debug(emailUrl);
		map.put("emailUrl", emailUrl.toString());
		boolean isSend = service.mail.send(email, "Gid Cloud开发者邮箱验证",
				"/validateEmail.vm", map);
		if (!isSend)
			return ResultMap.failed("验证邮箱发送失败，未注册成功");
		logger.info("用户:{} 注册,验证邮件发送结果{}.", userName, isSend);
		try {
			String bbsUid = "";
			if (service.bbs.isNeedSync()){
				bbsUid = service.bbs
						.uc_user_register(userName, password, email);
			}
			service.user.insertUser(userId, userName, EncryptUtil.md5(password), email, bbsUid);
			logger.info("用户ID:{} 注册成功.", userId);
			return ResultMap.successNote("注册成功");
		} catch (Exception e) {
			logger.error("user:{} bbs sync register error:{}", userId,
					e.getStackTrace());
			return ResultMap.failed("注册失败");
		}

	}

	@RequestMapping("/resetPwdReq")
	public String resetPwdReq(Model model, @RequestParam String u,
			@RequestParam String v) {
		Map<String, Object> user = service.user.getUserById(u);
		if (user == null)
			return "resetPassword/failedValidate";
		String password = (String) user.get("password");
		if (!v.equals(EncryptUtil.md5(password)))
			return "resetPassword/failedValidate";
		model.addAttribute("u", u);
		model.addAttribute("v", v);
		return "resetPassword/reset";
	}

	@ResponseBody
	@RequestMapping("/resetPwdDo")
	public Map<String, Object> resetPwdDo(@RequestParam String u,
			@RequestParam String v, @RequestParam String newPasswd,
			@RequestParam String confirmPasswd) {
		Map<String, Object> user = service.user.getUserById(u);
		if (user == null)
			return ResultMap.failed("修改用户密码链接不正确");
		String oldPassword = (String) user.get("password");
		if (!v.equals(EncryptUtil.md5(oldPassword)))
			return ResultMap.failed("修改用户密码链接不正确");
		if (!StrUtil.checkPassword(newPasswd))
			return ResultMap.failed("新密码过于简单");
		if (!confirmPasswd.equals(newPasswd))
			return ResultMap.failed("两次密码输入不一致");
		try {
			if (service.bbs.isNeedSync()){
				service.bbs.uc_user_edit(user, newPasswd);}
			service.user.updatePasswordById(u, EncryptUtil.md5(newPasswd));
			return ResultMap.successNote("修改用户密码成功");
		} catch (Exception e) {
			logger.error("user:{} bbs sync modPassword error:{}", u,
					e.getStackTrace());
			return ResultMap.failed("修改用户密码失败");
		}
	}

	@ResponseBody
	@RequestMapping("/resetPwd")
	public Map<String, Object> resetPwd(HttpServletRequest request,
			@RequestParam String email) {
		Map<String, Object> user = service.user.getUserByEmail(email);
		if (user == null)
			return ResultMap.failed("重置密码邮件发送失败");
		String userName = (String) user.get("username");
		String userId = (String) user.get("userId");
		String passwd = (String) user.get("password");

		Map<String, Object> map = new HashMap<>();
		map.put("userName", userName);
		StringBuffer emailUrl = request.getRequestURL();
		emailUrl.append("Req?v=");
		emailUrl.append(EncryptUtil.md5(passwd));
		emailUrl.append("&u=");
		emailUrl.append(userId);
		logger.debug(emailUrl);
		map.put("emailUrl", emailUrl.toString());
		boolean isSend = service.mail.send(email, "Gid Cloud重置密码",
				"/resetPasswd.vm", map);
		return isSend ? ResultMap.successNote("重置密码邮件发送成功") : ResultMap
				.failed("重置密码邮件发送失败");
	}

	@ResponseBody
	@RequestMapping("/getAccount")
	public Map<String, Object> getAccount(@RequestParam String token) {
		String userId = service.jedis.get(token);
		Map<String, Object> user = service.user.getUserById(userId);
		return ResultMap.successCopy(user, "userId", "email", "org", "phone");
	}

	@ResponseBody
	@RequestMapping("/modAccount")
	public Map<String, Object> modAccount(@RequestParam String userId,
			@RequestParam String org, @RequestParam String phone) {
		service.user.updateUserById(userId, org, phone);
		return ResultMap.successNote("修改用户信息成功");
	}

	@ResponseBody
	@RequestMapping("/modPassword")
	public Map<String, Object> modPassword(@RequestParam String token,
			@RequestParam String oldPasswd, @RequestParam String newPasswd,
			@RequestParam String confirmPasswd) {
		String userId = service.jedis.get(token);
		Map<String, Object> user = service.user.getUserById(userId);
		String correctPasswd = (String) user.get("password");
		if (!correctPasswd.equals(EncryptUtil.md5(oldPasswd)))
			return ResultMap.failed("旧密码不正确");
		if (!StrUtil.checkPassword(newPasswd))
			return ResultMap.failed("新密码过于简单");
		if (!confirmPasswd.equals(newPasswd))
			return ResultMap.failed("两次密码输入不一致");
		try {
			if (service.bbs.isNeedSync()){
				service.bbs.uc_user_edit(user, newPasswd);}
			service.user.updatePasswordById(userId, EncryptUtil.md5(newPasswd));
			return ResultMap.successNote("修改用户密码成功");
		} catch (Exception e) {
			logger.error("user:{} bbs sync modPassword error:{}", userId,
					e.getStackTrace());
			return ResultMap.failed("修改用户密码失败");
		}

	}

}
