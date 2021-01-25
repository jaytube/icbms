package com.wz.modules.lora.controller;

import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.dto.AddDeviceDto;
import com.wz.modules.lora.enums.LoRaCommand;
import com.wz.modules.lora.service.LoRaCommandService;
import com.wz.modules.lora.utils.RestUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: LoRaCommandController
 */
@RestController
@RequestMapping("/lora")
@Api(tags = "测试LoRa接口")
public class LoRaCommandController {

    @Autowired
    private LoRaCommandService loRaCommandService;

    @Autowired
    private RestUtil restUtil;

    @GetMapping("/cmd/{deviceId}/{command}")
    @ResponseBody
    public CommonResponse cmd(@PathVariable("deviceId") String deviceId, @PathVariable("command") LoRaCommand command) throws Exception {
        return loRaCommandService.executeCmd(command, deviceId);
    }

    @GetMapping("/start")
    @ResponseBody
    public CommonResponse start() {
        return loRaCommandService.startRoundRobin();
    }

    @GetMapping("/stop")
    @ResponseBody
    public CommonResponse stop() {
        return loRaCommandService.stopRoundRobin();
    }

    @GetMapping("/getToken")
    @ResponseBody
    public CommonResponse getToken() {
        return loRaCommandService.getToken();
    }

    @GetMapping("/getRedisToken")
    @ResponseBody
    public CommonResponse getRedisToken() {
        return CommonResponse.success(loRaCommandService.getRedisToken());
    }

    @GetMapping("/getDbInstance")
    @ResponseBody
    public CommonResponse getDbInstance(String code) {
        return loRaCommandService.getDbInstance(code);
    }

    @GetMapping("/getDbInstanceFromRedis")
    @ResponseBody
    public CommonResponse getDbInstanceFromRedis(String code) {
        return CommonResponse.success(loRaCommandService.getDbInstanceFromRedis(code));
    }

    @GetMapping("/getGatewayList")
    @ResponseBody
    public CommonResponse getGatewayList() {
        return loRaCommandService.getGatewayList();
    }

    @GetMapping("/getGateWayById")
    @ResponseBody
    public CommonResponse getGateWayById(String id) {
        return loRaCommandService.getGateWayById(id);
    }

    @GetMapping("/getTerminalTypes")
    @ResponseBody
    public CommonResponse getTerminalTypes() {
        return loRaCommandService.getTerminalTypes();
    }

    @GetMapping("/getTerminalByType")
    @ResponseBody
    public CommonResponse getTerminalByType(String type) {
        return loRaCommandService.getTerminalByType(type);
    }

    @GetMapping("/getDevice")
    @ResponseBody
    public CommonResponse getDevice(String deviceSn) {
        return loRaCommandService.getDevices(deviceSn);
    }

    @PostMapping("/addDevice")
    @ResponseBody
    public CommonResponse addDevice(@RequestBody AddDeviceDto addDeviceDto) {
        return loRaCommandService.addDevice(addDeviceDto);
    }

    @GetMapping("/deleteDevice")
    @ResponseBody
    public CommonResponse deleteDevice(String deviceSn) {
        return loRaCommandService.deleteDevice(deviceSn);
    }

    @PostMapping("/deleteDevices")
    @ResponseBody
    public CommonResponse deleteDevices(@RequestBody List<Integer> deviceIds) {
        return loRaCommandService.deleteDevices(deviceIds);
    }

    @GetMapping("/test")
    @ResponseBody
    public CommonResponse test(String URL) {
        return restUtil.doGetNoToken(URL);
    }

}
