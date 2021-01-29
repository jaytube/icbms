package com.wz.modules.lora.service;

import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.dto.AddDeviceDto;
import com.wz.modules.lora.dto.DeviceInfoDto;
import com.wz.modules.lora.dto.GateWayInfoDto;
import com.wz.modules.lora.dto.TerminalTypeDto;
import com.wz.modules.lora.entity.GatewayInfo;
import com.wz.modules.lora.enums.LoRaCommand;

import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: LoRaCommandService
 */
public interface LoRaCommandService {

    CommonResponse<Map> startRoundRobin(String gatewayIp);

    CommonResponse<Map> stopRoundRobin(String gatewayIp);

    CommonResponse<Map> executeCmd(String gatewayIp, LoRaCommand command, String deviceId);

    CommonResponse<String> getToken(String gatewayIp);

    String getRedisToken(String gatewayIp);

    CommonResponse<String> getDbInstance(String gatewayIp, String code);

    String getDbInstanceFromRedis(String gatewayIp, String code);

    CommonResponse<List<GatewayInfo>> getGatewayList(String gatewayIp);

    CommonResponse<GatewayInfo> getGateWayById(String gatewayIp, String gateWayId);

    CommonResponse<List<TerminalTypeDto>> getTerminalTypes(String gatewayIp);

    CommonResponse<List<TerminalTypeDto>> getTerminalByType(String gatewayIp, String type);

    CommonResponse addDevice(String gatewayIp, AddDeviceDto addDeviceDto);

    CommonResponse<List<DeviceInfoDto>> getDevices(String gatewayIp, String deviceKey);

    CommonResponse deleteDevice(String gatewayIp, String deviceSn);

    CommonResponse<Map> deleteDevices(String gatewayIp, List<Integer> deviceIds);
}
