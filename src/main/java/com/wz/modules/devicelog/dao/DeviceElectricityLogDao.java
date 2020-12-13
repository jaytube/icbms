package com.wz.modules.devicelog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.devicelog.entity.DeviceElecStatEntity;
import com.wz.modules.devicelog.entity.DeviceElectricityLogEntity;

/**
 * 每日用电日志表; InnoDB free: 395264 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-05-17 00:47:37
 */
@Mapper
public interface DeviceElectricityLogDao extends BaseDao<DeviceElectricityLogEntity> {

	public List<DeviceElecStatEntity> doStatDeviceElec(@Param("projectId") String projectId,
			@Param("startDate") String startDate);

    public List<DeviceElectricityLogEntity> doStatElectricCnt(@Param("projectId") String projectId,
            @Param("statDate") String statDate);

    public void doDeleteElectricCnt(@Param("projectId") String projectId, @Param("statDate") String statDate);

}
