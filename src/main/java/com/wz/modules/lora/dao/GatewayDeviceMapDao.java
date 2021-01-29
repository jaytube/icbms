package com.wz.modules.lora.dao;

import com.wz.modules.lora.entity.GatewayDeviceMap;
import org.apache.ibatis.annotations.Mapper;

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

    List<GatewayDeviceMap> findDevicesBySns(List<String> deviceSns);

    GatewayDeviceMap findDevice(String projectId, String deviceSn);

    void save(GatewayDeviceMap map);

    void deleteProject(String projectId);

    void deleteGateway(String gatewayId);

    void deleteBatch(List<String> deviceSns);
}
