package com.wz.modules.devicelog.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class DeviceAlarmFlowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "告警id")
	private String alarmId;

	@ApiModelProperty(value = "项目id")
	private String projectId;

	@ApiModelProperty(value = "工单号")
	private String workNo;

	@ApiModelProperty(value = "告警内容")
	private String alarmContent;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "附件路径")
	private String filePath;

	@ApiModelProperty(value = "状态,[0:待处理,1:处理中,2:已完成]")
	private String status;

	@ApiModelProperty(value = "处理人id")
	private String dealId;

	@ApiModelProperty(value = "处理时间")
	private Date dealTime;

	private Date createTime;

	private String createId;

	private String updateId;

	private Date updateTime;

	@ApiModelProperty(value = "告警")
	private DeviceAlarmInfoLogEntity alarmInfoLog;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}

	public String getAlarmContent() {
		return alarmContent;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateId() {
		return createId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceAlarmInfoLogEntity getAlarmInfoLog() {
		return alarmInfoLog;
	}

	public void setAlarmInfoLog(DeviceAlarmInfoLogEntity alarmInfoLog) {
		this.alarmInfoLog = alarmInfoLog;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
}