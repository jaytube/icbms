package com.wz.modules.deviceinfo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;

/**
 * 空开设备基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:57:43
 */
@Mapper
public interface DeviceSwitchInfoDao extends BaseDao<DeviceSwitchInfoEntity> {
	
}
