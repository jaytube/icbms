package com.wz.modules.projectinfo.entity;

import com.wz.modules.common.entity.BaseEntity;
import com.wz.modules.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目基础表; InnoDB free: 401408 kB
 *
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
public class ProjectInfoPlainEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // id主键
    private String id;
    // 项目名称
    private String projectName;
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

    private String createTimeStr;

    // 生效日期
    private String effectiveDate;

    // 失效日期
    private String expireDate;

    // 网关
    private String gatewayAddress;

    // 文件名称
    private String fileName;

    /**
     * 场馆ID
     */
    private int gymId;

    /**
     * 场馆名称
     */
    private String gymName;

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
     * 设置：项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 获取：项目名称
     */
    public String getProjectName() {
        return projectName;
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

    public String getCreateTimeStr() {
        if (this.createTime != null) {
            this.createTimeStr = DateUtils.format(this.createTime, "yyyy-MM-dd");
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getGatewayAddress() {
        return gatewayAddress;
    }

    public void setGatewayAddress(String gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }
}