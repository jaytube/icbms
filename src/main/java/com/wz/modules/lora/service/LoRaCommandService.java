package com.wz.modules.lora.service;

import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.dto.AddDeviceDto;
import com.wz.modules.lora.dto.DeviceInfoDto;
import com.wz.modules.lora.dto.GateWayInfoDto;
import com.wz.modules.lora.dto.TerminalTypeDto;
import com.wz.modules.lora.enums.LoRaCommand;

import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: LoRaCommandService
 */
public interface LoRaCommandService {

    CommonResponse<Map> startRoundRobin();

    CommonResponse<Map> stopRoundRobin();

    CommonResponse<Map> executeCmd(LoRaCommand command, String deviceId);

    CommonResponse<String> getToken();

    String getRedisToken();

    CommonResponse<String> getDbInstance(String code);

    String getDbInstanceFromRedis(String code);

    CommonResponse<List<GateWayInfoDto>> getGatewayList();

    CommonResponse<GateWayInfoDto> getGateWayById(String gateWayId);

    CommonResponse<List<TerminalTypeDto>> getTerminalTypes();

    CommonResponse<List<TerminalTypeDto>> getTerminalByType(String type);

    CommonResponse addDevice(AddDeviceDto addDeviceDto);

    CommonResponse<List<DeviceInfoDto>> getDevices(String deviceKey);

    CommonResponse deleteDevice(String deviceSn);

    CommonResponse<Map> deleteDevices(List<Integer> deviceIds);
}
