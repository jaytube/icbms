package com.wz.modules.kk.entity;

import java.io.Serializable;

/**
 * 电箱配置表; InnoDB free: 238592 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-08-19 23:11:35
 */
public class DeviceBoxConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	// id主键
	private String id;
	// 网关地址
	private String gatewayAddress;
	// 网关MAC地址
	private String gatewayMac;
	// 电箱地址
	private String deviceBoxAddress;
	// 电箱MAC地址
	private String deviceBoxMac;
	// 网关状态
	private String gatewayStatus;
	// 电箱状态
	private String deviceBoxStatus;

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
	 * 设置：网关地址
	 */
	public void setGatewayAddress(String gatewayAddress) {
		this.gatewayAddress = gatewayAddress;
	}

	/**
	 * 获取：网关地址
	 */
	public String getGatewayAddress() {
		return gatewayAddress;
	}

	/**
	 * 设置：网关MAC地址
	 */
	public void setGatewayMac(String gatewayMac) {
		this.gatewayMac = gatewayMac;
	}

	/**
	 * 获取：网关MAC地址
	 */
	public String getGatewayMac() {
		return gatewayMac;
	}

	/**
	 * 设置：电箱地址
	 */
	public void setDeviceBoxAddress(String deviceBoxAddress) {
		this.deviceBoxAddress = deviceBoxAddress;
	}

	/**
	 * 获取：电箱地址
	 */
	public String getDeviceBoxAddress() {
		return deviceBoxAddress;
	}

	/**
	 * 设置：电箱MAC地址
	 */
	public void setDeviceBoxMac(String deviceBoxMac) {
		this.deviceBoxMac = deviceBoxMac;
	}

	/**
	 * 获取：电箱MAC地址
	 */
	public String getDeviceBoxMac() {
		return deviceBoxMac;
	}

	public String getGatewayStatus() {
		return gatewayStatus;
	}

	public void setGatewayStatus(String gatewayStatus) {
		this.gatewayStatus = gatewayStatus;
	}

	public String getDeviceBoxStatus() {
		return deviceBoxStatus;
	}

	public void setDeviceBoxStatus(String deviceBoxStatus) {
		this.deviceBoxStatus = deviceBoxStatus;
	}

}