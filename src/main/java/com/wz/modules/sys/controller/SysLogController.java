package com.wz.modules.sys.controller;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wz.modules.common.utils.PageUtils;
import com.wz.modules.common.utils.Query;
import com.wz.modules.common.utils.Result;
import com.wz.modules.sys.entity.SysLogEntity;
import com.wz.modules.sys.service.SysLogService;

import java.util.List;
import java.util.Map;


/**
 * 类SysLogController的功能描述:
 * 系统日志
 * @auther hxy
 * @date 2017-08-25 16:20:16
 */
@CrossOrigin
@Controller
@Api(tags = "系统日志接口")
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<SysLogEntity> sysLogList = sysLogService.queryList(query);
		int total = sysLogService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysLogList, total, query.getLimit(), query.getPage());
		
		return Result.ok().put("page", pageUtil);
	}
	
}
