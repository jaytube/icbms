package com.wz.modules.projectinfo.entity;

import java.io.Serializable;

import com.wz.modules.common.entity.BaseEntity;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;

public class DboxLocLinkEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	// id主键
	private String id;
	//电箱信息
	private DeviceBoxInfoEntity dboxInfo;
	//位置信息
	private LocationInfoEntity locationInfo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DeviceBoxInfoEntity getDboxInfo() {
		return dboxInfo;
	}
	public void setDboxInfo(DeviceBoxInfoEntity dboxInfo) {
		this.dboxInfo = dboxInfo;
	}
	public LocationInfoEntity getLocationInfo() {
		return locationInfo;
	}
	public void setLocationInfo(LocationInfoEntity locationInfo) {
		this.locationInfo = locationInfo;
	}
	
	

}
