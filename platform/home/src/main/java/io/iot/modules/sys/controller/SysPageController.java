/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 *
 */

package io.iot.modules.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 *
 * @author Mark sunlightcs@gmail.com
 */
@Controller
public class SysPageController {

	@RequestMapping("modules/{module}/{url}.html")
	public String module(@PathVariable("module") String module, @PathVariable("url") String url){
		return "modules/" + module + "/" + url;
	}

	@RequestMapping("mobiles/{module}/{url}.html")
	public String mobiles(@PathVariable("module") String module, @PathVariable("url") String url){
		return "mobiles/modules" + module + "/" + url;
	}

	@RequestMapping(value = "mobiles/home.html")
	public String mobileHome(){
		return "mobile/home";
	}

	@RequestMapping("mobiles/login.html")
	public String mobileLogin(){
		return "mobile/login";
	}

	@RequestMapping(value = {"/", "index.html"})
	public String index(){
		return "index";
	}

	@RequestMapping("index1.html")
	public String index1(){
		return "index1";
	}

	@RequestMapping("login.html")
	public String login(){
		return "login";
	}

	@RequestMapping("main.html")
	public String main(){
		// 有可视化权限则跳转到可视化页面,否则跳转到欢迎页面
//		if (ShiroUtils.getSubject().isPermitted("visualization:module")) {
//			return "modules/policy/visualization.html";
//		}
//		return "redirect:http://192.168.100.92:11111";
		return "main";
	}

	@RequestMapping("404.html")
	public String notFound(){
		return "404";
	}

}
