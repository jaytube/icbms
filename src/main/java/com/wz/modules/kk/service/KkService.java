package com.wz.modules.kk.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.devicelog.entity.DeviceElectricityLogEntity;
import com.wz.modules.kk.entity.DeviceAlarm;
import com.wz.modules.kk.entity.LineUpper;
import com.wz.modules.kk.entity.PageInfo;

public interface KkService {

	/*public String setCmdSwitch(String deviceBoxMac, String switchAddStrs, String cmd);*/

	public String setCmdSwitch(String projectId, String deviceBoxMac, String switchAddStrs, String cmd);

	public List<DeviceElectricityLogEntity> getBoxDayPower(String deviceBoxMac);

	public List<DeviceSwitchInfoEntity> getBoxChannelsRealData(String deviceBoxMac, String projectId);

	public PageInfo<DeviceAlarm> getAlarmsByTime(String deviceBoxMac, String startTime, String endTime, String pageSize,
			String page);

	public String setUpdateDateTime(String deviceBoxMac, String cmd);

	public void doKkSchedule();

	public void doKkAlarmSchedule();

	public void doKkDayElectricity();

	public List<LineUpper> getLineUpper(String deviceBoxMac);

	public void processDeviceBoxOnline(List<DeviceBoxInfoEntity> deviceBoxList);

	public Map<String, Integer> statDeviceBoxOnline(String projectId);

	public void doKkProject();

	// 设置网关参数
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
	public void setGwParams(String gwAddress, String gwNum, String gwList, String startAddr, String endAddr,
			String gwDelayTime, String dxDelayTime, String getServerTimeWx, String okFlag, String queueFlag,
			String reportCycle, String gw2DxOverTime);

	// 读取网关参数
	public void getGwParams(String gwAddress);

	// 设置电箱参数
	public void setTerminalPsrams(String terminalAddr, String switchAddr, String maxPower, String maxEletric);

	// 读取电箱参数
	public Map<String, String> getTerminalParams(String termainlAddr, String switchAddr);
	//同步终端配置
	public void doSynTerminalConfig(String projectId);
}