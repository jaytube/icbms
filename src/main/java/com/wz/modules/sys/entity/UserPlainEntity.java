package com.wz.modules.sys.entity;


import java.io.Serializable;
import java.util.List;


/**
 * 系统用户表
 * 
 * @author chenshun
 * @email rui.sun.java@gmail.com
 * @date 2017-05-03 09:41:38
 */
public class UserPlainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID列表
	 */
	private List<String> roleIdList;
	/**
	 * 项目ID列表
	 */
	private List<String> projectIdList;

	private String projectIds;

	//id主键
	private String id;
	//用户名
	private String userName;
	//登陆帐户
	private String loginName;
	//状态(0正常 -1禁用)
	private String status;
	//电话
	private String phone;
	//头像
	private String photo;
	//邮箱
	private String email;

	/**
	 * 部门ids 部门数据权限
	 */
	private String baid;
	/**
	 * 机构ids 机构数据权限
	 */
	private String bapid;

	private String baName;

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
	 * 设置：用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：登陆帐户
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * 获取：登陆帐户
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * 设置：状态(0正常 -1禁用)
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0正常 -1禁用)
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：头像
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	/**
	 * 获取：头像
	 */
	public String getPhoto() {
		return photo;
	}
	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getBaid() {
		return baid;
	}

	public void setBaid(String baid) {
		this.baid = baid;
	}

	public String getBapid() {
		return bapid;
	}

	public void setBapid(String bapid) {
		this.bapid = bapid;
	}

	public String getBaName() {
		return baName;
	}

	public void setBaName(String baName) {
		this.baName = baName;
	}

	public List<String> getProjectIdList() {
		return projectIdList;
	}
	public void setProjectIdList(List<String> projectIdList) {
		this.projectIdList = projectIdList;
	}


	public String getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}
}
