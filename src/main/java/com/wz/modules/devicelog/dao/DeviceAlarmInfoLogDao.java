package com.wz.modules.devicelog.dao;

import com.wz.front.dto.ProjectAlarmTotalDto;
import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmStatEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 空开设备警告日志表; InnoDB free: 397312 kB
 *
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-05-16 19:57:54
 */
@Mapper
public interface DeviceAlarmInfoLogDao extends BaseDao<DeviceAlarmInfoLogEntity> {

    public DeviceAlarmInfoLogEntity doFindDeviceAlarmIsExist(@Param("deviceBoxId") String deviceBoxId);

    public DeviceAlarmInfoLogEntity doFindNewestDeviceAlarm(@Param("deviceBoxId") String deviceBoxId);

    public List<DeviceAlarmInfoLogEntity> queryAppList(@Param("beginTime") String beginTime,
                                                       @Param("endTime") String endTime, @Param("type") String type, @Param("deviceBoxMac") String deviceBoxMac,
                                                       @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("projectId") String projectId,
                                                       @Param("locationId") String locationId, @Param("deviceBoxId") String deviceBoxId,
                                                       @Param("alarmLevel") String alarmLevel);

    public List<DeviceAlarmInfoLogEntity> queryAppListByProjectIds(@Param("beginTime") String beginTime,
                                                                   @Param("endTime") String endTime, @Param("type") String type, @Param("deviceBoxMac") String deviceBoxMac,
                                                                   @Param("offset") Integer offset, @Param("limit") Integer limit,
                                                                   @Param("locationId") String locationId, @Param("deviceBoxId") String deviceBoxId,
                                                                   @Param("alarmLevel") String alarmLevel, @Param("ids") String[] ids);

    public List<DeviceAlarmInfoLogEntity> queryAppListByUserId(@Param("userId") String userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    public int queryAppTotal(@Param("beginTime") String beginTime, @Param("endTime") String endTime,
                             @Param("type") String type, @Param("deviceBoxMac") String deviceBoxMac,
                             @Param("projectId") String projectId, @Param("locationId") String locationId,
                             @Param("deviceBoxId") String deviceBoxId, @Param("alarmLevel") String alarmLevel);

    public List<ProjectAlarmTotalDto> queryProjectsTotal(String[] ids);

    public int queryProjectTotalInt(@Param("projectId") String projectId);

    public int queryProjectsTotalInt(String[] ids);

    public List<DeviceAlarmStatEntity> doStatDeviceAlarm(@Param("projectId") String projectId,
                                                         @Param("startDate") String startDate);

    public List<DeviceAlarmInfoLogEntity> queryDeviceAlarmList(@Param("deviceBoxId") String deviceBoxId);

    List<DeviceAlarmInfoLogEntity> getLatestAlarmOfDeviceByProjectId(@Param("projectId") String projectId);
}