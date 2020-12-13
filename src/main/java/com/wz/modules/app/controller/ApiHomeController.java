package com.wz.modules.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wz.modules.app.annotation.CurrentUser;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.app.entity.ApiUserEntity;
import com.wz.modules.app.service.ApiKKService;
import com.wz.modules.app.service.ApiProjectService;
import com.wz.modules.app.service.ApiUserService;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.Result;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.devicelog.entity.DeviceSwitchInfoLogEntity;
import com.wz.modules.devicelog.service.DeviceAlarmInfoLogService;
import com.wz.modules.devicelog.service.DeviceSwitchInfoLogService;
import com.wz.modules.kk.entity.DeviceAlarm;
import com.wz.modules.kk.entity.PageInfo;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;

/**
 * 类ApiLoginController的功能描述: APP登录授权
 * 
 * @auther Raymon
 * @date 2017-10-16 14:15:39
 */
@CrossOrigin
@Controller
@RequestMapping("/app/home")
public class ApiHomeController extends BaseController {
	private final static Logger logger = LoggerFactory.getLogger(ApiHomeController.class);
	@Autowired
	private ApiUserService userApiService;
	@Autowired
	private ApiProjectService projectApiService;
	@Autowired
	private LocationInfoService locatinInfoService;
	@Autowired
	private ApiKKService kkApiService;
	@Autowired
	private DeviceAlarmInfoLogService deviceAlarmInfoLogService;
	@Autowired
	private DeviceSwitchInfoLogService deviceSwitchInfoLogService;

	@Autowired
	private DeviceBoxInfoService deviceBoxInfoService;

	/**
	 * 用户信息
	 */
	@LoginRequired
	@ResponseBody
	public Result info(@CurrentUser ApiUserEntity apiUserEntity) {
		ApiUserEntity user = userApiService.userInfo(apiUserEntity.getId());
		return Result.ok().put("user", user);
	}

	/**
	 * 获取所有项目列表
	 */
	@RequestMapping(value = "getAllProjects", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectInfoEntity> getAllProjects() {
		logger.info("================>APP获取所有项目列表");
		List<ProjectInfoEntity> projectList = projectApiService.queryListAll();
		logger.info("================>APP获取所有项目列表返回数据：projectList:" + (projectList != null ? projectList.size() : 0));
		return projectList;
	}

	/**
	 * 根据项目ID获取所有位置列表
	 */
	@RequestMapping(value = "getLocationsByProjectId", method = RequestMethod.GET)
	@ResponseBody
	public List<LocationInfoEntity> getLocationsByProjectId(String projectId) {
		logger.info("================>APP获取位置列表，项目ID:" + projectId);
		List<LocationInfoEntity> locationList = locatinInfoService.findLocInfoByPId(projectId);
		logger.info("================>APP根据项目ID获取所有位置数据数据：locationList:"
				+ (locationList != null ? locationList.size() : 0));
		return locationList;
	}

	/**
	 * 根据位置ID获取所有电箱列表
	 */
	@RequestMapping(value = "getDeviceBoxesByLocId", method = RequestMethod.GET)
	@ResponseBody
	public List<DeviceBoxInfoEntity> getDeviceBoxesByLocId(String projectId, String locationId) {
		logger.info("================>APP根据位置获取所有电箱列表,位置ID:" + locationId);
		List<DeviceBoxInfoEntity> deviceBoxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByLId(projectId,
				locationId);
		logger.info("================>APP获取根据位置获取所有电箱列表返回数据：deviceBoxInfoList:"
				+ (deviceBoxInfoList != null ? deviceBoxInfoList.size() : 0));
		return deviceBoxInfoList;
	}

	/**
	 * 根据电箱获取所有空开列表
	 */
	@RequestMapping(value = "getSwitchesByDeviceBoxMac", method = RequestMethod.GET)
	@ResponseBody
	public List<DeviceSwitchInfoEntity> getSwitchesByDeviceBoxId(String deviceBoxMac) {
		logger.info("================>APP根据电箱MCA获取所有空开列表");
		List<DeviceSwitchInfoEntity> deviceSwitchList = kkApiService.getBoxChannelsRealData(deviceBoxMac,
				super.getCurrentProjectId());
		logger.info("================>APP根据电箱MAC获取所有空开列表返回数据：deviceSwitchList.size:"
				+ (deviceSwitchList != null ? deviceSwitchList.size() : 0) + ",deviceSwitchList:"
				+ deviceSwitchList.toString());
		return deviceSwitchList;
	}

	/**
	 * 控制电箱开关
	 */
	@RequestMapping(value = "setCmdToSwitch", method = RequestMethod.GET)
	@ResponseBody
	public String setCmdToSwitch(String deviceBoxMac, String switchAddr, String cmd) {
		logger.info("================>APP控制设备：deviceBoxMac:" + deviceBoxMac + ", switchAddr:" + switchAddr + ", cmd:"
				+ cmd);
		String result = kkApiService.setCmdSwitch(deviceBoxMac, switchAddr, cmd, super.getCurrentProjectId());
		logger.info("================>APP控制设备返回结果：result:" + result);
		return result;
	}

	/**
	 * 获取告警
	 */
	@RequestMapping(value = "getAlarmsData", method = RequestMethod.GET)
	@ResponseBody
	public List<DeviceAlarm> getAlarmsData(String deviceBoxMac, String startTime, String endTime, String pageSize,
			String page) {
		logger.info("================>获取告警数据：deviceBoxMac:" + deviceBoxMac + ", startTime:" + startTime + ", endTime:"
				+ endTime);
		PageInfo<DeviceAlarm> pageInfo = kkApiService.getAlarmsByTime(deviceBoxMac, startTime, endTime, pageSize, page);
		logger.info("================>获取告警返回数据：pageInfo:" + pageInfo.getDataList().size());
		return pageInfo.getDataList();
	}

	@RequestMapping(value = "getAlarmsPage", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo<DeviceAlarmInfoLogEntity> getAlarmsPage(String startTime, String endTime, String pageSize,
			String page, String projectId, String alarmLevel) {
		logger.info("================>获取告警数据: startTime:" + startTime + ", endTime:" + endTime);
		PageInfo<DeviceAlarmInfoLogEntity> pageInfo = deviceAlarmInfoLogService.queryAppPage(startTime, endTime, page,
				pageSize, projectId, null, alarmLevel);
		logger.info("================>获取告警返回数据：pageInfo:" + pageInfo.getDataList().size());
		return pageInfo;
	}

	@RequestMapping(value = "getSwitchesPage", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo<DeviceSwitchInfoLogEntity> getSwitchesPage(String projectId, String startTime, String endTime,
			String pageSize, String page) {
		logger.info("================>获取电箱分页数据: startTime:" + startTime + ", endTime:" + endTime);
		PageInfo<DeviceSwitchInfoLogEntity> pageInfo = deviceSwitchInfoLogService.queryAppPage(projectId, startTime,
				endTime, page, pageSize);
		logger.info("================>获取电箱分页数据：pageInfo:" + pageInfo.getDataList().size());
		return pageInfo;
	}
}