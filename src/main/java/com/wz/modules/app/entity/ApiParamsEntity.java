/**
 * 
 */
package com.wz.modules.app.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lkpc
 *
 */
@ApiModel(value = "电箱参数信息", description = "电箱参数信息")
public class ApiParamsEntity implements Serializable {
	@ApiModelProperty(value = "项目id", name = "projectId")
	private String projectId;
	@ApiModelProperty(value = "电箱MAC", name = "deviceBoxMac")
	private String deviceBoxMac;
	@ApiModelProperty(value = "线路地址", name = "switchAddr")
	private String switchAddr;
	@ApiModelProperty(value = "电流上限", name = "electricUpper")
	private String electricUpper;
	@ApiModelProperty(value = "功率上限", name = "powerUpper")
	private String powerUpper;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDeviceBoxMac() {
		return deviceBoxMac;
	}
	public void setDeviceBoxMac(String deviceBoxMac) {
		this.deviceBoxMac = deviceBoxMac;
	}
	public String getSwitchAddr() {
		return switchAddr;
	}
	public void setSwitchAddr(String switchAddr) {
		this.switchAddr = switchAddr;
	}
	public String getElectricUpper() {
		return electricUpper;
	}
	public void setElectricUpper(String electricUpper) {
		this.electricUpper = electricUpper;
	}
	public String getPowerUpper() {
		return powerUpper;
	}
	public void setPowerUpper(String powerUpper) {
		this.powerUpper = powerUpper;
	}
	
	
}
