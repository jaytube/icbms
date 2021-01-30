package com.wz.modules.lora.dao;

import com.wz.modules.lora.entity.GatewayDeviceMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GatewayDeviceMapDao
 */
@Mapper
public interface GatewayDeviceMapDao {

    List<String> findDeviceSns(int gymId, String projectId, String gatewayId);

    List<GatewayDeviceMap> findDevices(int gymId, String projectId);

    List<GatewayDeviceMap> findDevicesBySns(List<String> deviceInfoIds);

    GatewayDeviceMap findDevice(@Param("projectId") String projectId, @Param("deviceSn") String deviceSn);

    void save(GatewayDeviceMap map);

    void deleteProject(@Param("projectId") String projectId);

    void deleteGateway(@Param("gatewayId") String gatewayId);

    void deleteBatch(List<String> deviceSns);
}
