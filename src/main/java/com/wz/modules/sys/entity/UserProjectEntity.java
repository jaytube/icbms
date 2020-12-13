package com.wz.modules.sys.entity;

import java.io.Serializable;


/**
 * 用户角色关系表
 * 
 * @author chenshun
 * @email rui.sun.java@gmail.com
 * @date 2017-05-03 10:07:59
 */
public class UserProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户id
	private String userId;
	//角色id
	private String projectId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
