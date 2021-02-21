package com.wz.modules.lora.dto;

import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: DeviceInfoDto
 */
@Data
public class DeviceBoxInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;
    private String projectName;
    private String deviceBoxNum;
    private String deviceBoxName;
    private String remark;
    private String online = "1";
    private String fx;
    private String fy;
    private String secBoxGateway;
    private String standNo;
    private String controlFlag;
    private String boxCapacity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeviceBoxNum() {
        return deviceBoxNum;
    }

    public void setDeviceBoxNum(String deviceBoxNum) {
        this.deviceBoxNum = deviceBoxNum;
    }

    public String getDeviceBoxName() {
        return deviceBoxName;
    }

    public void setDeviceBoxName(String deviceBoxName) {
        this.deviceBoxName = deviceBoxName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
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
