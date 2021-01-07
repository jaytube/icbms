package com.wz.front.controller.app;

import com.wz.front.enums.DeviceBoxStatus;
import com.wz.front.service.AppDeviceBoxService;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.utils.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Cherry
 * @Date: 2021/1/6
 * @Desc: AppDeviceBoxInfoController
 */
@CrossOrigin
@RestController
@RequestMapping("/app/device")
@Api(tags = "App 电箱设备信息接口")
public class AppDeviceBoxInfoController {

    @Autowired
    private AppDeviceBoxService appDeviceBoxService;

    @GetMapping("/getAllProjectsDeviceBoxInfos")
    @LoginRequired
    @ApiOperation(value = "该账户下,所有展会的电箱节点")
    public CommonResponse getAllProjectsDeviceBoxInfos(DeviceBoxStatus deviceBoxStatus) {
        return appDeviceBoxService.getAllProjectsDeviceBoxInfos(deviceBoxStatus);
    }

    @GetMapping("/getProjectDeviceBoxInfos")
    @LoginRequired
    @ApiOperation(value = "单一展会下所有电箱节点")
    public CommonResponse getProjectDeviceBoxInfos(String projectId) {
        return appDeviceBoxService.getProjectDeviceBoxInfos(projectId);
    }

    @GetMapping("/getDeviceBoxInfo")
    @LoginRequired
    @ApiOperation(value = "单一展会下电箱节点详细信息")
    public CommonResponse getDeviceBoxInfo(String projectId, String deviceBoxMac) {
        return appDeviceBoxService.getDeviceBoxInfo(projectId, deviceBoxMac);
    }

}
