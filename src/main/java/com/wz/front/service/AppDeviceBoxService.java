package com.wz.front.service;

import com.wz.front.enums.DeviceBoxStatus;
import com.wz.modules.common.utils.CommonResponse;

/**
 * @Author: Cherry
 * @Date: 2021/1/6
 * @Desc: AppDeviceBoxService
 */
public interface AppDeviceBoxService {

    CommonResponse getAllProjectsDeviceBoxInfos(DeviceBoxStatus deviceBoxStatus);

    CommonResponse getProjectDeviceBoxInfos(String projectId);

    CommonResponse getDeviceBoxInfo(String projectId, String deviceBoxMac);

}
