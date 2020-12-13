package com.wz.modules.projectinfo.entity;

import java.io.Serializable;



/**
 * 项目角色关系表
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2019-10-15 22:59:42
 */
public class ProjectRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//项目id
	private String projectId;
	//角色id
	private String roleId;

	/**
	 * 设置：项目id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：项目id
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * 设置：角色id
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：角色id
	 */
	public String getRoleId() {
		return roleId;
	}
}
