package com.cpsdna.gidCloud.web.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticController {
	
	@RequestMapping("/")
	public String home(){
		return "index";
	}
	
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
	@RequestMapping("/doc")
	public String doc(){
		return "doc";
	}
	
	@RequestMapping("/faq")
	public String faq(){
		return "faq";
	}
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/resetPassword")
	public String resetPassword(){
		return "resetPassword/email";
	}	
	
	@RequestMapping("/price")
	public String price(){
		return "price";
	}
	
	@RequestMapping("/register")
	public String register(){
		return "register/register";
	}
	
	@RequestMapping("/protocol")
	public String protocol(){
		return "protocol";
	}
	
	@Level
	@RequestMapping("/device")
	public String device(){
		return "personalCenter/device";
	}
	
	@Level
	@RequestMapping("/group")
	public String group(){
		return "personalCenter/group";
	}
	
	@Level
	@RequestMapping("/account")
	public String account(){
		return "personalCenter/account";
	}
}
