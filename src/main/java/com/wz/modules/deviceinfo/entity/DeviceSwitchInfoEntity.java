package com.wz.modules.deviceinfo.entity;

import java.io.Serializable;
import java.util.Date;

import com.wz.modules.common.entity.BaseEntity;

/**
 * 空开设备基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:57:43
 */
public class DeviceSwitchInfoEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// id主键
	private String id;
	// 归属项目
	private String projectId;
	// 归属位置
	private String locationId;
	// 项目名称
	private String projectName;
	// 位置名称
	private String locationName;
	// 所属电箱
	private String deviceBoxId;
	// 电箱名称
	private String deviceBoxName;
	// 空开设备线路名称
	private String deviceSwitchName;
	// 空开设备状态
	private String deviceSwitchStatus;
	// 实时电流
	private String switchElectric;
	// 实时电压
	private String switchVoltage;
	// 实时温度
	private String switchTemperature;
	// 实时功率
	private String switchPower;
	// 漏电流
	private String switchLeakage;
	// 总用电量
	private String totalPower;

	// A相电压-2字节
	private String phaseVoltageA;

	// B相电压-2字节
	private String phaseVoltageB;

	// C相电压-2字节
	private String phaseVoltageC;

	// A相电流-2字节
	private String phaseCurrentA;

	// B相电流-2字节
	private String phaseCurrentB;

	// C相电流-2字节
	private String phaseCurrentC;

	// N相电流-2字节
	private String phaseCurrentN;

	// A相功率-2字节
	private String phasePowerA;

	// B相功率-2字节
	private String phasePowerB;

	// C相功率-2字节
	private String phasePowerC;

	public String getDeviceBoxName() {
		return deviceBoxName;
	}

	public void setDeviceBoxName(String deviceBoxName) {
		this.deviceBoxName = deviceBoxName;
	}

	// 新建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;
	// 创建者
	private String createId;
	// 更新者
	private String updateId;
	// 备注
	private String remark;
	// 开关地址
	private String address;

	// 接口更新时间
	private String interfaceUpdateTime;

	// 是否在线
	private String online;

	// 是否手动更新
	private String enableNetCtrl;

	/**
	 * 设置：id主键
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取：id主键
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置：所属电箱
	 */
	public void setDeviceBoxId(String deviceBoxId) {
		this.deviceBoxId = deviceBoxId;
	}

	/**
	 * 获取：所属电箱
	 */
	public String getDeviceBoxId() {
		return deviceBoxId;
	}

	/**
	 * 设置：空开设备线路名称
	 */
	public void setDeviceSwitchName(String deviceSwitchName) {
		this.deviceSwitchName = deviceSwitchName;
	}

	/**
	 * 获取：空开设备线路名称
	 */
	public String getDeviceSwitchName() {
		return deviceSwitchName;
	}

	/**
	 * 设置：空开设备状态
	 */
	public void setDeviceSwitchStatus(String deviceSwitchStatus) {
		this.deviceSwitchStatus = deviceSwitchStatus;
	}

	/**
	 * 获取：空开设备状态
	 */
	public String getDeviceSwitchStatus() {
		return deviceSwitchStatus;
	}

	/**
	 * 设置：实时电流
	 */
	public void setSwitchElectric(String switchElectric) {
		this.switchElectric = switchElectric;
	}

	/**
	 * 获取：实时电流
	 */
	public String getSwitchElectric() {
		return switchElectric;
	}

	/**
	 * 设置：实时电压
	 */
	public void setSwitchVoltage(String switchVoltage) {
		this.switchVoltage = switchVoltage;
	}

	/**
	 * 获取：实时电压
	 */
	public String getSwitchVoltage() {
		return switchVoltage;
	}

	/**
	 * 设置：实时温度
	 */
	public void setSwitchTemperature(String switchTemperature) {
		this.switchTemperature = switchTemperature;
	}

	/**
	 * 获取：实时温度
	 */
	public String getSwitchTemperature() {
		return switchTemperature;
	}

	/**
	 * 设置：实时功率
	 */
	public void setSwitchPower(String switchPower) {
		this.switchPower = switchPower;
	}

	/**
	 * 获取：实时功率
	 */
	public String getSwitchPower() {
		return switchPower;
	}

	/**
	 * 设置：漏电流
	 */
	public void setSwitchLeakage(String switchLeakage) {
		this.switchLeakage = switchLeakage;
	}

	/**
	 * 获取：漏电流
	 */
	public String getSwitchLeakage() {
		return switchLeakage;
	}

	/**
	 * 设置：新建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取：新建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置：创建者
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取：创建者
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置：更新者
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取：更新者
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getInterfaceUpdateTime() {
		return interfaceUpdateTime;
	}

	public void setInterfaceUpdateTime(String interfaceUpdateTime) {
		this.interfaceUpdateTime = interfaceUpdateTime;
	}

	public String getEnableNetCtrl() {
		return enableNetCtrl;
	}

	public void setEnableNetCtrl(String enableNetCtrl) {
		this.enableNetCtrl = enableNetCtrl;
	}

	public String getTotalPower() {
		return totalPower;
	}

	public void setTotalPower(String totalPower) {
		this.totalPower = totalPower;
	}

	public String getPhaseVoltageA() {
		return phaseVoltageA;
	}

	public void setPhaseVoltageA(String phaseVoltageA) {
		this.phaseVoltageA = phaseVoltageA;
	}

	public String getPhaseVoltageB() {
		return phaseVoltageB;
	}

	public void setPhaseVoltageB(String phaseVoltageB) {
		this.phaseVoltageB = phaseVoltageB;
	}

	public String getPhaseVoltageC() {
		return phaseVoltageC;
	}

	public void setPhaseVoltageC(String phaseVoltageC) {
		this.phaseVoltageC = phaseVoltageC;
	}

	public String getPhaseCurrentA() {
		return phaseCurrentA;
	}

	public void setPhaseCurrentA(String phaseCurrentA) {
		this.phaseCurrentA = phaseCurrentA;
	}

	public String getPhaseCurrentB() {
		return phaseCurrentB;
	}

	public void setPhaseCurrentB(String phaseCurrentB) {
		this.phaseCurrentB = phaseCurrentB;
	}

	public String getPhaseCurrentC() {
		return phaseCurrentC;
	}

	public void setPhaseCurrentC(String phaseCurrentC) {
		this.phaseCurrentC = phaseCurrentC;
	}

	public String getPhaseCurrentN() {
		return phaseCurrentN;
	}

	public void setPhaseCurrentN(String phaseCurrentN) {
		this.phaseCurrentN = phaseCurrentN;
	}

	public String getPhasePowerA() {
		return phasePowerA;
	}

	public void setPhasePowerA(String phasePowerA) {
		this.phasePowerA = phasePowerA;
	}

	public String getPhasePowerB() {
		return phasePowerB;
	}

	public void setPhasePowerB(String phasePowerB) {
		this.phasePowerB = phasePowerB;
	}

	public String getPhasePowerC() {
		return phasePowerC;
	}

	public void setPhasePowerC(String phasePowerC) {
		this.phasePowerC = phasePowerC;
	}

}
