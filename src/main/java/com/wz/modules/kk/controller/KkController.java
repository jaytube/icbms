package com.wz.modules.kk.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.wz.modules.common.annotation.SysLog;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.PageUtils;
import com.wz.modules.common.utils.Result;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.devicelog.service.DeviceAlarmInfoLogService;
import com.wz.modules.kk.entity.PageInfo;
import com.wz.modules.kk.service.KkService;

@CrossOrigin
@RestController
@RequestMapping("KK")
public class KkController extends BaseController {

	@Autowired
	private KkService kkService;
	@Autowired
	private DeviceAlarmInfoLogService deviceAlarmInfoLogService;

	@RequestMapping(value = "/setCmdSwitch")
	@SysLog("一键开关")
	public Result setCmdSwitch(String deviceBoxMac, String switchAddStrs, String cmd) {
		String code = kkService.setCmdSwitch(deviceBoxMac, switchAddStrs, cmd, super.getCurrentProjectId());
		return Result.ok().put("code", code);
	}

	/**
	 * @param gwAddress
	 *            网关地址
	 * @param gwNum
	 *            组网网关个数
	 * @param gwList
	 *            组网网关列表(从小到到)
	 * @param startAddr
	 *            组网起始节点地址
	 * @param endAddr
	 *            组网结束节点地址
	 * @param gwDelayTime
	 *            组网网关之间发送延时
	 * @param dxDelayTime
	 *            组网节点之间发送延时
	 * @param getServerTimeWx
	 *            获取服务器时间准许网络延时误差
	 * @param okFlag
	 *            配置有效标志位
	 * @param queueFlag
	 *            服务器发送命令至网关，网关插队或排队发送至节点标志位
	 * @param reportCycle
	 *            上报周期
	 * @param Gw2DxOverTime
	 *            网关发送节点命令回应超时时间
	 * @return
	 */
	@RequestMapping(value = "/setGwParams")
	@SysLog("设置网关参数")
	public Result setGwParams(String gwAddress, String gwNum, String gwList, String startAddr, String endAddr,
			String gwDelayTime, String dxDelayTime, String getServerTimeWx, String okFlag, String queueFlag,
			String reportCycle, String Gw2DxOverTime) {
		kkService.setGwParams(gwAddress, gwNum, gwList, startAddr, endAddr, gwDelayTime, dxDelayTime, getServerTimeWx,
				okFlag, queueFlag, reportCycle, Gw2DxOverTime);
		return Result.ok().put("code", "0");
	}

	@RequestMapping(value = "/getGwParams")
	@SysLog("读取网关参数")
	public Result getGwParams(String deviceBoxMac, String switchAddStrs, String cmd) {
		String code = kkService.setCmdSwitch(deviceBoxMac, switchAddStrs, cmd, super.getCurrentProjectId());
		return Result.ok().put("code", code);
	}

	@RequestMapping(value = "/setSwitchParams")
	@SysLog("设置终端参数")
	public Result setSwitchParams(String terminalAddr, String switchAddr, String maxPower, String maxEletric) {
		kkService.setTerminalPsrams(terminalAddr, switchAddr, maxPower, maxEletric);
		return Result.ok().put("code", "0");
	}

	@RequestMapping(value = "/getSwitchParams")
	@SysLog("读取终端参数")
	public Result getSwitchParams(String termainlAddr, String switchAddr) {
		Map<String, String> map = kkService.getTerminalParams(termainlAddr, switchAddr);
		return Result.ok().put("map", map);
	}

	@RequestMapping(value = "/getBoxChannelsRealData")
	public Result getBoxChannelsRealData(@RequestBody String deviceBoxMac) {
		List<DeviceSwitchInfoEntity> dsList = kkService.getBoxChannelsRealData(deviceBoxMac,
				super.getCurrentProjectId());
		return Result.ok().put("resultList", dsList);
	}

	@RequestMapping(value = "/getAlarmsByTime")
	public Result getAlarmsByTime(String locationId, String startTime, String endTime, String type, String deviceBoxMac,
			String pageSize, String page, String alarmLevel) {
		if (!StringUtils.isEmpty(startTime)) {
			startTime = startTime + " 00:00:00";
		}
		if (!StringUtils.isEmpty(endTime)) {
			endTime = endTime + " 23:59:59";
		}
		PageInfo<DeviceAlarmInfoLogEntity> pageInfo = deviceAlarmInfoLogService.queryDeviceAlarmPage(startTime, endTime,
				type, deviceBoxMac, getCurrentProjectId(), locationId, page, pageSize, alarmLevel);

		PageUtils pageUtil = new PageUtils(pageInfo.getDataList(), Integer.valueOf(pageInfo.getTotal()),
				Integer.valueOf(pageSize), Integer.valueOf(page));
		return Result.ok().put("page", pageUtil);
	}

	@RequestMapping(value = "/setUpdateDateTime/{deviceBoxMac}/{cmd}")
	public Result setUpdateDateTime(@PathVariable String deviceBoxMac, @PathVariable String cmd) {
		String code = kkService.setUpdateDateTime(deviceBoxMac, cmd);
		return Result.ok().put("code", code);
	}

	@RequestMapping(value = "/doSynTerminalConfig")
	@SysLog("同步终端配置")
	public Result doSynTerminalConfig() {
		String projectId = super.getCurrentProjectId();
		kkService.doSynTerminalConfig(projectId);
		return Result.ok().put("code", "0");
	}
}