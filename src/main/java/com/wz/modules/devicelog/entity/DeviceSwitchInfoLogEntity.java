package com.wz.modules.devicelog.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 空开设备日志表; InnoDB free: 398336 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-05-15 21:44:00
 */
public class DeviceSwitchInfoLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// id主键
	private String id;
	//
	private String projectId;
	// 所属电箱
	private String deviceBoxId;
	// 空开设备线路名称
	private String deviceSwitchName;
	//
	private String address;
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

	// 接口更新时间
	private String interfaceUpdateTime;

	//是否在线
	private String online;

	//是否手动更新
	private String enableNetCtrl;
	
	//电箱名称
	private String deviceBoxName;

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
	 * 设置：
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取：
	 */
	public String getProjectId() {
		return projectId;
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
	 * 设置：
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取：
	 */
	public String getAddress() {
		return address;
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

	public String getInterfaceUpdateTime() {
		return interfaceUpdateTime;
	}

	public void setInterfaceUpdateTime(String interfaceUpdateTime) {
		this.interfaceUpdateTime = interfaceUpdateTime;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getEnableNetCtrl() {
		return enableNetCtrl;
	}

	public void setEnableNetCtrl(String enableNetCtrl) {
		this.enableNetCtrl = enableNetCtrl;
	}

	public String getDeviceBoxName() {
		return deviceBoxName;
	}

	public void setDeviceBoxName(String deviceBoxName) {
		this.deviceBoxName = deviceBoxName;
	}
}
