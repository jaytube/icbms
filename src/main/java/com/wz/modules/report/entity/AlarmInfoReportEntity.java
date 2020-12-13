package com.wz.modules.report.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;



/**
 * 空开设备警告日志表; InnoDB free: 237568 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-08-23 20:27:10
 */
public class AlarmInfoReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id主键
	private String id;
	//
	private String projectId;
	//电箱MAC地址
	private String deviceBoxMac;
	//
	private String deviceBoxId;
	//记录ID
	private Integer autoId;
	//线路名称
	private String node;
	//
	private String type;
	//告警时间
	private Date recordTime;
	//告警信息内容
	private String info;
	//告警状态 1告警 0恢复
	private String alarmStatus;
	//新建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//创建者
	private String createId;
	//更新者
	private String updateId;
	//备注
	private String remark;
    //告警等级
	private String alarmLevel;

    //告警等级名称
    private String alarmLevelName;

	private String standNo;


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
	 * 设置：电箱MAC地址
	 */
	public void setDeviceBoxMac(String deviceBoxMac) {
		this.deviceBoxMac = deviceBoxMac;
	}
	/**
	 * 获取：电箱MAC地址
	 */
	public String getDeviceBoxMac() {
		return deviceBoxMac;
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
	/**
	 * 设置：记录ID
	 */
	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}
	/**
	 * 获取：记录ID
	 */
	public Integer getAutoId() {
		return autoId;
	}
	/**
	 * 设置：线路名称
	 */
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
	/**
	 * 设置：告警时间
	 */
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	/**
	 * 获取：告警时间
	 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRecordTime() {
		return recordTime;
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
	 * 设置：告警状态 1告警 0恢复
	 */
	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	/**
	 * 获取：告警状态 1告警 0恢复
	 */
	public String getAlarmStatus() {
		return alarmStatus;
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
	/**
	 * 设置：告警水平
	 */
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	/**
	 * 获取：告警水平
	 */
	public String getAlarmLevel() {
		return alarmLevel;
	}

    public String getAlarmLevelName() {
        return alarmLevelName;
    }

    public void setAlarmLevelName(String alarmLevelName) {
        this.alarmLevelName = alarmLevelName;
    }

	public String getStandNo() {
		return standNo;
	}

	public void setStandNo(String standNo) {
		this.standNo = standNo;
	}

}
