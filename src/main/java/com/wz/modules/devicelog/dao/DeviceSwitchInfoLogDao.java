package com.wz.modules.devicelog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.devicelog.entity.DeviceSwitchInfoLogEntity;

/**
 * 空开设备日志表; InnoDB free: 398336 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-05-15 21:44:00
 */
@Mapper
public interface DeviceSwitchInfoLogDao extends BaseDao<DeviceSwitchInfoLogEntity> {
	public List<DeviceSwitchInfoLogEntity> queryAppList(@Param("projectId") String projectId,
			@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	public List<DeviceSwitchInfoLogEntity> queryAppListByIds(@Param("projectIds") String[] projectIds,
														@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("offset") Integer offset,
														@Param("limit") Integer limit);

	public int queryAppTotal(@Param("projectId") String projectId, @Param("beginTime") String beginTime,
			@Param("endTime") String endTime);

	public int queryAppTotalByIds(@Param("projectIds") String[] projectId, @Param("beginTime") String beginTime,
							 @Param("endTime") String endTime);

	int insertDeviceSwitchHis(@Param("synDate") String synDate);

	int deleteDeviceSwitchLog(@Param("synDate") String synDate);
	
	List<DeviceSwitchInfoLogEntity> getDeviceBoxHistory(@Param("deviceBoxId") String deviceBoxId,
			@Param("projectId") String projectId, @Param("hours") String hours);
}