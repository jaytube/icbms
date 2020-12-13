package com.wz.modules.deviceinfo.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;

/**
 * 空开设备基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:57:43
 */
public interface DeviceSwitchInfoService {
	
	DeviceSwitchInfoEntity queryObject(String id);
	
	List<DeviceSwitchInfoEntity> queryList(Map<String, Object> map);

    List<DeviceSwitchInfoEntity> queryListByBean(DeviceSwitchInfoEntity entity);
	
	int queryTotal(Map<String, Object> map);
	
	int save(DeviceSwitchInfoEntity deviceSwitchInfo);
	
	int update(DeviceSwitchInfoEntity deviceSwitchInfo);
	
	int delete(String id);
	
	int deleteBatch(String[] ids);
}
