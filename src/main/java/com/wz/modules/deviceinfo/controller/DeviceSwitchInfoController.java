package com.wz.modules.deviceinfo.controller;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.PageUtils;
import com.wz.modules.common.utils.Query;
import com.wz.modules.common.utils.Result;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceSwitchInfoService;


/**
 * 空开设备基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:57:43
 */
@CrossOrigin
@RestController
@Api(tags = "空开设备管理接口")
@RequestMapping("deviceswitchinfo")
public class DeviceSwitchInfoController extends BaseController{
	@Autowired
	private DeviceSwitchInfoService deviceSwitchInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("deviceswitchinfo:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<DeviceSwitchInfoEntity> deviceSwitchInfoList = deviceSwitchInfoService.queryList(query);
		int total = deviceSwitchInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(deviceSwitchInfoList, total, query.getLimit(), query.getPage());
		
		return Result.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("deviceswitchinfo:info")
	public Result info(@PathVariable("id") String id){
		DeviceSwitchInfoEntity deviceSwitchInfo = deviceSwitchInfoService.queryObject(id);
		
		return Result.ok().put("deviceSwitchInfo", deviceSwitchInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("deviceswitchinfo:save")
	public Result save(@RequestBody DeviceSwitchInfoEntity deviceSwitchInfo){
		deviceSwitchInfoService.save(deviceSwitchInfo);
		
		return Result.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("deviceswitchinfo:update")
	public Result update(@RequestBody DeviceSwitchInfoEntity deviceSwitchInfo){
		deviceSwitchInfoService.update(deviceSwitchInfo);
		
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("deviceswitchinfo:delete")
	public Result delete(@RequestBody String[] ids){
		deviceSwitchInfoService.deleteBatch(ids);
		
		return Result.ok();
	}
	
}
