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

    private String REST_IP = "http://10.0.1.71";

    @GetMapping("/cmd/{deviceId}/{command}")
    @ResponseBody
    public CommonResponse cmd(@PathVariable("deviceId") String deviceId, @PathVariable("command") LoRaCommand command) throws Exception {
        return loRaCommandService.executeCmd(REST_IP, command, deviceId);
    }

    @GetMapping("/start")
    @ResponseBody
    public CommonResponse start() {
        return loRaCommandService.startRoundRobin(REST_IP);
    }

    @GetMapping("/stop")
    @ResponseBody
    public CommonResponse stop() {
        return loRaCommandService.stopRoundRobin(REST_IP);
    }

    @GetMapping("/getToken")
    @ResponseBody
    public CommonResponse getToken() {
        return loRaCommandService.getToken(REST_IP);
    }

    @GetMapping("/getRedisToken")
    @ResponseBody
    public CommonResponse getRedisToken() {
        return CommonResponse.success(loRaCommandService.getRedisToken(REST_IP));
    }

    @GetMapping("/getDbInstance")
    @ResponseBody
    public CommonResponse getDbInstance(String code) {
        return loRaCommandService.getDbInstance(REST_IP, code);
    }

    @GetMapping("/getDbInstanceFromRedis")
    @ResponseBody
    public CommonResponse getDbInstanceFromRedis(String code) {
        return CommonResponse.success(loRaCommandService.getDbInstanceFromRedis(REST_IP, code));
    }

    @GetMapping("/getGatewayList")
    @ResponseBody
    public CommonResponse getGatewayList() {
        return loRaCommandService.getGatewayList(REST_IP);
    }

    @GetMapping("/getGateWayById")
    @ResponseBody
    public CommonResponse getGateWayById(String id) {
        return loRaCommandService.getGateWayById(REST_IP, id);
    }

    @GetMapping("/getTerminalTypes")
    @ResponseBody
    public CommonResponse getTerminalTypes() {
        return loRaCommandService.getTerminalTypes(REST_IP);
    }

    @GetMapping("/getTerminalByType")
    @ResponseBody
    public CommonResponse getTerminalByType(String type) {
        return loRaCommandService.getTerminalByType(REST_IP, type);
    }

    @GetMapping("/getDevice")
    @ResponseBody
    public CommonResponse getDevice(String deviceSn) {
        return loRaCommandService.getDevices(REST_IP, deviceSn);
    }

    @PostMapping("/addDevice")
    @ResponseBody
    public CommonResponse addDevice(@RequestBody AddDeviceDto addDeviceDto) {
        return loRaCommandService.addDevice(REST_IP, addDeviceDto);
    }

    @GetMapping("/deleteDevice")
    @ResponseBody
    public CommonResponse deleteDevice(String deviceSn) {
        return loRaCommandService.deleteDevice(REST_IP, deviceSn);
    }

    @PostMapping("/deleteDevices")
    @ResponseBody
    public CommonResponse deleteDevices(@RequestBody List<Integer> deviceIds) {
        return loRaCommandService.deleteDevices(REST_IP, deviceIds);
    }

    @GetMapping("/test")
    @ResponseBody
    public CommonResponse test(String URL) {
        return restUtil.doGetNoToken(URL);
    }

}

