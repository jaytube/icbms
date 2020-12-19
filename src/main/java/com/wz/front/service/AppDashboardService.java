package com.wz.front.service;

import com.wz.modules.common.utils.CommonResponse;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: AppDashboardService
 */
public interface AppDashboardService {

    CommonResponse getAllBoxInfoCnt();

    CommonResponse getAllAlarmsPage(String startTime, String endTime, String pageSize, String page, String alarmLevel);

    CommonResponse getAllSwitchesPage(String startTime, String endTime, String pageSize, String page);


}
