package com.wz.front.service.impl;

import com.wz.front.service.AppDeviceBoxService;
import com.wz.front.service.AppProjectInfoService;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.deviceinfo.dao.DeviceBoxInfoDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.kk.service.KkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2021/1/6
 * @Desc: AppDeviceBoxServiceImpl
 */
@Service
@Slf4j
public class AppDeviceBoxServiceImpl implements AppDeviceBoxService {

    @Autowired
    private DeviceBoxInfoDao deviceBoxInfoDao;

    @Autowired
    private AppProjectInfoService appProjectInfoService;

    @Autowired
    private KkService kkService;

    @Override
    public CommonResponse getAllProjectsDeviceBoxInfos(boolean showBoxOnline) {
        String[] userProjectIds = appProjectInfoService.getUserProjectIds();
        if (userProjectIds == null || userProjectIds.length == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        List<DeviceBoxInfoEntity> deviceBoxInfoList = deviceBoxInfoDao.findDeviceBoxsInfoByProjectIds(userProjectIds);
        if (showBoxOnline) {
            try {
                this.kkService.processDeviceBoxOnline(deviceBoxInfoList);
            } catch (Exception e) {
                log.error("showBoxOnline error: ", e);
            }
        }
        return CommonResponse.success(sort(deviceBoxInfoList));
    }

    private Map<String, List<DeviceBoxInfoEntity>> sort(List<DeviceBoxInfoEntity> deviceBoxInfoList) {
        Map<String, List<DeviceBoxInfoEntity>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(deviceBoxInfoList)) {
            return result;
        }
        deviceBoxInfoList.forEach(deviceBoxInfoEntity -> {
            String projectId = deviceBoxInfoEntity.getProjectId();
            if (result.get(projectId) == null) {
                List<DeviceBoxInfoEntity> deviceBoxInfos = new ArrayList<>();
                deviceBoxInfos.add(deviceBoxInfoEntity);
                result.put(projectId, deviceBoxInfos);
            } else {
                result.get(projectId).add(deviceBoxInfoEntity);
            }
        });
        return result;
    }

    @Override
    public CommonResponse getProjectDeviceBoxInfos(String projectId, boolean showBoxOnline) {
        List<DeviceBoxInfoEntity> deviceBoxInfoList = deviceBoxInfoDao.findDeviceBoxsInfoByProjectId(projectId);
        if (showBoxOnline) {
            try {
                this.kkService.processDeviceBoxOnline(deviceBoxInfoList);
            } catch (Exception e) {
                log.error("showBoxOnline error: ", e);
            }
        }
        return CommonResponse.success(deviceBoxInfoList);
    }

    @Override
    public CommonResponse getDeviceBoxInfo(String projectId, String deviceBoxMac) {
        DeviceBoxInfoEntity device = deviceBoxInfoDao.queryByMac(deviceBoxMac, projectId);
        List<DeviceSwitchInfoEntity> switchList = kkService.getBoxChannelsRealData(deviceBoxMac, projectId);
        device.setSwitchList(switchList);
        return CommonResponse.success(device);
    }
}
