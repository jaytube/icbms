package com.wz.modules.devicelog.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.devicelog.entity.DeviceAlarmFlowEntity;
import com.wz.modules.kk.entity.PageInfo;

public interface DeviceAlarmFlowService {

	DeviceAlarmFlowEntity queryObject(String id);

	List<DeviceAlarmFlowEntity> queryList(Map<String, Object> map);

	List<DeviceAlarmFlowEntity> queryListByBean(DeviceAlarmFlowEntity entity);

	int queryTotal(Map<String, Object> map);

	int save(DeviceAlarmFlowEntity deviceAlarmFlow);

	int update(DeviceAlarmFlowEntity deviceAlarmFlow);

	int delete(String id);

	int deleteBatch(String[] ids);

	public PageInfo<DeviceAlarmFlowEntity> queryFlowPage(String projectId, String status, String alarmStartDate,
			String alarmEndDate, String deviceBoxMac, String standNo, String alarmType, String page, String pageSize);
}
