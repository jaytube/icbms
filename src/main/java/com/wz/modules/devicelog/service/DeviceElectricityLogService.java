package com.wz.modules.devicelog.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.devicelog.entity.DeviceElecStatEntity;
import com.wz.modules.devicelog.entity.DeviceElectricityLogEntity;

/**
 * 每日用电日志表; InnoDB free: 395264 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-05-17 00:47:37
 */
public interface DeviceElectricityLogService {

	DeviceElectricityLogEntity queryObject(String id);

	List<DeviceElectricityLogEntity> queryList(Map<String, Object> map);

	List<DeviceElectricityLogEntity> queryListByBean(DeviceElectricityLogEntity entity);

	int queryTotal(Map<String, Object> map);

	int save(DeviceElectricityLogEntity deviceElectricityLog);

	int update(DeviceElectricityLogEntity deviceElectricityLog);

	int delete(String id);

	int deleteBatch(String[] ids);

	List<DeviceElecStatEntity> doStatDeviceElec(String projectId, String startDate);

    public List<DeviceElectricityLogEntity> doStatElectricCnt(String projectId, String statDate);

    public void doDeleteElectricCnt(String projectId, String statDate);
}
