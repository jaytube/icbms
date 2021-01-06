package com.wz.front.service;

import com.wz.modules.common.utils.CommonResponse;

/**
 * @Author: Cherry
 * @Date: 2021/1/6
 * @Desc: AppDeviceBoxService
 */
public interface AppDeviceBoxService {

    CommonResponse getAllProjectsDeviceBoxInfos(boolean showBoxOnline);

    CommonResponse getProjectDeviceBoxInfos(String projectId, boolean showBoxOnline);

    CommonResponse getDeviceBoxInfo(String projectId, String deviceBoxMac);

}
