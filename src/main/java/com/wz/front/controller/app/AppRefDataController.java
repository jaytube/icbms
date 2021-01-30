package com.wz.front.controller.app;

import com.wz.front.service.AppRefDataService;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.entity.GatewayInfo;
import com.wz.modules.lora.entity.GymMaster;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: AppRefDataController
 */
@CrossOrigin
@RestController
@RequestMapping("/app/refdata")
@Api(tags = "APP 获取辅助数据API")
public class AppRefDataController {

    @Autowired
    private AppRefDataService appRefDataService;

    @GetMapping("/getGyms")
    @LoginRequired
    @ApiOperation(value = "获取场馆列表")
    public CommonResponse<List<GymMaster>> getGyms() {
        return CommonResponse.success(appRefDataService.getGyms());
    }

    @GetMapping("/getGateways")
    @LoginRequired
    @ApiOperation(value = "获取网关列表")
    public CommonResponse<List<GatewayInfo>> getGateways() {
        return CommonResponse.success(appRefDataService.getGateways());
    }

    @GetMapping("/initGatewayInfos")
    @LoginRequired
    @ApiOperation(value = "初始化网关信息")
    public CommonResponse initGatewayInfos() {
        return appRefDataService.initGatewayInfos();
    }

}
