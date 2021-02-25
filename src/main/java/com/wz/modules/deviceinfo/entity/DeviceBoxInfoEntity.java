package com.wz.modules.deviceinfo.entity;

import com.wz.front.enums.DeviceBoxStatus;
import com.wz.modules.common.entity.BaseEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.kk.entity.DeviceBoxConfigEntity;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceBoxInfoEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "电箱ID", name = "id", required = true)
    private String id;
    @ApiModelProperty(value = "项目ID", name = "projectId", required = true)
    private String projectId;

    @ApiModelProperty(value = "位置集合", name = "locationList", required = false)
    private List<LocationInfoEntity> locationList = new ArrayList<LocationInfoEntity>();
    @ApiModelProperty(value = "位置ID", name = "locationId")
    private String locationId;
    @ApiModelProperty(value = "位置名称", name = "locationName")
    private String locationName;
    // 电箱ID集合
    private String[] deviceIds;
    @ApiModelProperty(value = "项目名称", name = "projectName")
    private String projectName;
    @ApiModelProperty(value = "电箱MAC地址", name = "deviceBoxNum")
    private String deviceBoxNum;
    @ApiModelProperty(value = "电箱密码", name = "deviceBoxPass")
    private String deviceBoxPass;
    @ApiModelProperty(value = "电箱名称", name = "deviceBoxName")
    private String deviceBoxName;
    // 新建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 创建者
    private String createId;
    // 更新者
    private String updateId;
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    @ApiModelProperty(value = "是否在线[0:是,1:否]", name = "online")
    private String online = "1";
    @ApiModelProperty(value = "横坐标", name = "fx")
    private String fx;
    @ApiModelProperty(value = "纵坐标", name = "fy")
    private String fy;
    @ApiModelProperty(value = "二级电箱网关号", name = "secBoxGateway")
    private String secBoxGateway;
    @ApiModelProperty(value = "展位号", name = "standNo")
    private String standNo;
    @ApiModelProperty(value = "是否受控[0:否 1:是]", name = "controlFlag")
    private String controlFlag;

    @ApiModelProperty(value = "电箱容量", name = "boxCapacity")
    private String boxCapacity;

    // 空开信息
    private List<DeviceSwitchInfoEntity> switchList;

    // 告警信息
    private DeviceAlarmInfoLogEntity alarm;

    // 所有告警信息
    private List<DeviceAlarmInfoLogEntity> allAlarm;

    // 控制名称
    private String controlFlagName;

    // 告警ID
    private String alarmLogId;

    // 电箱是否在用 0:空闲 1:在用
    @ApiModelProperty(value = "电箱是否在用[0:空闲,1:在用]", name = "boxStatus")
    private String boxStatus;

    private DeviceBoxStatus deviceBoxStatus;

    @ApiModelProperty(value = "收电箱标记[0/null:未收,1:已收]", name = "placedFlag")
    private String placedFlag;

    // 配置信息
    private DeviceBoxConfigEntity config;

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
     * 设置：设备号(MAC)
     */
    public void setDeviceBoxNum(String deviceBoxNum) {
        this.deviceBoxNum = deviceBoxNum;
    }

    /**
     * 获取：设备号(MAC)
     */
    public String getDeviceBoxNum() {
        return deviceBoxNum;
    }

    /**
     * 设置：电箱密码
     */
    public void setDeviceBoxPass(String deviceBoxPass) {
        this.deviceBoxPass = deviceBoxPass;
    }

    /**
     * 获取：电箱密码
     */
    public String getDeviceBoxPass() {
        return deviceBoxPass;
    }

    /**
     * 设置：电箱名称
     */
    public void setDeviceBoxName(String deviceBoxName) {
        this.deviceBoxName = deviceBoxName;
    }

    /**
     * 获取：电箱名称
     */
    public String getDeviceBoxName() {
        return deviceBoxName;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public List<LocationInfoEntity> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationInfoEntity> locationList) {
        this.locationList = locationList;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String[] getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String[] deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public List<DeviceSwitchInfoEntity> getSwitchList() {
        return switchList;
    }

    public void setSwitchList(List<DeviceSwitchInfoEntity> switchList) {
        this.switchList = switchList;
    }

    public DeviceAlarmInfoLogEntity getAlarm() {
        return alarm;
    }

    public void setAlarm(DeviceAlarmInfoLogEntity alarm) {
        this.alarm = alarm;
    }

    public String getControlFlagName() {
        if ("1".equals(this.controlFlag)) {
            this.controlFlagName = "是";
        } else if ("0".equals(this.controlFlag)) {
            this.controlFlagName = "否";
        } else {
            this.controlFlagName = "";
        }
        return controlFlagName;
    }

    public void setControlFlagName(String controlFlagName) {
        this.controlFlagName = controlFlagName;
    }

    public List<DeviceAlarmInfoLogEntity> getAllAlarm() {
        return allAlarm;
    }

    public void setAllAlarm(List<DeviceAlarmInfoLogEntity> allAlarm) {
        this.allAlarm = allAlarm;
    }

    public String getAlarmLogId() {
        return alarmLogId;
    }

    public void setAlarmLogId(String alarmLogId) {
        this.alarmLogId = alarmLogId;
    }

    public String getBoxStatus() {
        return boxStatus;
    }

    public void setBoxStatus(String boxStatus) {
        this.boxStatus = boxStatus;
    }

    public DeviceBoxConfigEntity getConfig() {
        return config;
    }

    public void setConfig(DeviceBoxConfigEntity config) {
        this.config = config;
    }

    public String getPlacedFlag() {
        return placedFlag;
    }

    public void setPlacedFlag(String placedFlag) {
        this.placedFlag = placedFlag;
    }

    public DeviceBoxStatus getDeviceBoxStatus() {
        return deviceBoxStatus;
    }

    public void setDeviceBoxStatus(DeviceBoxStatus deviceBoxStatus) {
        this.deviceBoxStatus = deviceBoxStatus;
    }

}