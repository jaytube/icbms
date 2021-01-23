package com.wz.modules.lora.service.impl;

import com.alibaba.fastjson.JSON;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.dto.AddDeviceDto;
import com.wz.modules.lora.dto.DeviceInfoDto;
import com.wz.modules.lora.dto.GateWayInfoDto;
import com.wz.modules.lora.dto.TerminalTypeDto;
import com.wz.modules.lora.enums.LoRaCommand;
import com.wz.modules.lora.service.LoRaCommandService;
import com.wz.modules.lora.utils.Base64Util;
import com.wz.modules.lora.utils.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: LoRaCommandServiceImpl
 */
@Service
@Slf4j
public class LoRaCommandServiceImpl implements LoRaCommandService {

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String START_ROUND_ROBIN = "http://10.0.1.70:9900/api-sdm/v1/pUI";

    private static final String STOP_ROUND_ROBIN = "http://10.0.1.70:9900/api-sdm/v1/stpp";

    private static final String EXECUTE_CMD = "https://10.0.1.70:8080/api/";

    private static final String DEVICE_IP = "http://10.0.1.70:9900";

    @Override
    public CommonResponse<Map> startRoundRobin() {
        Map<String, Object> params = new HashMap<>();
        params.put("tenant", "cluing");
        params.put("type", "S08");
        params.put("time", "100");
        return restUtil.doPost(START_ROUND_ROBIN, params);
    }

    @Override
    public CommonResponse<Map> stopRoundRobin() {
        Map<String, Object> params = new HashMap<>();
        params.put("tenant", "cluing");
        return restUtil.doPost(STOP_ROUND_ROBIN, params);
    }

    @Override
    public CommonResponse<Map> executeCmd(LoRaCommand command, String deviceId) {
        byte[] commandBytes = Base64Util.hexStringToBytes(command.getCmd());
        Map<String, Object> map = new HashMap<>();
        map.put("confirmed", false);
        map.put("data", Base64Util.encodeToString(commandBytes));
        map.put("devEUI", deviceId);
        map.put("fPort", 4);
        map.put("reference", "reference");
        String action = "nodes/" + deviceId + "/queue";
        return restUtil.doPost(EXECUTE_CMD + action, map);
    }

