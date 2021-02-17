package com.wz.modules.deviceinfo.service;

import com.wz.modules.common.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface DeviceOperationService {

    void deleteDevices(String[] ids);

    Result addDevice(List<Map<String, String>> result, String userId, String deviceBoxMac, String projectId, HttpServletRequest request, String deviceBoxSn, int gymId, int gatewayId);
}
