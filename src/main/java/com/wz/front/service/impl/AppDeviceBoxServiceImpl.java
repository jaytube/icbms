package com.wz.front.service.impl;

import com.wz.front.enums.DeviceBoxStatus;
import com.wz.front.service.AppDeviceBoxService;
import com.wz.front.service.AppProjectInfoService;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.common.utils.StringUtils;
import com.wz.modules.deviceinfo.dao.DeviceBoxInfoDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.devicelog.dao.DeviceAlarmInfoLogDao;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.kk.service.KkService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DeviceAlarmInfoLogDao deviceAlarmInfoLogDao;

    @Override
    public CommonResponse getAllProjectsDeviceBoxInfos(DeviceBoxStatus deviceBoxStatus) {
        String[] userProjectIds = appProjectInfoService.getUserProjectIds();
        if (userProjectIds == null || userProjectIds.length == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        List<DeviceBoxInfoEntity> deviceBoxInfoList = deviceBoxInfoDao.findDeviceBoxsInfoByProjectIds(userProjectIds);
        Map<String, List<DeviceBoxInfoEntity>> deviceBoxInfoMap = sort(deviceBoxInfoList);
        Map<String, List<DeviceBoxInfoEntity>> filterMap = new HashMap<>();
        Map<String, String> redisTerminalStatus = redisUtil.hgetAll(0, "TERMINAL_STATUS");
        for (Map.Entry<String, List<DeviceBoxInfoEntity>> entry : deviceBoxInfoMap.entrySet()) {
            String projectId = entry.getKey();
            List<DeviceBoxInfoEntity> boxList = entry.getValue();
            if (CollectionUtils.isEmpty(boxList)) {
                filterMap.put(projectId, new ArrayList<>());
                continue;
            }
            List<DeviceBoxInfoEntity> allList = loadBoxesRecentStatus(projectId, boxList, redisTerminalStatus);
            List<DeviceBoxInfoEntity> boxInfoEntities = allList.stream().filter(box -> box.getDeviceBoxStatus() == deviceBoxStatus).collect(Collectors.toList());
            filterMap.put(projectId, boxInfoEntities);
        }
        return CommonResponse.success(filterMap);
    }


    @Override
    public CommonResponse getProjectDeviceBoxInfos(String projectId) {
        Map<String, String> redisTerminalStatus = redisUtil.hgetAll(0, "TERMINAL_STATUS");
        List<DeviceBoxInfoEntity> deviceBoxInfoEntityList = deviceBoxInfoDao.findDeviceBoxsInfoByProjectId(projectId);
        List<DeviceBoxInfoEntity> deviceBoxInfoList = loadBoxesRecentStatus(projectId, deviceBoxInfoEntityList, redisTerminalStatus);
        return CommonResponse.success(deviceBoxInfoList);
    }

    @Override
    public CommonResponse getDeviceBoxInfo(String projectId, String deviceBoxMac) {
        DeviceBoxInfoEntity device = deviceBoxInfoDao.queryByMac(deviceBoxMac, projectId);
        List<DeviceSwitchInfoEntity> switchList = kkService.getBoxChannelsRealData(deviceBoxMac, projectId);
        device.setSwitchList(switchList);
        return CommonResponse.success(device);
    }

    private List<DeviceBoxInfoEntity> loadBoxesRecentStatus(String projectId, List<DeviceBoxInfoEntity> boxList, Map<String, String> redisTerminalStatus) {
        List<DeviceAlarmInfoLogEntity> latestAlarmList = deviceAlarmInfoLogDao.getLatestAlarmOfDeviceByProjectId(projectId);
        Map<String, DeviceBoxInfoEntity> boxMap = new HashMap<>();
        for (DeviceBoxInfoEntity b : boxList) {
            boxMap.put(Integer.parseInt(b.getDeviceBoxNum().substring(10)) + "", b);
        }
        Map<String, DeviceAlarmInfoLogEntity> alarmInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(latestAlarmList)) {
            latestAlarmList.forEach(latestAlarm -> {
                String key = Integer.toString(Integer.parseInt(latestAlarm.getDeviceBoxMac().substring(10)));
                alarmInfoMap.put(key, latestAlarm);
            });
        }
        Map<String, String> tmpResult = new HashMap<>();
        tmpResult.putAll(redisTerminalStatus);
        for (Map.Entry<String, DeviceBoxInfoEntity> boxEntry : boxMap.entrySet()) {
            String key = boxEntry.getKey();
            DeviceBoxInfoEntity boxInfoEntity = boxEntry.getValue();
            String jsonStr = tmpResult.get(key);
            if (StringUtils.isEmpty(jsonStr)) {
                boxInfoEntity.setDeviceBoxStatus(DeviceBoxStatus.OFFLINE);
                continue;
            }
            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            if ("0".equals(jsonObj.getString("status"))) {
                DeviceAlarmInfoLogEntity alarm = alarmInfoMap.getOrDefault(key, null);
                if (alarm != null) {
                    if (alarm.getAlarmLevel().equals("4")) {
                        boxInfoEntity.setDeviceBoxStatus(DeviceBoxStatus.ERROR);
                    } else {
                        boxInfoEntity.setDeviceBoxStatus(DeviceBoxStatus.WARN);
                    }
                } else {
                    boxInfoEntity.setDeviceBoxStatus(DeviceBoxStatus.NORMAL);
                }
            } else {
                boxInfoEntity.setDeviceBoxStatus(DeviceBoxStatus.OFFLINE);
            }
        }
        return boxList;
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
}
