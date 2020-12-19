package com.wz.front.controller.app;

import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.service.AppDashboardService;
import com.wz.front.service.ClientProjectInfoService;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.DateUtils;
import com.wz.modules.devicelog.dao.DeviceElectricityLogDao;
import com.wz.modules.devicelog.entity.DeviceAlarmStatEntity;
import com.wz.modules.devicelog.entity.DeviceElecStatEntity;
import com.wz.modules.devicelog.service.DeviceAlarmInfoLogService;
import com.wz.modules.devicelog.service.DeviceElectricityLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.wz.modules.common.utils.DateUtils.DATE_PATTERN;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: AppDashboard
 */
@RestController
@RequestMapping("/app/dashboard")
@Api(tags = "AppDashboard APP端")
public class AppDashboardController {

    @Autowired
    private AppDashboardService appDashboardService;

    @Autowired
    private ClientProjectInfoService clientProjectInfoService;

    @Autowired
    private DeviceElectricityLogDao deviceElectricityLogDao;

    @Autowired
    private DeviceAlarmInfoLogService deviceAlarmInfoLogService;

    @Autowired
    private DeviceElectricityLogService deviceElectricityLogService;

    @GetMapping("/getAllBoxInfoCnt")
    @ApiOperation(value = "该账户下,该场馆所有可见的 设备总数,在线空开,离线空开")
    public CommonResponse getAllBoxInfoCnt() {
        return appDashboardService.getAllBoxInfoCnt();
    }

    @GetMapping("/getAllAlarmsPage")
    @ApiOperation(value = "实时数据列表")
    public CommonResponse getAllAlarmsPage(String startTime, String endTime, String pageSize, String page, String alarmLevel) {
        return appDashboardService.getAllAlarmsPage(startTime, endTime, pageSize, page, alarmLevel);
    }

    @GetMapping("/getAllSwitchesPage")
    @ApiOperation(value = "告警数据列表+告警总数")
    public CommonResponse getAllSwitchesPage(String startTime, String endTime, String pageSize, String page) {
        return appDashboardService.getAllSwitchesPage(startTime, endTime, pageSize, page);
    }

    @GetMapping("/getBoxInfoCnt")
    @ApiOperation(value = "单项目")
    public CommonResponse getBoxInfoCnt(String projectId) {
        ProjectBoxInfoCntDto projectBoxInfoCnt = clientProjectInfoService.getProjectBoxInfoCnt(projectId);
        List<DeviceElecStatEntity> deviceElecStatEntities = deviceElectricityLogDao.doStatDeviceElec(projectId, DateUtils.format(new Date(), DATE_PATTERN));
        if (CollectionUtils.isEmpty(deviceElecStatEntities) || deviceElecStatEntities.get(0) == null) {
            projectBoxInfoCnt.setDailyElecTotal("0.00");
        } else {
            projectBoxInfoCnt.setDailyElecTotal(deviceElecStatEntities.get(0).getElec());
        }
        return CommonResponse.success(projectBoxInfoCnt);
    }

    @GetMapping("/getAlarmsStat")
    @ApiOperation(value = "单项目 告警")
    public CommonResponse getAlarmsStat(String projectId, String startDate) {
        List<DeviceAlarmStatEntity> result = deviceAlarmInfoLogService.doStatDeviceAlarm(projectId, startDate);
        return CommonResponse.success(result);
    }

    @GetMapping("/getElecStat")
    @ApiOperation(value = "单项目 用电分析")
    public CommonResponse getElecStat(String projectId, String startDate) {
        List<DeviceElecStatEntity> result = deviceElectricityLogService.doStatDeviceElec(projectId, startDate);
        return CommonResponse.success(result);
    }

}
