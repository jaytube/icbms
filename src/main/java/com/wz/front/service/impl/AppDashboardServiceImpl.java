package com.wz.front.service.impl;

import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.service.AppDashboardService;
import com.wz.front.service.ClientProjectInfoService;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.devicelog.dao.DeviceAlarmInfoLogDao;
import com.wz.modules.devicelog.dao.DeviceSwitchInfoLogDao;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.devicelog.entity.DeviceSwitchInfoLogEntity;
import com.wz.modules.kk.entity.PageInfo;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.sys.service.CodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: AppDashboardServiceImpl TODO 将QUERY合并
 */
@Service
public class AppDashboardServiceImpl implements AppDashboardService {

    @Autowired
    private ClientProjectInfoService clientProjectInfoService;

    @Autowired
    private DeviceBoxInfoService deviceBoxInfoService;

    @Autowired
    private DeviceAlarmInfoLogDao deviceAlarmInfoLogDao;

    @Autowired
    private DeviceSwitchInfoLogDao deviceSwitchInfoLogDao;

    @Autowired
    private CodeService codeService;

    @ResponseBody
    public CommonResponse getAllBoxInfoCnt() {
        List<ProjectInfoEntity> userProjects = clientProjectInfoService.getUserProjects();
        if (CollectionUtils.isEmpty(userProjects)) {
            return CommonResponse.success(new HashMap<>());
        }
        int boxTotal = 0;
        int switchOnlineTotal = 0;
        int switchLeaveTotal = 0;

        int size = userProjects.size();
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = userProjects.get(i).getId();
        }
        List<ProjectBoxInfoCntDto> projectBoxInfoCntByIds = clientProjectInfoService.getProjectBoxInfoCntByIds(ids);
        for (ProjectBoxInfoCntDto dto : projectBoxInfoCntByIds) {
            boxTotal += dto.getBoxTotal();
            switchOnlineTotal += dto.getSwitchOnlineTotal();
            switchLeaveTotal += dto.getSwitchLeaveTotal();
        }
        ProjectBoxInfoCntDto all = new ProjectBoxInfoCntDto(boxTotal, switchOnlineTotal, switchLeaveTotal);
        return CommonResponse.success(all);
    }

    @Override
    public CommonResponse getAllAlarmsPage(String startTime, String endTime, String pageSize, String page, String alarmLevel) {
        List<ProjectInfoEntity> userProjects = clientProjectInfoService.getUserProjects();
        PageInfo<DeviceAlarmInfoLogEntity> pageInfo = new PageInfo<DeviceAlarmInfoLogEntity>();
        if (CollectionUtils.isEmpty(userProjects)) {
            return CommonResponse.success(pageInfo);
        }
        int totalCount = 0;
        int size = userProjects.size();
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = userProjects.get(i).getId();
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "5";
        }
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        List<DeviceAlarmInfoLogEntity> allLogEntityList = getAllProjectAlarmsPage(startTime, endTime, pageSize, page, ids, alarmLevel);
        totalCount += this.deviceAlarmInfoLogDao.queryProjectsTotalInt(ids);
        int totalPage = totalCount / Integer.parseInt(pageSize);
        if (totalCount % Integer.parseInt(pageSize) != 0) {
            totalPage = totalPage + 1;
        }
        pageInfo.setTotal(String.valueOf(totalCount));
        pageInfo.setTotalPage(String.valueOf(totalPage));
        pageInfo.setPage(page);
        pageInfo.setDataList(allLogEntityList);
        return CommonResponse.success(pageInfo);
    }

    @Override
    public CommonResponse getAllSwitchesPage(String startTime, String endTime, String pageSize, String page) {
        List<ProjectInfoEntity> userProjects = clientProjectInfoService.getUserProjects();
        PageInfo<DeviceSwitchInfoLogEntity> pageInfo = new PageInfo<DeviceSwitchInfoLogEntity>();
        if (CollectionUtils.isEmpty(userProjects)) {
            return CommonResponse.success(pageInfo);
        }
        int totalCount = 0;
        int size = userProjects.size();
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = userProjects.get(i).getId();
        }
        List<DeviceSwitchInfoLogEntity> allLogEntityList = getAllProjectSwitchesPage(ids, startTime, endTime, pageSize, page);
        totalCount += this.deviceSwitchInfoLogDao.queryAppTotalByIds(ids, startTime, endTime);
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "5";
        }
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        int totalPage = totalCount / Integer.parseInt(pageSize);
        if (totalCount % Integer.parseInt(pageSize) != 0) {
            totalPage = totalPage + 1;
        }
        pageInfo.setTotal(String.valueOf(totalCount));
        pageInfo.setTotalPage(String.valueOf(totalPage));
        pageInfo.setPage(page);
        pageInfo.setDataList(allLogEntityList);
        return CommonResponse.success(pageInfo);
    }

    private List<DeviceAlarmInfoLogEntity> getProjectAlarmsPage(String startTime, String endTime, String pageSize, String page, String projectId, String alarmLevel) {
        String deviceBoxId = null;
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "5";
        }
        int curPage = Integer.parseInt(page);

        Integer offset = (curPage - 1) * Integer.parseInt(pageSize);
        List<DeviceAlarmInfoLogEntity> result = this.deviceAlarmInfoLogDao.queryAppList(startTime, endTime, null, null,
                offset, Integer.parseInt(pageSize), projectId, null, deviceBoxId, alarmLevel);
        if (null != result && 0 < result.size()) {
            Map<String, String> levelMap = this.codeService.queryChildsMapByMark("alarm_level");
            for (DeviceAlarmInfoLogEntity alarm : result) {
                if (levelMap.containsKey(alarm.getAlarmLevel())) {
                    alarm.setAlarmLevelName(levelMap.get(alarm.getAlarmLevel()));
                }
            }
        }
        return result;
    }

    private List<DeviceAlarmInfoLogEntity> getAllProjectAlarmsPage(String startTime, String endTime, String pageSize, String page, String[] projectIds, String alarmLevel) {
        String deviceBoxId = null;
        int curPage = Integer.parseInt(page);
        Integer offset = (curPage - 1) * Integer.parseInt(pageSize);
        List<DeviceAlarmInfoLogEntity> result = this.deviceAlarmInfoLogDao.queryAppListByProjectIds(startTime, endTime, null, null,
                offset, Integer.parseInt(pageSize), null, deviceBoxId, alarmLevel, projectIds);
        if (null != result && 0 < result.size()) {
            Map<String, String> levelMap = this.codeService.queryChildsMapByMark("alarm_level");
            for (DeviceAlarmInfoLogEntity alarm : result) {
                if (levelMap.containsKey(alarm.getAlarmLevel())) {
                    alarm.setAlarmLevelName(levelMap.get(alarm.getAlarmLevel()));
                }
            }
        }
        return result;
    }

    private List<DeviceSwitchInfoLogEntity> getProjectSwitchesPage(String projectId, String startTime, String endTime, String pageSize, String page) {
        Map<String, String> boxMap = new HashMap<String, String>();
        List<DeviceBoxInfoEntity> boxList = deviceBoxInfoService.findDeviceBoxsInfoByProjectId(projectId);
        for (DeviceBoxInfoEntity box : boxList) {
            boxMap.put(box.getId(), box.getDeviceBoxName());
        }

        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "5";
        }
        Integer offset = Integer.parseInt(page) * Integer.parseInt(pageSize);
        List<DeviceSwitchInfoLogEntity> result = this.deviceSwitchInfoLogDao.queryAppList(projectId, startTime, endTime,
                offset, Integer.parseInt(pageSize));
        for (DeviceSwitchInfoLogEntity entity : result) {
            entity.setDeviceBoxName(boxMap.get(entity.getDeviceBoxId()));
        }
        return result;
    }

    private List<DeviceSwitchInfoLogEntity> getAllProjectSwitchesPage(String[] projectIds, String startTime, String endTime, String pageSize, String page) {
        Map<String, String> boxMap = new HashMap<String, String>();
        List<DeviceBoxInfoEntity> boxList = deviceBoxInfoService.findDeviceBoxsInfoByProjectIds(projectIds);
        for (DeviceBoxInfoEntity box : boxList) {
            boxMap.put(box.getId(), box.getDeviceBoxName());
        }

        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "5";
        }
        Integer offset = Integer.parseInt(page) * Integer.parseInt(pageSize);
        List<DeviceSwitchInfoLogEntity> result = this.deviceSwitchInfoLogDao.queryAppListByIds(projectIds, startTime, endTime,
                offset, Integer.parseInt(pageSize));
        for (DeviceSwitchInfoLogEntity entity : result) {
            entity.setDeviceBoxName(boxMap.get(entity.getDeviceBoxId()));
        }
        return result;
    }

}
