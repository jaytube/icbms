package com.wz.front.controller.app;

import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.service.AppDashboardService;
import com.wz.front.service.AppProjectInfoService;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.DateUtils;
import com.wz.modules.devicelog.dao.DeviceAlarmInfoLogDao;
import com.wz.modules.devicelog.dao.DeviceElectricityLogDao;
import com.wz.modules.devicelog.entity.DeviceAlarmInfoLogEntity;
import com.wz.modules.devicelog.entity.DeviceAlarmStatEntity;
import com.wz.modules.devicelog.entity.DeviceElecStatEntity;
import com.wz.modules.devicelog.service.DeviceAlarmInfoLogService;
import com.wz.modules.devicelog.service.DeviceElectricityLogService;
import com.wz.modules.kk.entity.PageInfo;
import com.wz.modules.kk.service.KkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
@RestController
@RequestMapping("/app/dashboard")
@Api(tags = "AppDashboard APP端")
public class AppDashboardController {

    private final static Logger logger = LoggerFactory.getLogger(AppDashboardController.class);

    @Autowired
    private AppDashboardService appDashboardService;

    @Autowired
    private AppProjectInfoService appProjectInfoService;

    @Autowired
    private DeviceElectricityLogDao deviceElectricityLogDao;

    @Autowired
    private DeviceAlarmInfoLogService deviceAlarmInfoLogService;

    @Autowired
    private DeviceElectricityLogService deviceElectricityLogService;

    @Autowired
    private DeviceAlarmInfoLogDao deviceAlarmInfoLogDao;

    @Autowired
    @Qualifier("kkService")
    private KkService kkService;

    @GetMapping("/getAllBoxInfoCnt")
    @LoginRequired
    @ApiOperation(value = "该账户下,该场馆所有可见的 设备总数,在线空开,离线空开")
    public CommonResponse getAllBoxInfoCnt() {
        return appDashboardService.getAllBoxInfoCnt();
    }

    @GetMapping("/getAllAlarmsPage")
    @LoginRequired
    @ApiOperation(value = "告警数据列表+告警总数")
    public CommonResponse getAllAlarmsPage(String startTime, String endTime, String pageSize, String page, String alarmLevel) {
        return appDashboardService.getAllAlarmsPage(startTime, endTime, pageSize, page, alarmLevel);
    }

    @GetMapping("/getAllSwitchesPage")
    @LoginRequired
    @ApiOperation(value = "实时数据列表")
    public CommonResponse getAllSwitchesPage(String startTime, String endTime, String pageSize, String page) {
        return appDashboardService.getAllSwitchesPage(startTime, endTime, pageSize, page);
    }

    @GetMapping("/getBoxInfoCnt")
    @LoginRequired
    @ApiOperation(value = "单项目")
    public CommonResponse getBoxInfoCnt(String projectId) {
        ProjectBoxInfoCntDto projectBoxInfoCnt = appProjectInfoService.getProjectBoxInfoCnt(projectId);
        List<DeviceElecStatEntity> deviceElecStatEntities = deviceElectricityLogDao.doStatDeviceElec(projectId, DateUtils.format(new Date(), DATE_PATTERN));
        int alarmTotal = this.deviceAlarmInfoLogDao.queryProjectTotalInt(projectId);
        if (CollectionUtils.isEmpty(deviceElecStatEntities) || deviceElecStatEntities.get(0) == null) {
            projectBoxInfoCnt.setDailyElecTotal("0.00");
        } else {
            projectBoxInfoCnt.setDailyElecTotal(deviceElecStatEntities.get(0).getElec());
        }
        projectBoxInfoCnt.setAlarmTotal(alarmTotal);
        return CommonResponse.success(projectBoxInfoCnt);
    }

    @GetMapping("/getBoxStatusCnt")
    @LoginRequired
    @ApiOperation(value = "单项目")
    public CommonResponse getBoxStatusCnt(String projectId) {
        return CommonResponse.success(kkService.getBoxesRecentStatus(projectId));
    }

    @GetMapping("/getAlarmsStat")
    @LoginRequired
    @ApiOperation(value = "单项目 告警")
    public CommonResponse getAlarmsStat(String projectId, String startDate) {
        List<DeviceAlarmStatEntity> result = deviceAlarmInfoLogService.doStatDeviceAlarm(projectId, startDate);
        return CommonResponse.success(result);
    }

    @GetMapping("/getAlarmsPageByDevice")
    @LoginRequired
    @ApiOperation(value = "获取电箱告警列表-某个电箱")
    public CommonResponse getAlarmsPageByDevice(String projectId, String deviceBoxId, String startTime, String endTime, String pageSize, String page, String alarmLevel) {
        logger.info("================>获取告警数据: startTime:" + startTime + ", endTime:" + endTime);
        PageInfo<DeviceAlarmInfoLogEntity> pageInfo = deviceAlarmInfoLogService.queryAppPage(startTime, endTime, page, pageSize, projectId, deviceBoxId, alarmLevel);
        logger.info("================>获取告警返回数据：pageInfo:" + pageInfo.getDataList().size());
        return CommonResponse.success(pageInfo);
    }

    @GetMapping("/getAlarmsPageByProject")
    @LoginRequired
    @ApiOperation(value = "获取电箱告警列表-某个电箱")
    public CommonResponse getAlarmsPageByProject(String projectId, String startTime, String endTime, String pageSize, String page, String alarmLevel) {
        logger.info("================>获取告警数据: startTime:" + startTime + ", endTime:" + endTime);
        PageInfo<DeviceAlarmInfoLogEntity> pageInfo = deviceAlarmInfoLogService.queryAppPage(startTime, endTime, page, pageSize, projectId, null, alarmLevel);
        logger.info("================>获取告警返回数据：pageInfo:" + pageInfo.getDataList().size());
        return CommonResponse.success(pageInfo);
    }

    @GetMapping("/getElecStat")
    @LoginRequired
    @ApiOperation(value = "单项目 用电分析")
    public CommonResponse getElecStat(String projectId, String startDate) {
        List<DeviceElecStatEntity> result = deviceElectricityLogService.doStatDeviceElec(projectId, startDate);
        return CommonResponse.success(result);
    }

}
