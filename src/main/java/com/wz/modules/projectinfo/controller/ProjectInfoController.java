package com.wz.modules.projectinfo.controller;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wz.modules.common.annotation.SysLog;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.PageUtils;
import com.wz.modules.common.utils.Query;
import com.wz.modules.common.utils.Result;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;
import com.wz.modules.projectinfo.service.ProjectInfoService;

/**
 * 项目基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
@CrossOrigin
@RestController
@Api(tags = "项目基础信息相关操作接口")
@RequestMapping("projectinfo")
public class ProjectInfoController extends BaseController {
	@Autowired
	private ProjectInfoService projectInfoService;

	@Autowired
	private LocationInfoService locationInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("projectinfo:list")
	@SysLog("查看项目信息")
	public Result list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<ProjectInfoEntity> projectInfoList = projectInfoService.queryList(query);
		int total = projectInfoService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(projectInfoList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("projectinfo:info")
	@SysLog("查看项目信息")
	public Result info(@PathVariable("id") String id) {
		ProjectInfoEntity projectInfo = projectInfoService.queryObject(id);
		projectInfo.setRoleIdList(projectInfoService.queryRoleIdList(id));
		return Result.ok().put("projectInfo", projectInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("projectinfo:save")
	@SysLog("新增项目信息")
	public Result save(@RequestBody ProjectInfoEntity projectInfo) {
		projectInfoService.save(projectInfo);
		return Result.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("projectinfo:update")
	@SysLog("修改项目信息")
	public Result update(@RequestBody ProjectInfoEntity projectInfo) {
		projectInfoService.update(projectInfo);
		return Result.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("projectinfo:delete")
	@SysLog("删除项目信息")
	public Result delete(@RequestBody String[] ids) {
		projectInfoService.deleteBatch(ids);
		return Result.ok();
	}

	/**
	 * 查询所有项目信息
	 */
	@RequestMapping("/listAll")
	@RequiresPermissions("projectinfo:list")
	public Result listAll() {
		List<ProjectInfoEntity> projectinfoList = projectInfoService.queryListAll();
		Result result = Result.ok().put("projectInfoList", projectinfoList);
		return result;
	}

	@RequestMapping("/getCurrentProject")
	public Result getCurrProject() {
		List<LocationInfoEntity> locInfos = locationInfoService.findLocInfoByPId(this.getCurrentProject().getId());
		Result result = Result.ok().put("currentProject", this.getCurrentProject()).put("locInfos", locInfos);
		return result;
	}
}