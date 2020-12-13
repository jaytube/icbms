package com.wz.modules.devicelog.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日用电日志表; InnoDB free: 395264 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-05-17 00:47:37
 */
public class DeviceElectricityLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// id主键
	private String id;
	//
	private String projectId;
	// 所属电箱
	private String deviceBoxId;
	// 线路地址
	private String addr;
	// 用电量
	private String electricity;
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
	// 统计日期
	private String statDate;

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
	 * 设置：线路地址
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * 获取：线路地址
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * 设置：用电量
	 */
	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}

	/**
	 * 获取：用电量
	 */
	public String getElectricity() {
		return electricity;
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

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

}
