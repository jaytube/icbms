package com.wz.modules.devicelog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.devicelog.entity.DeviceAlarmFlowEntity;

@Mapper
public interface DeviceAlarmFlowDao extends BaseDao<DeviceAlarmFlowEntity> {

	public List<DeviceAlarmFlowEntity> queryFlowPage(@Param("projectId") String projectId,
			@Param("status") String status, @Param("alarmStartDate") String alarmStartDate,
			@Param("alarmEndDate") String alarmEndDate, @Param("deviceBoxMac") String deviceBoxMac,
			@Param("standNo") String standNo, @Param("alarmType") String alarmType, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	public int queryFlowTotal(@Param("projectId") String projectId,
			@Param("status") String status, @Param("alarmStartDate") String alarmStartDate,
			@Param("alarmEndDate") String alarmEndDate, @Param("deviceBoxMac") String deviceBoxMac,
			@Param("standNo") String standNo, @Param("alarmType") String alarmType);
}
