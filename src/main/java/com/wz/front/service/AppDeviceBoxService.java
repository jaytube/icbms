package com.wz.front.service;

import com.wz.front.enums.DeviceBoxStatus;
import com.wz.modules.app.entity.SwitchBoxInfo;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.Result;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.lora.entity.GatewayDeviceMap;
import com.wz.modules.lora.enums.LoRaCommand;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/6
 * @Desc: AppDeviceBoxService
 */
public interface AppDeviceBoxService {

    CommonResponse getAllProjectsDeviceBoxInfos(DeviceBoxStatus deviceBoxStatus);

    CommonResponse getProjectDeviceBoxInfos(String projectId);

    CommonResponse getDeviceBoxInfo(String projectId, String deviceBoxMac);

    CommonResponse deleteDevice(String projectId, String deviceSn);

    CommonResponse deleteBatch(List<String> deviceSns);

    CommonResponse addDevice(GatewayDeviceMap map, DeviceBoxInfoEntity entity);

    Result remoteControlSwitches(SwitchBoxInfo boxInfo, LoRaCommand loRaCommand);

}
