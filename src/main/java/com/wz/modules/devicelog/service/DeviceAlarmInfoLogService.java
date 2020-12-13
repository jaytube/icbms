package com.wz.modules.devicelog.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmStatEntity;
import com.wz.modules.kk.entity.PageInfo;

public interface DeviceAlarmInfoLogService {

	DeviceAlarmInfoLogEntity queryObject(String id);

	List<DeviceAlarmInfoLogEntity> queryList(Map<String, Object> map);

	List<DeviceAlarmInfoLogEntity> queryListByBean(DeviceAlarmInfoLogEntity entity);

	int queryTotal(Map<String, Object> map);

	int save(DeviceAlarmInfoLogEntity deviceAlarmInfoLog);

	int update(DeviceAlarmInfoLogEntity deviceAlarmInfoLog);

	int delete(String id);

	int deleteBatch(String[] ids);

	DeviceAlarmInfoLogEntity doFindDeviceAlarmIsExist(String deviceBoxId);

	public PageInfo<DeviceAlarmInfoLogEntity> queryAppPage(String beginTime, String endTime, String page,
			String pageSize, String projectId, String deviceBoxId, String alarmLevel);

	public PageInfo<DeviceAlarmInfoLogEntity> queryDeviceAlarmPage(String beginTime, String endTime, String type,
			String deviceBoxMac, String projectId, String locationId, String page, String pageSize, String alarmLevel);

	public List<DeviceAlarmStatEntity> doStatDeviceAlarm(String projectId, String startDate);

	public List<DeviceAlarmInfoLogEntity> queryDeviceAlarmList(String deviceBoxId);

	public void doSetNewestDeviceAlarm(DeviceBoxInfoEntity config);
}
