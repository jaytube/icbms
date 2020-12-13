package com.wz.modules.kk.entity;

import java.io.Serializable;

public class DeviceAlarm implements Serializable, Comparable<DeviceAlarm>{
	
	private static final long serialVersionUID = 1L;
	private String node;//线路名称
	private String type;//告警类型(电器状态);告警(故障报警)
	private String time;
	private String info;

	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public int compareTo(DeviceAlarm o) {
		return o.getTime().compareTo(this.getTime());
	}
	
}
