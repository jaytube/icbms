package com.wz.modules.devicelog.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class DeviceAlarmInfoLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "告警主键", name = "id", required = true)
	private String id;
	@ApiModelProperty(value = "项目ID", name = "projectId", required = true)
	private String projectId;
	@ApiModelProperty(value = "电箱ID", name = "deviceBoxId", required = true)
	private String deviceBoxId;
	@ApiModelProperty(value = "电箱MAC", name = "deviceBoxMac", required = true)
	private String deviceBoxMac;
	@ApiModelProperty(value = "autoId", name = "autoId", hidden = true)
	private Long autoId;
	@ApiModelProperty(value = "线路", name = "node")
	private String node;
	@ApiModelProperty(value = "告警类型", name = "type")
	private String type;
	@ApiModelProperty(value = "记录时间", name = "recordTime")
	private Date recordTime;
	@ApiModelProperty(value = "告警信息内容", name = "info")
	private String info;
	@ApiModelProperty(value = "告警信息创建时间", name = "createTime")
	private Date createTime;
	@ApiModelProperty(value = "告警信息更新时间", name = "updateTime", hidden = true)
	private Date updateTime;
	@ApiModelProperty(value = "创建者", name = "createId", hidden = true)
	private String createId;
	@ApiModelProperty(value = "更新者", name = "updateId", hidden = true)
	private String updateId;
	@ApiModelProperty(value = "备注", name = "remark")
	private String remark;
	@ApiModelProperty(value = "告警状态", name = "alarmStatus")
	private String alarmStatus;
	@ApiModelProperty(value = "告警水平", name = "alarmLevel")
	private String alarmLevel;
	@ApiModelProperty(value = "电箱名称", name = "deviceBoxName")
	private String deviceBoxName;

	@ApiModelProperty(value = "告警等级名称", name = "alarmLevelName")
	private String alarmLevelName;

	@ApiModelProperty(value = "二级电箱网关号", name = "secBoxGateway")
	private String secBoxGateway;

	@ApiModelProperty(value = "展位号", name = "standNo")
	private String standNo;

	@ApiModelProperty(value = "是否受控,[0:否,1:是]", name = "controlFlag")
	private String controlFlag;

	@ApiModelProperty(value = "电箱容量", name = "boxCapacity")
	private String boxCapacity;

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
	 * 设置：
	 */
	public void setDeviceBoxId(String deviceBoxId) {
		this.deviceBoxId = deviceBoxId;
	}

	/**
	 * 获取：
	 */
	public String getDeviceBoxId() {
		return deviceBoxId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public void setNode(String node) {
		this.node = node;
	}

	/**
	 * 获取：线路名称
	 */
	public String getNode() {
		return node;
	}

	/**
	 * 设置：
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取：
	 */
	public String getType() {
		return type;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	/**
	 * 设置：告警信息内容
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * 获取：告警信息内容
	 */
	public String getInfo() {
		return info;
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

	public String getDeviceBoxName() {
		return deviceBoxName;
	}

	public void setDeviceBoxName(String deviceBoxName) {
		this.deviceBoxName = deviceBoxName;
	}

	public String getDeviceBoxMac() {
		return deviceBoxMac;
	}

	public void setDeviceBoxMac(String deviceBoxMac) {
		this.deviceBoxMac = deviceBoxMac;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmLevelName() {
		return alarmLevelName;
	}

	public void setAlarmLevelName(String alarmLevelName) {
		this.alarmLevelName = alarmLevelName;
	}

	public String getSecBoxGateway() {
		return secBoxGateway;
	}

	public void setSecBoxGateway(String secBoxGateway) {
		this.secBoxGateway = secBoxGateway;
	}

	public String getStandNo() {
		return standNo;
	}

	public void setStandNo(String standNo) {
		this.standNo = standNo;
	}

	public String getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(String controlFlag) {
		this.controlFlag = controlFlag;
	}

	public String getBoxCapacity() {
		return boxCapacity;
	}

	public void setBoxCapacity(String boxCapacity) {
		this.boxCapacity = boxCapacity;
	}
}