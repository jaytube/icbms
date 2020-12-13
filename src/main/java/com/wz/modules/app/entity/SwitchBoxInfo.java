package com.wz.modules.app.entity;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SwitchBoxInfo {

	@ApiModelProperty(value = "电箱集合", name = "boxList", required = true)
	private List<String> boxList;

	@ApiModelProperty(value = "项目ID", name = "projectId", required = true)
	private String projectId;

	@ApiModelProperty(value = "用户ID", name = "userId", required = true)
	private String userId;

	public List<String> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<String> boxList) {
		this.boxList = boxList;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
