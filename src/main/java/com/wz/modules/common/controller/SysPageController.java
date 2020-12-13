package com.wz.modules.common.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 * 
 * @author admin
 * @date 2017年3月24日 下午11:05:27
 */
@CrossOrigin
@Api(tags = "系统页面视图接口")
@Controller
public class SysPageController {

	@RequestMapping("{module}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("url") String url){
		return  "modules/"+ module + "/" + url + ".html";
	}

	@RequestMapping("/")
	public String index(){
		return "login.html";
	}
}
