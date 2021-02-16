package com.wz.modules.deviceinfo.dao;

import com.wz.modules.deviceinfo.entity.DeviceMacSnEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMacSnMapDao {

    DeviceMacSnEntity findById(String deviceMac);

    void batchInsert(@Param("deviceMacSnEntities")List<DeviceMacSnEntity> deviceMacSnEntities);
}
