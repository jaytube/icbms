package com.wz.modules.deviceinfo.service.impl;

import com.alibaba.fastjson.JSON;
import com.wz.front.service.AppDeviceBoxService;
import com.wz.front.util.DeviceType;
import com.wz.front.util.DeviceUtils;
import com.wz.modules.app.dto.DevicePlainInfoDto;
import com.wz.modules.common.exception.MyException;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.common.utils.Result;
import com.wz.modules.deviceinfo.dao.DeviceBoxInfoDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.deviceinfo.service.DeviceOperationService;
import com.wz.modules.lora.dao.GatewayDeviceMapDao;
import com.wz.modules.lora.entity.GatewayDeviceMap;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DeviceOperationServiceImpl implements DeviceOperationService {

    @Autowired
    private DeviceBoxInfoService deviceBoxInfoService;

    @Autowired
    private AppDeviceBoxService appDeviceBoxService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceBoxInfoDao deviceBoxInfoDao;

    @Autowired
    private GatewayDeviceMapDao gatewayDeviceMapDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public void deleteDevices(String[] ids) {
        deviceBoxInfoService.deleteBatch(ids, "app");
        appDeviceBoxService.deleteBatch(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public Result addDevice(List<Map<String, String>> result, String userId, String deviceBoxMac, String projectId, HttpServletRequest request, String deviceBoxSn, int gymId, int gatewayId) {
        UserEntity user = this.userService.queryObject(userId);
        DeviceBoxInfoEntity deviceBoxInfoEntity = deviceBoxInfoDao.queryByBoxNum(deviceBoxMac);
        if (deviceBoxInfoEntity == null) {
            List<DeviceBoxInfoEntity> saveResult = deviceBoxInfoService.saveBoxLocBatch(result, projectId, user);
            deviceBoxInfoEntity = saveResult.get(0);
        }
        DeviceType deviceType = DeviceUtils.checkDeviceType(request);
        if (deviceType == DeviceType.MOBILE_APP) {
            GatewayDeviceMap device = gatewayDeviceMapDao.findDevice(projectId, deviceBoxInfoEntity.getId());
            if (device != null) {
                return Result.error("该设备已绑定网关，如需修改，请先移除该设备再添加");
            }
            GatewayDeviceMap gatewayDeviceMap = new GatewayDeviceMap();
            gatewayDeviceMap.setDeviceSn(deviceBoxSn.toLowerCase());
            gatewayDeviceMap.setDeviceInfoId(deviceBoxInfoEntity.getId());
            gatewayDeviceMap.setGymId(gymId);
            gatewayDeviceMap.setDeviceBoxNum(deviceBoxMac);
            gatewayDeviceMap.setGatewayId(gatewayId);
            gatewayDeviceMap.setProjectId(projectId);
            CommonResponse commonResponse = appDeviceBoxService.addDevice(gatewayDeviceMap, deviceBoxInfoEntity);
            if(commonResponse.getCode()!=200)
                throw new MyException("remote add device failed.");

            if (commonResponse.getCode() == 200) {
                DevicePlainInfoDto dto = new DevicePlainInfoDto(deviceBoxMac, deviceBoxSn, gatewayId, projectId);
                redisUtil.hset(0, "DEVICE_INFO", deviceBoxMac, JSON.toJSONString(dto));
                return Result.ok("添加成功");
            } else {
                return Result.error("添加设备失败");
            }
        }

        return Result.error("必须是手机端APP设备");
    }
}
