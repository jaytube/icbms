package com.wz.modules.deviceinfo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.devicelog.entity.DeviceSwitchInfoLogEntity;
import com.wz.modules.projectinfo.entity.DboxLocLinkEntity;

/**
 * 电箱设备基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:57:43
 */
@Mapper
public interface DeviceBoxInfoDao extends BaseDao<DeviceBoxInfoEntity> {

	List<DeviceBoxInfoEntity> findDeviceBoxsInfoByLId(@Param("projectId") String projectId,
			@Param("locationId") String locationId);

	List<DeviceBoxInfoEntity> findDeviceBoxsInfoByProjectId(@Param("projectId") String projectId);

	List<DeviceBoxInfoEntity> findDeviceBoxsInfoByProjectIds(String[] ids);

	List<DeviceBoxInfoEntity> findDeviceBoxsInfoByUserId(@Param("userId") String userId);

	void updateDeviceBoxOnline(String deviceMac, String online);

	// 保存电箱和位置
	void saveBoxLocLink(DboxLocLinkEntity dboxLocation);

	// 根据电箱ID获取配置的电箱位置关联关系
	List<DboxLocLinkEntity> queryLinkListByBoxId(Map<String, String> paramsMap);

	// 更新电箱和位置
	void updateBoxLocLink(@Param("dboxLocation") DboxLocLinkEntity dboxLocation);

	// 批量删除电箱
	void deleteBoxLocLinkBatch(String[] ids);

	List<String> findSingleBoxLocation(@Param("deviceBoxIds") String[] deviceBoxIds);

	// 根据Mac查找电箱
	List<DeviceBoxInfoEntity> queryListByMac(@Param("deviceMac") String deviceMac);

	DeviceBoxInfoEntity queryByMac(@Param("deviceMac") String deviceMac, @Param("projectId") String projectId);

	int xyReset(DeviceBoxInfoEntity deviceBoxInfo);

	DeviceBoxInfoEntity queryProjectMac(@Param("projectId") String projectId, @Param("deviceMac") String deviceMac);

	DeviceBoxInfoEntity queryByBoxNum(@Param("deviceBoxNum")String deviceBoxNum);

	DeviceBoxInfoEntity queryProjectDeviceBox(@Param("projectId") String projectId,
			@Param("deviceMac") String deviceMac);

	List<DeviceBoxInfoEntity> findDeviceBoxsInfoLike(@Param("projectId") String projectId,
			@Param("deviceMac") String deviceMac);

	// 删除项目关联电箱
	int deleteProjectDeviceBox(@Param("projectId") String projectId);

	/**
	 * @param locationIds
	 *            位置ids
	 */
	void deleteBoxInfoByLocation(@Param("locationIds") String[] locationIds);

	int updateBoxPlacedFlag(@Param("projectId") String projectId, @Param("deviceMac") String deviceMac,
			@Param("placedFlag") String placedFlag);

}