    @Override
    public CommonResponse<String> getToken() {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("scope", "all");
        params.put("client_id", "cluing");
        params.put("client_secret", "CngWVDbTSn");
        CommonResponse<Map> response = restUtil.doPostFormDataNoToken(DEVICE_IP + "/api-dca/oauth/token", params);
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), JSON.toJSONString(response.getData()));
        }
        Map result = response.getData();
        Object token_type = result.get("token_type");
        Object access_token = result.get("access_token");
        Object expires_in = result.get("expires_in");
        String bearer_token = null;
        if (token_type != null && access_token != null && expires_in != null) {
            bearer_token = Objects.toString(token_type) + " " + Objects.toString(access_token);
            redisTemplate.opsForValue().set("BEARER_TOKEN", bearer_token);
            redisTemplate.expire("BEARER_TOKEN", Long.parseLong(Objects.toString(expires_in)), TimeUnit.SECONDS);
        }

        if (StringUtils.isNotBlank(bearer_token)) {
            CommonResponse<String> success = CommonResponse.success();
            return success.setData(bearer_token);
        } else {
            return CommonResponse.faild("GET TOKEN FAILED.");
        }
    }

    @Override
    public String getRedisToken() {
        Object bearer_token = redisTemplate.opsForValue().get("BEARER_TOKEN");
        if (bearer_token == null) {
            getToken();
            bearer_token = redisTemplate.opsForValue().get("BEARER_TOKEN");
        }
        return Objects.toString(bearer_token);
    }

    @Override
    public CommonResponse<String> getDbInstance(String code) {
        // code cluing
        CommonResponse<Map> response = restUtil.doGetNoToken(DEVICE_IP + "/api-tms/pass/scptenant/" + code + "_123");
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), JSON.toJSONString(response.getData()));
        }
        Map result = response.getData();
        Map datas = MapUtils.getMap(result, "datas");
        String instance = MapUtils.getString(datas, "instance");
        if (StringUtils.isNotBlank(instance)) {
            CommonResponse<String> success = CommonResponse.success();
            return success.setData(instance);
        } else {
            return CommonResponse.faild("GET DbInstance FAILED.");
        }
    }

    @Override
    public String getDbInstanceFromRedis(String code) {
        Object db_instance_tenant = redisTemplate.opsForValue().get("DB_INSTANCE_TENANT");
        if (db_instance_tenant != null) {
            return Objects.toString(db_instance_tenant);
        }
        CommonResponse<String> dbInstance = getDbInstance(code);
        if (dbInstance.getCode() == 200) {
            return dbInstance.getData();
        }
        return null;
    }

    @Override
    public CommonResponse<List<GateWayInfoDto>> getGatewayList() {
        CommonResponse<Map> response = restUtil.doGetWithToken(DEVICE_IP + "/api-sdm/SdmGateway?page=1&limit=99");
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject(result, "data");
        if (list == null || list.size() == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        return CommonResponse.success(list.stream().map(map -> convertGateWay(map)).collect(Collectors.toList()));
    }

    @Override
    public CommonResponse<GateWayInfoDto> getGateWayById(String gateWayId) {
        CommonResponse<Map> response = restUtil.doGetWithToken(DEVICE_IP + "/api-sdm/SdmGateway/" + gateWayId);
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        Map data = MapUtils.getMap(result, "datas");
        if (data == null) {
            return CommonResponse.success(null);
        }
        return CommonResponse.success(convertGateWay(data));
    }

    @Override
    public CommonResponse<List<TerminalTypeDto>> getTerminalTypes() {
        CommonResponse<Map> response = restUtil.doGetWithToken(DEVICE_IP + "/api-sdm/SdmTemplate?page=1&limit=99");
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject(result, "data");
        if (list == null || list.size() == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        return CommonResponse.success(list.stream().map(map -> convertTerminalType(map)).collect(Collectors.toList()));
    }

    @Override
    public CommonResponse<List<TerminalTypeDto>> getTerminalByType(String type) {
        // S08
        CommonResponse<Map> response = restUtil.doGetWithToken(DEVICE_IP + "/api-sdm/SdmDevice/getTemplatesByType?type=" + type);
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        Map<String, Object> datas = MapUtils.getMap(result, "datas");
        List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject(datas, "templates");
        if (list == null || list.size() == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        return CommonResponse.success(list.stream().map(map -> convertTerminalType(map)).collect(Collectors.toList()));
    }

    /**
     * {
     * "applicationId": "34",
     * "deviceSn": "393235306A55566",
     * "gatewayId": "39",
     * "name": "测试55566",
     * "templateId": "8",
     * "type": "S08",
     * "toLora":1,
     * "typeName": "RCMII"
     * }
     *
     * @return
     */
    @Override
    public CommonResponse addDevice(AddDeviceDto addDeviceDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("applicationId", addDeviceDto.getApplicationId());
        params.put("deviceSn", addDeviceDto.getDeviceSn());
        params.put("gatewayId", addDeviceDto.getGatewayId());
        params.put("name", addDeviceDto.getName());
        params.put("templateId", addDeviceDto.getTemplateId());
        params.put("type", addDeviceDto.getType());
        params.put("toLora", addDeviceDto.getToLora());
        params.put("typeName", addDeviceDto.getTypeName());
        return restUtil.doPostWithToken(DEVICE_IP + "/api-sdm/SdmDevice", params);
    }

    @Override
    public CommonResponse<List<DeviceInfoDto>> getDevices(String deviceKey) {
        CommonResponse<Map> response = restUtil.doGetWithToken(DEVICE_IP + "/api-sdm/SdmDevice?page=1&limit=99&keyWord=" + deviceKey);
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject(result, "data");
        if (list == null || list.size() == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        return CommonResponse.success(list.stream().map(map -> convertDeviceInfo(map)).collect(Collectors.toList()));
    }

    @Override
    public CommonResponse deleteDevice(String deviceSn) {
        return restUtil.doDeleteWithToken(DEVICE_IP + "/api-sdm/SdmDevice/" + deviceSn);
    }

    private GateWayInfoDto convertGateWay(Map<String, Object> map) {
        GateWayInfoDto gateWayInfoDto = new GateWayInfoDto();
        gateWayInfoDto.setId(MapUtils.getIntValue(map, "id"));
        gateWayInfoDto.setCreateTime(MapUtils.getLongValue(map, "createTime"));
        gateWayInfoDto.setUpdateTime(MapUtils.getLongValue(map, "updateTime"));
        gateWayInfoDto.setLoraId(MapUtils.getObject(map, "loraId"));
        gateWayInfoDto.setName(MapUtils.getString(map, "name"));
        gateWayInfoDto.setApplicationId(MapUtils.getIntValue(map, "applicationId"));
        gateWayInfoDto.setApplicationName(MapUtils.getString(map, "applicationName"));
        gateWayInfoDto.setLoraApplicatonId(MapUtils.getObject(map, "loraApplicatonId"));
        gateWayInfoDto.setSceneId(MapUtils.getIntValue(map, "sceneId"));
        gateWayInfoDto.setSceneName(MapUtils.getString(map, "sceneName"));
        gateWayInfoDto.setLoraSceneId(MapUtils.getIntValue(map, "loraSceneId"));
        gateWayInfoDto.setMacAddress(MapUtils.getString(map, "macAddress"));
        gateWayInfoDto.setDes(MapUtils.getString(map, "des"));
        gateWayInfoDto.setMgrUrl(MapUtils.getString(map, "mgrUrl"));
        gateWayInfoDto.setCreateUserId(MapUtils.getIntValue(map, "createUserId"));
        gateWayInfoDto.setUpdateUserName(MapUtils.getString(map, "updateUserName"));
        gateWayInfoDto.setUpdateUserId(MapUtils.getIntValue(map, "updateUserId"));
        gateWayInfoDto.setIsDel(MapUtils.getIntValue(map, "isDel"));
        return gateWayInfoDto;
    }

    private TerminalTypeDto convertTerminalType(Map<String, Object> map) {
        TerminalTypeDto terminalTypeDto = new TerminalTypeDto();
        terminalTypeDto.setId(MapUtils.getIntValue(map, "id"));
        terminalTypeDto.setCreateTime(MapUtils.getLongValue(map, "createTime"));
        terminalTypeDto.setUpdateTime(MapUtils.getLongValue(map, "updateTime"));
        terminalTypeDto.setType(MapUtils.getString(map, "type"));
        terminalTypeDto.setProcessIsShow(MapUtils.getIntValue(map, "processIsShow"));
        terminalTypeDto.setImgUrl(MapUtils.getString(map, "imgUrl"));
        terminalTypeDto.setDes(MapUtils.getString(map, "des"));
        terminalTypeDto.setTitle(MapUtils.getString(map, "title"));
        terminalTypeDto.setTypeName(MapUtils.getString(map, "typeName"));
        terminalTypeDto.setTypeColor(MapUtils.getString(map, "typeColor"));
        terminalTypeDto.setVersion(MapUtils.getIntValue(map, "version"));
        terminalTypeDto.setCreateUserId(MapUtils.getIntValue(map, "createUserId"));
        terminalTypeDto.setUpdateUserName(MapUtils.getString(map, "updateUserName"));
        terminalTypeDto.setUpdateUserId(MapUtils.getIntValue(map, "updateUserId"));
        terminalTypeDto.setIsDel(MapUtils.getIntValue(map, "isDel"));
        terminalTypeDto.setChildren((List<Object>) MapUtils.getObject(map, "children"));
        return terminalTypeDto;
    }

    private DeviceInfoDto convertDeviceInfo(Map<String, Object> map) {
        DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
        deviceInfoDto.setId(MapUtils.getIntValue(map, "id"));
        deviceInfoDto.setCreateTime(MapUtils.getString(map, "createTime"));
        deviceInfoDto.setUpdateTime(MapUtils.getString(map, "updateTime"));
        deviceInfoDto.setLoraId(MapUtils.getIntValue(map, "loraId"));
        deviceInfoDto.setApplicationId(MapUtils.getIntValue(map, "applicationId"));
        deviceInfoDto.setApplicationName(MapUtils.getString(map, "applicationName"));
        deviceInfoDto.setLoraAppId(MapUtils.getIntValue(map, "loraAppId"));
        deviceInfoDto.setGatewayId(MapUtils.getIntValue(map, "gatewayId"));
        deviceInfoDto.setGatewayName(MapUtils.getString(map, "gatewayName"));
        deviceInfoDto.setType(MapUtils.getString(map, "type"));
        deviceInfoDto.setDeviceSn(MapUtils.getString(map, "deviceSn"));
        deviceInfoDto.setName(MapUtils.getString(map, "name"));
        deviceInfoDto.setDes(MapUtils.getString(map, "des"));
        deviceInfoDto.setLot(MapUtils.getString(map, "lot"));
        deviceInfoDto.setLat(MapUtils.getString(map, "lat"));
        deviceInfoDto.setGroups(MapUtils.getString(map, "groups"));
        deviceInfoDto.setTemplateId(MapUtils.getIntValue(map, "templateId"));
        deviceInfoDto.setTypeName(MapUtils.getString(map, "typeName"));
        deviceInfoDto.setTypeColor(MapUtils.getString(map, "typeColor"));
        deviceInfoDto.setCreateUserId(MapUtils.getIntValue(map, "createUserId"));
        deviceInfoDto.setUpdateUserId(MapUtils.getIntValue(map, "updateUserId"));
        deviceInfoDto.setUpdateUserName(MapUtils.getString(map, "updateUserName"));
        deviceInfoDto.setIsDel(MapUtils.getIntValue(map, "isDel"));
        deviceInfoDto.setPointNum(MapUtils.getIntValue(map, "pointNum"));
        deviceInfoDto.setTemplateDes(MapUtils.getString(map, "templateDes"));
        deviceInfoDto.setToLora(MapUtils.getIntValue(map, "toLora"));
        deviceInfoDto.setImgUrl(MapUtils.getString(map, "imgUrl"));
        deviceInfoDto.setMgrUrl(MapUtils.getString(map, "mgrUrl"));
        deviceInfoDto.setSelected(MapUtils.getBooleanValue(map, "selected"));
        deviceInfoDto.setCardId(MapUtils.getIntValue(map, "cardId"));
        deviceInfoDto.setDeviceTemplateId(MapUtils.getIntValue(map, "deviceTemplateId"));
        return deviceInfoDto;
    }
}
