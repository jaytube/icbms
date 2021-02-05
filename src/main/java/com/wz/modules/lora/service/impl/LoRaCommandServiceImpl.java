package com.wz.modules.lora.service.impl;

import com.alibaba.fastjson.JSON;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.lora.dto.AddDeviceDto;
import com.wz.modules.lora.dto.DeviceInfoDto;
import com.wz.modules.lora.dto.TerminalTypeDto;
import com.wz.modules.lora.entity.GatewayInfo;
import com.wz.modules.lora.enums.LoRaCommand;
import com.wz.modules.lora.service.LoRaCommandService;
import com.wz.modules.lora.utils.Base64Util;
import com.wz.modules.lora.utils.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.wz.modules.lora.utils.Base64Util.hexStringToBytes;
import static com.wz.modules.lora.utils.RestUtil.HTTP_HEADER_CONTENT_TYPE;

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
    private RedisUtil redisUtil;

    private static final String START_ROUND_ROBIN_URI = ":9900/api-sdm/v1/pUI";

    private static final String STOP_ROUND_ROBIN_URI = ":9900/api-sdm/v1/stpp";

    private static final String EXECUTE_CMD_URI = ":8080/api/";

    private static final String DEVICE_IP_URI = ":9900";

    @Override
    public CommonResponse<Map> startRoundRobin(String gatewayIp) {
        Map<String, Object> params = new HashMap<>();
        params.put("tenant", "cluing");
        params.put("type", "S08");
        params.put("time", "100");
        return restUtil.doPost(gatewayIp + START_ROUND_ROBIN_URI, params);
    }

    @Override
    public CommonResponse<Map> stopRoundRobin(String gatewayIp) {
        Map<String, Object> params = new HashMap<>();
        params.put("tenant", "cluing");
        return restUtil.doPost(gatewayIp + STOP_ROUND_ROBIN_URI, params);
    }

    @Override
    public CommonResponse<Map> executeCmd(String gatewayIp, LoRaCommand command, String deviceId) {
        byte[] commandBytes = hexStringToBytes(command.getCmd());
        Map<String, Object> map = new HashMap<>();
        map.put("confirmed", false);
        map.put("data", Base64Util.encodeToString(commandBytes));
        map.put("devEUI", deviceId);
        map.put("fPort", 4);
        map.put("reference", "reference");
        String action = "nodes/" + deviceId + "/queue";
        return restUtil.doPost("https" + gatewayIp.substring(4) + EXECUTE_CMD_URI + action, map);
    }

    @Override
    public CommonResponse<String> getToken(String gatewayIp) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("scope", "all");
        params.put("client_id", "cluing");
        params.put("client_secret", "CngWVDbTSn");
        CommonResponse<Map> response = restUtil.doPostFormDataNoToken(gatewayIp + DEVICE_IP_URI + "/api-dca/oauth/token", params);
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
            try {
                redisUtil.setString(gatewayIp, bearer_token, Integer.valueOf(Objects.toString(expires_in)));
            } catch (Exception e) {
                log.error("SET BEARER_TOKEN TO REDIS", e);
            }
        }

        if (StringUtils.isNotBlank(bearer_token)) {
            CommonResponse<String> success = CommonResponse.success();
            return success.setData(bearer_token);
        } else {
            return CommonResponse.faild("GET TOKEN FAILED.");
        }
    }

    @Override
    public String getRedisToken(String gatewayIp) {
        String bearer_token = null;
        try {
            bearer_token = redisUtil.getString(gatewayIp);
            if (bearer_token == null) {
                getToken(gatewayIp);
                bearer_token = redisUtil.getString(gatewayIp);
            }
            return bearer_token;
        } catch (Exception e) {
            log.error("GET BEARER_TOKEN FROM REDIS", e);
        }
        return bearer_token;
    }

    @Override
    public CommonResponse<String> getDbInstance(String gatewayIp, String code) {
        // code cluing
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", HTTP_HEADER_CONTENT_TYPE);
        requestHeaders.add("Authorization", getRedisToken(gatewayIp));
        CommonResponse<Map> response = restUtil.doGetWithToken(gatewayIp + DEVICE_IP_URI + "/api-tms/pass/scptenant/" + code + "_123", requestHeaders);
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
    public String getDbInstanceFromRedis(String gatewayIp, String code) {
        String db_instance_tenant = null;
        try {
            db_instance_tenant = redisUtil.getString("DB_INSTANCE_TENANT" + gatewayIp);
            if (db_instance_tenant != null) {
                return db_instance_tenant;
            }
            CommonResponse<String> dbInstance = getDbInstance(gatewayIp, code);
            if (dbInstance.getCode() == 200) {
                db_instance_tenant = dbInstance.getData();
                redisUtil.setString("DB_INSTANCE_TENANT" + gatewayIp, db_instance_tenant);
                return db_instance_tenant;
            }
        } catch (Exception e) {
            log.error("GET DB_INSTANCE_TENANT FROM REDIS", e);
        }
        return null;
    }

    @Override
    public CommonResponse<List<GatewayInfo>> getGatewayList(String gatewayIp) {
        CommonResponse<Map> response = restUtil.doGetWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmGateway?page=1&limit=99");
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.getObject(result, "data");
        if (list == null || list.size() == 0) {
            return CommonResponse.success(new ArrayList<>());
        }
        return CommonResponse.success(list.stream().map(map -> convertGateWay(gatewayIp, map)).collect(Collectors.toList()));
    }

    @Override
    public CommonResponse<GatewayInfo> getGateWayById(String gatewayIp, String gateWayId) {
        CommonResponse<Map> response = restUtil.doGetWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmGateway/" + gateWayId);
        if (response.getCode() != 200) {
            return CommonResponse.faild(response.getMsg(), null);
        }
        Map result = response.getData();
        Map data = MapUtils.getMap(result, "datas");
        if (data == null) {
            return CommonResponse.success(null);
        }
        return CommonResponse.success(convertGateWay(gatewayIp, data));
    }

    @Override
    public CommonResponse<List<TerminalTypeDto>> getTerminalTypes(String gatewayIp) {
        CommonResponse<Map> response = restUtil.doGetWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmTemplate?page=1&limit=99");
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
    public CommonResponse<List<TerminalTypeDto>> getTerminalByType(String gatewayIp, String type) {
        // S08
        CommonResponse<Map> response = restUtil.doGetWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmDevice/getTemplatesByType?type=" + type);
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
    public CommonResponse addDevice(String gatewayIp, AddDeviceDto addDeviceDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("applicationId", addDeviceDto.getApplicationId());
        params.put("deviceSn", addDeviceDto.getDeviceSn());
        params.put("gatewayId", addDeviceDto.getGatewayId());
        params.put("name", addDeviceDto.getName());
        params.put("templateId", addDeviceDto.getTemplateId());
        params.put("type", addDeviceDto.getType());
        params.put("toLora", addDeviceDto.getToLora());
        params.put("typeName", addDeviceDto.getTypeName());
        return restUtil.doPostWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmDevice", params);
    }

    @Override
    public CommonResponse<List<DeviceInfoDto>> getDevices(String gatewayIp, String deviceKey) {
        String uri = "/api-sdm/SdmDevice?page=1&limit=99";
        if (StringUtils.isNotBlank(deviceKey)) {
            uri += "&keyWord=" + deviceKey;
        }
        CommonResponse<Map> response = restUtil.doGetWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + uri);
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
    public CommonResponse deleteDevice(String gatewayIp, String deviceSn) {
        return restUtil.doDeleteWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmDevice/" + deviceSn);
    }

    @Override
    public CommonResponse<Map> deleteDevices(String gatewayIp, List<Integer> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return CommonResponse.error("deviceIds 为空。");
        }
        List<Map> body = deviceIds.stream().map(id -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("isDel", 1);
            return map;
        }).collect(Collectors.toList());
        return restUtil.doDeleteWithToken(gatewayIp, gatewayIp + DEVICE_IP_URI + "/api-sdm/SdmDevice/batchDel", body);
    }

    private GatewayInfo convertGateWay(String gatewayIp, Map<String, Object> map) {
        GatewayInfo gateWayInfo = new GatewayInfo();
        gateWayInfo.setCreateTime(MapUtils.getLongValue(map, "createTime"));
        gateWayInfo.setUpdateTime(MapUtils.getLongValue(map, "updateTime"));
        gateWayInfo.setLoraId(MapUtils.getIntValue(map, "loraId"));
        gateWayInfo.setName(MapUtils.getString(map, "name"));
        gateWayInfo.setApplicationId(MapUtils.getIntValue(map, "applicationId"));
        gateWayInfo.setApplicationName(MapUtils.getString(map, "applicationName"));
        gateWayInfo.setLoraApplicationId(MapUtils.getIntValue(map, "loraApplicationId"));
        gateWayInfo.setSceneId(MapUtils.getIntValue(map, "sceneId"));
        gateWayInfo.setSceneName(MapUtils.getString(map, "sceneName"));
        gateWayInfo.setLoraSceneId(MapUtils.getIntValue(map, "loraSceneId"));
        gateWayInfo.setMacAddress(MapUtils.getString(map, "macAddress"));
        gateWayInfo.setDes(MapUtils.getString(map, "des"));
        gateWayInfo.setMgrUrl(MapUtils.getString(map, "mgrUrl"));
        gateWayInfo.setCreateUserId(MapUtils.getIntValue(map, "createUserId"));
        gateWayInfo.setUpdateUserName(MapUtils.getString(map, "updateUserName"));
        gateWayInfo.setUpdateUserId(MapUtils.getIntValue(map, "updateUserId"));
        gateWayInfo.setIsDel(MapUtils.getIntValue(map, "isDel"));
        gateWayInfo.setIpAddress(gatewayIp);
        gateWayInfo.setGatewayId(MapUtils.getIntValue(map, "id"));
        return gateWayInfo;
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
