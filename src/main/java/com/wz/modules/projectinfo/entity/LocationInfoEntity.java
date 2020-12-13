package com.wz.modules.projectinfo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wz.modules.common.entity.BaseEntity;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;

/**
 * 位置基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
public class LocationInfoEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// id主键
	private String id;
	// 项目号
	private String projectId;
	// 父菜单id
	private String parentId;
	// 位置名称
	private String name;
	// 位置图标
	private String icon;
	// 排序
	private Integer sort;
	// 状态（0显示，-1隐藏)
	private String status;
	// 备注
	private String remark;
	// 位置类型
	private String type;
	// 父位置名字
	private String parentName;
	// 子类位置
	private List list;
	// 是否展开
	private String open;;
	// 对应的电箱
	private List<DeviceBoxInfoEntity> dboxList = new ArrayList<DeviceBoxInfoEntity>();
	// 是否含有紧急告警状态
	private String hasAlarm = "0";

	// 根路径
	private String root;

	// 文件名称
	private String fileName;

	// 在线状态
	private String online;

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public List<DeviceBoxInfoEntity> getDboxList() {
		return dboxList;
	}

	public void setDboxList(List<DeviceBoxInfoEntity> dboxList) {
		this.dboxList = dboxList;
	}

	public String getHasAlarm() {
		return hasAlarm;
	}

	public void setHasAlarm(String hasAlarm) {
		this.hasAlarm = hasAlarm;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

}