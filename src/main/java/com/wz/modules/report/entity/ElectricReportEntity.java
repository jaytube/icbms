package com.wz.modules.report.entity;

import java.io.Serializable;

public class ElectricReportEntity implements Serializable {

    private String projectId;
    private String deviceBoxId;
	private String standNo;
	private String secBoxGateway;
    private String statDate;
    private String deviceBoxNum;
    private String addr;
    private String electricity;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDeviceBoxId() {
        return deviceBoxId;
    }

    public void setDeviceBoxId(String deviceBoxId) {
        this.deviceBoxId = deviceBoxId;
    }

	public String getStandNo() {
		return standNo;
	}

	public void setStandNo(String standNo) {
		this.standNo = standNo;
	}

	public String getSecBoxGateway() {
		return secBoxGateway;
	}

	public void setSecBoxGateway(String secBoxGateway) {
		this.secBoxGateway = secBoxGateway;
	}

	public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getDeviceBoxNum() {
        return deviceBoxNum;
    }

    public void setDeviceBoxNum(String deviceBoxNum) {
        this.deviceBoxNum = deviceBoxNum;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

}
